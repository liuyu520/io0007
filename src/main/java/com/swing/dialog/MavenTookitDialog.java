package com.swing.dialog;

import com.cmd.dos.hw.util.CMDUtil;
import com.common.bean.MvnInstallConfig;
import com.common.bean.PomDependency;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.io.hw.json.XmlYunmaUtil;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextArea;
import com.swing.component.AssistPopupTextField;
import com.swing.component.ComponentUtil;
import com.swing.component.GenerateJsonTextField;
import com.swing.dialog.toast.ToastMessage;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

public class MavenTookitDialog extends GenericDialog implements ItemListener {
    protected static final Logger logger = Logger.getLogger(MavenTookitDialog.class);
    public static final String MAVEN_INSTALL_JAR_CMD = "mvn install:install-file  -Dfile=%s  -DgroupId=%s  -DartifactId=%s -Dversion=%s -Dpackaging=%s";
    private static final long serialVersionUID = -3076948674378465960L;
    private final GenerateJsonTextField jarTextField;
    private final AssistPopupTextField groupIdTextField_1;
    private final AssistPopupTextField artifactIdTextField_1;
    private final AssistPopupTextField versionTextField_1;
    private final AssistPopupTextField packagingTextField_1;
    private AssistPopupTextField textField_1;
    private AssistPopupTextArea resultTextArea;
    private boolean isXml = false;
    private JRadioButton rdbtnPom;
    private JRadioButton groupIdRadio;
    public static final String MVN_INSTALL_DEPENDENCY = "<dependency>" + SystemHWUtil.CRLF +
            "            <groupId>%s</groupId>" + SystemHWUtil.CRLF +
            "            <artifactId>%s</artifactId>" + SystemHWUtil.CRLF +
            "            <version>%s</version>" + SystemHWUtil.CRLF +
            "        </dependency>";

    private MvnInstallConfig mvnInstallConfig;
    private AssistPopupTextArea pomXmlArea;

    /**
     * Create the frame.
     */
    public MavenTookitDialog(MvnInstallConfig mvnInstallConfig) {
        this.mvnInstallConfig = mvnInstallConfig;
        if (null == this.mvnInstallConfig) {
            this.mvnInstallConfig = new MvnInstallConfig();
        }
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("安装本地jar");
        setModal(true);
//		setBounds(100, 100, 450, 300);
//        setLoc(550, 500);
        fullScreenMinus30();
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JLabel label = new JLabel("文件");
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.anchor = GridBagConstraints.WEST;
        gbc_label.gridx = 0;
        gbc_label.gridy = 0;
        contentPane.add(label, gbc_label);

        jarTextField = new GenerateJsonTextField();
        if (!ValueWidget.isNullOrEmpty(mvnInstallConfig) && !ValueWidget.isNullOrEmpty(mvnInstallConfig.getJarFilePath())) {
            jarTextField.setText(mvnInstallConfig.getJarFilePath());
        }
        drag((jarTextField));
        drag(label, jarTextField);
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 0);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 0;
        contentPane.add(jarTextField, gbc_textField);
        jarTextField.setColumns(10);

        ButtonGroup btnGroup = new ButtonGroup();
//        JLabel lblNewLabel = new JLabel("groupId");
        groupIdRadio = new JRadioButton("groupId");
        groupIdRadio.setSelected(true);
        groupIdRadio.addItemListener(this);
        btnGroup.add(groupIdRadio);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        contentPane.add(groupIdRadio, gbc_lblNewLabel);

        groupIdTextField_1 = new AssistPopupTextField();
        if (!ValueWidget.isNullOrEmpty(mvnInstallConfig.getGroupId())) {
            groupIdTextField_1.setText(mvnInstallConfig.getGroupId());
        }
        GridBagConstraints gbc_groupIdTextField_1 = new GridBagConstraints();
        gbc_groupIdTextField_1.insets = new Insets(0, 0, 5, 0);
        gbc_groupIdTextField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_groupIdTextField_1.gridx = 1;
        gbc_groupIdTextField_1.gridy = 1;
        contentPane.add(groupIdTextField_1, gbc_groupIdTextField_1);
        groupIdTextField_1.setColumns(10);

