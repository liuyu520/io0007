package com.io.hw.awt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.common.util.SystemHWUtil;
import com.file.hw.filter.chain.FileFilterChain;
import com.file.hw.filter.impl.HiddenFileFilter;
import com.file.hw.filter.impl.NoHiddenFileFilter;
import com.file.hw.filter.impl.SuffixSpecifiedFileFilter;
import com.file.hw.filter.util.FilesScanner;
import com.io.hw.awt.color.CustomColor;
import com.io.hw.bean.ResultBean;
import com.io.hw.chain.ContentFilterChain;
import com.io.hw.exception.NoFilterException;
import com.io.hw.file.util.FullContentReplace;
import com.io.hw.impl.ContentFilterImpl;

public class FindSwing extends JPanel implements ItemListener, ActionListener,Runnable
{
    private static final long  serialVersionUID = -5524513474629121625L;
    public static final String SUFFIX_JS        = "js";
    public static final String SUFFIX_JAVA      = "java";
    public static final String SUFFIX_SGML      = "sgml";
    public static final String SUFFIX_ZUL       = "zul";
    public static final String SUFFIX_JSP       = "jsp";
    public static final String SUFFIX_HTML      = "html";
    public static final String SUFFIX_TXT       = "txt";
    public static final String SUFFIX_XML       = "xml";
    public static final String SUFFIX_WORD      = "word";
    public static final String SUFFIX_DSL       = "dsl";
    public static final String SUFFIX_CSS       = "css";
    public static final String SUFFIX_PHP       = "php";
    public static final String SUFFIX_C         = "c";
    public static final String SUFFIX_JAR       = "jar";
    public static final String SUFFIX_PYTHON    = "py";
    public static final String SUFFIX_CONF      = "conf";
    public static String[]     suffixStrs       =
                                                { SUFFIX_JS, SUFFIX_JAVA, SUFFIX_SGML, SUFFIX_ZUL, SUFFIX_JSP,
        SUFFIX_HTML, SUFFIX_TXT, SUFFIX_XML, SUFFIX_WORD, SUFFIX_DSL, SUFFIX_CSS, SUFFIX_PHP, SUFFIX_C, SUFFIX_JAR,
        SUFFIX_PYTHON                          };

    JLabel                     isFiltSuffixLabel;                        /*  */
    JPanel                     suffixSelectPanel;                        /*  */
    JTextField                 pathField;                                /*  */
    JRadioButton               isHideButtonYes;                          /*  */
    JRadioButton               isHideButtonNo;                           /*  */
    ArrayList<String>          suffixes;                                 /*  */
    JTextField                 findWordField;                            /*  */

    boolean                    isFiltSuffix     = false;                 /*  */
    boolean                    isHideFile       = false;                 /*  */

    JCheckBox                  jsButton;
    JCheckBox                  javaButton;
    JCheckBox                  sgmlButton;
    JCheckBox                  zulButton;
    JCheckBox                  jspButton;
    JCheckBox                  htmlButton;
    JCheckBox                  txtButton;
    JCheckBox                  xmlButton;
    JCheckBox                  wordButton;
    JCheckBox                  dslButton;
    JCheckBox                  cssButton;
    JCheckBox                  phpButton;
    JCheckBox                  cButton;
    JCheckBox                  jarButton;
    JCheckBox                  pythonButton;
    JCheckBox                  confButton;

    JPanel                     resultPanel;
    JButton                    findButton;                               /*  */
    BottomPanel                progressBar      = null;     /*  */
//    WaitThread                 waitThread;    
    private ArrayList<String >addSiffList=new ArrayList<String>();/*  */

