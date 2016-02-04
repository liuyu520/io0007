package com.common.bean;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

/***
 * 
 * @author huangwei
 * @since 2013-10-28
 */
public class PrivPubKeyBean implements Serializable {

	private static final long serialVersionUID = 1888415926054715509L;
	/***
	 * 私钥
	 */
	private PrivateKey privKey;
	/***
	 * 公钥
	 */
	private PublicKey publKey;
	/***
	 * 签名算法
	 */
	private String sigAlgName;
	public PrivateKey getPrivKey() {
		return privKey;
	}
	public void setPrivKey(PrivateKey privKey) {
		this.privKey = privKey;
	}
	public PublicKey getPublKey() {
		return publKey;
	}
	public void setPublKey(PublicKey publKey) {
		this.publKey = publKey;
	}
	public String getSigAlgName() {
		return sigAlgName;
	}
	public void setSigAlgName(String sigAlgName) {
		this.sigAlgName = sigAlgName;
	}
	@Override
	public String toString() {
		return "PrivPubKeyBean [privKey=" + privKey + ", publKey=" + publKey
				+ ", sigAlgName=" + sigAlgName + "]";
	}
	
	
}
