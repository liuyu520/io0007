package com.swing.image;

import com.swing.component.ComponentUtil;
import com.swing.dialog.DialogUtil;
import com.swing.dialog.GenericDialog;
import com.swing.dialog.toast.ToastMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EditSnapShootDialog extends GenericDialog implements ItemListener {

    private JPanel contentPane;
    private BufferedImage bufferedImage;
    private Graphics2D g2d;
    /***
     * 原始图片,没有经过scale的
     */
    private BufferedImage originImage;
    private JLabel imageLabel;
    private int x, y, xEnd, yEnd;    //记录鼠标坐标
    private BufferedImage bi;        //用于双缓冲
    private JRadioButton metricRadioButton;
    private JRadioButton lineRadioButton;
    private JRadioButton circleRadioButton;
    /***
     * 1:长方形;<br />2:线;<br />3:圆
     */
    private int type = 1;
    private Color shapeColor = Color.red;
    private JRadioButton redRadioButton;
    private JRadioButton blueRadioButton;
    private JRadioButton greenRadioButton;
    /***
     * 线条的粗细
     */
    private JSpinner spinner;

    /**
     * Create the frame.
     */
    public EditSnapShootDialog(BufferedImage img, BufferedImage originImage, Graphics2D g2d) {
        setTitle("编辑截图(后续增加支持箭头)");
        this.bufferedImage = img;
        this.originImage = originImage;
        this.g2d = g2d;
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setBounds(100, 100, 650, 300);
        setLoc(650, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 306, 100/*save 按钮所在panel的宽度*/, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JLabel label = new JLabel("颜色");
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.gridx = 0;
        gbc_label.gridy = 0;
        contentPane.add(label, gbc_label);

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.anchor = GridBagConstraints.WEST;
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.VERTICAL;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 0;
        contentPane.add(panel, gbc_panel);

        redRadioButton = new JRadioButton("红色");
        redRadioButton.setSelected(true);
        panel.add(redRadioButton);

        blueRadioButton = new JRadioButton("蓝色");
        panel.add(blueRadioButton);

        greenRadioButton = new JRadioButton("绿色");
        panel.add(greenRadioButton);
        redRadioButton.addItemListener(this);
        blueRadioButton.addItemListener(this);
        greenRadioButton.addItemListener(this);
        ButtonGroup colorButtonGroup = new ButtonGroup();
        colorButtonGroup.add(redRadioButton);
        colorButtonGroup.add(blueRadioButton);
        colorButtonGroup.add(greenRadioButton);

        JSeparator separator = new JSeparator();
        panel.add(separator);

        spinner = new JSpinner();
        spinner.setValue(1);
        spinner.setToolTipText("线条粗细");
        panel.add(spinner);

        JPanel panel_4 = new JPanel();
        GridBagConstraints gbc_panel_4 = new GridBagConstraints();
        gbc_panel_4.gridheight = 2;
        gbc_panel_4.insets = new Insets(0, 0, 5, 0);
        gbc_panel_4.fill = GridBagConstraints.BOTH;
        gbc_panel_4.gridx = 2;
        gbc_panel_4.gridy = 0;
        contentPane.add(panel_4, gbc_panel_4);
        panel_4.setLayout(new GridLayout(2, 1, 0, 0));

        JButton btnSave = new JButton("save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (imageLabel.getIcon() == null) {
                    JOptionPane.showMessageDialog(EditSnapShootDialog.this, "没有图片信息，请先截图", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

//				File file = null;
                //取得imageLabel中的图像
                EditSnapShootDialog.this.g2d.dispose();
//                Image img = ((ImageIcon) imageLabel.getIcon()).getImage();
//                File file = new File("/Users/whuanghkl/Pictures/personal/aaa.jpg");
                String format = "jpg";
                File selectedFile = DialogUtil.chooseFileDialog(null, "保存截图", EditSnapShootDialog.this, format);
                if (null == selectedFile) {
                    return;
                }
                try {
                    ImageIO.write((BufferedImage) EditSnapShootDialog.this.bufferedImage, format/*"jpg"*/, selectedFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel_4.add(btnSave);

        /*JButton rollbackButton = new JButton("还原"); //TODO
        panel_4.add(rollbackButton);*/

        JButton copy2clipButton = new JButton("复制");
        copy2clipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditSnapShootDialog.this.g2d.dispose();
                ComponentUtil.setClipboardImage(EditSnapShootDialog.this.getContentPane(), EditSnapShootDialog.this.bufferedImage);
                ToastMessage.toast("复制图片到剪切板", 3000);
            }
        });
        panel_4.add(copy2clipButton);

        JLabel label_1 = new JLabel("形状");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_1.gridx = 0;
        gbc_label_1.gridy = 1;
        contentPane.add(label_1, gbc_label_1);

        JPanel panel_1 = new JPanel();
        metricRadioButton = new JRadioButton("长方形");
        metricRadioButton.setSelected(true);
        panel_1.add(metricRadioButton);
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.anchor = GridBagConstraints.WEST;
        gbc_panel_1.insets = new Insets(0, 0, 5, 5);
        gbc_panel_1.fill = GridBagConstraints.VERTICAL;
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 1;
        contentPane.add(panel_1, gbc_panel_1);

        lineRadioButton = new JRadioButton("直线");
        panel_1.add(lineRadioButton);

        circleRadioButton = new JRadioButton("圆");
        panel_1.add(circleRadioButton);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(metricRadioButton);
        buttonGroup.add(lineRadioButton);
        buttonGroup.add(circleRadioButton);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 3;
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 2;
        contentPane.add(scrollPane, gbc_scrollPane);

        JPanel panel_3 = new JPanel();
        scrollPane.setViewportView(panel_3);

        imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(this.originImage));
        panel_3.add(imageLabel);
        metricRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (metricRadioButton.isSelected()) {
                    type = 1;
                }
            }
        });
        lineRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (lineRadioButton.isSelected()) {
                    type = 2;
                }
            }
        });

        circleRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (circleRadioButton.isSelected()) {
                    type = 3;
                }
            }
        });

        imageLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //鼠标按下的点，作为画线的最初的起点
                x = e.getX();
                y = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                //该方法只用作画线或画圆时处理
                //鼠标弹起时需要将最后定为的图像 bi，调用imageLabel.setIcon()方法，设置进去。
                //这样就可以将线段或圆真的画进去了。为了使用变量 bi 将其转为 该类的private变量
                imageLabel.setIcon(new ImageIcon(bi));
