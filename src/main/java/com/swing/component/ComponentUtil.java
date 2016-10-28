package com.swing.component;

import com.common.dict.Constant2;
import com.common.util.*;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
import com.swing.dialog.toast.ToastMessage;
import com.swing.menu.MenuUtil2;
import com.swing.messagebox.GUIUtil23;
import com.time.util.TimeHWUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ComponentUtil {
	/***
	 * 为了防止JTextField 重复增加 DocumentListener
	 */
	public static List<JTextField> tfs = new ArrayList<JTextField>();

	/***
	 * 获取指定页面 复选框被选中的个数
	 * 
	 * @param checkBoxes
	 * @param startIndex
	 *            :currentPage * size_per_page
	 * @param count
	 * @return
	 */
	public static int getSelSum(List checkBoxes, int startIndex, int count) {
		if (checkBoxes == null || checkBoxes.size() == 0) {
			return 0;
		} else {
			JCheckBox[] chkArr = getCurrentPageChkbox(checkBoxes, startIndex,
					count);
			if (chkArr == null) {
				return 0;
			}
			int tmp = 0;
			for (int i = 0; i < chkArr.length; i++) {
				JCheckBox array_element = chkArr[i];
				tmp += TypeUtil.bool2int(array_element.isSelected());
			}
			return tmp;
		}
	}

	/***
	 * 
	 * @param checkBoxes
	 * @param startIndex
	 *            :0,5,10,15 ;if =-1,indicate no paging
	 * @param count
	 *            :size of per page
	 */
	public static void setSelect(List checkBoxes, int startIndex, int count) {
		JCheckBox[] chkArr = getCurrentPageChkbox(checkBoxes, startIndex, count);
		if (ValueWidget.isNullOrEmpty(chkArr)) {
			return;
		}
		for (int i = 0; i < chkArr.length; i++) {
			JCheckBox chk = chkArr[i];
			chk.setSelected(true);
//			System.out.println(2);
		}
	}

	/***
	 * 
	 * @param checkBoxes
	 * @param startIndex
	 *            :0,5,10,15 ;if =-1,indicate no paging and omit count
	 * @param count
	 *            :omit when startIndex=-1
	 * @return
	 */
	public static JCheckBox[] getCurrentPageChkbox(List checkBoxes,
			int startIndex, int count) {
		if (checkBoxes == null || checkBoxes.size() == 0) {
			return null;
		} else {
			int endIndex = startIndex + count;
			int sum_chk = checkBoxes.size();
			if (/*startIndex == -1*/startIndex <0) {
				startIndex = 0;
			} else {
				if (sum_chk < endIndex) {
					endIndex = sum_chk;
				}
			}
			JCheckBox[] chkArr = new JCheckBox[endIndex - startIndex];
			int index3 = 0;
			for (int i = startIndex; i < endIndex&i<sum_chk; i++) {
				JCheckBox chk = (JCheckBox) checkBoxes.get(i);
				chkArr[index3] = chk;
				index3++;
			}
			return chkArr;
		}
	}

	public static int getCurrentPageChkboxSum(List checkBoxes, int startIndex,
			int count) {
		if (checkBoxes == null || checkBoxes.size() == 0) {
			return 0;
		} else {
			JCheckBox[] chkArr = getCurrentPageChkbox(checkBoxes, startIndex,
					count);
			return chkArr.length;
		}
	}

	/***
	 * 当文本框中输入\ 时，自动扫描该目录下是否只有一个文件，若只有一个文件，设置文本框的值为该文件的绝对路径
	 * 
	 * @param sourceTF
	 * @param e
	 * @throws BadLocationException
	 */
	public static void assistantTF(final JTextField sourceTF, DocumentEvent e)
			throws BadLocationException {
		int changeLength = e.getLength();
		if (changeLength == 1) {// 表示一次性增加的字符个数是1
			final Document doc = e.getDocument();
			final String input = doc.getText(e.getDocument().getLength() - 1,
					changeLength);
			String filepath = null;
			File[] files = null;
			final String sourceFileStr = sourceTF.getText();
			if (input.endsWith(SystemHWUtil.SEPARATOR)) {// 输入的必须是一个字符，必须是\
				files = FileUtils.getFilesByPathAndSuffix(sourceFileStr, "");

			} else {

				String fatherFolder = SystemHWUtil.getParentDir(sourceFileStr);
				if (!ValueWidget.isNullOrEmpty(fatherFolder)) {
					files = FileUtils.getFilesByPathAndPrefix(fatherFolder,
							SystemHWUtil.getFileSimpleName(sourceFileStr));
				}

			}
			if (!ValueWidget.isNullOrEmpty(files)) {
				if (files.length == 1) {
					filepath = files[0].getAbsolutePath();
					// System.out.println(filepath);
				}
			}
			if (!ValueWidget.isNullOrEmpty(filepath)) {
				// System.out.println("input:" + filepath);
				// 临时变量
				final String filepath_tmp = filepath;
				new Thread(new Runnable() {
					@Override
					public void run() {
						// int length2=sourceFileStr.length();
						// try {
						// doc.insertString(length2-1, "abc", null);
						// } catch (BadLocationException e4) {
						// e4.printStackTrace();
						// }
						sourceTF.setText(filepath_tmp);// 应该放在线程中，不然会报异常：java.lang.IllegalStateException:
														// Attempt to mutate in
														// notification，参考：http://stackoverflow.com/questions/2788779/java-lang-illegalstateexception-while-using-document-listener-in-textarea-java
						sourceTF.setCaretPosition(filepath_tmp.length());
						// sourceTF.updateUI();
					}
				}).start();
			}
		}
	}

	public static void assistantTF(final JTextField sourceTF) {
		boolean isContains = tfs.contains(sourceTF);
		if (isContains) {
			throw new RuntimeException(
					"This JTextField has added the DocumentListener.");
		}
		final Document doc = sourceTF.getDocument();
		DocumentListener docLis = new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// System.out.println("insert");
				try {
					ComponentUtil.assistantTF(sourceTF, e);// Assist path
															// complement
				} catch (BadLocationException e2) {
					e2.printStackTrace();
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// System.out.println("change");
			}
		};
		doc.addDocumentListener(docLis);
		tfs.add(sourceTF);
	}

	/***
	 * Get a copy button.
	 * 
	 * @param tp
	 * @return
	 */
	public static JButton getCopyBtn(final JTextPane tp) {
		JButton copyBtn = new JButton(MenuUtil2.ACTION_STR_COPY);
		copyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!ValueWidget.isNullOrEmpty(tp)){
				String input = tp.getText();
				if (!ValueWidget.isNullOrEmpty(input)) {
					WindowUtil.setSysClipboardText(input);
				}
			}}
		});
		return copyBtn;
	}

	/***
	 * Get a copy button.
	 * 
	 * @param tf
	 * @return
	 *//*
	public static JButton getCopyBtn(final JTextField tf) {
		JButton copyBtn = new JButton(MenuUtil2.ACTION_STR_COPY);
		copyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!ValueWidget.isNullOrEmpty(tf)){
				String input = tf.getText();
				if (!ValueWidget.isNullOrEmpty(input)) {
					WindowUtil.setSysClipboardText(input);
				}
			}}
		});
		return copyBtn;
	}*/
	public static JButton getCopyBtn(final JTextComponent ta) {
		return getCopyBtn(ta, null);
	}
	/***
	 * Get a copy button.
	 * 
	 * @param ta
	 * @return
	 */
	public static JButton getCopyBtn(final JTextComponent ta,String title) {
		if(null==title){
			title=MenuUtil2.ACTION_STR_COPY;
		}
		JButton copyBtn = new JButton(title);
		copyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ValueWidget.isNullOrEmpty(ta)){
					ToastMessage.toast("文本框为null,请确认文本框是否已经创建",4000,Color.red);
				}else{
					String input = ta.getText();
					if (!ValueWidget.isNullOrEmpty(input)) {
						WindowUtil.setSysClipboardText(input);
					}
				}
			}
		});
		return copyBtn;
	}

	/***
	 * Get a paste button.
	 * 
	 * @param ta
	 * @return
	 */
	public static JButton getPasteBtn(final JTextComponent ta) {
		JButton copyBtn = new JButton(MenuUtil2.ACTION_STR_PASTE);
		copyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = WindowUtil.getSysClipboardText();
				if (!ValueWidget.isNullOrEmpty(input)) {
					ta.setText(input);
					ta.requestFocus();
				}
			}
		});
		return copyBtn;
	}

	/***
	 * Get a paste button.
	 * 
	 * @param tf
	 * @return
	 */
	/*public static JButton getPasteBtn(final JTextField tf) {
		JButton copyBtn = new JButton(MenuUtil2.ACTION_STR_PASTE);
		copyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = WindowUtil.getSysClipboardText();
				if (!ValueWidget.isNullOrEmpty(input)) {
					tf.setText(input);
				}
			}
		});
		return copyBtn;
	}*/

	/***
	 * Get a paste button.
	 * 
	 * @param tp
	 * @return
	 */
	public static JButton getPasteBtn(final JTextPane tp) {
		if(ValueWidget.isNullOrEmpty(tp)){
			return null;
		}
		JButton copyBtn = new JButton(MenuUtil2.ACTION_STR_PASTE);
		copyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = WindowUtil.getSysClipboardText();
				if (!ValueWidget.isNullOrEmpty(input)) {
					tp.setText(input);
				}
			}
		});
		return copyBtn;
	}
	/***
	 * 默认显示时间
	 * @param resultTextArea
	 * @param result
	 * @param isAddDivide
	 */
	public static void appendResult(JTextComponent resultTextArea, String result,
			boolean isAddDivide){
		appendResult(resultTextArea, result, isAddDivide, true);
	}
	/***
	 * 
	 * @param resultTextArea
	 * @param result
	 * @param isAddDivide
	 * @param prependTime : 是否显示时间
	 */
	public static void appendResult(JTextComponent resultTextArea, Object result,
			boolean isAddDivide,boolean prependTime) 
	{
		if(ValueWidget.isNullOrEmpty(resultTextArea)){
			return;
		}
		appendResult(resultTextArea, result, isAddDivide, false,prependTime);
	}
	/***
	 * 向结果文本框追加内容,不会删除原来的内容。
	 * 
	 * @param result
	 * @param CRLFbefore : 表示是否在前面添加一个回车换行
	 * @param isAddDivide
	 * @param prependTime : 是否显示时间
	 */
	public static void appendResult(JTextComponent resultTextArea, Object result2,
			boolean isAddDivide,boolean CRLFbefore,boolean prependTime) {
		if(ValueWidget.isNullOrEmpty(resultTextArea)){
			return;
		}
		String result;
		
		if (result2 == null) {
			result = "";
		} else {
			result = (String)result2 + SystemHWUtil.CRLF;
			if(prependTime){
				String currentTime=TimeHWUtil.getCurrentMiniuteSecondZH();
				result = currentTime+"\t|  "+result;
			}
		}
		if(CRLFbefore){
			result=SystemHWUtil.CRLF+result;
		}
		Document doc = resultTextArea.getDocument();
		int length = doc.getLength();
		try {
			doc.insertString(length, result, null);
			length = length + result.length();
			if (isAddDivide) {
				doc.insertString(length, SystemHWUtil.DIVIDING_LINE
						+ SystemHWUtil.CRLF, null);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
			GUIUtil23.warningDialog(e.getMessage());
		}

	}
	/***
	 * 向结果文本框追加内容,不会删除原来的内容。
	 * 
	 * @param result
	 * @param isAddDivide
	 */
	public static void appendResult(JTextPane resultTextArea, String result,
			boolean isAddDivide) {
		if(ValueWidget.isNullOrEmpty(resultTextArea)){
			return;
		}
		appendResult(resultTextArea, result, null, isAddDivide);
	}
	/***
	 * 向结果文本框追加内容,不会删除原来的内容。
	 * 
	 * @param result
	 * @param isAddDivide
	 */
	public static void appendResult(JTextPane resultTextPane, String result, AttributeSet set,
			boolean isAddDivide) {
		if(ValueWidget.isNullOrEmpty(resultTextPane)){
			return;
		}
		if (result == null) {
			result = "";
		} else {
			result = result + SystemHWUtil.CRLF;
		}
		Document doc = resultTextPane.getDocument();resultTextPane.getText();
		int length = doc.getLength();
//		if(length>7){
//			length=length-17;
//		}
		try {
			doc.insertString(length, result, set);
			length = length + result.length();
			if (isAddDivide) {
				doc.insertString(length, SystemHWUtil.DIVIDING_LINE
						+ SystemHWUtil.CRLF, null);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
			GUIUtil23.warningDialog(e.getMessage());
		}

	}

	/***
	 * 在实际使用过程中，应该放在线程中
	 * 
	 * @param in
	 * @param sourceSize
	 *            : 输入流的长度，即要读取的字节个数
	 * @param targfile
	 */
	public static void progress(JProgressBar copyProgressBar, InputStream in,
			long sourceSize, File targfile) {
		FileOutputStream target = null;
		try {
			int bytesArrLeng = 0;
			if (sourceSize < SystemHWUtil.BUFF_SIZE_1024) {//
				bytesArrLeng = (int) sourceSize;
			} else {
				long shangOne = sourceSize / SystemHWUtil.NUMBER_100;
				if (shangOne == 0) {// sourceSize<100
					shangOne = shangOne + 1;
				}

				if (shangOne <= SystemHWUtil.BUFF_SIZE_1024) {
					bytesArrLeng = (int) shangOne;
				} else {
					long shangTwo = shangOne / SystemHWUtil.BUFF_SIZE_1024;
					if (shangOne % SystemHWUtil.BUFF_SIZE_1024 != 0) {
						shangTwo = shangTwo + 1L;
					}
					bytesArrLeng = (int) (shangOne / shangTwo);
				}
			}
			System.out.println("字节数组的长度是：" + bytesArrLeng);
			target = new FileOutputStream(targfile);
			byte[] buffer = new byte[bytesArrLeng];
			int byteNum;
			long hasReadByte = 0L;// 已经读取的字节个数
			float result;
			int progressSize = 0;
			while ((byteNum = in.read(buffer)) != SystemHWUtil.NEGATIVE_ONE) {
				target.write(buffer, 0, byteNum);
				hasReadByte = hasReadByte + byteNum;
				result = (float) ((double) hasReadByte / sourceSize);
				progressSize = Math.round(result * SystemHWUtil.NUMBER_100);
				updateProgress(copyProgressBar, progressSize);
			}
			if (progressSize < SystemHWUtil.NUMBER_100) {
				progressSize = SystemHWUtil.NUMBER_100;
				updateProgress(copyProgressBar, progressSize);
			}
			copyProgressBar.setForeground(Color.blue);

		} catch (Exception e) {
			e.printStackTrace();
			GUIUtil23.errorDialog(e);
			// copyFileBtn.setEnabled(true);
		} finally {
			in = closeInputStream(in, target);
		}
	}

	public static InputStream closeInputStream(InputStream in, FileOutputStream target) {
        closeStream(in);
        closeStream(target);
        return in;
	}

    public static void closeStream(Closeable target) {
        if (target != null) {
            try {
                target.close();
            } catch (IOException e) {
                e.printStackTrace();
                GUIUtil23.errorDialog(e);
            }
        }
    }

    /***
     * 更新进度条上得进度数字
	 * 
	 * @param copyProgressBar
	 * @param progressSize
	 */
	private static void updateProgress(JProgressBar copyProgressBar,
			int progressSize) {
		copyProgressBar.setString(progressSize + "%");
		copyProgressBar.setValue(progressSize);
	}
	/***
	 * 复制图片到剪切板
	 * @param image
	 */
	public static void setClipboardImage(Container frame, final Image image) {
		Transferable trans = new Transferable() {
			@Override
			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor)) {
					return image;
				}
				throw new UnsupportedFlavorException(flavor);
			}

			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}
		};
		
		frame.getToolkit().getSystemClipboard().setContents(trans, null);
	}
	public static BufferedImage getClipboardImage(Frame frame) {
		// java.lang.ClassCastException: sun.awt.datatransfer.TransferableProxy cannot be cast to sun.awt.datatransfer.ClipboardTransferable
		Transferable trans=frame.getToolkit().getSystemClipboard().getContents(null);
		BufferedImage image=null;
//		if(trans instanceof ClipboardTransferable){
//		ClipboardTransferable clipboardTrans =(ClipboardTransferable)trans;
		
		try {
			if (null != trans && trans.isDataFlavorSupported(DataFlavor.imageFlavor)) {   
			Object obj22=trans.getTransferData(DataFlavor.imageFlavor);
			if(!ValueWidget.isNullOrEmpty(obj22)){
				if(obj22 instanceof BufferedImage){
                    image = (BufferedImage) obj22;
                } else if (obj22 instanceof sun.awt.image.MultiResolutionCachedImage) {//兼容mac os
                    sun.awt.image.MultiResolutionCachedImage cachedImage = (sun.awt.image.MultiResolutionCachedImage) obj22;
                    if (null == cachedImage) {
                        return null;
                    }
                    sun.awt.image.ToolkitImage toolkitImage = (sun.awt.image.ToolkitImage) cachedImage.getScaledInstance(cachedImage.getWidth(null), cachedImage.getHeight(null), Image.SCALE_SMOOTH);
                    if (null == toolkitImage) {
                        return null;
                    }
                    java.awt.image.FilteredImageSource filteredImageSource = (java.awt.image.FilteredImageSource) ReflectHWUtils.getObjectValue(toolkitImage, "source");
                    if (null == filteredImageSource) {
                        return null;
                    }
                    sun.awt.image.OffScreenImageSource imageSource = (sun.awt.image.OffScreenImageSource) ReflectHWUtils.getObjectValue(filteredImageSource, "src");
                    image = (BufferedImage) ReflectHWUtils.getObjectValue(imageSource, "image");
//					System.out.println(imageSource);
                }
			}
			}
		} catch (UnsupportedFlavorException e1) {
			e1.printStackTrace();
			GUIUtil23.errorDialog(e1);
		} catch (IOException e1) {
			e1.printStackTrace();
			GUIUtil23.errorDialog(e1);
		}
//		}
        catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

		
		/*try {
			Map map=(Map)ReflectHWUtils.getObjectValue(clipboardTrans, "flavorsToData");
			Object val=null;
			for(Object obj:map.keySet()){
				val=map.get(obj);
				break;
			}
			byte[] data=(byte[])ReflectHWUtils.getObjectValue(val, "data");
			
			return data;//(BufferedImage)trans.getTransferData(trans.getTransferDataFlavors()[0]);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}*/
		return image;
	}
	/***
	 * 
	 * @param comb
	 * @param urls
	 */
	public static void fillComboBox(JComboBox<String> comb,String urls[]){
		if(comb==null||ValueWidget.isNullOrEmpty(urls)){
			return;
		}
		for(int i=0;i<urls.length;i++){
			comb.addItem(urls[i]);
		}
	}
	/***
	 * 
	 * @param comb : 下拉框
	 * @param picUrls : 以;;分割的字符串
	 */
	public static void fillComboBox(JComboBox<String> comb,String picUrls){
		if(!ValueWidget.isNullOrEmpty(picUrls)){//为空判断
			String urls[]=picUrls.split(Constant2.SHAREDPICDIVISION);
			ComponentUtil.fillComboBox(comb, SystemHWUtil.unique(urls));
		}
	}
	public static JComboBox<String> comboBoxSelectedHandle(JComboBox<String> comboBox,final JTextField ipTextField){
		if(ValueWidget.isNullOrEmpty(comboBox)){
			comboBox = new JComboBox<String>();
		}
		comboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JComboBox<String> target=(JComboBox<String>)e.getSource();
				String  selectedPort=(String)target.getSelectedItem();
                if(!ValueWidget.isNullOrEmpty(selectedPort)){
                	ipTextField.setText(selectedPort);
                }
//				System.out.println(e.getSource());
			}
		});
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				JComboBox<String> target=(JComboBox<String>)e.getSource();
				String  selectedPort=(String)target.getSelectedItem();
                if(!ValueWidget.isNullOrEmpty(selectedPort)){
                	ipTextField.setText(selectedPort);
                }
			}
		});
		return comboBox;
	}
	
	/***
	 * 
	 * @param tc
	 */
	public static void trim(JTextComponent tc){
		String text=tc.getText();
		if(text!=null && text.length()>0){
			tc.setText(text.trim());
		}
	}
	/***
	 * 获取用户输入的高度
	 * @param ta
	 * @return
	 */
	public static int getImageHeight(JTextComponent ta){
		String newName = JOptionPane.showInputDialog(ta.getParent()/*应该是JFrame*/,
				"请输入图片高度:", ta.getHeight());
		if (newName != null)
		{
			if (newName.equals("")) {
				ToastMessage.toast("file name can not be empty.",3000,Color.red);
				return SystemHWUtil.NEGATIVE_ONE;
			}
			return Integer.parseInt(newName);
			
		}else{/* indicate [cancel] button has been clicked */
			ToastMessage.toast("已取消",2000,Color.red);
			return SystemHWUtil.NEGATIVE_ONE;
		}
	}
	/***
	 * 复制图片到剪切板
	 * @param ta
	 * @param isSpecifyHeight
	 */
	public static void copyImage(JTextComponent ta,boolean isSpecifyHeight){
		Integer specifiedHeight=null;
		if(isSpecifyHeight){//ta.getHeight()
			int inputHeight=getImageHeight(ta);
			if(inputHeight!=SystemHWUtil.NEGATIVE_ONE){//不等于-1,说明用户指定了高度
				specifiedHeight=inputHeight;
			}
		}
		generateImageAndCopy(ta, specifiedHeight,-1);
	}

    /***
     * 截图,截屏
     * @param ta
     * @param specifiedHeight
     * @param specifiedWidth
     */
    public static void generateImageAndCopy(JTextComponent ta, Integer specifiedHeight,Integer specifiedWidth) {
		BufferedImage img = ImageHWUtil.generateImage(ta, null, "jpg"/*picFormat*/,specifiedHeight,specifiedWidth);
		if(ValueWidget.isNullOrEmpty(img)){
			return;
		}
		ComponentUtil.setClipboardImage(ta.getParent(),img);
		ToastMessage.toast("复制图片到剪切板",3000);
	}

	/***
	 * 保存图片时选择要保存为的文件路径
	 * @param ta
	 * @param picFormat
	 * @param isSpecifyHeight
	 */
	public static void chooseDestFile(JTextArea ta,String picFormat,boolean isSpecifyHeight){
		JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        File selectedFile=new File("C:\\Users\\Administrator\\Pictures\\"+TimeHWUtil.formatDate(new Date(), "yyyyMM")
        		+File.separator+TimeHWUtil.formatDate(new Date(), "yyyy-MM-dd_HH-mm-ss"));
        //home目录应该动态获取
//        System.out.println(selectedFile.getAbsolutePath());
        if(!ValueWidget.isNullOrEmpty(selectedFile)){
        	chooser.setSelectedFile(selectedFile);
        }
        chooser.setName("二维码."+picFormat);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "picture Files", picFormat, "二维码");
            chooser.setFileFilter(filter);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setControlButtonsAreShown(true);
        chooser.setDialogTitle("保存二维码");
        //            chooser.setVisible(true);
        int result = chooser.showSaveDialog(ta);
        System.out.println("New file:" + result);
        if (result == JOptionPane.OK_OPTION)
        {
            selectedFile = chooser.getSelectedFile();
            if(! SystemHWUtil.isHasSuffix(selectedFile)){
            	selectedFile=new File(selectedFile.getAbsolutePath()+ SystemHWUtil.ENGLISH_PERIOD+picFormat);
            }
            Integer specifiedHeight=null;
    		if(isSpecifyHeight){//ta.getHeight()
    			int inputHeight=getImageHeight(ta);
    			if(inputHeight!=SystemHWUtil.NEGATIVE_ONE){//不等于-1,说明用户指定了高度
    				specifiedHeight=inputHeight;
    			}
    		}
            ImageHWUtil.generateImage(ta, selectedFile, picFormat,specifiedHeight);
            ToastMessage.toast("保存图片成功",3000);
            System.out.println("select file:" + selectedFile);
        }else{
        	ToastMessage.toast("已取消",2000,Color.red);
        }
	}
	/***
	 * 获取编码 下拉框
	 * @return
	 */
	public static JComboBox<String> getEncodingComboBox(){
		//文件的编码,window里面一般是GBK,linux中一般是UTF-8
		JComboBox<String> encodingComboBox = new JComboBox<String>();
		encodingComboBox.addItem(SystemHWUtil.EMPTY);
		encodingComboBox.addItem(SystemHWUtil.CHARSET_UTF);
		encodingComboBox.addItem(SystemHWUtil.CHARSET_GBK);
		encodingComboBox.addItem(SystemHWUtil.CHARSET_GB2312);
		encodingComboBox.addItem(SystemHWUtil.CHARSET_GB18030);
		encodingComboBox.addItem(SystemHWUtil.CHARSET_ISO88591);
		return encodingComboBox;
	}
}
