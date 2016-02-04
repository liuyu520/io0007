package com.swing.dialog;

import java.awt.*;
import java.io.File;
import java.util.List;
/***
 * 
 * @author huangwei
 * @since 2014年11月15日
 */
public class GenericPanel extends AbstractPanel {
	private static final long serialVersionUID = 1579430722824137898L;

	@Override
	public void dragResponse(List<File> list, Component component) {
		DialogUtil.dragResponse(list, component);
	}
}
