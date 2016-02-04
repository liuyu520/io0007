package com.file.hw.filter.impl;

import java.io.File;
import java.util.List;

import com.file.hw.filter.infac.IFileFilter;

public class SuffixSpecifiedFileFilter implements IFileFilter {
	String suffix;

	public SuffixSpecifiedFileFilter(String suffix) {
		this.suffix = suffix.trim();
	}

	public SuffixSpecifiedFileFilter(List<String> suffixList) {
		if (suffixList != null && suffixList.size() > 0) {
			StringBuffer sb = new StringBuffer(suffixList.get(0));
			for (int i = 1; i < suffixList.size(); i++) {
				sb.append(",").append(suffixList.get(i));
			}

			this.suffix = sb.toString().trim();
		} else {
			suffix = "*";
		}
	}

	/**
	 * �ж��ļ��ĺ�׺���Ƿ��Ϲ�������
	 * ������򷵻�true
	 */
	@Override
	public boolean isRight(File file) {
		int index = file.getName().indexOf(".");
		if (index != -1 && (0 < index) && (index < file.getName().length() - 1)) {
			if (suffix.indexOf("*")>-1) {
				return true;
			} else {
				if(suffix.indexOf(",")==suffix.length()-1){
					suffix=suffix.substring(0, suffix.length()-1);
				}
				String[] suffs = suffix.split("[\\s,.]");
				
				if (suffs.length > 1) {
					for (int j = 0; j < suffs.length; j++) {
						if (file.getName().endsWith("." + suffs[j])) {
							return true;
						}
					}
				} else if (file.getName().endsWith("." + suffix)) {
					return true;
				}
			}
		}
		return false;
	}
}
