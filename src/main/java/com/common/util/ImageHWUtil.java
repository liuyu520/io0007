package com.common.util;

import com.cmd.dos.hw.util.CMDUtil;
import com.common.bean.BufferedImageBean;
import com.common.bean.ImageBean;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.RandomUtils;
import com.string.widget.util.ValueWidget;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.swing.component.ComponentUtil;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
public class ImageHWUtil {
	public static final String thumbnail="_thumbnail";
	/***
	 * 转化后的图片路径不变
	 * @param imagePath
	 * @param quality : 调用mogrify.exe转化为RGB格式的同时改变quality
	 * @param cmdFolder
	 * @return
	 */
	  public static String convert2RGB(String imagePath,int quality,String cmdFolder)
	    {
	       String qualityArg="100";
	       if(quality>0 && quality<=100){
	    	   qualityArg=String.valueOf(quality);
	       }
	        String []command = null;
//	        if(ValueWidget.isNullOrEmpty(cmdFolder)){
//	        	cmdFolder="D:\\Program Files\\ImageMagick-6.8.9-Q16\\";
//	        }
	        
//	        command = new String[]{"cmd"," /c ","dir"};//cmd /c is needed
//	        imagePath="D:\\software\\eclipse\\workspace2\\demo_channel_terminal\\src\\main\\resources\\mini.jpg";
	        if(SystemHWUtil.isWindows){
	        command = new String[]{"cmd"," /c ","mogrify.exe" ,"-colorspace", "RGB", "-quality" ,qualityArg ,"\""+imagePath+"\""};//cmd /c is needed
	        }else{
	        	command = new String[]{"mogrify" ,"-colorspace", "RGB", "-quality" ,qualityArg ,"\""+imagePath+"\""};//linux
	        }
	        System.out.println("command:"+SystemHWUtil.formatArr(command, " "));
	        //命令执行结果
	        String result=null;
			try {
				result = CMDUtil.execute(command, cmdFolder,null/*编码*/);
				System.out.println("result:"+result);
		        return result;
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return result;
	    }

	  /***
	   * 只调用mogrify.exe转化为RGB格式,而不改变quality
	   * @param imagePath
	   * @param cmdFolder
	   * @return
	   */
	  public static String convert2RGB(String imagePath,String cmdFolder)
	  {
		  return convert2RGB(imagePath, SystemHWUtil.NEGATIVE_ONE, cmdFolder);
	  }
	/***
	 * 仅仅读取文件,若图片文件不是RGB模式,则报错	  
	 * @param srcImgPath
	 * @return
	 * @throws IOException
	 */
	  public static BufferedImage inputImage(String srcImgPath) throws IOException {
		  return inputImage(new File(srcImgPath));
	  }
	  
	  public static BufferedImage inputImage(File srcImgPath) throws IOException {
		  FileInputStream in = new FileInputStream(srcImgPath);
		  return inputImage(in);
	  }
		/** 
		 * 图片文件读取 <br>仅仅读取文件,若图片文件不是RGB模式,则报错
		 * @param in
		 * @return 
		 * @throws IOException 
		 */
		public static BufferedImage inputImage(InputStream in) throws IOException {
			BufferedImage srcImage = null;
			
			try {
				srcImage = javax.imageio.ImageIO.read(in);
				in.close();//is important,不然文件就被java程序占用
			} catch (IIOException e) {
				System.out.println("读取图片文件出错！" + e.getMessage());
//				e.printStackTrace();
				throw e;
			}
			return srcImage;
		}
		/**
		 * 
		 * 自己设置压缩质量来把图片压缩成byte[]
		 * 
		 * @param bufferedImage
		 *            压缩源图片
		 * @param quality
		 *            压缩质量，在0-1之间，
		 * @return 返回的字节数组
		 */
		public static byte[] bufferedImageTobytes(BufferedImage bufferedImage, float quality) {
			System.out.println("jpeg " + quality + "质量  开始打包" + getCurrentTime());
			// 如果图片空，返回空
			if (bufferedImage == null) {
				return null;
			}
			// 得到指定Format图片的writer
			Iterator<ImageWriter> iter = ImageIO
					.getImageWritersByFormatName("jpeg");// 得到迭代器
			ImageWriter writer = (ImageWriter) iter.next(); // 得到writer

			// 得到指定writer的输出参数设置(ImageWriteParam )
			ImageWriteParam iwp = writer.getDefaultWriteParam();
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
			iwp.setCompressionQuality(quality); // 设置压缩质量参数

			iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

			ColorModel colorModel = ColorModel.getRGBdefault();
			// 指定压缩时使用的色彩模式
			iwp.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel,
					colorModel.createCompatibleSampleModel(16, 16)));

