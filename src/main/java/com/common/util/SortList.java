package com.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortList<E> {
	public  void sort(List<E> list, final String method, final String sort) {
		Collections.sort(list, new Comparator() {
			public int compare(Object a, Object b) {
				int ret = 0;
				try {
					Method m1 = ((E) a).getClass().getMethod(method, (Class<?>) null);
					Method m2 = ((E) b).getClass().getMethod(method, (Class<?>) null);
					if (sort != null && "desc".equals(sort))// 倒序
						ret = m2.invoke(((E) b), null).toString()
								.compareTo(m1.invoke(((E) a), null).toString());
					else
						// 正序
						ret = m1.invoke(((E) a), null).toString()
								.compareTo(m2.invoke(((E) b), null).toString());
				} catch (NoSuchMethodException ne) {
					ne.printStackTrace();
				} catch (IllegalAccessException ie) {
					ie.printStackTrace();
				} catch (InvocationTargetException it) {
					it.printStackTrace();
				}
				return ret;
			}
		});
	}
}