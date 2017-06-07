package com.common.bean;

import java.awt.image.BufferedImage;

public class BufferedImageBean {
	private BufferedImage image ;
	/***
	 * 是否是RGB模式
	 */
    private boolean beRGB;

	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}

    public boolean isBeRGB() {
        return beRGB;
    }

    public void setBeRGB(boolean isRGB) {
        this.beRGB = isRGB;
    }
	
}
