/**  
 * @Title: listUtil.java  
 * @Package com.gsww.util  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author 强周亮  
 * @date 2019年1月8日 上午11:18:49  
 * @version V1.0  
 */
package com.qzl.util;

import java.util.HashSet;
import java.util.List;

/**  
 * @ClassName: listUtil  
 * @Description:
 * @author 强周亮  
 * @date 2019年1月8日 上午11:18:49 
 *    
 */
public class listUtil {
	/**
	 * @Title: removeDuplicate  
	 * @Description: list去重 
	 * @author 强周亮   
	 * @date 2019年1月8日 上午11:19:14  
	 */
	public static List removeDuplicate(List list) {   
	    HashSet h = new HashSet(list);   
	    list.clear();   
	    list.addAll(h);   
	    return list;   
	}   
}
