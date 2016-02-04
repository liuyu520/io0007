package com.io.hw.awt;

import com.io.hw.file.util.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ResultInfoPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8868930688257323651L;
	private String fileName=null;
	private long rowNum;
	private String fullPath=null;
	public ResultInfoPanel(String fileName,ArrayList<Integer> arrayList,String fullPath) {
		super();
		JPanel fileNamePanel = new JPanel();
		fileNamePanel.setLayout(new FlowLayout());
		JTextField fileNameField = new JTextField(30);
		fileNameField.setText(fileName);
		fileNameField.setEditable(false);
		fileNameField.setFocusable(true);
//		fileNameField.setLocation(12, 34);
		fileNameField.setBackground(new Color(100,255,200));
		JLabel findRowNumLabel = new JLabel(""+arrayList);/* ��һ�η��ֵ��к� */
		findRowNumLabel.setBackground(Color.yellow);
		findRowNumLabel.setForeground(Color.red);
//		findRowNumLabel.setBackground(new Color(255,200,100));
		findRowNumLabel.setFocusable(true);
		fileNamePanel.add(fileNameField);
		fileNamePanel.add(findRowNumLabel);
		this.add(fileNamePanel);
//		this.setLayout(new GridLayout(1, 2));
		this.setLayout(new FlowLayout(0));
		JTextField fullFilePathTF = new JTextField(60);
		fullFilePathTF.setText(fullPath);
		fullFilePathTF.setBackground(new Color(255,15,89));
		fullFilePathTF.setFont(new Font("����", Font.BOLD, 14));
		fullFilePathTF.setFocusable(true);
		fullFilePathTF.setEditable(false);
//		fullFilePathLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		/*fullFilePathLabel.setBorder(new Border() {
			
			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width,
					int height) {
				g.draw3DRect(x, y, width, height, false);
				
			}
			
			@Override
			public boolean isBorderOpaque() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Insets getBorderInsets(Component c) {
				// TODO Auto-generated method stub
				return null;
			}
		});*/
		fullFilePathTF.addMouseListener(new MyMouseListener(fullFilePathTF));
		this.add(fullFilePathTF);
		
	}
	public ResultInfoPanel(LayoutManager layout) {
		super(layout);
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public long getRowNum() {
		return rowNum;
	}
	public void setRowNum(long rowNum) {
		this.rowNum = rowNum;
	}
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	
	static class MyMouseListener implements MouseListener{
		JTextField fullFilePathTF;
		public MyMouseListener(JTextField fullFilePathTF){
			this.fullFilePathTF=fullFilePathTF;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				String fullPath=this.fullFilePathTF.getText();
				this.fullFilePathTF.setSelectionStart(0);
				this.fullFilePathTF.setSelectionEnd(fullPath.length());
				int index=fullPath.lastIndexOf("\\");
				fullPath=fullPath.substring(0, index);
//				System.out.println(fullPath);
				FileUtils.open_directory(fullPath);
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