        JLabel lblArtifactid = new JLabel("artifactId");
        GridBagConstraints gbc_lblArtifactid = new GridBagConstraints();
        gbc_lblArtifactid.anchor = GridBagConstraints.WEST;
        gbc_lblArtifactid.insets = new Insets(0, 0, 5, 5);
        gbc_lblArtifactid.gridx = 0;
        gbc_lblArtifactid.gridy = 2;
        contentPane.add(lblArtifactid, gbc_lblArtifactid);

        artifactIdTextField_1 = new AssistPopupTextField();
        if (!ValueWidget.isNullOrEmpty(mvnInstallConfig.getArtifactId())) {
            artifactIdTextField_1.setText(mvnInstallConfig.getArtifactId());
        }
        GridBagConstraints gbc_artifactIdTextField_1 = new GridBagConstraints();
        gbc_artifactIdTextField_1.insets = new Insets(0, 0, 5, 0);
        gbc_artifactIdTextField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_artifactIdTextField_1.gridx = 1;
        gbc_artifactIdTextField_1.gridy = 2;
        contentPane.add(artifactIdTextField_1, gbc_artifactIdTextField_1);
        artifactIdTextField_1.setColumns(10);

        JLabel lblVersion = new JLabel("version");
        GridBagConstraints gbc_lblVersion = new GridBagConstraints();
        gbc_lblVersion.anchor = GridBagConstraints.WEST;
        gbc_lblVersion.insets = new Insets(0, 0, 5, 5);
        gbc_lblVersion.gridx = 0;
        gbc_lblVersion.gridy = 3;
        contentPane.add(lblVersion, gbc_lblVersion);

        versionTextField_1 = new AssistPopupTextField();
        if (!ValueWidget.isNullOrEmpty(mvnInstallConfig.getVersion())) {
            versionTextField_1.setText(mvnInstallConfig.getVersion());
        }
        GridBagConstraints gbc_versionTextField_1 = new GridBagConstraints();
        gbc_versionTextField_1.insets = new Insets(0, 0, 5, 0);
        gbc_versionTextField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_versionTextField_1.gridx = 1;
        gbc_versionTextField_1.gridy = 3;
        contentPane.add(versionTextField_1, gbc_versionTextField_1);
        versionTextField_1.setColumns(10);

        JLabel lblPackaging = new JLabel("packaging");
        GridBagConstraints gbc_lblPackaging = new GridBagConstraints();
        gbc_lblPackaging.anchor = GridBagConstraints.WEST;
        gbc_lblPackaging.insets = new Insets(0, 0, 5, 5);
        gbc_lblPackaging.gridx = 0;
        gbc_lblPackaging.gridy = 4;
        contentPane.add(lblPackaging, gbc_lblPackaging);

        packagingTextField_1 = new AssistPopupTextField("jar");
        if (!ValueWidget.isNullOrEmpty(mvnInstallConfig.getPackaging())) {
            packagingTextField_1.setText(mvnInstallConfig.getPackaging());
        }
        GridBagConstraints gbc_packagingTextField_1 = new GridBagConstraints();
        gbc_packagingTextField_1.insets = new Insets(0, 0, 5, 0);
        gbc_packagingTextField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_packagingTextField_1.gridx = 1;
        gbc_packagingTextField_1.gridy = 4;
        contentPane.add(packagingTextField_1, gbc_packagingTextField_1);
        packagingTextField_1.setColumns(10);

		/*JLabel label_1 = new JLabel("预留");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.WEST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 5;
		contentPane.add(label_1, gbc_label_1);

		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 5;
		contentPane.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);*/

        rdbtnPom = new JRadioButton("pom");
        rdbtnPom.addItemListener(this);
        btnGroup.add(rdbtnPom);
        GridBagConstraints gbc_rdbtnPom = new GridBagConstraints();
        gbc_rdbtnPom.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnPom.gridx = 0;
        gbc_rdbtnPom.gridy = 5;
        contentPane.add(rdbtnPom, gbc_rdbtnPom);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 1;
        gbc_scrollPane.gridy = 5;
        contentPane.add(scrollPane, gbc_scrollPane);

