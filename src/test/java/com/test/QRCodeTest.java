package com.test;

import com.common.util.QRCodeUtil;
import com.google.zxing.WriterException;
import com.io.hw.file.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class QRCodeTest {

    //	@Test
    public void test01() {
        String content = "小袁，你很好看";
        try {
            byte[] bytes = QRCodeUtil.encode(content);
            FileUtils.writeBytesToFile(bytes, new File("d:\\bin\\a.jpg"));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //	@Test
    public void test02() {
        String content = "http://www.baidu.com";
        try {
            byte[] bytes = QRCodeUtil.encode(content, 300);
            FileUtils.writeBytesToFile(bytes, new File("d:\\bin\\a.jpg"));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