    public FindSwing(BottomPanel                progressBar){
        this.progressBar=progressBar;
    }
    public void launchFrame()
    {

        Toolkit tk = Toolkit.getDefaultToolkit();
        //		String pathstr=.getPath();
//        System.out.println(ResultInfoPanel.class.getClassLoader().getSystemClassLoader().getSystemResource(""));
//        System.out.println(ResultInfoPanel.class.getClassLoader().getResource("/kisql.png"));
//        System.out.println(System.getProperty("user.dir"));
        String imagePath=System.getProperty("user.dir")+"\\src\\com\\kisql.png";
//        System.out.println("imagePath:"+imagePath);
        Image img = tk.getImage(imagePath);
        // ImageIcon icon = new ImageIcon("src/com/kisql.png");
        // Image img = icon.getImage();
//        this.setIconImage(img);
//        this.setTitle("Java");
        this.setVisible(true);
        this.setBounds(100, 200, 1200, 440);
        this.setBackground(Color.red);
//        Container c = this.getContentPane();
        // this.pack();
        JPanel conditionPanel = new JPanel();
        JPanel pathPanel = new JPanel();
        pathPanel.setBackground(new Color(255, 200, 200));
        JPanel isHidePanel = new JPanel();
        isHidePanel.setBackground(new Color(255, 100, 200));
        JPanel suffixFilterPanel = new JPanel();
        suffixSelectPanel = new JPanel();
        JPanel findPanel = new JPanel();
        resultPanel = new JPanel();
        JPanel resultInfoPanel = new JPanel();

        resultPanel.setSize(new Dimension(50, 10));
        resultPanel.add(resultInfoPanel);

        JLabel suffixSelectLabel = new JLabel("select suffix");
        suffixSelectLabel.addMouseListener(new MouseAdapter()
        {
            boolean isselect = false;

            @Override
            public void mouseClicked(MouseEvent e)
            {

                if (e.getClickCount() == 2)
                {
                    isselect = !isselect;
                    JCheckBox[] suffixButtons = new JCheckBox[]
                    { jsButton, javaButton, sgmlButton, zulButton, jspButton, htmlButton, txtButton, xmlButton,
                        wordButton, dslButton, cssButton, phpButton, cButton, jarButton };
                    super.mouseClicked(e);
                    for (int i = 0; i < suffixButtons.length; i++)
                    {
                        suffixButtons[i].setSelected(isselect);
                    }
                }
            }
        });
        // ButtonGroup suffixbuttonGroup = new ButtonGroup();
        jsButton = new JCheckBox("js", false);
        javaButton = new JCheckBox("java", false);
        sgmlButton = new JCheckBox("sgml", false);
        zulButton = new JCheckBox("zul", false);
        jspButton = new JCheckBox("jsp", false);
        htmlButton = new JCheckBox("html", false);
        txtButton = new JCheckBox("txt", false);
        xmlButton = new JCheckBox("xml", false);
        wordButton = new JCheckBox("word", false);
        dslButton = new JCheckBox("dsl", false);
        cssButton = new JCheckBox(SUFFIX_CSS, false);
        phpButton = new JCheckBox(SUFFIX_PHP, false);
        cButton = new JCheckBox(SUFFIX_C, false);
        jarButton = new JCheckBox(SUFFIX_JAR, false);
        pythonButton = new JCheckBox(SUFFIX_PYTHON, false);
        confButton = new JCheckBox(SUFFIX_CONF, false);

        jsButton.addActionListener(this);
        javaButton.addActionListener(this);
        sgmlButton.addActionListener(this);
        zulButton.addActionListener(this);
        jspButton.addActionListener(this);
        htmlButton.addActionListener(this);
        txtButton.addActionListener(this);
        xmlButton.addActionListener(this);
        wordButton.addActionListener(this);
        dslButton.addActionListener(this);
        cssButton.addActionListener(this);
        phpButton.addActionListener(this);
        cButton.addActionListener(this);
        jarButton.addActionListener(this);
        pythonButton.addActionListener(this);
        confButton.addActionListener(this);
        // suffixbuttonGroup.add(jsButton);
        // suffixbuttonGroup.add(javaButton);
        suffixSelectPanel.setBackground(new Color(100, 200, 255));
        suffixSelectPanel.setLayout(new FlowLayout());
        suffixSelectPanel.add(suffixSelectLabel);
        suffixSelectPanel.add(jsButton);
        suffixSelectPanel.add(javaButton);
        suffixSelectPanel.add(sgmlButton);
        suffixSelectPanel.add(zulButton);
        suffixSelectPanel.add(jspButton);
        suffixSelectPanel.add(htmlButton);
        suffixSelectPanel.add(txtButton);
        suffixSelectPanel.add(xmlButton);
        suffixSelectPanel.add(wordButton);
        suffixSelectPanel.add(dslButton);
        suffixSelectPanel.add(cssButton);
        suffixSelectPanel.add(phpButton);
        suffixSelectPanel.add(cButton);
        suffixSelectPanel.add(jarButton);
        suffixSelectPanel.add(pythonButton);
        suffixSelectPanel.add(confButton);
        suffixSelectPanel.setVisible(false);

        JLabel suffixLable = new JLabel();
        suffixLable.setText("suffix:");
        // suffixLable.setSize(200, 100);
        JToggleButton toggleSuffixButton = new JToggleButton("button");
        toggleSuffixButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                isFiltSuffix = !isFiltSuffix;
                String isFiltLabelStr = null;
                if (isFiltSuffix)
                {
                    isFiltLabelStr = "";
                    suffixSelectPanel.setVisible(true);
                }
                else
                {
                    isFiltLabelStr = "";
                    suffixSelectPanel.setVisible(false);
                }
                isFiltSuffixLabel.setText(isFiltLabelStr);

            }
        });
        isFiltSuffixLabel = new JLabel("whether filter suffix");
        suffixFilterPanel.setLayout(new FlowLayout(1));
        suffixFilterPanel.setBackground(new Color(200, 255, 200));
        suffixFilterPanel.add(suffixLable);
        suffixFilterPanel.add(toggleSuffixButton);
        suffixFilterPanel.add(isFiltSuffixLabel);
        suffixFilterPanel.setVisible(true);
        JLabel isHideLabel = new JLabel();
        isHideLabel.setText("whether hidden");

        ButtonGroup isHidebuttonGroup = new ButtonGroup();
        isHideButtonYes = new JRadioButton("hidden", false);
        isHideButtonNo = new JRadioButton("not hidden", true);
        isHideButtonYes.addItemListener(this);
        isHideButtonNo.addItemListener(this);

        isHidebuttonGroup.add(isHideButtonYes);
        isHidebuttonGroup.add(isHideButtonNo);
        isHidePanel.setLayout(new FlowLayout());
        isHidePanel.add(isHideLabel);
        isHidePanel.add(isHideButtonYes);
        isHidePanel.add(isHideButtonNo);

        pathPanel.setLayout(new FlowLayout());/*  */
        JLabel pathLabel = new JLabel();
        pathField = new JTextField(40);
        pathField.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                findButton.doClick();// 
            }
        });
        pathLabel.setText(" path");
        pathLabel.addMouseListener(new MouseAdapter()
        {
            /**
			 *  
			 */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    super.mouseClicked(e);
                    pathField.setText("path:");
                }
            }
        });

        JLabel findWordsLabel = new JLabel(" search word:");
        findWordField = new JTextField(30);
        findWordsLabel.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    super.mouseClicked(e);
                    findWordField.setText("search");
                }
            }
        });
        JButton browserButton = new JButton("browser");
        browserButton.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {

                JFileChooser file = new JFileChooser();
                file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int resule = file.showOpenDialog(new JPanel());
                System.out.println(" :" + resule);
                // if (resule == JFileChooser.DIRECTORIES_ONLY) {
                // String fileName = file.getSelectedFile().getName();
                File selectedFile = file.getSelectedFile();
                String dir = null;
                if (selectedFile != null)
                {
                    dir = selectedFile.getAbsolutePath();
                    /**
					 *  
					 */
                    int isOpen = JOptionPane.showConfirmDialog(null, dir, " ", JOptionPane.YES_OPTION);
                    // System.out.println("open:"+isOpen);
                    if (isOpen == 0)
                    {
                        System.out.println(" ");
                        // String fullContent = FileUtils.getFullContent(dir);
                        pathField.setText(dir);
                        // System.out.println(fullContent);
                        // textArea.setText(fullContent);
                    }
                    else if (isOpen == 1)
                    {
                        System.out.println(" ...");
                    }
                }

            }

        });

        pathPanel.add(pathLabel);
        pathPanel.add(pathField);
        pathPanel.add(browserButton);

        pathPanel.add(findWordsLabel);
        pathPanel.add(this.findWordField);
        findWordField.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                findButton.doClick();//  

            }
        });

        findButton = new JButton();
        findPanel.setBackground(new Color(255, 200, 122));
        findButton.setText("search ");
        JButton clearButton = new JButton(" clean");
        findButton.addActionListener(new MyActionListener(this));
        findPanel.setLayout(new FlowLayout(1));//  
        findButton.addMouseListener(new MouseAdapter()
        {

            /*
             * @Override public void mouseReleased(MouseEvent e) {
             * waitThread.isStop = !waitThread.isStop; //
             * getProgressBar().stop(); }
             */

            @Override
            public void mousePressed(MouseEvent e)
            {
                //				waitThread = new WaitThread(progressBar);
                // if(waitThread.isAlive()){
                // waitThread.destroy();
                //				waitThread.start();/* run   */
                // getProgressBar().continueTime();
            }
        });
        findPanel.setSize(new Dimension(5, 1));
        findPanel.setLayout(new FlowLayout(1));
        findPanel.add(findButton);
        findPanel.add(clearButton);
        final JTextField addSiffTF = new JTextField(10);
        JButton addSiffButton = new JButton("add siffex");
        findPanel.add(addSiffTF);
        findPanel.add(addSiffButton);
        addSiffButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String suffix2 = addSiffTF.getText();
                if (suffix2 != null && (!"".equals(suffix2.trim())))
                {
                    addSiffList.add(suffix2);
                    addSiffTF.setText("");
                }

            }
        });
        clearButton.addActionListener(new ClearActionListener(this));
        conditionPanel.setLayout(new GridLayout(5, 1));
        conditionPanel.add(pathPanel);
        conditionPanel.add(isHidePanel);
        conditionPanel.add(suffixFilterPanel);
        conditionPanel.add(suffixSelectPanel);
        conditionPanel.add(findPanel);
        this.add(conditionPanel, BorderLayout.NORTH);
        // c.add(findPanel, BorderLayout.CENTER);
        JScrollPane js = new JScrollPane(resultPanel);
        js.setFocusable(true);
        this.add(js, BorderLayout.CENTER);
