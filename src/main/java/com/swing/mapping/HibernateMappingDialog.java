package com.swing.mapping;

import com.common.bean.MappingInfo;
import com.common.util.ClassFinder;
import com.common.util.ReflectHWUtils;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextField;
import com.swing.mapping.component.MappingRelationPane;
import com.swing.mapping.component.table.MyMappingPanelEditor;
import com.swing.mapping.component.table.MyMappingPanelRender;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HibernateMappingDialog extends JFrame {
    protected static Logger logger = Logger.getLogger(HibernateMappingDialog.class);
    private JPanel contentPane;
    private AssistPopupTextField entityTextField;
    private JScrollPane scrollPane;
    private JTable columnsTable;
    private JPanel panel_1;
    private JButton btnGenerate;
    private static String[] columnNames = new String[]{"成员变量名称", "类型", "映射关系"};
    private List<MappingRelationPane> mappingRelationPanes = new ArrayList<>();
    private Class currentEntityClass;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HibernateMappingDialog frame = new HibernateMappingDialog();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public HibernateMappingDialog() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("自动生成实体类的注解");
        setBounds(100, 100, 650, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        contentPane.add(panel, BorderLayout.NORTH);

        JLabel label = new JLabel("实体类路径");
        panel.add(label);

        entityTextField = new AssistPopupTextField();
        entityTextField.placeHolder("实体类的路径");
        panel.add(entityTextField);
        entityTextField.setColumns(40);
        entityTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Class clazz = Student.class;
                String packagePath = ClassFinder.getJavaPackage(entityTextField.getText());
                try {
                    currentEntityClass = Class.forName(packagePath);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                    logger.error(e1.getMessage(), e1);
                }
                //仅获取 复杂对象的成员变量
                List<Field> fields = ReflectHWUtils.getAllFieldList(currentEntityClass, true);
                int size = fields.size();
                Object[][] datas = new Object[size][];
                mappingRelationPanes.clear();
                for (int i = 0; i < size; i++) {
                    Field field = fields.get(i);
                    String columnname = field.getName();
                    // 成员变量的类型
                    String typeName = field.getType().getSimpleName();
                    boolean isArray = false;
                    //判断成员变量是否是集合类型
                    if (field.getType().isArray() || typeName.equals("List") || typeName.equals("Set")) {
                        isArray = true;
                        typeName = field.getGenericType().getTypeName().replaceAll("[a-zA-Z.]+\\.", "");
                    }
                    MappingRelationPane mappingRelationPane = new MappingRelationPane(getMappingItems(isArray), columnname);
                    datas[i] = new Object[]{columnname, typeName, mappingRelationPane};
                    mappingRelationPanes.add(mappingRelationPane);

                }
                setTableModel(datas);
            }
        });

        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        columnsTable = new JTable();

//        Object[][] datas = new Object[2][];
//        datas[0] = new Object[]{"student", "Student", new MappingRelationPane(null)};
//        datas[1] = new Object[]{"cats", "list<Cat>", new MappingRelationPane(null)};

