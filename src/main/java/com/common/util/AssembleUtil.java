package com.common.util;

import com.string.widget.util.ValueWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by huangweii on 2016/1/23.
 */
public class AssembleUtil {
    /***
     * @param base      :[a,b,c,d]
     * @param times :0表示忽略
     * @param remaining : 剩余要选择的个数
     * @return
     */
    public static void assemble(List<String> result, StringBuffer buffer, String base[], int times, int remaining, boolean isSort) {
        if (remaining <= 1) {
            buffer.append(base[base.length - 1]);
            if(isRightTimes(base, times, remaining)){
            	addElementBySort(result, buffer, isSort);
            }
            
        } else {
            for (int i = 0; i < remaining; i++) {
                StringBuffer bufferTmp = new StringBuffer(buffer);
                bufferTmp.append(base[base.length - 1 - i]);
                if(isRightTimes(base, times, remaining)){
                	addElementBySort(result, bufferTmp, isSort);
                }
                assemble(result, bufferTmp, SystemHWUtil.aheadElement(base, base.length - 1 - i), times, remaining - 1, isSort);
            }
        }

    }

    public static boolean isRightTimes(String[] base, int times, int remaining) {
        return times<=0||times==base.length-remaining+1;
    }

    /***
     * @param base
     * @param times :0表示忽略
     * @param isSort    : 是否对"acb"进行排序,<br />排序结果:"abc"
     * @return
     */
    public static List<String> assemble(String base[], int times,  boolean isSort) {
//        Set<String> result = new HashSet<String>();
        List<String> result = new ArrayList<String>();
        AssembleUtil.assemble(result, new StringBuffer(), base, times, base.length, isSort/*isSort ,必须是true,否则就会重复*/);
        if(isSort){
        	Collections.sort(result);
    		Collections.sort(result,new Comparator<String>() {
    			@Override
    			public int compare(String str1, String str2) {
    				if(str2.length()>str1.length()){
    					return -1;
    				}else{
    					return 1;
    				}
    			}
    		});
        }
        
        return result;
    }

    public static List<String> assemble(String base[], int times,  boolean isSort,/*String[]args*/ String seperate) {
    	List<String> result=AssembleUtil.assemble( base, times, isSort);
    	List<String> realArg=new ArrayList<String>();
    	for(int i=0;i<result.size();i++){
    		String str=result.get(i);
    		String[]strs=str.split(SystemHWUtil.EMPTY);
    		/*for(int ii=0;ii<strs.length;ii++){
    			strs[ii]=RegexUtil.sedY(strs[ii], base, args);
    		}*/
    		realArg.add(SystemHWUtil.formatArr(strs, seperate));
//    		realArg.add(HWJacksonUtils.getJsonP(strs));
    	}
    	return realArg;
    }
    public static void addElementBySort(List<String> result, StringBuffer buffer, boolean isSort) {
        String str = buffer.toString();
        if (isSort) {
            str = ValueWidget.sortStr(str);
        }
        if (result.size() == 0 || (!result.contains(str))) {
            result.add(str);
        }
    }

    /***
     * 参数的取值个数,ab和ba算一种
     *
     * @param argCount
     * @return
     */
    public static int getAssembleSum(int argCount) {
        int sum = 0;
        for (int i = 0; i < argCount; i++) {
            int count = i + 1;//参数组合的个数
            sum += (SystemHWUtil.factorial(argCount, count) / SystemHWUtil.arrayArrange(count));
        }
        return sum;
    }

}