//        this.add(progressBar, BorderLayout.SOUTH);
        pathField.requestFocus();//  
        this.validate();// refresh the panel
    }

//    public static void main(String[] args)
//    {
//        new Thread(new FindSwing()).start();
//    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource() == this.isHideButtonNo)
        {
            isHideFile = false;
        }
        else if (e.getSource() == this.isHideButtonYes)
        {
            isHideFile = true;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        /*
         * if (e.getSource() == this.jsButton) { System.out.println("js"); }
         * else if (e.getSource() == this.javaButton) {
         * System.out.println("java"); } else if (e.getSource() ==
         * this.sgmlButton) { System.out.println("sgml"); } else if
         * (e.getSource() == this.zulButton) { System.out.println("zul"); } else
         * if (e.getSource() == this.jspButton) { System.out.println("jsp"); }
         * else if (e.getSource() == this.htmlButton) {
         * System.out.println("html"); } else if (e.getSource() ==
         * this.txtButton) { System.out.println("txt"); } else if (e.getSource()
         * == this.xmlButton) { System.out.println("xml"); } else if
         * (e.getSource() == this.wordButton) { System.out.println("word"); }
         * else if (e.getSource() == this.dslButton) {
         * System.out.println("dsl"); } else if (e.getSource() ==
         * this.cssButton) { System.out.println(SUFFIX_CSS); }
         */
    }

    public ArrayList<String> getSuffixes()
    {

        if (isFiltSuffix)
        {
            suffixes = new ArrayList<String>();
            if (this.jsButton.isSelected())
            {
                suffixes.add(SUFFIX_JS);
            }
            if (this.javaButton.isSelected())
            {
                suffixes.add(SUFFIX_JAVA);
            }
            if (this.sgmlButton.isSelected())
            {
                suffixes.add(SUFFIX_SGML);
            }
            if (this.zulButton.isSelected())
            {
                suffixes.add(SUFFIX_ZUL);
            }
            if (this.jspButton.isSelected())
            {
                suffixes.add(SUFFIX_JSP);
            }
            if (this.htmlButton.isSelected())
            {
                suffixes.add(SUFFIX_HTML);
            }
            if (this.txtButton.isSelected())
            {
                suffixes.add(SUFFIX_TXT);
            }
            if (this.xmlButton.isSelected())
            {
                suffixes.add(SUFFIX_XML);
            }
            if (this.wordButton.isSelected())
            {
                suffixes.add(SUFFIX_WORD);
            }
            if (this.dslButton.isSelected())
            {
                suffixes.add(SUFFIX_DSL);
            }
            if (this.cssButton.isSelected())
            {
                suffixes.add(SUFFIX_CSS);
            }
            if (this.phpButton.isSelected())
            {
                suffixes.add(SUFFIX_PHP);
            }
            if (this.cButton.isSelected())
            {
                suffixes.add(SUFFIX_C);
            }
            if (this.jarButton.isSelected())
            {
                suffixes.add(SUFFIX_JAR);
            }
            if (this.pythonButton.isSelected())
            {
                suffixes.add(SUFFIX_PYTHON);
            }
            if (this.confButton.isSelected())
            {
                suffixes.add(SUFFIX_CONF);
            }
            if (suffixes == null || suffixes.size() == 0)
            {
                this.suffixes = new ArrayList<String>();
                for (int i = 0; i < suffixStrs.length; i++)
                {
                    this.suffixes.add(suffixStrs[i]);
                }
            }
        }
        else
        {
            // TODO  
            // if (null != this.suffixes && suffixes.size() > 0) {
            // this.suffixes.clear();
            // }
            this.suffixes = new ArrayList<String>();
            for (int i = 0; i < suffixStrs.length; i++)
            {
                this.suffixes.add(suffixStrs[i]);
            }
        }
        suffixes.addAll(addSiffList);
        return suffixes;
    }

    class MyActionListener implements ActionListener
    {
        FindSwing frame;

        public MyActionListener()
        {
            super();
        }

        public MyActionListener(FindSwing frame)
        {
            super();
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            progressBar.startProgress();
            //			System.out.println(isFiltSuffix);
            resultPanel.removeAll();
            JLabel findingLabel = new JLabel("  ...");
            findingLabel.setFont(new Font(" ", Font.BOLD, 30));
            findingLabel.setForeground(Color.red);/*   */
            resultPanel.add(findingLabel);/*   " */
            ArrayList<ResultBean> resultBeans = new ArrayList<ResultBean>();
            JPanel innerPanle = null;
            // if (innerPanle != null) {

            // this.frame.getContentPane().validate();
            String filePath = pathField.getText();
            if (filePath == null || "".endsWith(filePath))
            {
                return;
            }
            if (filePath.indexOf("\\") != 2)
            {
                filePath = filePath.substring(0, 2) + "\\" + filePath.substring(2);
            }
            // System.out.println(filePath);
            FilesScanner filesScanner = new FilesScanner();
            FileFilterChain fileFilterChain = new FileFilterChain();
            if (!isHideFile)
            {
                System.out.println("no hide");
                fileFilterChain.removeFilter(new HiddenFileFilter());
                fileFilterChain.add(new NoHiddenFileFilter());//  
            }
            else
            {
                System.out.println("hide");
                fileFilterChain.removeFilter(new NoHiddenFileFilter());
                fileFilterChain.add(new HiddenFileFilter());
            }

            fileFilterChain.add(new SuffixSpecifiedFileFilter(getSuffixes()));

            // fileFilterChain.add(new SuffixSpecifiedFileFilter("*"));
            filesScanner.setFileFilterChain(fileFilterChain);
            List<File> files = filesScanner.getAllFilesAfterFilt(filePath);
            FullContentReplace fullContentReplace = new FullContentReplace();
            ContentFilterChain contentFilterChain = new ContentFilterChain();
            try
            {
                contentFilterChain.add(new ContentFilterImpl(findWordField.getText(), null));
            }
            catch (NoFilterException e1)
            {
                e1.printStackTrace();
            }
            fullContentReplace.setFilterChain(contentFilterChain);
            // System.out.println("----------------------------------------------------------------------------------------------");
            for (int i = 0; i < files.size(); i++)
            {
                File file = files.get(i);
                ResultBean resultBean = null;
                try
                {
                    resultBean = fullContentReplace.getFullContent(file,SystemHWUtil.CHARSET_UTF);
                }
                catch (NoFilterException e2)
                {
                    e2.printStackTrace();
                }
                if (resultBean != null && resultBean.getChangedRow() > 0)
                {
                    resultBeans.add(resultBean);

                    // System.out.println(file.getName()+"\t\t\t"+resultBean.getChangedRow()+"\t"+resultBean.getFile().getAbsolutePath());
                }
            }
            if (resultBeans.size() == 0)
            {
                resultPanel.removeAll();
                JLabel notFoundLabel = new JLabel("  ...");
                notFoundLabel.setFont(new Font(" ", Font.BOLD, 30));
                notFoundLabel.setForeground(Color.red);
                notFoundLabel.setSize(100, 100);
                resultPanel.add(notFoundLabel);
            }
            else
            {
                resultPanel.removeAll();
                // }
                resultPanel.setLayout(new FlowLayout(0));
                resultPanel.setLayout(new GridLayout(resultBeans.size(), 1));
                for (int i = 0; i < resultBeans.size(); i++)
                {
                    ResultBean resultBean = resultBeans.get(i);
                    innerPanle = new JPanel();
                    innerPanle.setBackground(CustomColor.getColor());
                    innerPanle.setLayout(new FlowLayout(0));
                    innerPanle.add(new ResultInfoPanel(resultBean.getFile().getName(), resultBean.getRows(), resultBean
                            .getFile().getAbsolutePath()));
                    resultPanel.add(innerPanle);
                }
            }
//             this.frame.getProgressBar().stop();
//            if (waitThread != null)
//            {
//                waitThread.isStop = !waitThread.isStop;
//            }
        }
    }

    

    class ClearActionListener implements ActionListener
    {

        public ClearActionListener()
        {
            super();
        }

        public ClearActionListener(FindSwing findSwing)
        {
            super();
            this.findSwing = findSwing;
        }

        FindSwing findSwing;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            resultPanel.removeAll();
        }

    }

//    class WaitThread extends Thread
//    {
//        public boolean isStop = true;
//        BottomPanel    progressBar;
//
//        public WaitThread(BottomPanel progressBar)
//        {
//            this.progressBar = progressBar;
//        }
//
//        @Override
//        public void run()
//        {
//            if (!progressBar.getTime().isRunning())
//            {
//                this.progressBar.startProgress();
//            }
//            while (isStop)
//            {
//                // this.progressBar.is
//                this.progressBar.continueTime();
//            }
//            if (progressBar.getTime().isRunning())
//            {
//                this.progressBar.stopProgress();
//            }
//        }
//        // public
//
//    }

    public BottomPanel getProgressBar()
    {
        return progressBar;
    }

    public void setProgressBar(BottomPanel progressBar)
    {
        this.progressBar = progressBar;
    }

    @Override
    public void run()
    {
        launchFrame();
        
    }

}
