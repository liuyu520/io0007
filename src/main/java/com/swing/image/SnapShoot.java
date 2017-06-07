package com.swing.image;

import com.swing.dialog.GenericDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 屏幕截图小程序
 *
 * @author pengranxiang
 */
public class SnapShoot extends GenericDialog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JButton snapButton;
    private JLabel imageLabel;

    private int x, y, xEnd, yEnd;    //记录鼠标坐标

    private boolean isDoodle;        //涂鸦开关
    private boolean isLine;            //画线开关
    private boolean isCircle;        //画圆开关

    private BufferedImage bi;        //用于双缓冲

    private ButtonGroup buttonGroup;    //管理Radio开关
    private JRadioButton doodleButton;
    private JRadioButton lineButton;
    private JRadioButton circleButton;

    private JButton saveButton;
    private JFileChooser chooser;
    private BufferedImage bufferedImage;
    private Graphics2D g2d;
    /***
     * 原始图片,没有经过scale的
     */
    private BufferedImage originImage;

    public SnapShoot(BufferedImage img, BufferedImage originImage, Graphics2D g2d) {
        this.bufferedImage = img;
        this.originImage = originImage;
        this.g2d = g2d;
        initUI();
        initLayout();
        createAction();

        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setTitle("截图小工具");
        this.setLocationRelativeTo(null);    //居中


        imageLabel.setIcon(new ImageIcon(this.originImage));
        this.setVisible(true);
    }

    private void initUI() {
        snapButton = new JButton("开始截图（点右键退出）");
        imageLabel = new JLabel();

        buttonGroup = new ButtonGroup();
        doodleButton = new JRadioButton("涂鸦");
        lineButton = new JRadioButton("画线");
        circleButton = new JRadioButton("画圆");

        buttonGroup.add(doodleButton);
        buttonGroup.add(lineButton);
        buttonGroup.add(circleButton);

        saveButton = new JButton("保存");
        chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        chooser.setFileFilter(filter);
    }

    private void initLayout() {
        JPanel pane = new JPanel();
        pane.add(imageLabel);
        JScrollPane imgScrollPane = new JScrollPane(pane);

        Container container = this.getContentPane();
        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup();
        hGroup
                .addGroup(layout.createSequentialGroup()/*.addComponent(snapButton)*/.addComponent(doodleButton).addComponent(lineButton).addComponent(circleButton).addComponent(saveButton))
                .addComponent(imgScrollPane);
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)/*.addComponent(snapButton)*/.addComponent(doodleButton).addComponent(lineButton).addComponent(circleButton).addComponent(saveButton))
                .addComponent(imgScrollPane);
        layout.setVerticalGroup(vGroup);
    }

    private void createAction() {
        /*snapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				try {
					//开启模拟屏幕，将显示截图的目标组件传入
					new ScreenWindow(imageLabel);
				} catch (AWTException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});*/

        doodleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (doodleButton.isSelected()) {
                    isDoodle = true;
                    isLine = false;
                    isCircle = false;
                }
            }
        });

        lineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (lineButton.isSelected()) {
                    isDoodle = false;
                    isLine = true;
                    isCircle = false;
                }
            }
        });

        circleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (circleButton.isSelected()) {
                    isDoodle = false;
                    isLine = false;
                    isCircle = true;
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (imageLabel.getIcon() == null) {
                    JOptionPane.showMessageDialog(SnapShoot.this, "没有图片信息，请先截图", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

//				File file = null;
                //取得imageLabel中的图像
                SnapShoot.this.g2d.dispose();
//                Image img = ((ImageIcon) imageLabel.getIcon()).getImage();
                File file = new File("/Users/whuanghkl/Pictures/personal/aaa.jpg");
                try {
                    ImageIO.write((BufferedImage) SnapShoot.this.bufferedImage, "jpg"/*"jpg"*/, file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
				/*int returnVal = chooser.showOpenDialog(SnapShoot.this);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       file = chooser.getSelectedFile();
			    }
		
			    //取得imageLabel中的图像
			    Image img = ((ImageIcon)imageLabel.getIcon()).getImage();
			    
				try {
					if(file != null)  {
						ImageIO.write((BufferedImage)img, "png", file);
						JOptionPane.showMessageDialog(SnapShoot.this, "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}*/
            }
        });

        imageLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //鼠标按下的点，作为画线的最初的起点
                x = e.getX();
                y = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                if (isLine || isCircle) {
                    //该方法只用作画线或画圆时处理
                    //鼠标弹起时需要将最后定为的图像 bi，调用imageLabel.setIcon()方法，设置进去。
                    //这样就可以将线段或圆真的画进去了。为了使用变量 bi 将其转为 该类的private变量
                    imageLabel.setIcon(new ImageIcon(bi));
//                    SnapShoot.this.g2d.drawImage(bi2, 0, 0, null);
                    if (isLine) {
                        SnapShoot.this.g2d.drawLine(x, y, xEnd, yEnd);    //Java中没有提供点的绘制，使用起点和终点为同一个点的画线代替
                    }
                    if (isCircle) {
                        SnapShoot.this.g2d.drawOval(Math.min(x, xEnd), Math.min(y, yEnd), Math.abs(xEnd - x), Math.abs(yEnd - y));
                    }
                }
            }
        });

        imageLabel.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {

                if (isDoodle) {    //涂鸦开关
                    xEnd = e.getX();
                    yEnd = e.getY();

                    //鼠标移动时，在imageLabel展示的图像中，绘制点
                    //1. 取得imageLabel中的图像
                    Image img = ((ImageIcon) imageLabel.getIcon()).getImage();

                    //2. 创建一个缓冲图形对象 bi
                    bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
//                    g2d = (Graphics2D) bi.getGraphics();

                    //3. 将截图的原始图像画到 bi
                    g2d.drawImage(img, 0, 0, null);

                    //4. 在鼠标所在的点，画一个点
                    g2d.setColor(Color.RED);    //设置画笔颜色为红色
                    g2d.drawLine(x, y, xEnd, yEnd);    //Java中没有提供点的绘制，使用起点和终点为同一个点的画线代替

                    g2d.dispose();

                    //5. 为了保留每一个点，不能直接使用imageLabel.getGraphics()来画，
                    //需要使用imageLabel.setIcon()来直接将画了点的图像，设置到imageLabel中，
                    //这样，在第一步中，取得img时，就为已经划过上一个点的图像了。
                    imageLabel.setIcon(new ImageIcon(bi));

                    //下次画线起点是设置为这次画线的终点
                    x = xEnd;
                    y = yEnd;
                }

                if (isLine || isCircle) {    //画线，画圆开关，两个很像，放一起了
                    xEnd = e.getX();
                    yEnd = e.getY();

                    //鼠标移动时，在imageLabel展示的图像中，绘制点
                    //1. 取得imageLabel中的图像
                    Image img = ((ImageIcon) imageLabel.getIcon()).getImage();

                    //2. 创建一个缓冲图形对象 bi
                    bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = (Graphics2D) bi.getGraphics();
//                    bi2 = new BufferedImage(SnapShoot.this.bufferedImage.getWidth(null), SnapShoot.this.bufferedImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
//                    Graphics2D g2d2 = (Graphics2D) bi2.getGraphics();
                    //3. 将截图的原始图像画到 bi
                    g2d.drawImage(img, 0, 0, null);
//                    g2d2.drawImage(SnapShoot.this.bufferedImage, 0, 0, null);
                    //4. 在鼠标所在的点，画一个点
                    g2d.setColor(Color.RED);    //设置画笔颜色为红色
                    SnapShoot.this.g2d.setColor(Color.RED);

                    if (isLine) {
                        g2d.drawLine(x, y, xEnd, yEnd);    //Java中没有提供点的绘制，使用起点和终点为同一个点的画线代替
//                        g2d2.drawLine(x, y, xEnd, yEnd);
                    }
                    if (isCircle) {
                        //因为如果鼠标向上，或向左移动时，xEnd > x, yEnd > y ，所以画圆的起点要取两者中的较小的，
                        //而宽度和高度是不能  < 0 的，所以取绝对值
                        g2d.drawOval(Math.min(x, xEnd), Math.min(y, yEnd), Math.abs(xEnd - x), Math.abs(yEnd - y));
//                        g2d2.drawOval(Math.min(x, xEnd), Math.min(y, yEnd), Math.abs(xEnd - x), Math.abs(yEnd - y));
                    }

                    g2d.dispose();
//                    g2d2.dispose();
                    //5. 不需要保留在鼠标拖动过程中所画的线段，所以直接使用imageLabel.getGraphics()绘制
                    //这样imageLabel.getIcon()并没有被改变，所以每次都只到原始截图和多了一条线，即为最后效果的演示
                    Graphics g = imageLabel.getGraphics();
                    g.drawImage(bi, 0, 0, null);    //将处理后的图片，画到imageLabel
//                    SnapShoot.this.g2d.dispose();
//                    SnapShoot.this.g2d=(Graphics2D)SnapShoot.this.bufferedImage.getGraphics();
                    g.dispose();
                }
            }

            public void mouseMoved(MouseEvent e) {

            }
        });
    }

	/*public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}
		new SnapShoot();
	}*/
}