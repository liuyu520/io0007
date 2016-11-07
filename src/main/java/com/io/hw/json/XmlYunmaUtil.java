package com.io.hw.json;

import com.common.bean.PomDependency;
import com.common.bean.StubRange;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 用于解析XML文件
 * @author Administrator
 *
 */
public class XmlYunmaUtil {
    public static final char TAG_BEGIN_CHAR = '<';
    public static final String TAG_BEGIN_String = "<";

    public static final char TAG_END_CHAR = '>';
    public static final String TAG_END_STRING = ">";
    static int index;


    private static ParseBean parseElementName(String xmlContent, int pos) {
        int totalLength = xmlContent.length();
        int i = pos;
        StringBuffer sbing = null;
        while (i < totalLength) {
            char c = xmlContent.charAt(i);
            if (!ValueWidget.isWordChar(c)) {
                i++;
                if (!ValueWidget.isNullOrEmpty(sbing)) {
//					return sbing.toString();
                    break;
                }
                continue;
            }
            if (ValueWidget.isNullOrEmpty(sbing)) {
                sbing = new StringBuffer();
            }
            sbing.append(c);
            i++;
        }
        ParseBean parseBean = new ParseBean();
        parseBean.setLengthHasRead(i - 1);
        if (!ValueWidget.isNullOrEmpty(sbing)) {
            parseBean.setResult(sbing.toString());
        }
        return parseBean;//sbing==null?null:sbing.toString();
    }

    public static int getTextNode(String xmlContent, int pos) {
        int totalLength = xmlContent.length();
        int i = pos;
        boolean isQuote = false;
        while (i < totalLength) {
            char c = xmlContent.charAt(i);
            if (c == '"' && xmlContent.charAt(i - 1) != '\\') {
                if (isQuote) {
                    isQuote = false;
                } else {
                    isQuote = true;
                }
            }
//			if(ValueWidget.isBlank(c)||ValueWidget.isWordChar(c)){//如果是汉字,有问题
            if (c != '>' && c != '<' || (i > 1 && (c == '<' || c == '>') && xmlContent.charAt(i - 1) == '"')/*"<div 不算开始标签*/
                    || (isQuote && (c == '<' || c == '>'))) {
                i++;
//				if(i==372){
//					System.out.println(i);
//				}
                continue;
            }
            if (c == '<' && xmlContent.charAt(i + 1) != '/') {
                return -1;
            } else if (c == '<') {
                return i;
            }
        }
        return -1;
    }

    private static ParseBean parseAttribute(String xmlContent, int pos) {
        int totalLength = xmlContent.length();
        if (pos >= totalLength) {
            return null;
        }
        int i = pos;
        StringBuffer sbing = null;
        boolean continue2 = false;
        while (i < totalLength) {
            char c = xmlContent.charAt(i);
            if (ValueWidget.isBlank(c) && continue2 == false) {
                i++;
                if (!ValueWidget.isNullOrEmpty(sbing)) {
//					return sbing.toString();
                    break;
                }
                continue;
            }
            if (c == '>' || c == '<') {
                break;
            }
            if (ValueWidget.isNullOrEmpty(sbing)) {
                sbing = new StringBuffer();
            }

            if (c == '"' && continue2 == false) {//第一个双引号
                continue2 = true;
            } else {
                if (c == '"' && continue2 == true) {//最后一个双引号
                    continue2 = false;
                    sbing.append(c);
                    i++;
                    break;
                }
            }

            sbing.append(c);
            i++;
        }
        ParseBean parseBean = new ParseBean();
        parseBean.setLengthHasRead(i);
        if (!ValueWidget.isNullOrEmpty(sbing)) {
            parseBean.setResult(sbing.toString());
        }
        return parseBean;//sbing==null?null:sbing.toString();
    }

    /***
     * 是否到了> <br> test ok
     * @param input
     * @param pos
     * @return
     */
    public static int isEndOfTag(String input, int pos) {
        int totalLength = input.length();
        int i = pos;
        while (i < totalLength) {
            char c = input.charAt(i);
            if (ValueWidget.isBlank(c)) {
                i++;
                continue;
            }
            if (c == XmlYunmaUtil.TAG_END_CHAR/*  > */) {
                return i;
            } else {//不是空格,但是又不是>,所以还没有结尾
                break;
            }
        }
        return -1;
    }