			// 开始打包图片，写入byte[]
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // 取得内存输出流
			IIOImage iIamge = new IIOImage(bufferedImage, null, null);
			try {
				// 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput
				// 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput
				writer.setOutput(ImageIO
						.createImageOutputStream(byteArrayOutputStream));
				writer.write(null, iIamge, iwp);
			} catch (IOException e) {
				System.out.println("write errro");
				e.printStackTrace();
			}
			System.out.println("jpeg" + quality + "质量完成打包-----" + getCurrentTime()
					+ "----length----" + byteArrayOutputStream.toByteArray().length);
			byte[]result=byteArrayOutputStream.toByteArray();
			writer.dispose();
			try {
				byteArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		/**
		 * 自己定义格式，得到当前系统时间
		 * 
		 * @return
		 */
		public static String getCurrentTime() {
			Calendar c = new GregorianCalendar();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int min = c.get(Calendar.MINUTE);
			int second = c.get(Calendar.SECOND);
			int millsecond = c.get(Calendar.MILLISECOND);
			String time = hour + "点" + min + "分" + second + "秒" + millsecond;
			return time;
		}

		/***
		 * 调用了ImageHWUtil.inputImage 方法,功能更强大<br>包含把非RGB图转化为RGB图,调用 mogrify.exe命令
		 * <br>如果是RGB模式,则不修改质量
		 * @param srcImgPath
		 * @param mogrifyQuality : 调用mogrify.exe转化为RGB格式的同时改变quality<br>[0-100]
		 * @param cmdFolder
		 * @return
		 * @throws IOException 
		 */
		public static BufferedImageBean getBufferedImage(String srcImgPath,int mogrifyQuality,String cmdFolder) throws IOException{
			BufferedImageBean bufferedImageBean=new BufferedImageBean();
			BufferedImage bufferedImage = null;
			try {
				bufferedImage=ImageHWUtil.inputImage(srcImgPath);
			} catch (javax.imageio.IIOException e) {
//				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
			if(ValueWidget.isNullOrEmpty(bufferedImage)){//说明不是RGB格式的,上面会发生异常
//				String cmdFolder="D:\\Program Files\\ImageMagick-6.8.9-Q16\\";
				String result=ImageHWUtil.convert2RGB(srcImgPath,mogrifyQuality,cmdFolder);//转化图片为RGB格式,会调用mogrify.exe本地文件
				System.out.println("[getBufferedImage]result:"+result);
				bufferedImage=ImageHWUtil.inputImage(srcImgPath);
				bufferedImageBean.setRGB(false);
			}else{
				bufferedImageBean.setRGB(true);
			}
			bufferedImageBean.setImage(bufferedImage);
			return bufferedImageBean;
		}
		
		public static BufferedImageBean getBufferedImage(InputStream in) throws IOException{
			BufferedImageBean bufferedImageBean=new BufferedImageBean();
			BufferedImage image = null;
			bufferedImageBean.setRGB(false);
			try {
				image=ImageHWUtil.inputImage(in);
				bufferedImageBean.setRGB(true);
			} catch (javax.imageio.IIOException e) {
//				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
			bufferedImageBean.setImage(image);
			return bufferedImageBean;
		}
		
		/***
		 * 是上述方法的重载,省略了cmdFolder
		 * @param srcImgPath
		 * @param mogrifyQuality
		 * @return
		 * @throws IOException 
		 */
		public static BufferedImageBean getBufferedImage(String srcImgPath,int mogrifyQuality) throws IOException{
			return getBufferedImage(srcImgPath, mogrifyQuality,null);
		}
		/***
		 * 
		 * @param srcImgPath : 原文件的路径
		 * @param mogrifyQuality : 调用mogrify.exe转化为RGB格式的同时改变quality
		 * @param quality : 压缩的质量,取值范围:[0,1.0]
		 * @param destPath : 目标文件的路径
		 * @param cmdFolder : 命令"mogrify.exe"的路径
		 * @param isScale : 是否改变图片的像素尺寸
		 * @param fixedWidth : 目标图片的尺寸,<br> 当<param>isScale</param>为true时,才起作用
		 * @return
		 * @throws IOException
		 */
		public static File reduceImage(String srcImgPath,int mogrifyQuality,float quality,String destPath,String cmdFolder,boolean isScale,int fixedWidth) throws IOException{
			BufferedImageBean bufferedImageBean=new BufferedImageBean();
			BufferedImage image = null;
			bufferedImageBean=getBufferedImage(srcImgPath,mogrifyQuality, cmdFolder);
			if(ValueWidget.isNullOrEmpty(bufferedImageBean.getImage())){
				return null;
			}
			if(isScale){//是否缩放
				image = bufferImage(bufferedImageBean.getImage(), BufferedImage.TYPE_INT_RGB,fixedWidth);
				if(!ValueWidget.isNullOrEmpty(image)){
					bufferedImageBean.setImage(image);
				}
			}
			return reduceImage(bufferedImageBean.getImage(), quality, destPath);
		}
		/***
		 * 生成缩略图,质量为80
		 * @param srcImgPath
		 * @throws IOException
		 */
		public static void thumbnail(String srcImgPath,int fixedWidth) throws IOException {
			thumbnail(srcImgPath, 80,fixedWidth);
		}
		/***
		 * 生成缩略图,宽度为55px
		 * @param srcImgPath
		 * @param quality
		 * @throws IOException
		 */
		public static void thumbnail(String srcImgPath,float quality,int fixedWidth) throws IOException {
			
			thumbnail(srcImgPath, quality, fixedWidth, new File(getThumbnailName(srcImgPath)));
		}
		/***
		 * 生成缩略图名称
		 * @param srcImgPath
		 * @return
		 */
		public static String getThumbnailName(String srcImgPath){
			int index=srcImgPath.lastIndexOf('.');
			
			return srcImgPath.substring(0, index)+thumbnail+srcImgPath.substring(index);
		}
		/***
		 * 
		 * @param bufferedImageBean
		 * @param quality :取值范围:[0-1.0]
		 * @param fixedWidth
		 * @param descFile
		 * @throws IOException
		 */
		public static void thumbnail(BufferedImageBean bufferedImageBean,float quality,int fixedWidth,File descFile) throws IOException {
//			BufferedImageBean bufferedImageBean=new BufferedImageBean();
//			bufferedImageBean.setImage(image22);
			if(fixedWidth>10){
				BufferedImage image = bufferImage(bufferedImageBean.getImage(), BufferedImage.TYPE_INT_RGB,fixedWidth);
				bufferedImageBean.setImage(image);
				System.out.println("修改宽度为:"+fixedWidth);
			}else{
				System.out.println("没有修改宽度");
			}
			
			
			
			
			if(quality>0&&quality<1){
				reduceImage(bufferedImageBean.getImage(), quality, descFile);
				System.out.println("压缩质量:"+quality);
			}else{
				ImageIO.write(bufferedImageBean.getImage(),"jpg",descFile);
			}
			
			
		}
		/***
		 * 生成缩略图
		 * @param srcImgPath
		 * @param quality :取值范围:[0-1.0]
		 * @param fixedWidth
		 * @param descFile
		 * @throws IOException
		 */
		public static void thumbnail(String srcImgPath,float quality,int fixedWidth,File descFile) throws IOException {
			BufferedImageBean bufferedImageBean=getBufferedImage(srcImgPath,90, null);
			boolean isNoDestFile=ValueWidget.isNullOrEmpty(descFile);
			if(isNoDestFile){//没有提供目标文件
				String destPath=getThumbnailName(srcImgPath);
				descFile=new File(destPath);
			}
			thumbnail(bufferedImageBean, quality, fixedWidth, descFile);
			
			if(isNoDestFile){//没有提供目标文件
				File srcImgFile=new File(srcImgPath);
				if(srcImgFile.delete()){
					descFile.renameTo(srcImgFile);
				}
			}
		}
		public static File thumbnail(InputStream in,float quality,int fixedWidth,File descFile) throws IOException {
			BufferedImageBean bufferedImageBean=getBufferedImage(in);
			boolean isNoDestFile=ValueWidget.isNullOrEmpty(descFile);
			if(isNoDestFile){//没有提供目标文件
				String destPath=RandomUtils.getTimeRandom2()+thumbnail+".jpg";
				descFile=new File(destPath);
			}
			thumbnail(bufferedImageBean, quality, fixedWidth, descFile);
			return descFile;
		}
		/***
		 * 自己设置压缩质量来把图片压缩成byte[]
		 * @param image
		 * @param quality : 0-1.0
		 * @param destFile
		 * @return
		 * @throws IOException
		 */
		public static File reduceImage(BufferedImage image,float quality,File destFile) throws IOException{
			byte[] result = ImageHWUtil.bufferedImageTobytes(image, quality);
			
			FileUtils.writeBytesToFile(result, destFile);
			return destFile;
		}
		/***
		 * 
		 * @param image
		 * @param quality : 0-1.0
		 * @param destPath
		 * @return
		 * @throws IOException
		 */
		public static File reduceImage(BufferedImage image,float quality,String destPath) throws IOException{
			File destFile=new File(destPath);
			return reduceImage(image, quality, destFile);
		}
		/***
		 * 
		 * @param srcImgPath : 原文件的路径
		 * @param destPath
		 * @param cmdFolder  : 刚把命令加到path中时仍要指定命令所在路径,<br>重启eclipse就不用指定了,因为path中有.
		 * @param imageBean
		 * @param mogrifyQuality : 调用mogrify.exe转化为RGB格式的同时改变quality
		 * @return
		 * @throws IOException
		 */
		public static File reduceImage(String srcImgPath,String destPath,int mogrifyQuality,String cmdFolder,ImageBean imageBean) throws IOException{
			return reduceImage(srcImgPath,mogrifyQuality, imageBean.getQuality(), destPath, cmdFolder, imageBean.isScale(), imageBean.getFixedWidth());
		}

		/***
		 * 只调用mogrify.exe转化为RGB格式,而不改变quality
		 * @param srcImgPath
		 * @param destPath
		 * @param cmdFolder
		 * @param imageBean
		 * @return
		 * @throws IOException
		 */
		public static File reduceImage(String srcImgPath,String destPath,String cmdFolder,ImageBean imageBean) throws IOException{
			return reduceImage(srcImgPath,SystemHWUtil.NEGATIVE_ONE, imageBean.getQuality(), destPath, cmdFolder, imageBean.isScale(), imageBean.getFixedWidth());
		}

		/***
		 * 只改变图像大小
		 * @param srcFile
		 * @param fixedWidth
		 * @param destFile
		 * @throws IOException
		 */
		public static void modifyPixel(File srcFile,int fixedWidth,File destFile) throws IOException{
			BufferedImage image = null;
			image=ImageHWUtil.inputImage(srcFile);
			image = bufferImage(image, BufferedImage.TYPE_INT_RGB,fixedWidth);
			ImageIO.write(image, SystemHWUtil.getFileSuffixName(srcFile.getName()), destFile);
		}

		/***
		 * 修改宽度为1024px
		 * @param image
		 * @param type
		 * @return
		 */
		public static BufferedImage bufferImage(BufferedImage image, int type) {
			return bufferImage(image, type, 1024);
		}
		/***
		 * 修改图片的像素
		 * @param image
		 * @param type
		 * @param fixedWidth
		 * @return
		 */
		public static BufferedImage bufferImage(BufferedImage image, int type,int fixedWidth) {
			int width22 = image.getWidth(null);
			int height22 = image.getHeight(null);
			System.out.println("width22:" + width22);
			System.out.println("height22:" + height22);
			if (width22 > fixedWidth) {
				float oldWidth22 = width22;
				width22 = fixedWidth;
				float height33 = ((float) height22) * (((float)fixedWidth) / oldWidth22);
				height22 = (int) height33;
			}else{
				System.out.println("没有修改宽度");
				return image;
			}
			//设置图片的像素大小
			BufferedImage bufferedImage = new BufferedImage(width22, height22, type);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, width22, height22, null);
			
			waitForImage(bufferedImage);
			
			return bufferedImage;
		}
		private static void waitForImage(BufferedImage bufferedImage) {
			final ImageLoadStatus imageLoadStatus = new ImageLoadStatus();
			bufferedImage.getHeight(new ImageObserver() {
				public boolean imageUpdate(Image img, int infoflags, int x, int y,
						int width, int height) {
					if (infoflags == ALLBITS) {
						imageLoadStatus.heightDone = true;
						return true;
					}
					return false;
				}
			});

		}

		public static void compressImg(File dir,JTextArea resultTA1,File destPath) throws IOException{
			File[]files=FileUtils.getJPGImageFiles(dir);
			for(int i=0;i<files.length;i++){

				File file=files[i];
				if(file.getName().endsWith("png")||file.getName().endsWith("PNG")){
					continue;
				}
				long fileSize=FileUtils.getFileSize2(file);//获取文件的大小,单位Byte
				if(fileSize<1024*1024){//如果小于1MB,则不进行压缩
//					ComponentUtil.appendResult(resultTA1, "忽略:"+SystemHWUtil.CRLF+file.getAbsolutePath(), false);
					continue;
				}
				ComponentUtil.appendResult(resultTA1, "before compress:"+file.getAbsolutePath()+ SystemHWUtil.CRLF+" size:"+FileUtils.formatFileSize2(file), false);

				int mogrifyQuality=mogrifyRule(file);
				ComponentUtil.appendResult(resultTA1, "mogrify compress rate:"+ mogrifyQuality+"%",false);
				BufferedImageBean imageBean=getBufferedImage(file.getAbsolutePath(),mogrifyQuality);//主要目的是获取图片的像素大小,若不是RGB,则转化为RGB格式,同时mogrifyQuality会生效
				ComponentUtil.appendResult(resultTA1, imageBean.isRGB()?"RGB图片":"CMYK模式", false);
//				image.flush();
				String fileSize2=FileUtils.formatFileSize2(file);

				if(!imageBean.isRGB()){//经过mogrify命令压缩之后
					ComponentUtil.appendResult(resultTA1, "after mogrify compress size:"+ fileSize2,false);
					if(FileUtils.getFileSize2(file)<2*1024*1024){
					ComponentUtil.appendResult(resultTA1, "忽略2:"+SystemHWUtil.CRLF+file.getAbsolutePath(), true);
					continue;
					}
				}else{//RGB模式
					if(FileUtils.getFileSize2(file)<1024*1024){
						continue;
					}
				}
				float quality=mogrifyRule2(file);//纯Java 压缩
				boolean offerDest=true;
				if(ValueWidget.isNullOrEmpty(destPath)){
					destPath=new File(file.getAbsolutePath()+RandomUtils.getTimeRandom2());
					offerDest=false;
				}

//				ImageHWUtil.reduceImage(imageBean.getImage(), quality/100.0f, destPath.getAbsolutePath());
				thumbnail(imageBean, quality/100.0f, 3000/*最大宽度*/, destPath);
				long destSize=FileUtils.getFileSize2(destPath);
				while(destSize>1024*1024){
					destPath.delete();
					quality-=6;
//					ImageHWUtil.reduceImage(imageBean.getImage(), quality/100.0f, destPath.getAbsolutePath());
					thumbnail(imageBean, quality/100.0f, 3000/*最大宽度*/, destPath);
					destSize=FileUtils.getFileSize2(destPath);
				}
				ComponentUtil.appendResult(resultTA1, "compress rate:"+ quality+"%",false);
				if(!offerDest){//如果提供了offerDest
				boolean isDeleteSuccess=file.delete();
				if(!isDeleteSuccess){
					ComponentUtil.appendResult(resultTA1, "Failed to delete:"+ file.getName(),true);
					continue;
				}

				destPath.renameTo(file);//重命名
				}
				ComponentUtil.appendResult(resultTA1, "after java compress:"+FileUtils.formatFileSize2(file), true);
			}
		}
		
		/***
		 * 适用于 mogrify.exe 压缩
		 * @param fileSize
		 * @return
		 */
		public static int mogrifyRule(long fileSize){
			long M=1024*1024;//1M 的大小
			if(fileSize>50*M){
				return 60;
			}else if(fileSize>8*M){
				return (int)(75-(15*(fileSize-8*M)/((50-8)*M)));
			}else if(fileSize>2*M){
				return (int)(96-(21*(fileSize-2*M)/((8-2)*M)));
			}else if(fileSize>M){
				return (int)(98-(2*(fileSize-1*M)/((2-1)*M)));
			}
			return 100;
		}
		
		/***
		 * 适用于 mogrify.exe 压缩
		 * @param file
		 * @return
		 */
		public static int mogrifyRule(File file){
			long size=FileUtils.getFileSize2(file);
			return mogrifyRule(size);
		}

		/***
		 * 适用于纯Java压缩
		 * @param fileSize
		 * @return
		 */
		public static float mogrifyRule2(long fileSize){
			float M=1024*1024f;//1M 的大小
			if(fileSize>50*M){
				return 70;
			}else if(fileSize>8*M){
				return (80.0f-(10.0f*(fileSize-8*M)/((float)(50-8)*M)));
			}else if(fileSize>1*M){
				return (95.0f-(15.0f*(fileSize-1*M)/((float)(8-1)*M)));
			}
			return 100f;
		}
		
		/***
		 * 适用于纯Java压缩
		 * @param file
		 * @return
		 */
		public static float mogrifyRule2(File file){
			long size=FileUtils.getFileSize2(file);
			return mogrifyRule2(size);
		}

		/***
		 * 从hex,解析出RGB<br>例如256eb0
		 * @param hex22
		 * @return
		 */
		public static int[] parseHexColor(String hex22){
			int rgb[]=new int[3];
			if(ValueWidget.isNullOrEmpty(hex22)){
				return rgb;
			}
			int length=hex22.length();

			if(length==6){
				String red=hex22.substring(0, 2);
				String green=hex22.substring(2, 4);
				String blue=hex22.substring(4, 6);
//				System.out.println("red:"+red);
//				System.out.println("green:"+green);
//				System.out.println("blue:"+blue);
//				SystemHWUtil.h
				rgb[0]=Integer.parseInt(red,16);
				rgb[1]=Integer.parseInt(green,16);
				rgb[2]=Integer.parseInt(blue,16);

			}else{
				String red=hex22.substring(0, 1);
				String green=hex22.substring(1, 2);
				String blue=hex22.substring(2, 3);
				rgb[0]=Integer.parseInt(red+red,16);
				rgb[1]=Integer.parseInt(green+green,16);
				rgb[2]=Integer.parseInt(blue+blue,16);

//				result=String.valueOf(Integer.parseInt(red+red,16))+" , "
//				+Integer.parseInt(green+green,16)+" , "+Integer.parseInt(blue+blue,16);
			}
			return rgb;
		}
		
		/***
		 * Windows-Preferences-Java-Complicer-
		 * Errors/Warnings里面的Deprecated and restricted API中的Forbidden references(access rules)选为Warning就可以编译通过
		 * @param filename
		 * @param thumbWidth
		 * @param thumbHeight
		 * @param quality
		 * @param outFilename
		 * @throws InterruptedException
		 * @throws FileNotFoundException
		 * @throws IOException
		 */
	    public static void createThumbnail(String filename, int thumbWidth, int thumbHeight, int quality, String outFilename)
	            throws InterruptedException, IOException
	        {
	            // load image from filename
	            Image image = Toolkit.getDefaultToolkit().getImage(filename);
	            MediaTracker mediaTracker = new MediaTracker(new Container());
	            mediaTracker.addImage(image, 0);
	            mediaTracker.waitForID(0);
	            // use this to test for errors at this point: System.out.println(mediaTracker.isErrorAny());

	            // determine thumbnail size from WIDTH and HEIGHT
	            double thumbRatio = (double)thumbWidth / (double)thumbHeight;
	            int imageWidth = image.getWidth(null);
	            int imageHeight = image.getHeight(null);
	            double imageRatio = (double)imageWidth / (double)imageHeight;
	            //为了保证图片比例不变.
	            if (thumbRatio < imageRatio) {
	                thumbHeight = (int)(thumbWidth / imageRatio);
	            } else {
	                thumbWidth = (int)(thumbHeight * imageRatio);
	            }

	            // draw original image to thumbnail image object and
	            // scale it to the new size on-the-fly
	            BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
	            Graphics2D graphics2D = thumbImage.createGraphics();
	            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	            graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

	            // save thumbnail image to outFilename
	            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFilename));
	            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
	            quality = Math.max(0, Math.min(quality, 100));
	            param.setQuality((float)quality / 100.0f, false);
	            encoder.setJPEGEncodeParam(param);
	            encoder.encode(thumbImage);
	            out.close();
	        }

