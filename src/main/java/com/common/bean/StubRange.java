package com.common.bean;

import com.common.util.SystemHWUtil;
import com.string.widget.util.ValueWidget;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄威 on 5/3/16.<br >
 */
public class StubRange implements Serializable {
    /***
     * 应答json 列表,真正访问时,一定会返回其中的一个
     */
    private List<String> stubs;
    /***
     * 请求参数名称<br />
     * String parameterVal=request.getParameter(attributeName);
     */
    private String attributeName;
    /***
     * 请求参数的值 和序号的映射关系<br />
     * value 从0开始
     */
    private Map attributeValIndexMap = new HashMap();
    /***
     * 当前的stub,序号从零开始
     */
    private int selectedIndex;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /***
     * 当前的stub,序号从零开始
     * @param selectedIndex
     * @return
     */
    public StubRange setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        return this;
    }

    public List<String> getStubs() {
        return stubs;
    }

    public StubRange setStubs(List<String> stubs) {
        this.stubs = stubs;
        return this;
    }

    public boolean hasAttributeVal(String val) {
        if (ValueWidget.isNullOrEmpty(val)) {
            return false;
        }
        return this.getAttributeValIndexMap().containsKey(val);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public StubRange setAttributeName(String attributeName) {
        this.attributeName = attributeName;
        return this;
    }

    public Map<String, Integer> getAttributeValIndexMap() {
        return attributeValIndexMap;
    }

    public Map<Integer, String> getIndexAttributeValMap() {
        return SystemHWUtil.reverseMap(this.getIndexAttributeValMap());
    }

    public StubRange setAttributeValIndexMap(Map attributeValIndexMap) {
        this.attributeValIndexMap = attributeValIndexMap;
        return this;
    }
}
