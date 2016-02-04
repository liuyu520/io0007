package com.file.hw.filter.impl;

import java.io.File;

import com.file.hw.filter.infac.IFileFilter;

public class HasSuffixFileFilter implements IFileFilter {

	/**
	 * �ж��ļ��Ƿ��к�׺�� ���к�׺���򷵻�true
	 */
	@Override
	public boolean isRight(File file) {
		
		return isRight(file.getName());
	}
	/**
	 * �ж��ļ��Ƿ��к�׺�� ���к�׺���򷵻�true
	 * @param fileName �� a.txt,����e:/test/a.txt
	 * ע�⣺������·��
	 * @return
	 */
	public boolean isRight(String fileName){
		int index = fileName.indexOf(".");
		int leng=fileName.length();
		if (index == -1) {
			return false;
		} else if (index > 0 && index < leng - 1) {
			return true;
		}
		return false;
	}
	public boolean isRight(Object file){
		if(file instanceof File){
			return isRight((File)file);
		}else{
			return isRight(file.toString());
		}
	}
	public static void main(String[] args) {
		HasSuffixFileFilter hff=new HasSuffixFileFilter();
		String fileName="at.";//output "false"
//		String fileName=".at";//output "false"
//		String fileName="a.t";//output "true"
		System.out.println(hff.isRight(fileName));
	}

}