    /***
     * 是否到了< <br> 有待测试
     * @param input
     * @param pos
     * @return
     */
    public static int isBeginOfTag(String input, int pos) {
        int totalLength = input.length();
        int i = pos;
        while (i < totalLength) {
            char c = input.charAt(i);
            if (ValueWidget.isBlank(c)) {
                i++;
                continue;
            }
            if (c == XmlYunmaUtil.TAG_BEGIN_CHAR/*  > */) {
                return i;
            } else {//不是空格,但是又不是>,所以还没有结尾
                break;
            }
        }
        return -1;
    }

    /***
     * </html>
     * @param xmlContent
     * @param pos
     * @param endTag
     * @return
     */
    public static int isEnd(String xmlContent, int pos, String endTag) {
        int totalLength = xmlContent.length();
        index = pos;
        while (index < totalLength) {
            char c = xmlContent.charAt(index);
            if (ValueWidget.isBlank(c)) {//忽略空格
                index++;
                continue;
            }
            int foundIndex = SystemHWUtil.findReadLength2(xmlContent, endTag, index);
            return foundIndex;
        }
        return -1;
    }

    public static Element getElement(String xmlContent, int pos, Element parentElement) {
        xmlContent = ValueWidget.deleteHTMLComment(xmlContent);
        Element root = null;
        int totalLength = xmlContent.length();
        int j, k = 0;
        index = pos;
        j = 0;
        Map<String, Object> attributes = new HashMap<String, Object>();
        while (index < totalLength) {
            char c = xmlContent.charAt(index);
            if (ValueWidget.isBlank(c)) {//忽略空格
                index++;
                continue;
            }
            int begin = isBeginOfTag(xmlContent, index);
            boolean end = false;
            if (begin != -1) {//遇到了<
                char c2 = xmlContent.charAt(begin + 1);//可能是/
                if (c2 == '/') {
                    break;
                } else {
                    ParseBean parseBean = parseElementName(xmlContent, begin + 1);
                /*if(parseBean.getResult().equals("level")){
					System.out.println("level");
				}*/
                    System.out.println(parseBean);
                    index = parseBean.getLengthHasRead();
                    root = new Element();
                    root.setTextNode(false);
                    root.setAttributes(attributes);
                    List<Element> children = new ArrayList<Element>();//子节点
                    root.setChildren(children);

                    root.setName(parseBean.getResult());
                    if (!ValueWidget.isNullOrEmpty(parentElement)) {
                        parentElement.getChildren().add(root);
                        parentElement.addChildToMap(root);
                        root.setParent(parentElement);
                    }
                }
            } else {
                int endIndex = isEndOfTag(xmlContent, index);
                if (endIndex != -1) {//找到了<html>的>
                    end = true;
                    index = endIndex + 1;
                    int textNodeIndex = getTextNode(xmlContent, index);
                    if (textNodeIndex == -1) {
                        Element element2 = getElement(xmlContent, index, root);
                    } else {//Text Node
                        Element element2 = new Element();
                        element2.setTextNode(true);
                        element2.setName(xmlContent.substring(index, textNodeIndex).trim());
                        List<Element> children2 = root.getChildren();
                        if (children2 == null) {
                            children2 = new ArrayList<Element>();
                        }
                        children2.add(element2);
                        root.addChildToMap(element2);
                        root.setChildren(children2);
                        element2.setParent(root);
                        index = root.getEndTag().length() + textNodeIndex;
                    }
                }
            }
            if (index >= totalLength) {
                break;
            }
            boolean loop = false;
            if (end) {
                int endIndex = isEnd(xmlContent, index, parentElement.getEndTag());
                if (endIndex != -1) {
                    index = endIndex;
                    if (parentElement.getName().equals("cc")) {
                        System.out.println("cc");
                    }
                    break;
                } else {
                    //兄弟节点
                    Element element33 = getElement(xmlContent, index, parentElement);
                    if (element33 != null && element33.getName().equals("value")) {
                        System.out.println("value");
                    }
                    loop = true;
                    if (null != element33 && null != element33.getParent()) {
                        element33.getParent().addChildToMap(element33);
                    }
                    break;//到了兄弟节点,说明本节点已经扫描结束了.
                }
            }
            ParseBean parseBean = null;
            do {
                parseBean = parseAttribute(xmlContent, index);
                if (ValueWidget.isNullOrEmpty(parseBean)) {
                    continue;
                }
                String attributeStr = parseBean.getResult();
                if (!ValueWidget.isNullOrEmpty(attributeStr)) {
                    String[] strs = attributeStr.split("=");
                    //删除属性值两边的双引号
                    attributes.put(strs[0], SystemHWUtil.delDoubleQuotation(strs[1]));
                    index = parseBean.getLengthHasRead();
                }
            } while (parseBean != null && (!ValueWidget.isNullOrEmpty(parseBean.getResult())));

//			ParseBean parseBean=parseElementName(xmlContent, i);
//			if (xmlContent.charAt(i) == keyWord.charAt(j)) {
//				++i;
//				++j;
//				if (j == keyWord.length()) {
//					k = k + 1;// k++
//					j = 0;
//				}
//			} else {
//				i = i - j + 1;
//				j = 0;
//			}
        }
        return root;
    }

