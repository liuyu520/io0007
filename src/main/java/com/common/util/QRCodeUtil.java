package com.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.string.widget.util.ValueWidget;

/**
 * 
 * @author huangwei
 */
public class QRCodeUtil {
    private static Log logger = LogFactory.getLog(QRCodeUtil.class);

    /**
     * 字符串转二维码图片字节数组
     *
     * @param content
     * @param size : 像素
     * @return
     * @throws com.google.zxing.WriterException
     * @throws java.io.IOException
     */
    public static byte[] encode(String content,int size)
            throws WriterException, IOException {
    	if(ValueWidget.isNullOrEmpty(content)){//非空判断
    		return null;
    	}
        return encode(content, "utf8", "png", size, size);
    }
    
    public static byte[] encode(String content) throws WriterException, IOException{
    	if(ValueWidget.isNullOrEmpty(content)){//非空判断
    		return null;
    	}
    	return encode(content, 500);
    }

    /**
     * 把字符串转二维码图片字节数组
     *
     * @param content 二维码中的数据
     * @param charSet 数据编码字符集
     * @param format  二维码图片格式
     * @param width  : 像素 ,二维码图片宽度
     * @param height : 像素 ,二维码图片高度
     * @return byte[] 二维码图片字节数组
     * @throws com.google.zxing.WriterException
     * @throws java.io.IOException
     */
    public static byte[] encode(String content, String charSet, String format, int width, int height)
            throws WriterException, IOException {
    	if(ValueWidget.isNullOrEmpty(content)){//非空判断
    		return null;
    	}
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Hashtable<EncodeHintType, Integer> hints = new Hashtable<EncodeHintType, Integer>();
     // 二维码边界空白大小 ，如：1、2、3、4 默认好像是4
        //参考网址:http://bbs.csdn.net/topics/380128389
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix byteMatrix = multiFormatWriter.encode(
                new String(content.getBytes(charSet), "ISO-8859-1"),
                BarcodeFormat.QR_CODE, width, height,hints
        );
     
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(byteMatrix, format, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * 获取二维码图片中的内容
     * 默认utf8编码
     *
     * @param qrCodeBytes
     * @return
     * @throws java.io.IOException
     * @throws com.google.zxing.NotFoundException
     */
    public static String decode(byte[] qrCodeBytes)
            throws IOException, NotFoundException {
    	if(ValueWidget.isNullOrEmpty(qrCodeBytes)){//非空判断
    		return null;
    	}
        return decode(qrCodeBytes, "utf8");
    }

    /**
     * 获取二维码图片中的内容
     *
     * @param qrCodeBytes 二维码图片字节数组
     * @param charSet     二维码字符集编码
     * @return String 二维码图片中的数据
     * @throws java.io.IOException
     * @throws com.google.zxing.NotFoundException
     */
    public static String decode(byte[] qrCodeBytes, String charSet)
            throws IOException, NotFoundException {
    	if(ValueWidget.isNullOrEmpty(qrCodeBytes)){//非空判断
    		return null;
    	}
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(qrCodeBytes);
        BufferedImage image = ImageIO.read(byteArrayInputStream);

        if (image == null) {
            if (logger.isErrorEnabled()) {
                logger.error("data error, cannot decode image.");
            }
            throw new RuntimeException("data error, cannot decode image");
        }
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Hashtable hints = new Hashtable();
        if(ValueWidget.isNullOrEmpty(charSet)){
        	charSet=SystemHWUtil.CHARSET_UTF;
        }
        hints.put(DecodeHintType.CHARACTER_SET, charSet);

        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }
}
