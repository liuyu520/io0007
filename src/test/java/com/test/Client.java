package com.test;

import com.io.hw.json.HWJacksonUtils;
import com.test.bean.ChildSelect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    public static void main(String[] args) {
        Map<String, Object> childSelectMap = new HashMap<String, Object>();
//		childSelectMap.put("jquery图片特效", value);

        ChildSelect childSelect = new ChildSelect();

        List list = new ArrayList();

        Map<String, Object> childSelectMap1_2 = new HashMap<String, Object>();
        childSelectMap1_2.put("jquery图片切换", "jqpic-01");
        childSelectMap1_2.put("jquery幻灯片", "jqpic-02");
        childSelectMap1_2.put("jquery图片滚动", "jqpic-03");
        childSelectMap1_2.put("jquery图片放大", "jqpic-04");
        childSelectMap1_2.put("jquery广告", "jqpic-05");

        childSelect.setVal("jqpic");
        childSelect.setItems(childSelectMap1_2);

        System.out.println(HWJacksonUtils.getJsonP(childSelect));
    }
}
