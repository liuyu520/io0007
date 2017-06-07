package com.swing.dialog;

import com.common.bean.ModifyResponseBodyBean;
import com.common.bean.ModifyTypeInfo;
import com.common.dict.Constant2;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.io.hw.json.HWJacksonUtils;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextArea;
import com.swing.component.AssistPopupTextField;
import com.swing.dialog.toast.ToastMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/***
 * 为项目/Users/whuanghkl/work/mygit/lanproxy 服务<br />
 * 修改的配置文件是 客户端的配置
 */
public class ProxyModifyResponseDialog extends GenericDialog {

    private JPanel contentPane;
    private AssistPopupTextField servletPathtextField;
    private AssistPopupTextField configFileTextField;
    private java.util.List<String> servletPathList;
    private JComboBox<String> modifyTypeComboBox;
    /***
     * 配置文件
     */
    private File configFile;
    private AssistPopupTextField keyTextField;
    private Map<String, ModifyTypeInfo> proxyModifyResponseBodyMap;
    private AssistPopupTextArea responseBodyTextArea;
    private ModifyResponseBodyBean modifyResnseBodyBean;
    //    private ModifyResponseBodyBean modifyResponseBodyBean;
    private JComboBox<String> contentTypeComboBox;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProxyModifyResponseDialog frame = new ProxyModifyResponseDialog(null);
                    frame.launch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String readConfig(String configFilePath) throws IOException {
        configFile = new File(configFilePath);
        if (!configFile.exists()) {
            configFile = new File(".share_http.properties");
        }
        if (configFile.exists()) {
            InputStream inStream = new FileInputStream(configFile);
            String resumeInput = FileUtils.getFullContent4(inStream, SystemHWUtil.CHARSET_UTF);
            inStream.close();//及时关闭资源
            return resumeInput;

        }
        return null;
    }

    @Override
    public void close2() {
        this.dispose();
    }

