package com.io.hw.awt.color;

import java.awt.*;
import java.util.Random;

public class CustomColor {
    public final static Color red_little = new Color(255, 200, 200);
    public final static Color green_little = new Color(150, 255, 150);
    public final static Color red_little_more = new Color(255, 230, 230);
    public final static Color red_middle = new Color(255, 100, 100);
    public final static Color blue_little_ocean = new Color(130, 200, 255);
    public final static Color blue_little_gray = new Color(200, 200, 255);
    
    
	public static Color getColor(int r, int g, int b) {
		return new Color(r,g,b);
	}
	/***
	 * 
	 * @param isLightColour : 浅色(亮色)
	 * @return
	 */
	public static Color getColor(boolean isLightColour){
	    int r = randomInt(isLightColour);
        int g = randomInt(isLightColour);
        int b = randomInt(isLightColour);
        while (r == 255 && g == 255 && b == 255)
        {
            r = randomInt(isLightColour);
            g = randomInt(isLightColour);
            b = randomInt(isLightColour);
        }
        return new Color(r, g, b);
	}
	public static Color getColor(int  base){
	    int r = randomInt(base);
        int g = randomInt(base);
        int b = randomInt(base);
        while (r == 255 && g == 255 && b == 255)
        {
            r = randomInt(base);
            g = randomInt(base);
            b = randomInt(base);
        }
        return new Color(r, g, b);
	}
	/***
	 * 深色
	 * @return
	 */
	public static Color getDeepColor(){
		return getColor(160);
	}
	public static Color getColor(){
		return getColor(false);
	}
	public static Color getLightColor(){
		return getLightColor(125);
	}
	/***
	 * 更亮的颜色
	 * @return
	 */
	public static Color getMoreLightColor(){
		return getLightColor(30);
	}
	/***
	 * 获取 
	 * @return
	 */
	public static Color getLightColor(int delta){
		 int r = randomIntDelta(delta);
        int g = randomIntDelta(delta);
        int b = randomIntDelta(delta);
        while (r == 255 && g == 255 && b == 255)
        {
            r = randomIntDelta(delta);
            g = randomIntDelta(delta);
            b = randomIntDelta(delta);
        }
        return new Color(r, g, b);
	}
	

	/***
	 * 
	 * @param isLightColour : 是否浅色(亮色)
	 * @return
	 */
	public static int randomInt(boolean isLightColour) {
		Random dom = new Random();
		if(isLightColour){//浅色(亮色)
			int base=125;
			return randomIntDelta(base);
		}else{
			return dom.nextInt(255);
		}

	}
	/***
	 * 差量
	 * @param base : 越小,颜色越亮(淡)
	 * @return
	 */
	public static int randomIntDelta(int base) {
		Random dom = new Random();
		return Math.abs(dom.nextInt(base))+(255-base);
	}
	/***
	 * 
	 * @param base
	 * @return
	 */
	public static int randomInt(int base) {
		Random dom = new Random();
		return dom.nextInt(base);
	}
	public static int randomInt() {
		return randomInt(false/*isLightColour*/);
	}
	/***
	 * 浅色
	 * @return
	 */
	public static int randomIntLightColour() {
		return randomIntDelta(125);
	}

	public static void main(String[] args) {
		System.out.println(randomInt());
		System.out.println(randomInt());
		System.out.println(randomInt());
	}

}
