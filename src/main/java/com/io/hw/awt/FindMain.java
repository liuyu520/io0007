package com.io.hw.awt;

import com.common.util.SystemHWUtil;
import com.file.hw.filter.chain.FileFilterChain;
import com.file.hw.filter.impl.NoHiddenFileFilter;
import com.file.hw.filter.impl.SuffixSpecifiedFileFilter;
import com.file.hw.filter.util.FilesScanner;
import com.file.hw.props.PropUtil;
import com.io.hw.bean.ResultBean;
import com.io.hw.chain.ContentFilterChain;
import com.io.hw.exception.NoFilterException;
import com.io.hw.file.util.FileUtils;
import com.io.hw.file.util.FullContentReplace;
import com.io.hw.impl.ContentFilterImpl;
import com.string.widget.util.ValueWidget;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

public class FindMain extends Frame
{

    private static final long serialVersionUID = 7259328583976957600L;
    TextField fileNameTF;
    TextField fileRowNumTF;
    TextField filePathTF;

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        new FindMain().launchFrame();
    }

    public void launchFrame()
    {
        this.setLayout(new BorderLayout());
        Panel northPanel=new Panel();
        northPanel.setLayout(new FlowLayout());
        northPanel.setBackground(Color.gray);
        Button findBtn=new Button("find");
        findBtn.setSize(10, 300);
        findBtn.addActionListener(new TffMonitor2());
        northPanel.add(findBtn);
        
        this.add(northPanel,BorderLayout.NORTH);
        
        Panel southPanel=new Panel();
        southPanel.setSize(100, 100);
        southPanel.setBackground(Color.green);
        southPanel.setLayout(new GridLayout(3, 2));
        Label fileName=new Label("�ļ���");
        Label fileRowNum=new Label("���·����");
        fileRowNum.setBackground(Color.gray);
        Label filePath=new Label("�кţ�");
        
        fileNameTF=new TextField();
        fileNameTF.setEditable(false);
        
        fileRowNumTF=new TextField();
        fileRowNumTF.setEditable(false);
        
        filePathTF=new TextField();
        filePathTF.setEditable(false);
        
        southPanel.add(fileName);
        southPanel.add(fileNameTF);
        southPanel.add(fileRowNum);
        southPanel.add(fileRowNumTF);
        southPanel.add(filePath);
        southPanel.add(filePathTF);
        this.add(southPanel,BorderLayout.CENTER);
        this.setBounds(300, 100, 400, 400);
        this.setVisible(true);
//        this.pack();
        this.addWindowListener(new MyWindowListener());
    }

   static class MyWindowListener extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent arg0)
        {
            super.windowClosing(arg0);
            System.exit(0);
        }

    }
    
    class TffMonitor2 implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e)
        {
            FilesScanner filesScanner = new FilesScanner();
            FileFilterChain fileFilterChain = new FileFilterChain();
            fileFilterChain.add(new NoHiddenFileFilter());//�������ļ�
//            fileFilterChain.add(new JavaFileFilter());//������java �ļ�
            fileFilterChain.add(new SuffixSpecifiedFileFilter("dsl sgml"));
            fileFilterChain.add(new SuffixSpecifiedFileFilter("*"));
            filesScanner.setFileFilterChain(fileFilterChain);
            PropUtil propUtil = new PropUtil("conf.properties");
            String fileName = propUtil.getStr("filename");
            String oldRegex = propUtil.getStr("oldregex");
//            String replacement = propUtil.getStr("replacement");
            List<File> files = filesScanner.getAllFilesAfterFilt(fileName);

            FullContentReplace fullContentReplace = new FullContentReplace();
            ContentFilterChain contentFilterChain = new ContentFilterChain();
            try {
				contentFilterChain.add(new ContentFilterImpl(oldRegex,null));
			} catch (NoFilterException e2) {
				e2.printStackTrace();
			}
            fullContentReplace.setFilterChain(contentFilterChain);

//            System.out.println("�ļ���"+"\t\t\t\t�ı����ʼ�к�\t\t���·��");
//            System.out.println("----------------------------------------------------------------------------------------------");
            for (int i = 0; i < files.size(); i++)
            {
                File file = files.get(i);
                ResultBean resultBean = null;
                try
                {
                    resultBean = fullContentReplace.getFullContent(file,SystemHWUtil.CHARSET_UTF);
                }
                catch (NoFilterException e1)
                {
                    e1.printStackTrace();
                }
                if(resultBean!=null){
                	if (resultBean.isChanged())
                    {
//                        System.out.println("is changed");
                        FileUtils.writeToFile(file, resultBean.getFullContent(),SystemHWUtil.CURR_ENCODING);
                    }
                    else
                    {
//                        System.out.println("is not changed");
                    }
                }
                
//                System.out.println(file.getName()+"\t\t\t"+
//                    (resultBean.getChangedRow()==0?"û�ҵ�":(resultBean.getChangedRow())+"\t"+resultBean.getFile().getAbsolutePath()));
                if(!ValueWidget.isNullOrEmpty(resultBean)&& resultBean.getChangedRow()>0){
                    fileNameTF.setText(resultBean.getFile().getName());
                    fileRowNumTF.setText(resultBean.getFile().getAbsolutePath());
                    filePathTF.setText(""+resultBean.getChangedRow());
//                    System.out.println(file.getName()+"\t\t\t"+resultBean.getChangedRow()+"\t"+resultBean.getFile().getAbsolutePath());
                }
            }
            
           
        }
       
    }
}