//        setTableModel(datas);
        scrollPane.setViewportView(columnsTable);

        panel_1 = new JPanel();
        contentPane.add(panel_1, BorderLayout.SOUTH);

        //生成实体类的注解,并且写入文件
        btnGenerate = new JButton("generate");
        btnGenerate.addActionListener(new ActionListener() {
            /**
             * 0:多对一;<br />
             * 1:一对一<br />
             * 2:一对多;<br />
             * 3:多对多
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Integer> mappingMap = new HashMap<>();
                int size = mappingRelationPanes.size();
                for (int i = 0; i < size; i++) {
                    MappingRelationPane mappingRelationPane = mappingRelationPanes.get(i);
                    mappingMap.put(mappingRelationPane.getColumnName(), mappingRelationPane.mappingType());
                }
                try {
                    autoGenerateMappingCode(mappingMap);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
//                System.out.println(mappingMap);
            }

        });
        panel_1.add(btnGenerate);
    }

    private void autoGenerateMappingCode(Map<String, Integer> mappingMap) throws IOException {
        File entityFile = new File(entityTextField.getText() /*"/Users/whuanghkl/code/mygit/io0007/src/main/java/com/common/bean/Student.java"*/);
        String content = FileUtils.getFullContent2(entityFile);
        for (String columnName : mappingMap.keySet()) {
            Integer mappingType = mappingMap.get(columnName);
            String annotation = getMappingByType(mappingType, columnName);
            String methodName = "get" + ValueWidget.capitalize(columnName);
//            content=content.replaceAll()
            content = RegexUtil.replaceAll2(content, "^(.*public.*" + methodName + "\\(\\))", "    " + annotation + SystemHWUtil.CRLF + "$1", Pattern.MULTILINE);

        }
        // 增加主键注解
        content = RegexUtil.replaceAll2(content, "^(.*public.*getId\\(\\))", "    " + "@Id\n" +
                "    @GeneratedValue" + SystemHWUtil.CRLF + "$1", Pattern.MULTILINE);
        // 增加依赖 import javax.persistence.*;
        content = RegexUtil.replaceAll2(content, "^(package.*)", "$1" + SystemHWUtil.CRLF + "import javax.persistence.*;" + SystemHWUtil.CRLF
                + "import com.common.annotation.ColumnDescription;", Pattern.MULTILINE);
        System.out.println("content :" + content);
        // 实体类名称,例如Student
        String entityName = entityFile.getName().replaceAll(".java$", "");
        //增加table 注解
        content = RegexUtil.replaceAll2(content, "^(.*public[\\s]+class[\\s]+" + entityName + ")", "@Entity\n" +
                "@Table(name = \"t_" + ValueWidget.title(entityName) + "\")" + SystemHWUtil.CRLF + "$1", Pattern.MULTILINE);

        //增加注解@ColumnDescription("<xxx>")
        content = RegexUtil.replaceAll2(content, "^([\\s]*private[\\s\\w]+)", "    @ColumnDescription(\"\")" + SystemHWUtil.CRLF + "$1", Pattern.MULTILINE);
        FileUtils.writeToFile(entityFile, content, false);
    }

    /***
     * 0:多对一;<br />
     * 1:一对一<br />
     * 2:一对多;<br />
     * 3:多对多
     * @param mappingType
     * @return
     */
    public String getMappingByType(Integer mappingType, String columnName) {
        String annotation = null;
        String currentClassName = ValueWidget.title(currentEntityClass.getSimpleName());
        switch (mappingType) {
            case 0:
                annotation = "@ManyToOne" + SystemHWUtil.CRLF + "    @JoinColumn (name=\"" + columnName + "Id\")";
                break;
            case 1:
                annotation = "@OneToOne" + SystemHWUtil.CRLF + "    @JoinColumn(name=\"" + columnName + "Id\")";
                break;
            case 2:
                annotation = "@OneToMany" + SystemHWUtil.CRLF + "    @JoinColumn(name=\"" + currentClassName + "Id\")";
                break;
            case 3:
                annotation = "@ManyToMany" + SystemHWUtil.CRLF + "    @JoinTable(name=\"t_" + currentClassName + "_" + columnName + "\",\n" +
                        "\t\tjoinColumns={@JoinColumn(name=\"" + currentClassName + "_id\")},\n" +
                        "\t\tinverseJoinColumns={@JoinColumn(name=\"" + columnName + "Id\")}\n" +
                        "\t\t)";
                break;
        }
        return annotation;
    }

    /***
     * 0:多对一;<br />
     * 1:一对一<br />
     * 2:一对多;<br />
     * 3:多对多
     * @param isArray
     * @return
     */
    private List<MappingInfo> getMappingItems(boolean isArray) {
        List<MappingInfo> mappingInfoList = new ArrayList<>();
        if (isArray) {
            MappingInfo mappingInfo = new MappingInfo().setDisplayName("一对多").setType(2);
            mappingInfoList.add(mappingInfo);

            mappingInfo = new MappingInfo().setDisplayName("多对多").setType(3);
            mappingInfoList.add(mappingInfo);
        } else {
            MappingInfo mappingInfo = new MappingInfo().setDisplayName("多对一").setType(0);
            mappingInfoList.add(mappingInfo);

            mappingInfo = new MappingInfo().setDisplayName("一对一").setType(1);
            mappingInfoList.add(mappingInfo);
        }
        return mappingInfoList;
    }

    public void setTableModel(Object[][] datas) {
        DefaultTableModel model = new DefaultTableModel(datas, columnNames);
        columnsTable.setModel(model);

        rendTable();
    }

    public void rendTable() {
        this.columnsTable.setRowHeight(52);//高度以后固定不变

        columnsTable.getColumnModel().getColumn(2)
                .setCellEditor(new MyMappingPanelEditor());
        columnsTable.getColumnModel().getColumn(2)
                .setCellRenderer(new MyMappingPanelRender());
        columnsTable.repaint();
    }

}
