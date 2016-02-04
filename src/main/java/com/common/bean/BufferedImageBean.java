package com.common.bean;

import java.awt.image.BufferedImage;

public class BufferedImageBean {
	private BufferedImage image ;
	/***
	 * 是否是RGB模式
	 */
	private boolean isRGB;
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public boolean isRGB() {
		return isRGB;
	}
	public void setRGB(boolean isRGB) {
		this.isRGB = isRGB;
	}
	
}