	    public static void thumbSize(int thumbWidth, int thumbHeight,int imageWidth,int imageHeight){
	    	 double thumbRatio = (double)thumbWidth / (double)thumbHeight;
	    	 double imageRatio = (double)imageWidth / (double)imageHeight;
	            if (thumbRatio < imageRatio) {
	                thumbHeight = (int)(thumbWidth / imageRatio);
	            } else {
	                thumbWidth = (int)(thumbHeight * imageRatio);
	            }
	            System.out.println(thumbWidth+"\t"+thumbHeight);
	            System.out.println(imageWidth+"\t"+imageHeight);
	    }

	    public static BufferedImage genericImage(JComponent ta,File destFile,String format){
	    	return generateImage(ta, destFile, format, null);
	    }
	public static BufferedImage generateImage(JComponent ta,File destFile,String format,Integer specifiedHeight) {//TODO 如何提高分辨率
		return generateImage(ta,destFile,format,specifiedHeight,-1);
	}
	    /***
	     * convert JTextArea to image
	     * @param ta
	     * @param destFile
	     * @param format
	     */
	    public static BufferedImage generateImage(JComponent ta,File destFile,String format,Integer specifiedHeight,Integer specifiedWidth){//TODO 如何提高分辨率
	    	int height=ta.getHeight();
			int width=ta.getWidth();
	    	if(specifiedHeight!=null&&specifiedHeight!=SystemHWUtil.NEGATIVE_ONE){//如果指定了高度
	    		height=specifiedHeight;
	    	}
			if(specifiedWidth!=null&&specifiedWidth!=SystemHWUtil.NEGATIVE_ONE){//如果指定了高度
				width=specifiedWidth;
			}
			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g2d = img.createGraphics();
	        ta.printAll(g2d);
	        g2d.dispose();
	        if(!ValueWidget.isNullOrEmpty(destFile)){
	        	try {
		            ImageIO.write(img, format/*"jpg"*/, destFile);
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
	        }

	        return img;
		}

		static class ImageLoadStatus {
			public boolean widthDone = false;
			public boolean heightDone = false;
		}
}
