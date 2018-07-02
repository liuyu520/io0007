package com.io.hw.json;

import com.common.bean.PomDependency;
import com.common.bean.StubRange;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import org.apache.log4j.Logger;

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
    protected static final Logger logger = Logger.getLogger(XmlYunmaUtil.class);
    public static final char TAG_BEGIN_CHAR = '<';
    public static final String TAG_BEGIN_String = "<";

    public static final char TAG_END_CHAR = '>';
    public static final String TAG_END_STRING = ">";
    static int index;
    /***
     * 是否兼容特殊字符<br />
     * 例如:"a<script></script>"
     */
    public static boolean isCompatibleSpecial = true;


    /**
     * 获取标签名称
     *
     * @param xmlContent
     * @param pos
     * @return
     */
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
        boolean hasSpecialStart = false;
        while (i < totalLength) {//a<script></script>
            char c = xmlContent.charAt(i);
            if (c == '"' && xmlContent.charAt(i - 1) != '\\') {
                isQuote = !isQuote;
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
            if (c == '<') {
                if (xmlContent.charAt(i + 1) != '/') {
                    if (isCompatibleSpecial && isSpecialKeyword(xmlContent, i)) {
                        hasSpecialStart = true;//遇到<script> 的第一个<
                        i++;
                        continue;
                    }
                    return -1;
                } else if (isCompatibleSpecial && hasSpecialStart) {//遇到"a<script></script>"的</中的<时
                    hasSpecialStart = false;
                    if (isCompatibleSpecial && isSpecialKeyword(xmlContent, i + 1)) {
                        i += 2;
                        continue;
                    }
                    // "accd<script></scriptdd"
                    //find TODO
                    //优化:</aaa>如果没有对应的开始标签,则直接忽略
                }
            }
            if (c == '<') {
                return i;
            }
            if (c == '>') {
                if (isCompatibleSpecial && isSpecialKeyword2(xmlContent, i)) {//遇到<script> 的第一个>
                    i++;
                    continue;
                }
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
                if (isCompatibleSpecial && isSpecialKeyword(input, i)) {
                    return -1;
                }
                return i;
            }
            //不是空格,但是又不是>,所以还没有结尾
                break;
            }
        return -1;
    }

    private static boolean isSpecialKeyword(String input, int i) {
        return isContainsKeyword(input, i, "script") ||
                isContainsKeyword(input, i, "image");
    }

    private static boolean isSpecialKeyword2(String input, int i) {
        return isContainsKeyword2(input, i, "script") ||
                isContainsKeyword2(input, i, "image");
    }

    /***
     * 如果是特殊符号,特殊标签,例如"script","image",则直接忽略<br />
     * '<'...
     * @param input
     * @param i
     * @param keyword
     * @return
     */
    private static boolean isContainsKeyword(String input, int i, String keyword) {
        int length2 = keyword.length();
        return input.substring(i + 1, i + length2 + 1).equalsIgnoreCase(keyword)
                && RegexUtil.equalsWildcard(input.substring(i + length2 + 1, i + length2 + 2), "[\\s,.>]");
    }

    /***
     * ...'>'
     * @param input
     * @param i
     * @param keyword
     * @return
     */
    private static boolean isContainsKeyword2(String input, int i, String keyword) {
        int length2 = keyword.length();
        return input.substring(i - length2, i).equalsIgnoreCase(keyword)
                && RegexUtil.equalsWildcard(input.substring(i - length2 - 1, i - length2), "[\\s,.></]");
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
                if (c2 == '/') {//'</value>'中的斜杠
                    //正常情况下,不会走到这一步,因为下面的index = root.getEndTag().length() + textNodeIndex; 直接跳过了结束标签
                    break;
                } else {
                    root = readElement(xmlContent, parentElement, attributes, begin);
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
                        textNode(xmlContent, root, textNodeIndex);
                        index = root.getEndTag().length() + textNodeIndex;
                    }
                }
            }
            if (index >= totalLength) {
                break;
            }
            boolean loop = false;
            if (end && (null != parentElement)) {
                int endIndex = isEnd(xmlContent, index, parentElement.getEndTag());
                if (endIndex != -1) {
                    index = endIndex;
                    /*if (parentElement.getName().equals("cc")) {
                        System.out.println("cc");
                    }*/
                    break;
                } else {
                    //兄弟节点
                    brotherNode(xmlContent, parentElement);
                    break;//到了兄弟节点,说明本节点已经扫描结束了.
                }
            }
            ParseBean parseBean = null;
            do {
                parseBean = readAttributeAction(xmlContent, attributes);
            } while (parseBean != null && (!ValueWidget.isNullOrEmpty(parseBean.getResult())));

        }
        return root;
    }

    /***
     * 读取属性
     * @param xmlContent
     * @param attributes
     * @return
     */
    private static ParseBean readAttributeAction(String xmlContent, Map<String, Object> attributes) {
        ParseBean parseBean;
        parseBean = parseAttribute(xmlContent, index);
        if (ValueWidget.isNullOrEmpty(parseBean)) {
            return parseBean;
        }
        String attributeStr = parseBean.getResult();
        if (!ValueWidget.isNullOrEmpty(attributeStr)) {
            String[] strs = attributeStr.split("=");
            if (strs.length < 2) {
                strs = new String[]{strs[0], ""};
            }
            //删除属性值两边的双引号
            attributes.put(strs[0], SystemHWUtil.delDoubleQuotation(strs[1]));
            index = parseBean.getLengthHasRead();
        }
        return parseBean;
    }

    private static void brotherNode(String xmlContent, Element parentElement) {
        boolean loop;
        Element element33 = getElement(xmlContent, index, parentElement);
        if (element33 != null && element33.getName().equals("value")) {
            System.out.println("value");
        }
        loop = true;
        if (null != element33 && null != element33.getParent()) {
            element33.getParent().addChildToMap(element33);
        }
    }

    private static void textNode(String xmlContent, Element root, int textNodeIndex) {
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
    }

    private static Element readElement(String xmlContent, Element parentElement, Map<String, Object> attributes, int begin) {
        Element root;
        ParseBean parseBean = parseElementName(xmlContent, begin + 1);
                /*if(parseBean.getResult().equals("level")){
                    System.out.println("level");
				}*/
//        System.out.println(parseBean);
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
        }
        //解决只有一个stub时, 设置attributeName不生效的问题
        /*if (stubRange.getStubs().size() == 1) {
            return stubRange.getStubs().get(0);
        }*/
            List<String> stubs = stubRange.getStubs();
        StringBuffer stringBuffer = new StringBuffer("<list index=\"" + stubRange.getSelectedIndex() + "\" ");
        if (!ValueWidget.isNullOrEmpty(stubRange.getAttributeName())) {
            stringBuffer.append(" attributeName=\"" + stubRange.getAttributeName() + "\"");
        }
        stringBuffer.append(" >");
            stringBuffer.append(SystemHWUtil.CRLF);
            for (int i = 0; i < stubs.size(); i++) {
                stringBuffer.append("   <value ");
                String attributeVal = (String) SystemHWUtil.reverseMap(stubRange.getAttributeValIndexMap()).get(i);
                stringBuffer.append(" attributeVal=\"" + (attributeVal == null ? SystemHWUtil.EMPTY : attributeVal) + "\" ");
                stringBuffer.append(" >").append(SystemHWUtil.CRLF).append(stubs.get(i)).append(SystemHWUtil.CRLF).append("</value>").append(SystemHWUtil.CRLF);
            }
            stringBuffer.append("</list>");
            return stringBuffer.toString();
        }

    public static void assembleStubAndSave(StubRange stubRange, File file) {
        String result = assembleStub(stubRange);
        FileUtils.writeStrToFile(file, result, true);
    }

    public static List<PomDependency> getPomDependencies(String xml) {
        if (ValueWidget.isNullOrEmpty(xml)) {
            logger.error("xml is null");
            return null;
        }
        xml = xml.trim();

        Element root = getElement(xml, 0, null);
        List<PomDependency> pomDependencies = new ArrayList<>();
        if (root.getName().equals("dependencies")) {
            List<Element> elements = root.getChildren();
            int size = elements.size();
            for (int i = 0; i < size; i++) {
                pomDependencies.add(getSingleDependency(elements.get(i)));
            }
        } else {
            pomDependencies.add(getSingleDependency(root));
        }
        return pomDependencies;
    }

    public static PomDependency getSingleDependency(Element root) {
        PomDependency pomDependency = new PomDependency();
        pomDependency.setGroupId(root.getChildByName("groupId").getTextNode())
                .setArtifactId(root.getChildByName("artifactId").getTextNode())
                .setVersion(root.getChildByName("version").getTextNode());
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
            if (!"list".equals(root.getName())) {
                return null;
            }
            StubRange stubRange = new StubRange();
            setAttributeName(root, stubRange);

                List<String> stubs = new ArrayList<String>();
                List<Element> list = root.getChildren();

                for (int i = 0; i < list.size(); i++) {
                    Element elementValue = list.get(i);
                    if ("value".equals(elementValue.getName())) {
                        setAttributeValIndexMap(stubRange, i, elementValue);
                        Element textElement = elementValue.getChildren().get(0);
                        stubs.add(textElement.getName());
                    }
                }
                String indexStr = (String) root.getAttributes().get("index");
                if (!ValueWidget.isNullOrEmpty(indexStr) && ValueWidget.isNumeric(indexStr)) {
                    stubRange.setSelectedIndex(Integer.parseInt(indexStr));
                }

                stubRange.setStubs(stubs);
                return stubRange;

            }
            List<String> stubs = new ArrayList<String>();
            stubs.add(input);
            StubRange stubRange = new StubRange();
        stubRange.setSelectedIndex(0)
                .setStubs(stubs);
            return stubRange;
        }

    private static void setAttributeValIndexMap(StubRange stubRange, int i, Element elementValue) {
        Map<String, Object> attributes2 = elementValue.getAttributes();
        String keyOfValue = "attributeVal";
        if (attributes2.containsKey(keyOfValue)) {
            String val = (String) attributes2.get(keyOfValue);
            if (!ValueWidget.isNullOrEmpty(val)) {
                stubRange.getAttributeValIndexMap().put(val, i);
            }
        }
    }

    private static void setAttributeName(Element root, StubRange stubRange) {
        Map<String, Object> attributes = root.getAttributes();
        String key = "attributeName";
        if (attributes.containsKey(key)) {
            String val = (String) attributes.get(key);
            if (null != val) {
                stubRange.setAttributeName(val);
            }
        }
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
                , SystemHWUtil.CHARSET_UTF);
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
            String content = FileUtils.getFullContent2(new File(xmlFile), SystemHWUtil.CHARSET_UTF, true);
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
        String xml = "<dependencies><dependency>" +
                "            <groupId>log4j333</groupId>" +
                "            <artifactId>log4j</artifactId>" +
                "            <version>1.2.15</version>" +
                "            <type>jar</type>" +
                "</dependency>" +
                " <dependency>" +
                "            <groupId>log4j444</groupId>" +
                "            <artifactId>log5j</artifactId>" +
                "            <version>1.2.16</version>" +
                "            <type>jar</type>" +
                "</dependency></dependencies>";
        List<PomDependency> pomDependency = getPomDependencies(xml);
        System.out.println(" :" + JSONHWUtil.formatJson(HWJacksonUtils.getJsonP(pomDependency)));
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
