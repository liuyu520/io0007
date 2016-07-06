package com.swing.dialog;

import com.cmd.dos.hw.util.CMDUtil;
import com.string.widget.util.ValueWidget;
import com.swing.component.AssistPopupTextArea;
import com.swing.component.AssistPopupTextField;
import com.swing.component.GenerateJsonTextField;
import com.swing.dialog.toast.ToastMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MavenTookitDialog extends GenericDialog {

    public static final String MAVEN_INSTALL_JAR_CMD = "mvn install:install-file  -Dfile=%s  -DgroupId=%s  -DartifactId=%s -Dversion=%s -Dpackaging=%s";
    private static final long serialVersionUID = -3076948674378465960L;
    private final GenerateJsonTextField jarTextField;
    private final AssistPopupTextField groupIdTextField_1;
    private final AssistPopupTextField artifactIdTextField_1;
    private final AssistPopupTextField versionTextField_1;
    private final AssistPopupTextField packagingTextField_1;
    private AssistPopupTextField textField_1;
    private AssistPopupTextArea resultTextArea;

    /**
     * Launch the application.
     */
    /*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MavenTookitDialog frame = new MavenTookitDialog();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

    /**
     * Create the frame.
     */
    public MavenTookitDialog() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("安装本地jar");
        setModal(true);
//		setBounds(100, 100, 450, 300);
        setLoc(450, 300);
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
        drag(jarTextField);
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 0);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 0;
        contentPane.add(jarTextField, gbc_textField);
        jarTextField.setColumns(10);

        JLabel lblNewLabel = new JLabel("groupId");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);

        groupIdTextField_1 = new AssistPopupTextField();
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
        
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 2;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 5;
        contentPane.add(scrollPane, gbc_scrollPane);
        
        JTextArea textArea = new JTextArea();
        scrollPane.setViewportView(textArea);

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.gridwidth = 2;
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 6;
        contentPane.add(panel, gbc_panel);

        JButton button_1 = new JButton("安装");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!DialogUtil.verifyTFAndExist(jarTextField, "jar 文件")) {
                    return;
                }
                String jarPath = jarTextField.getText();
                jarPath = jarPath.replace("\\", "/");
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
                String groupId = groupIdTextField_1.getText();
                String artifactId = artifactIdTextField_1.getText();
                String version = versionTextField_1.getText();
                String packaging = packagingTextField_1.getText();
                if (ValueWidget.isNullOrEmpty(packaging)) {
                    packaging = "jar";
                }
                String cmd = String.format(MAVEN_INSTALL_JAR_CMD, jarPath, groupId, artifactId, version, packaging);
//				System.out.println(cmd);
                resultTextArea.setText(cmd);
                try {
                    CMDUtil.getResult4cmd(cmd);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    ToastMessage.toast(e1.getMessage(), 2000, Color.red);
                }

            }
        });
        panel.add(button_1);

        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.gridwidth = 2;
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 7;
        contentPane.add(panel_1, gbc_panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));
        resultTextArea = new AssistPopupTextArea();
        JScrollPane js = new JScrollPane(resultTextArea);
        panel_1.add(js);
    }

}
