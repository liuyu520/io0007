package com.swing.table;

import com.common.bean.ParameterIncludeBean;
import com.common.bean.RequestInfoBean;
import com.common.bean.ResponseResult;
import com.common.bean.TableDataBean;
import com.common.util.SystemHWUtil;
import com.io.hw.awt.color.CustomColor;
import com.io.hw.json.HWJacksonUtils;
import com.string.widget.util.ValueWidget;
import com.swing.callback.ActionCallback;
import com.swing.component.AssistPopupTextArea;
import com.swing.component.GenerateJsonTextArea;
import com.swing.component.RadioButtonPanel;
import com.swing.component.inf.IPlaceHolder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TableUtil3 {
	/***
	 * 是否执行System out语句
	 */
	private static boolean isDetail = false;
	public TableUtil3() {
//		throw new Error("Don't let anyone instantiate this class.");
	}

	/***
	 * hide specified column
	 * 
	 * @param table
	 * @param column
	 */
	public static void hideTableColumn(JTable table, int column) {
		TableColumnModel columns = table.getColumnModel();
		int sum_column = columns.getColumnCount();
		if (column <= sum_column) {// 考虑到没有表格数据的情况
			TableColumn column_id_data = columns.getColumn(column);
			column_id_data.setMaxWidth(0);
			column_id_data.setPreferredWidth(0);
			column_id_data.setMinWidth(0);

			TableColumn column_id_header = table.getTableHeader()
					.getColumnModel().getColumn(column);
			column_id_header.setMaxWidth(0);
			column_id_header.setPreferredWidth(0);
			column_id_header.setMinWidth(0);
		}
	}

	/***
	 * show specified column
	 * 
	 * @param table
	 * @param column
	 */
	public static void showTableColumn(JTable table, int column) {
		int width = 40;
		TableColumnModel columns = table.getColumnModel();
		int sum_column = columns.getColumnCount();
		if (column <= sum_column) {// 考虑到没有表格数据的情况
			TableColumn column_id_data = columns.getColumn(column);

			column_id_data.setMaxWidth(width + 100);
			column_id_data.setPreferredWidth(width);
			column_id_data.setMinWidth(width);

			// column_id_data.setResizable(true);
			TableColumn column_id_header = table.getTableHeader()
					.getColumnModel().getColumn(column);
			column_id_header.setMaxWidth(width + 100);
			column_id_header.setPreferredWidth(width);
			column_id_header.setMinWidth(width);
			// column_id_header.setResizable(true);
		}
	}
	/***
     * 获取表格中的请求要素
     *
     * @return
     */
    /*public static Object[][] getParameter4Table(JTable parameterTable_1){
    	return getParameter4Table(parameterTable_1, false);
    }*/
	 /***
     * 获取表格中的请求要素
     *
     * @return
	  */
	 public static TableDataBean getParameter4Table(JTable parameterTable_1/*,boolean isTextComponent*/) {
		 TableDataBean tableDataBean = new TableDataBean();
		 TableModel model= parameterTable_1.getModel();
        int rowCount = model.getRowCount();//参数的个数
        int columnCount=model.getColumnCount();
        Object[][] data2 = new Object[rowCount][];
		 Object[][] data2SwingComponent = new Object[rowCount][];
		 for (int rowIndex = 0; rowIndex< rowCount; rowIndex++) {
            if (!ValueWidget.isNullOrEmpty(model.getValueAt(rowIndex, 0))) {
                Object[] objs = new Object[columnCount];
				Object[] objsSwingComponent = new Object[columnCount];
				for (int j = 0; j < columnCount; j++) {
                    Object val = model.getValueAt(rowIndex, j);
                    if (!ValueWidget.isNullOrEmpty(val)) {
                		if(/*isTextComponent && */val instanceof JScrollPane){
                			JScrollPane js=(JScrollPane)val;
                			if(js.getViewport().getComponentCount()>0){
                				JTextComponent tf=(JTextComponent)js.getViewport().getComponent(0);
                            	objs[j] = getTFVal(tf);
                			}
                        }else if(/*isTextComponent &&*/ val instanceof JTextComponent){
                        	JTextComponent tf=(JTextComponent)val;
                        	objs[j] = getTFVal(tf);
                        }else{
                        	objs[j] = val;
						}
						objsSwingComponent[j]=val;
					}
                }
                data2[rowIndex] = objs;
				data2SwingComponent[rowIndex] = objsSwingComponent;
			}

		}
		 tableDataBean.setComplexSwingComponent(data2SwingComponent);
		 tableDataBean.setOnlyStringValue(data2);
//    	System.out.println(data2.length);
		 return tableDataBean;
	 }
    private static String getTFVal(JTextComponent tf){
    	String valTmp=null;
		if(tf instanceof IPlaceHolder){
			IPlaceHolder placeHolder=(IPlaceHolder)tf;
			valTmp=placeHolder.getText2();
		}else{
			valTmp=tf.getText();
		}
		return valTmp;
    }
    public static List<ParameterIncludeBean> getTableParameters(JTable parameterTable_1/*,boolean isTextComponent*/) {
		Object[][] data2 = getParameter4Table(parameterTable_1/*,isTextComponent*/).getOnlyStringValue();
		return getTableParameters(data2);
    }
    public static List<ParameterIncludeBean> getTableParameters(Object[][] data2) {
    	List<ParameterIncludeBean> parameters=new ArrayList<ParameterIncludeBean>();
        if (data2.length > 0) {
            for (int rowIndex = 0; rowIndex < data2.length; rowIndex++) {
                ParameterIncludeBean parameterIncludeBean = new ParameterIncludeBean();
                if (!ValueWidget.isNullOrEmpty(data2[rowIndex])) {
                    Object key = data2[rowIndex][0];
                    String keyStr=(String)key;
                    if(null!=keyStr){//trim 掉key的空格
                    	keyStr=keyStr.trim();
                    }
                    //过滤掉相同的key
                    if (!ValueWidget.isNullOrEmpty(key)&&!isContain(parameters, keyStr)) {
                        parameterIncludeBean.setKey(keyStr);
                        Object valueObj = data2[rowIndex][1];
                        String val = null;
                        if (valueObj == null) {
                            val = SystemHWUtil.EMPTY;
                        } else {
                            val = (String) valueObj;
                            val=val.trim();//参数值trim
                        }
                        parameterIncludeBean.setValue(val);
                        RadioButtonPanel radioButtonPanel = (RadioButtonPanel) data2[rowIndex][2];
                        parameterIncludeBean.setIgnore(radioButtonPanel.isIgnore());
                        parameterIncludeBean.setInclude(radioButtonPanel.isInclude());
                        parameters.add(parameterIncludeBean);
                    }
                }
            }
        }

//        System.out.println(parameters.size());
        return parameters;
    }
    /***
     * 判断parameters 中是否包含 key
     *
     * @param parameters
     * @param key
     * @return
     */
    public static int isContains2(List<ParameterIncludeBean> parameters, String key) {
        if (ValueWidget.isNullOrEmpty(parameters)) {
            return SystemHWUtil.NEGATIVE_ONE;
        }
        int length = parameters.size();
        for (int i = 0; i < length; i++) {
            ParameterIncludeBean parameterIncludeBean = parameters.get(i);
            if (parameterIncludeBean.getKey().equals(key)) {
                return i;
            }
        }
        return SystemHWUtil.NEGATIVE_ONE;
    }
    public static  boolean isContain(List<ParameterIncludeBean> parameters ,String key){
    	if(ValueWidget.isNullOrEmpty(parameters)){
    		return false;
    	}
    	int size=parameters.size();
    	for(int i=0;i<size;i++){
    		if(parameters.get(i).getKey().equals(key)){
    			return true;
    		}
    	}
    	return false;
    }
    /***
     * 表格增加一行
     */
    public static void addParameter(JTable parameterTable_1, String key, boolean hasTextField, boolean isTF_table_cell, Map<String, ActionCallback> actionCallbackMap) {
//        System.out.println("增加一行");
        List<ParameterIncludeBean> parameterIncludeBeans = getParameterIncludeBeans(key);
        addParameter(parameterTable_1, hasTextField, isTF_table_cell, parameterIncludeBeans, actionCallbackMap);
    }

    public static void addParameter(JTable parameterTable_1, boolean hasTextField, boolean isTF_table_cell, List<ParameterIncludeBean> parameterIncludeBeans, Map<String, ActionCallback> actionCallbackMap) {
        DefaultTableModel tableModel = (DefaultTableModel) parameterTable_1.getModel();
        if (ValueWidget.isNullOrEmpty(parameterIncludeBeans)) {
            addTableRow(hasTextField, isTF_table_cell, tableModel, null, actionCallbackMap);
        } else {
            int size = parameterIncludeBeans.size();
            for (int i = 0; i < size; i++) {
                ParameterIncludeBean parameterIncludeBean = parameterIncludeBeans.get(i);
                addTableRow(hasTextField, isTF_table_cell, tableModel, parameterIncludeBean, actionCallbackMap);
            }
        }

    }

    public static void addTableRow(boolean hasTextField, boolean isTF_table_cell, DefaultTableModel tableModel, ParameterIncludeBean parameterIncludeBean
            , Map<String, ActionCallback> actionCallbackMap) {
        Object[] rowData = null;
        RadioButtonPanel panel = new RadioButtonPanel();
        panel.init(hasTextField);

        if (isTF_table_cell) {
            Color clor = CustomColor.getMoreLightColor();
            JTextArea keyTA = null;
            if (parameterIncludeBean == null) {
                keyTA = new AssistPopupTextArea(actionCallbackMap);
            } else {
                keyTA = new AssistPopupTextArea(parameterIncludeBean.getKey(), actionCallbackMap);
            }

            keyTA.setBackground(clor);
            JComponent keyTA2 = new JScrollPane(keyTA);

            JTextArea valTA = null;
            if (parameterIncludeBean == null) {
                valTA = new GenerateJsonTextArea();
            } else {
                valTA = new GenerateJsonTextArea(parameterIncludeBean.getValue());
            }

            valTA.setBackground(clor);
            JComponent valScroll = new JScrollPane(valTA);
            rowData = new Object[]{keyTA2, valScroll, panel};
        } else {
            rowData = new Object[]{parameterIncludeBean.getKey(), parameterIncludeBean.getValue(), panel};
        }
        tableModel.addRow(rowData);
    }

    public static ParameterIncludeBean getParameterIncludeBean(String key) {
        ParameterIncludeBean parameterIncludeBean = new ParameterIncludeBean();
		if (!ValueWidget.isNullOrEmpty(key)) {
			if (key.contains("=") || key.contains(":")) {
				String[] strs = key.split("[:=]",2);//解决黏贴时,冒号后面被截断的问题
				parameterIncludeBean.setKey(strs[0]);
				parameterIncludeBean.setValue(strs[1]);
			} else {
				parameterIncludeBean.setKey(key);
			}
            parameterIncludeBean.setInclude(true);
        }
		return parameterIncludeBean;
	}

    public static List<ParameterIncludeBean> getParameterIncludeBeans(String key) {
        if (ValueWidget.isNullOrEmpty(key)) {
            return null;
        }
        List<ParameterIncludeBean> beans = new ArrayList<ParameterIncludeBean>();
        /***
         * windows	\r\n
         linux	\n
         */
        String[] keyVals = key.split("(\n)|(\r\n)");
        int length = keyVals.length;
        for (int i = 0; i < length; i++) {
            ParameterIncludeBean parameterIncludeBean = getParameterIncludeBean(keyVals[i]);
            beans.add(parameterIncludeBean);
        }
        return beans;
    }

    public static TreeMap getParameterMap(List<ParameterIncludeBean> parameters, boolean isUrlEncoding, String requestCharset) {
		TreeMap map = new TreeMap();
        int size = parameters.size();
        for (int i = 0; i < size; i++) {
            ParameterIncludeBean parameterIncludeBean = parameters.get(i);
            if (parameterIncludeBean.isInclude()) {
            	String val=parameterIncludeBean.getValue();
            	if(isUrlEncoding&& !ValueWidget.isNullOrEmpty(requestCharset)){
            		try {
						val=URLEncoder.encode(val, requestCharset);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
            	}
                map.put(parameterIncludeBean.getKey(), val);
            }
        }
        return map;
   }

    public static void setTableData3(JTable parameterTable_1, Map requestMap, boolean hasTextField, boolean isTF_table_cell, String[] columnNames, final Map<String, ActionCallback> actionCallbackMap) {
        int length = requestMap.size();
        if (length > 0) {
            Object[][] datas = new Object[length][];
            int count = 0;
            for (Object obj : requestMap.keySet()) {
                Object val = requestMap.get(obj);
                Object[] objs = new Object[3];
                RadioButtonPanel panel = new RadioButtonPanel();
                panel.init(hasTextField);
                objs[2] = panel;
                Color backColor=CustomColor.getMoreLightColor();
//		    		objs[2]="c"+i;
                if(isTF_table_cell){
                    JTextArea keyTA = new AssistPopupTextArea(String.valueOf(obj), actionCallbackMap);
                    keyTA.setBackground(backColor);
                	objs[0] = new JScrollPane(keyTA);
                }else{
                	objs[0] =obj;
                }
                
                if (ValueWidget.isNullOrEmpty(val) || val.equals("null")
                		|| val.equals("undefined")) {//配置文件中保存的是"null",而不是null
                    val = SystemHWUtil.EMPTY;
                }
                if(isTF_table_cell){
                	String valString=null;
                	if(val instanceof Map){//val有可能是LinkedHashMap
                		valString=HWJacksonUtils.getJsonP(val);
                	}else{
                		valString=String.valueOf(val);
                	}
                    JTextArea valTA = new GenerateJsonTextArea(valString, actionCallbackMap);
                    valTA.setBackground(backColor);
                	objs[1] = new JScrollPane(valTA);
                }else{
                	objs[1] = val;
                }
                
                datas[count] = objs;
                count++;
            }//for
//            setTableData2(parameterTable_1,datas,columnNames);
            appendTableData(parameterTable_1, datas, columnNames);
        }//if
	}

	public static void setTableData2(JTable parameterTable_1, Object[][] datas, String[] columnNames) {
		DefaultTableModel model = new DefaultTableModel(datas, columnNames);
        parameterTable_1.setModel(model);
        parameterTable_1.setRowHeight(30);
//        rendTable();
    }
    /***
     * 追加,原有输入框仍保留,所以可以使用Ctrl+Z
     * @param datas
     */
    public static void appendTableData(JTable parameterTable_1,Object[][] datas,String[]columnNames){
    	int rowCount=parameterTable_1.getRowCount();
    	if(rowCount<1){//如果表格本来为空
    		setTableData2(parameterTable_1,datas,columnNames);
			if (isDetail) {
				System.out.println("表格为空");
			}
		}else{//表格不为空
			if (isDetail) {
				System.out.println("表格不为空,行数:" + rowCount);
			}
			for(int i=0;i<rowCount;i++){
    			setTableValueAt(parameterTable_1,i, 0, datas);
    			setTableValueAt(parameterTable_1,i, 1, datas);
    		}
    		DefaultTableModel tableModel = (DefaultTableModel) parameterTable_1.getModel();
    		for(int i=rowCount;i<datas.length;i++){
    			tableModel.addRow(datas[i]);
    		}
    	}
	}

	public static void setTableValueAt(JTable parameterTable_1, int rowIndex, int columnIndex, Object[][] datas) {
		if (rowIndex >= datas.length) {//防止数组越界
			return;
		}
		Object keyObj = datas[rowIndex][columnIndex];
		setTableValueAt(parameterTable_1, rowIndex, columnIndex, keyObj);
	}

	/***
     * 设置表格单元格的值
     * @param rowIndex
     * @param columnIndex
	 * @param keyObj
	 */
	public static void setTableValueAt(JTable parameterTable_1, int rowIndex, int columnIndex, Object keyObj){
		if(keyObj instanceof JScrollPane){
			JScrollPane js=(JScrollPane)keyObj;
        	JTextComponent tf=(JTextComponent)js.getViewport().getComponent(0);
        	keyObj = tf.getText();
		}
		String key=null;
		if(keyObj instanceof String){
			key=(String)keyObj;
		}else{
			key=String.valueOf(keyObj);
		}
    	Object valueAtObj=parameterTable_1.getValueAt(rowIndex, columnIndex);
        setTableCellVal(parameterTable_1, rowIndex, columnIndex, keyObj, key, valueAtObj);

    }

    public static void setTableCellVal(JTable parameterTable_1, int rowIndex, int columnIndex, Object keyObj, String key, Object valueAtObj) {
        if (valueAtObj instanceof JScrollPane) {
            JScrollPane keyScrollPane = (JScrollPane) valueAtObj;
            JTextArea keyTA = (JTextArea) keyScrollPane.getViewport().getComponent(0);
            keyTA.setText(key);
        } else if (valueAtObj instanceof JTextComponent) {
            JTextComponent jTextComponent = (JTextComponent) valueAtObj;
            jTextComponent.setText(key);
        } else {
            if (null != keyObj) {
                parameterTable_1.setValueAt(keyObj, rowIndex, columnIndex);
            }
        }
    }

    /***
     * 清空单元格<br>
     * @param parameterTable_1
     * @param rowIndex
     * @param columnIndex
     */
    private static void cleanTableValue(JTable parameterTable_1,int rowIndex,int columnIndex){
    	Object valueAtObj=parameterTable_1.getValueAt(rowIndex, columnIndex);
        setTableCellVal(parameterTable_1, rowIndex, columnIndex, null, SystemHWUtil.EMPTY, valueAtObj);
    }
    /***
     * 清空表格数据<br>
     * @param parameterTable_1
     */
    public static void cleanTableData(JTable parameterTable_1){
    	int rowCount=parameterTable_1.getRowCount();
    	if(rowCount>0){
    		for(int i=0;i<rowCount;i++){
    			cleanTableValue(parameterTable_1,i, 0);
    			cleanTableValue(parameterTable_1,i, 1);
    		}
    	}
    }
	/**
	 * JTable
	 *
	 * @param table JTable
	 * @param column
	 */
	public static void hiddenCell(JTable table, int column)
	{
		TableColumn tc = table.getTableHeader().getColumnModel().getColumn(column);
		tc.setMaxWidth(0);
		tc.setPreferredWidth(0);
		tc.setWidth(0);
		tc.setMinWidth(0);
		table.getTableHeader().getColumnModel().getColumn(column).setMaxWidth(0);
		table.getTableHeader().getColumnModel().getColumn(column).setMinWidth(0);
	}

	/**
	 * JTable
	 *
	 * @param table JTable
	 * @param column
	 * @param width
	 */
	public static void showColumn(JTable table, int column, int width)
	{
		TableColumn tc = table.getColumnModel().getColumn(column);
		tc.setMaxWidth(width);
		tc.setPreferredWidth(width);
		tc.setWidth(width);
		tc.setMinWidth(width);
		table.getTableHeader().getColumnModel().getColumn(column).setMaxWidth(width);
		table.getTableHeader().getColumnModel().getColumn(column).setMinWidth(width);
    }

    public static String getPreRequestVal(ResponseResult preResponseResult, RequestInfoBean requestInfoBean) {
        Map<String, String> responseMapLogin = preResponseResult.getResponseJsonMap();
        String preRequestKey = requestInfoBean.getPreRequestParameterName();
        if (ValueWidget.isNullOrEmpty(preRequestKey)) {
            System.out.println("preRequestKey is null.servlet path:" + requestInfoBean.getActionPath());
        }
        return responseMapLogin.get(preRequestKey);
    }
}
