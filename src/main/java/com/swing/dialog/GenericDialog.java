package com.swing.dialog;

import java.awt.*;
import java.io.File;
import java.util.List;

public class GenericDialog extends AbstractDialog {

	
	private static final long serialVersionUID = 3160202220512707196L;
	

	public GenericDialog(Frame owner, boolean modal) {
		super(owner, modal);
	}


	public GenericDialog() {
		super();
	}

//	public GenericDialog(JFrame owner, String title) {
//		super(owner, title, true);
//	}

	/***
	 * 必须执行launchFrame ,layout3才会被调用
	 * @param contentPane
     */
	@Override
	public void layout3(Container contentPane) {
		
	}

	@Override
	public void dragResponse(List<File> list, Component component) {
		DialogUtil.dragResponse(list, component);
	}

}