    public static Element getElement(StringBuffer xmlContent, int pos, Element parentElement) {
        return getElement(xmlContent.toString(), pos, parentElement);
    }

    public static void getTree(Element root, StringBuffer string) {
        if (null == string) {
            string = new StringBuffer();
        }
        boolean isParent = ValueWidget.isNullOrEmpty(root.getParent());
        if (isParent) {
            String div_before = "<div id=\"divTree\" class=\"line\"" +
                    "		style=\"width: 213px; border-right-style: solid; border-right-width: 1px; border-right-color: rgb(226, 156, 186); border-left-style: solid; border-left-width: 1px; border-left-color: rgb(226, 156, 186); display: block;\">" + SystemHWUtil.CRLF;
            string.append(div_before);
        }

        String div_normal = "		<div class=\"%s\">" + SystemHWUtil.CRLF +
                "			<div class=\"mleft\">" + SystemHWUtil.CRLF +
                "				<a class=\"normal\" title=\"%s\" href=\"?ids=47_51\">%s</a>%s" + SystemHWUtil.CRLF +
                "			</div>" + SystemHWUtil.CRLF +
                "		</div>" + SystemHWUtil.CRLF;

//		System.out.println(div_normal);

        String div_mleft = "<div class=\"line mleft\" style=\"display: block;\" id=\"divTree47\">" + SystemHWUtil.CRLF;
//				+ "	%?"+SystemHWUtil.CRLF
//				+ "</div>"+SystemHWUtil.CRLF;
//		System.out.println(div_mleft);
        String attribute = ValueWidget.isNullOrEmpty(root.getAttributes()) ? "" : root.getAttributes().toString();
        attribute = attribute.replace("\"", "'");
        boolean childIsText = (!root.isTextNode()) && (!ValueWidget.isNullOrEmpty(root.getChildren())) && root.getChildren().get(0).isTextNode();
        String name22 = root.getName();
        String value33 = "";
        if (childIsText) {
            value33 += ("&nbsp;&nbsp;->" + root.getChildren().get(0).getName());
        }
        String str = (String.format(div_normal, (isParent ? "rootminus" : "line1"), attribute, name22, value33));
        string.append(str);
        if ((!root.isTextNode()) && (!ValueWidget.isNullOrEmpty(root.getChildren()))) {
            string.append(div_mleft);
            int length = root.getChildren().size();
            for (int i = 0; i < length; i++) {
                Element element = root.getChildren().get(i);
//				string.append(String.format(div_normal, element.getName()));
                if (element.isTextNode()) {
                    break;
                } else {
                    getTree(element, string);
                }
            }
            string.append("</div>");
        }
        if (isParent) {
            string.append("</div>");
        }
    }