//                    EditSnapShootDialog.this.g2d.drawImage(bi2, 0, 0, null);
                    /*if (type==2) {
                        EditSnapShootDialog.this.g2d.drawLine(x, y, xEnd, yEnd);    //Java中没有提供点的绘制，使用起点和终点为同一个点的画线代替
                    }else
                    if (type==3) {
                        EditSnapShootDialog.this.g2d.drawOval(Math.min(x, xEnd), Math.min(y, yEnd), Math.abs(xEnd - x), Math.abs(yEnd - y));
                    }*/

                drawCustom(EditSnapShootDialog.this.g2d);
            }
        });
        imageLabel.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {

                xEnd = e.getX();
                yEnd = e.getY();

                //鼠标移动时，在imageLabel展示的图像中，绘制点
                //1. 取得imageLabel中的图像
                Image img = ((ImageIcon) imageLabel.getIcon()).getImage();

                //2. 创建一个缓冲图形对象 bi
                bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = (Graphics2D) bi.getGraphics();
//                    bi2 = new BufferedImage(EditSnapShootDialog.this.bufferedImage.getWidth(null), EditSnapShootDialog.this.bufferedImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
//                    Graphics2D g2d2 = (Graphics2D) bi2.getGraphics();
                //3. 将截图的原始图像画到 bi
                g2d.drawImage(img, 0, 0, null);
//                    g2d2.drawImage(SnapShoot.this.bufferedImage, 0, 0, null);
                //4. 在鼠标所在的点，画一个点

                EditSnapShootDialog.this.g2d.setColor(Color.RED);

                drawCustom(g2d);

                g2d.dispose();
//                    g2d2.dispose();
                //5. 不需要保留在鼠标拖动过程中所画的线段，所以直接使用imageLabel.getGraphics()绘制
                //这样imageLabel.getIcon()并没有被改变，所以每次都只到原始截图和多了一条线，即为最后效果的演示
                Graphics g = imageLabel.getGraphics();
                g.drawImage(bi, 0, 0, null);    //将处理后的图片，画到imageLabel
//                    EditSnapShootDialog.this.g2d.dispose();
//                    EditSnapShootDialog.this.g2d=(Graphics2D)EditSnapShootDialog.this.bufferedImage.getGraphics();
                g.dispose();
            }

            public void mouseMoved(MouseEvent e) {

            }
        });
        setVisible(true);
    }

    public void drawCustom(Graphics2D g2d) {
        if (null == shapeColor) {
            g2d.setColor(Color.RED);    //设置画笔颜色为红色
        } else {
            g2d.setColor(shapeColor);
        }
        g2d.setStroke(new BasicStroke((Integer) spinner.getValue()));
        if (type == 1) {
            g2d.drawRect(Math.min(x, xEnd), Math.min(y, yEnd), Math.abs(xEnd - x), Math.abs(yEnd - y));
//                        g2d2.drawLine(x, y, xEnd, yEnd);
        } else if (type == 2) {
            g2d.drawLine(x, y, xEnd, yEnd);    //Java中没有提供点的绘制，使用起点和终点为同一个点的画线代替
//                        g2d2.drawLine(x, y, xEnd, yEnd);
        } else if (type == 3) {
            //因为如果鼠标向上，或向左移动时，xEnd > x, yEnd > y ，所以画圆的起点要取两者中的较小的，
            //而宽度和高度是不能  < 0 的，所以取绝对值
            g2d.drawOval(Math.min(x, xEnd), Math.min(y, yEnd), Math.abs(xEnd - x), Math.abs(yEnd - y));
//                        g2d2.drawOval(Math.min(x, xEnd), Math.min(y, yEnd), Math.abs(xEnd - x), Math.abs(yEnd - y));
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (redRadioButton.isSelected()) {
            shapeColor = Color.red;
        } else if (blueRadioButton.isSelected()) {
            shapeColor = Color.blue;
        } else if (greenRadioButton.isSelected()) {
            shapeColor = Color.green;
        }
    }
}
