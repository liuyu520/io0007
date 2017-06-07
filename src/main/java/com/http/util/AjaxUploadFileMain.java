package com.http.util;

import com.io.hw.json.HWJacksonUtils;
import com.string.widget.util.ValueWidget;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class AjaxUploadFileMain {
    public static final String ERROR_TIPS = "参数不对:file path which will be uploaded  real filename";

    public static void main(String[] args) {

        if (ValueWidget.isNullOrEmpty(args)) {
            System.out.println(ERROR_TIPS);
            return;
        }
        if (args.length < 1) {
            System.out.println(ERROR_TIPS);
            return;
        }
            String fileName = null;
            String filePathStr = args[0];
            if (args.length > 1) {
                fileName = args[1];
            } else {
                java.io.File file = new java.io.File(filePathStr);
                if (!file.exists()) {
                    System.out.println("文件不存在:" + filePathStr);
                    return;
                }
                fileName = file.getName();
            }

            try {
                FileInputStream ins = new FileInputStream(filePathStr);
                if (null == ins) {
                    System.out.println("文件不存在:" + filePathStr);
                    return;
                }
                Map<String, String> parameters = null;
                String result = HttpSocketUtil.uploadFile("http://blog.yhskyc.com/convention2/ajax_image/upload",
                        ins, parameters, fileName);

                Map map = (Map) HWJacksonUtils.deSerialize(result, Map.class);
                System.out.println(map.get("fullUrl"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}