    private void parseConfig() {
        try {
            String configJson = readConfig(configFileTextField.getText2());
            if (ValueWidget.isNullOrEmpty(configJson)) {
                String errorMessage = "配置文件路径不对,或者内容为空:" + configFileTextField.getText2();
                System.out.println(errorMessage);
                ToastMessage.toast(errorMessage, 3000, Color.red);
                return;
            }
            proxyModifyResponseBodyMap = HWJacksonUtils.deSerializeMap(configJson, ModifyTypeInfo.class);
            servletPathList = new ArrayList<String>();
            for (String key : proxyModifyResponseBodyMap.keySet()) {
                servletPathList.add(key);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    public ProxyModifyResponseDialog(ModifyResponseBodyBean modifyResponseBodyBean) {
        this.modifyResnseBodyBean = modifyResponseBodyBean;
        this.setModal(true);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setBounds(100, 100, 850, 600);
        setLoc(850, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(null);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(140);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        tabbedPane.addTab("篡改应答体", null, splitPane, null);

        JPanel leftPanel = new JPanel();
        splitPane.setLeftComponent(leftPanel);
        GridBagLayout gbl_leftPanel = new GridBagLayout();
        gbl_leftPanel.columnWidths = new int[]{0, 0, 0, 0};
        gbl_leftPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gbl_leftPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_leftPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        leftPanel.setLayout(gbl_leftPanel);

        JLabel label_3 = new JLabel("配置文件");
        GridBagConstraints gbc_label_3 = new GridBagConstraints();
        gbc_label_3.anchor = GridBagConstraints.EAST;
        gbc_label_3.insets = new Insets(0, 0, 5, 5);
        gbc_label_3.gridx = 1;
        gbc_label_3.gridy = 0;
        leftPanel.add(label_3, gbc_label_3);

        configFileTextField = new AssistPopupTextField("/Users/whuanghkl/work/mygit/lanproxy/distribution/proxy-client-0.0.1/conf/modify_client.properties");
        if (!ValueWidget.isNullOrEmpty(getModifyResnseBodyBean())
                && !ValueWidget.isNullOrEmpty(getModifyResnseBodyBean().getConfigFilePath())) {
            configFileTextField.setText(getModifyResnseBodyBean().getConfigFilePath());
        }
        GridBagConstraints gbc_configFileTextField = new GridBagConstraints();
        gbc_configFileTextField.insets = new Insets(0, 0, 5, 0);
        gbc_configFileTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_configFileTextField.gridx = 2;
        gbc_configFileTextField.gridy = 0;
        leftPanel.add(configFileTextField, gbc_configFileTextField);
        configFileTextField.setColumns(10);
        configFileTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseConfig();
            }
        });

        JLabel label = new JLabel("接口路径");
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.anchor = GridBagConstraints.EAST;
        gbc_label.gridx = 1;
        gbc_label.gridy = 1;
        leftPanel.add(label, gbc_label);

        servletPathtextField = new AssistPopupTextField();
        if (!ValueWidget.isNullOrEmpty(getModifyResnseBodyBean())) {
            servletPathtextField.setText(getModifyResnseBodyBean().getServletPath());
        }
        GridBagConstraints gbc_servletPathtextField = new GridBagConstraints();
        gbc_servletPathtextField.insets = new Insets(0, 0, 5, 0);
        gbc_servletPathtextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_servletPathtextField.gridx = 2;
        gbc_servletPathtextField.gridy = 1;
        leftPanel.add(servletPathtextField, gbc_servletPathtextField);
        servletPathtextField.setColumns(10);
        servletPathtextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!DialogUtil.verifyTFEmpty(servletPathtextField, "接口路径")) {
                    return;
                }
                searchServletPath();
            }
        });

        JLabel label_1 = new JLabel("接口参数");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.anchor = GridBagConstraints.EAST;
        gbc_label_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_1.gridx = 1;
        gbc_label_1.gridy = 2;
        leftPanel.add(label_1, gbc_label_1);

        JLabel lblContenttype = new JLabel("contentType");
        GridBagConstraints gbc_lblContenttype = new GridBagConstraints();
        gbc_lblContenttype.anchor = GridBagConstraints.EAST;
        gbc_lblContenttype.insets = new Insets(0, 0, 5, 5);
        gbc_lblContenttype.gridx = 1;
        gbc_lblContenttype.gridy = 3;
        leftPanel.add(lblContenttype, gbc_lblContenttype);

        contentTypeComboBox = new JComboBox<String>();
        contentTypeComboBox.addItem(Constant2.NULL);
        contentTypeComboBox.addItem(SystemHWUtil.RESPONSE_CONTENTTYPE_JSON_UTF);
        contentTypeComboBox.addItem(SystemHWUtil.RESPONSE_CONTENTTYPE_PLAIN_UTF);
        contentTypeComboBox.addItem(SystemHWUtil.CONTENTTYPE_JSON);
        contentTypeComboBox.addItem(SystemHWUtil.RESPONSE_CONTENTTYPE_PLAIN);
        contentTypeComboBox.addItem(SystemHWUtil.RESPONSE_CONTENTTYPE_HTML);
        contentTypeComboBox.addItem(SystemHWUtil.RESPONSE_CONTENTTYPE_BINARY);
        contentTypeComboBox.addItem(SystemHWUtil.RESPONSE_CONTENTTYPE_JAVASCRIPT2);
        GridBagConstraints gbc_contentTypeComboBox = new GridBagConstraints();
        gbc_contentTypeComboBox.insets = new Insets(0, 0, 5, 0);
        gbc_contentTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_contentTypeComboBox.gridx = 2;
        gbc_contentTypeComboBox.gridy = 3;
        leftPanel.add(contentTypeComboBox, gbc_contentTypeComboBox);

        JLabel label_2 = new JLabel("篡改类型");
        GridBagConstraints gbc_label_2 = new GridBagConstraints();
        gbc_label_2.anchor = GridBagConstraints.EAST;
        gbc_label_2.insets = new Insets(0, 0, 0, 5);
        gbc_label_2.gridx = 1;
        gbc_label_2.gridy = 4;
        leftPanel.add(label_2, gbc_label_2);

        modifyTypeComboBox = new JComboBox<String>();
        modifyTypeComboBox.addItem("完全替换");
        modifyTypeComboBox.addItem("关键字替换");
        modifyTypeComboBox.addItem("正则表达式替换");
        GridBagConstraints gbc_modifyTypeComboBox = new GridBagConstraints();
        gbc_modifyTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_modifyTypeComboBox.gridx = 2;
        gbc_modifyTypeComboBox.gridy = 4;
        leftPanel.add(modifyTypeComboBox, gbc_modifyTypeComboBox);

        JPanel rightBottomPanel = new JPanel();
        splitPane.setRightComponent(rightBottomPanel);
        rightBottomPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        rightBottomPanel.add(scrollPane, BorderLayout.CENTER);

        responseBodyTextArea = new AssistPopupTextArea();
        responseBodyTextArea.setLineWrap(true);
        responseBodyTextArea.setWrapStyleWord(true);
        scrollPane.setViewportView(responseBodyTextArea);

        JPanel panel_1 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        scrollPane.setColumnHeaderView(panel_1);

        JLabel label_4 = new JLabel("关键字");
        panel_1.add(label_4);

        keyTextField = new AssistPopupTextField();
        keyTextField.placeHolder("关键字,也可以是正则表达式");
        panel_1.add(keyTextField);
        keyTextField.setColumns(10);

        JPanel panel = new JPanel();
        rightBottomPanel.add(panel, BorderLayout.SOUTH);

        JButton btnSave = new JButton("save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAction();
            }
        });
        panel.add(btnSave);

        JButton btnAdd = new JButton("add");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String servletPath = servletPathtextField.getText2();
                saveOrAddAction(servletPath, true);
            }
        });
        panel.add(btnAdd);

        JButton btnClose = new JButton("close");
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ToastMessage.toast("不保存", 1000);
                close2();//关闭不保存;
            }
        });
        panel.add(btnClose);

        //增加key 快捷键
        setShortCuts(responseBodyTextArea);
    }

    public void launch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                parseConfig();
                if (!ValueWidget.isNullOrEmpty(servletPathtextField.getText2())) {
                    searchServletPath();
                }
            }
        }).start();
        this.setVisible(true);
    }

    public void saveAction() {
        String servletPath = servletPathtextField.getText2();
        saveOrAddAction(servletPath, false);
    }

    public void searchServletPath() {
        ModifyTypeInfo modifyTypeInfo = proxyModifyResponseBodyMap.get(servletPathtextField.getText2());
        if (ValueWidget.isNullOrEmpty(modifyTypeInfo)) {
            ToastMessage.toast("该接口不存在,请点击add", 2000, Color.red);
            return;
        }
        modifyTypeComboBox.setSelectedIndex(modifyTypeInfo.getModifyType() - 1);
        keyTextField.setText(modifyTypeInfo.getKeyword());
        responseBodyTextArea.setText(modifyTypeInfo.getReplacement());
        if (!ValueWidget.isNullOrEmpty(modifyTypeInfo.getContentType())) {
            contentTypeComboBox.setSelectedItem(modifyTypeInfo.getContentType());
        }
    }

    /***
     *
     * @param servletPath
     * @param addNew : 是否新建
     */
    public void saveOrAddAction(String servletPath, boolean addNew) {
        if (!DialogUtil.verifyTFEmpty(responseBodyTextArea, "请求体(replacement)")) {
            return;
        }
        if (addNew) {//增加新的
            if (proxyModifyResponseBodyMap.containsKey(servletPath)) {
                servletPath = servletPath + "(2)";
                servletPathtextField.setText(servletPath);
            }
            ModifyTypeInfo modifyTypeInfo = new ModifyTypeInfo();
            save(servletPath, modifyTypeInfo);
            close2();//新建关闭对话框;保存不关闭
            ToastMessage.toast("新建成功", 2000);
            return;
        }
        //更新原有的
        if (!proxyModifyResponseBodyMap.containsKey(servletPath)) {
            ToastMessage.toast(" 不存在该接口,请先 add", 2000, Color.red);
            return;
        }
        ModifyTypeInfo modifyTypeInfo = proxyModifyResponseBodyMap.get(servletPath);
        save(servletPath, modifyTypeInfo);
        ToastMessage.toast("保存成功", 2000, 120);
    }

    public void save(String servletPath, ModifyTypeInfo modifyTypeInfo) {

        modifyTypeInfo.setModifyType(modifyTypeComboBox.getSelectedIndex() + 1);
        modifyTypeInfo.setKeyword(keyTextField.getText2());
        modifyTypeInfo.setReplacement(responseBodyTextArea.getText2());
        modifyTypeInfo.setContentType(contentTypeComboBox.getSelectedItem().toString());
        proxyModifyResponseBodyMap.put(servletPath, modifyTypeInfo);
        saveConf();
        getModifyResnseBodyBean().setConfigFilePath(configFileTextField.getText2());
        getModifyResnseBodyBean().setServletPath(servletPath);
    }

    private void saveConf() {
        String saveJson = HWJacksonUtils.getJsonP(proxyModifyResponseBodyMap);
        FileUtils.writeToFile(configFile, saveJson, SystemHWUtil.CHARSET_UTF);
    }

    public ModifyResponseBodyBean getModifyResnseBodyBean() {
        return modifyResnseBodyBean;
    }

    public void setModifyResnseBodyBean(ModifyResponseBodyBean modifyResnseBodyBean) {
        this.modifyResnseBodyBean = modifyResnseBodyBean;
    }

    @Override
    protected void saveConfig() {
        super.saveConfig();
        saveAction();
    }
}