    /***
     * 组装stub
     *
     * @param stubRange
     * @return
     */
    public static String assembleStub(StubRange stubRange) {
        if (ValueWidget.isNullOrEmpty(stubRange.getStubs())) {
            return SystemHWUtil.EMPTY;
        } else if (stubRange.getStubs().size() == 1) {
            return stubRange.getStubs().get(0);
        } else {
            List<String> stubs = stubRange.getStubs();
            StringBuffer stringBuffer = new StringBuffer("<list index=\"" + stubRange.getSelectedIndex() + "\" >");
            stringBuffer.append(SystemHWUtil.CRLF);
            for (int i = 0; i < stubs.size(); i++) {
                stringBuffer.append("   <value>").append(stubs.get(i)).append("</value>").append(SystemHWUtil.CRLF);
            }
            stringBuffer.append("</list>");
            return stringBuffer.toString();
        }
    }

    public static void assembleStubAndSave(StubRange stubRange, File file) {
        String result = assembleStub(stubRange);
        FileUtils.writeStrToFile(file, result, true);
    }

    public static PomDependency getPomDependency(String xml) {
        PomDependency pomDependency = new PomDependency();
        Element root = getElement(xml, 0, null);
        pomDependency.setGroupId(root.getChildByName("groupId").getTextNode());
        pomDependency.setArtifactId(root.getChildByName("artifactId").getTextNode());
        pomDependency.setVersion(root.getChildByName("version").getTextNode());
        if (!ValueWidget.isNullOrEmpty(root.getChildByName("type"))) {
            pomDependency.setType(root.getChildByName("type").getTextNode());
        }
        if (!ValueWidget.isNullOrEmpty(root.getChildByName("packaging"))) {
            pomDependency.setPackaging(root.getChildByName("packaging").getTextNode());
        }
//        System.out.println(root.getChildrenMap());
//        System.out.println(root.getChildren());

        return pomDependency;
    }

