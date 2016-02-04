package com.common.enu;
/***
 * 签名算法.
 * @author huangwei
 * @since 2013-10-28
 */
public enum SignatureAlgorithm {
	SIGNATURE_ALGORITHM_MD5withRSA("MD5withRSA"),
	SIGNATURE_ALGORITHM_SHA1withRSA("SHA1withRSA"),
	SIGNATURE_ALGORITHM_SHA256withRSA("SHA256withRSA");
	
	private final String value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    SignatureAlgorithm(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