        pomXmlArea = new AssistPopupTextArea();
        pomXmlArea.placeHolder("<dependency>\n" +
                "            <groupId>com2.tdr.test</groupId>\n" +
                "            <artifactId>unicode_chinese</artifactId>\n" +
                "            <version>0.0.3-SNAPSHT</version>\n" +
                "        </dependency>");
        if (!ValueWidget.isNullOrEmpty(mvnInstallConfig.getPomDepency())) {
            pomXmlArea.setText(mvnInstallConfig.getPomDepency());
            if (ValueWidget.isNullOrEmpty(artifactIdTextField_1.getText2())) {
                rdbtnPom.setSelected(true);
            }
        }
        pomXmlArea.setLineWrap(true);
        pomXmlArea.setWrapStyleWord(true);
        scrollPane.setViewportView(pomXmlArea);
        Border border1 = BorderFactory.createEtchedBorder(Color.white,
                new Color(148, 145, 140));
        TitledBorder openFileTitle = new TitledBorder(border1, "pom xml格式");
        scrollPane.setBorder(openFileTitle);
        pomXmlArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                // System.out.println("remove");
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = pomXmlArea.getText2();
                if (!ValueWidget.isNullOrEmpty(text)) {
                    if (!rdbtnPom.isSelected()) {
                        rdbtnPom.setSelected(true);
                    }
                }
                // System.out.println("insert");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // System.out.println("change");
            }
        });

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.gridwidth = 2;
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 6;
        contentPane.add(panel, gbc_panel);

        final JButton button_1 = new JButton("安装");
        button_1.addActionListener(new ActionListener() {
            public File filterByPomDependency(java.util.List<File> files, String artifactId) {
                int size = files.size();
                for (int i = 0; i < size; i++) {
                    File file = files.get(i);
                    if (file.getAbsolutePath().contains(artifactId)) {
                        System.out.println("filterByPomDependency file22 :" + file);
                        return file;
                    }
                }
                return null;
            }

            public void actionPerformed(ActionEvent e) {
                String groupId = null;
                String artifactId = null;
                String version = null;
                String packaging = null;

                final String jarPath = jarTextField.getText().replace("\\", "/");
                if (isXml) {
                    String xml = pomXmlArea.getText();
                    final java.util.List<PomDependency> pomDependencies = XmlYunmaUtil.getPomDependencies(xml);
                    final int size = pomDependencies.size();
                    if (size == 1) {
                        PomDependency pomDependency = pomDependencies.get(0);
                        System.out.println(pomDependency);
                        groupId = pomDependency.getGroupId();
                        artifactId = pomDependency.getArtifactId();
                        version = pomDependency.getVersion();
                        packaging = pomDependency.getType();
                        if (ValueWidget.isNullOrEmpty(packaging)) {
                            packaging = pomDependency.getPackaging();
                        }
                    } else {
                        final java.util.List<File> files = FileUtils.getListFiles("", "jar");
                        int size2 = files.size();
                       /* for (int j = 0; j < size2; j++) {
                            System.out.println("file :" + files.get(j).getAbsolutePath());
                        }*/
//                        System.out.println("files :" + files);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                button_1.setEnabled(false);
                                pomXmlArea.setEditable(false);
                                for (int i = 0; i < size; i++) {
                                    PomDependency pomDependency = pomDependencies.get(i);
                                    File jarFile2 = filterByPomDependency(files, pomDependency.getArtifactId());

                                    if (null != jarFile2) {
                                        mvnInstall(pomDependency.getGroupId(), pomDependency.getArtifactId(), pomDependency.getVersion(), pomDependency.getPackaging(), jarFile2.getAbsolutePath());
                                    }
                                    System.out.println("-------------- ");
                                }
                                button_1.setEnabled(true);
                                pomXmlArea.setEditable(true);
                            }
                        }).start();
                        MavenTookitDialog.this.mvnInstallConfig.setPomDepency(xml);
                        return;
                    }

                } else {
                    if (!DialogUtil.verifyTFAndExist(jarTextField, "jar 文件")) {
                        return;
                    }

                    if (!DialogUtil.verifyTFEmpty(groupIdTextField_1, "groupId")) {
                        return;
                    }
                    if (!DialogUtil.verifyTFEmpty(artifactIdTextField_1, "artifactId")) {
                        return;
                    }
                    if (!DialogUtil.verifyTFEmpty(versionTextField_1, "version")) {
                        return;
                    }
                    if (!DialogUtil.verifyTFEmpty(packagingTextField_1, "packaging")) {
                        return;
                    }
                    groupId = groupIdTextField_1.getText();
                    artifactId = artifactIdTextField_1.getText();
                    version = versionTextField_1.getText();
                    packaging = packagingTextField_1.getText();
                }
                MavenTookitDialog.this.mvnInstallConfig.setGroupId(groupId)
                        .setArtifactId(artifactId)
                        .setVersion(version)
                        .setPackaging(packaging)
                        .setJarFilePath(jarPath)
                        .setPomDepency(pomXmlArea.getText());
                if (ValueWidget.isNullOrEmpty(packaging)) {
                    packaging = "jar";
                }
                final PomDependency pomDependency = new PomDependency();
                pomDependency.setGroupId(groupId)
                        .setArtifactId(artifactId)
                        .setVersion(version)
                        .setPackaging(packaging);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        button_1.setEnabled(false);
                        pomXmlArea.setEditable(false);
                        mvnInstall(pomDependency, jarPath);
                        button_1.setEnabled(true);
                        pomXmlArea.setEditable(true);
                    }
                }).start();
            }

            public void mvnInstall(PomDependency pomDependency, String jarPath) {
                mvnInstall(pomDependency.getGroupId(), pomDependency.getArtifactId(), pomDependency.getVersion(), pomDependency.getPackaging(), jarPath);

            }

            public void mvnInstall(String groupId, String artifactId, String version, String packaging, String jarPath) {
                if (ValueWidget.isNullOrEmpty(artifactId)) {
                    logger.error("artifactId is null");
                    return;
                }
                final String cmd = String.format(MAVEN_INSTALL_JAR_CMD, jarPath, groupId, artifactId, version, packaging);
                /**
                 * <dependency>
                 <groupId>com.huangkunlun</groupId>
                 <artifactId>clientutil</artifactId>
                 <version>1.0.0</version>
                 </dependency>
                 */
                final String dependency = String.format(MVN_INSTALL_DEPENDENCY, groupId, artifactId, version);
                ComponentUtil.appendResult(resultTextArea, cmd + SystemHWUtil.CRLF + SystemHWUtil.CRLF + dependency, false);
                System.out.println("cmd:" + cmd);
                System.out.println("jarPath :" + jarPath);

                try {
                    String result = CMDUtil.getResult4cmd(cmd);
                    ComponentUtil.appendResult(resultTextArea, SystemHWUtil.CRLF, false);
                    ComponentUtil.appendResult(resultTextArea, result, true);

                    if (ValueWidget.isNullOrEmpty(result)) {
                        result = "命令可能执行失败,请查看cmd命令行窗口";
                    } else {
                        ToastMessage.toast("安装完成!", 2000);
                    }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            button_1.setEnabled(true);
                    pomXmlArea.setEditable(true);
                            ToastMessage.toast(e1.getMessage(), 2000, Color.red);
                        }
                    }
        });
        panel.add(button_1);
        resultTextArea = new AssistPopupTextArea();
        JButton copyBtn = ComponentUtil.getCopyBtn(this.resultTextArea);
        panel.add(copyBtn);

        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.gridwidth = 2;
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 7;
        contentPane.add(panel_1, gbc_panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setEditable(false);
        resultTextArea.placeHolder("执行的命令");
        JScrollPane js = new JScrollPane(resultTextArea);
        panel_1.add(js);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MavenTookitDialog frame = new MavenTookitDialog(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if (!(e.getStateChange() == ItemEvent.SELECTED && source instanceof JRadioButton)) {
            return;
        }
            JRadioButton selectedRadio = (JRadioButton) source;
            if (selectedRadio == groupIdRadio) {
                isXml = false;
            } else {
                isXml = true;
            }
            System.out.println("isXml:" + isXml);
        }

    public MvnInstallConfig getMvnInstallConfig() {
        return mvnInstallConfig;
    }

    public void setMvnInstallConfig(MvnInstallConfig mvnInstallConfig) {
        this.mvnInstallConfig = mvnInstallConfig;
    }
}