    /***
     * 反序列化
     *
     * @param input
     * @return
     */
    public static StubRange deAssembleStub(String input) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return null;
        }
        input = input.trim();
        if (input.startsWith("<list")) {//因为list可以有属性index
            Element root = getElement(input, 0, null);
            if ("list".equals(root.getName())) {
                List<String> stubs = new ArrayList<String>();
                List<Element> list = root.getChildren();
                for (int i = 0; i < list.size(); i++) {
                    if ("value".equals(list.get(i).getName())) {
                        Element element = list.get(i).getChildren().get(0);
                        stubs.add(element.getName());
                    }
                }
                StubRange stubRange = new StubRange();
                String indexStr = (String) root.getAttributes().get("index");
                if (!ValueWidget.isNullOrEmpty(indexStr) && ValueWidget.isNumeric(indexStr)) {
                    stubRange.setSelectedIndex(Integer.parseInt(indexStr));
                }

                stubRange.setStubs(stubs);
                return stubRange;
            }
        } else {
            List<String> stubs = new ArrayList<String>();
            stubs.add(input);
            StubRange stubRange = new StubRange();
            stubRange.setSelectedIndex(0);
            stubRange.setStubs(stubs);
            return stubRange;
        }

        return null;
    }

    //    @Test
    public void test_assembleStub() {
        StubRange stubRange = new StubRange();
        List<String> stubs = new ArrayList<String>();
        stubs.add("aaa");
        stubs.add("bbb");
        stubs.add("ccc");
        stubRange.setStubs(stubs);
        stubRange.setSelectedIndex(2);
        String result = assembleStub(stubRange);
        System.out.println(result);
    }

    //    @Test
    public void test_re() {
        String input = "<list  index=\"2\" >" +
                "   <value>ddd</value>" +
                "   <value>ccc</value>" +
                "   <value>bbb</value>" +
                "</list>";
        StubRange stubRange = deAssembleStub(input);
        System.out.println(stubRange.getStubs());
        System.out.println(stubRange.getSelectedIndex());
    }

    //	@Test
    public void test_parseAttribute() {
        String xml = "   id=\"2 2\"  a   ";
        ParseBean parseBean = parseAttribute(xml, 0);
        System.out.println(parseBean);
    }

    //	@Test
    public void test_getTextNode() {
        String xmlContent = "</fdsf";
        System.out.println(getTextNode(xmlContent, 0));
    }

    //	@Test
    public void test_isEndOfTag() {
        String input = " 	a  >";
        System.out.println(isEndOfTag(input, 0));
    }

    //	@Test
    public void test_isbEGINOfTag() {
        String input = " 		  </  ";
        System.out.println(isBeginOfTag(input, 0));
        System.out.println(input.charAt(isBeginOfTag(input, 0) + 1));
    }

    //	@Test
    public void test_001() {
        String input = "   ab.c  ";
        ParseBean parseBean = parseElementName(input, 0);
        System.out.println(parseBean.getResult().length());
        System.out.println(parseBean.getResult());
        System.out.println(parseBean.getLengthHasRead());
    }

    //	@Test
    public void test_isEnd() {
        String xmlContent = "  </tag>  ";
        System.out.println(isEnd(xmlContent, 0, "</tag>"));
    }

    //	@Test
    public void test22() throws IOException {
        StringBuffer content = FileUtils.getFullContent3("D:\\software\\eclipse\\workspace3\\oa_framework\\src\\main\\resources\\ask_for_leave.xml"
                , "UTF-8");
//		String content="<flow id=\"1 2\"  > <name id=\"111\" > <html id=\"ggg\"  > "
//				+ "<body>aaaa </body>  </html>  </name>   <name id=\"222\" >  bbb  </name>  </flow>";
//		String content="<flow id=\"1 2\">aaa   </flow>";
        System.out.println(content);
        Element root = getElement(content, 0, null);
        System.out.println(root);
        StringBuffer string = new StringBuffer();
        getTree(root, string);
        System.out.println("-----------------");
        System.out.println(string);

    }

    //    @Test
    public void test233() throws IOException {
        String content = "<flow id=\"1 2\"  > <name id=\"111\" > <html id=\"ggg\"  > "
                + "<body>aaaa </body>  </html>  </name>   <name id=\"222\" >  bbb  </name>  </flow>";
//		String content="<flow id=\"1 2\">aaa   </flow>";
        System.out.println(content);
        Element root = getElement(content, 0, null);
        System.out.println(root);
        List<Element> list = root.getChildren();
        System.out.println(list);
    }

    //    @Test
    public void test_stub() {
        String xmlFile = "/Users/whuanghkl/work/project/stub_test/src/main/webapp/stub/a/b/c/d.json";
        try {
            String content = FileUtils.getFullContent2(new File(xmlFile), "UTF-8", true);
            Element root = getElement(content, 0, null);
            System.out.println(root);
            List<Element> list = root.getChildren();
            System.out.println(list.get(1).getChildren().get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //	@Test
    public void test_getTree() {
        String content = "<flow id=\"1 2\"  > <name id=\"111\" > <html id=\"ggg\"  > "
                + "<body>aaaa </body>  </html>  </name>   <name id=\"222\" >  bbb  </name>  </flow>";
//		String content="<flow id=\"1 2\">aaa   </flow>";
        System.out.println(content);
        Element root = getElement(content, 0, null);
        StringBuffer string = new StringBuffer();


        getTree(root, string);
        System.out.println("-----------------");
        System.out.println(string);
    }

    //    @org.junit.Test
    public void test_getPomDependency() {
        String xml = "<dependency>" +
                "            <groupId>log4j</groupId>" +
                "            <artifactId>log4j</artifactId>" +
                "            <version>1.2.15</version>" +
                "            <type>jar</type>" +
                "</dependency>";
        PomDependency pomDependency = getPomDependency(xml);

    }

    private static class ParseBean {
        /***
         * 已经读取了多少个字符
         */
        private int lengthHasRead;
        /***
         * 读取的结果,比如element的name
         */
        private String result;

        public int getLengthHasRead() {
            return lengthHasRead;
        }

        public void setLengthHasRead(int lengthHasRead) {
            this.lengthHasRead = lengthHasRead;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "ParseBean [lengthHasRead=" + lengthHasRead + ", result="
                    + result + "]";
        }

    }
}
