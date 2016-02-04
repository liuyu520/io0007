package com.encrypt;

import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.io.*;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

public class InspectionSign {
	private static char[] HexCode = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	protected Log logger = LogFactory.getLog(this.getClass());
	// 签名原文字段
	private String srcData;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Provider provider = new BouncyCastleProvider();
			Security.addProvider(provider);
			String sign = "";
			StringBuilder sdf = new StringBuilder();
//			sdf.append("MIIHtgYJKoZIhvcNAQcCoIIHpzCCB6MCAQExCzAJBgUrDgMCGgUAMIIBAAYJKoZI")
//					.append("hvcNAQcBoIHyBIHvMDAwMDAwMDAwMDAwMDAwMjIxU04wMTAwMTbovazlhaXotKbm")
//					.append("iLflj7c6U1YwMTAwMTUyMjIyMjIyMjIyMjIyMjFTTjAyMDAxOei9rOWFpei0puaI")
//					.append("t+Wnk+WQjTpTVjAyMDAwNuadjuWbm1NOMDMwMDIx6L2s6LSm6YeR6aKdKOWwj+WG")
//					.append("mSk6U1YwMzAwMDgxMDAwMC4wMEROMDEwMDE26L2s5Ye66LSm5oi35Y+3OkRWMDEw")
//					.append("MDE1MjIyMjIyMjIyMjIyMjIyRE4wMjAwMTnovazlh7rotKbmiLflp5PlkI06RFYw")
//					.append("MjAwMDblvKDkuImgggUyMIIFLjCCBBagAwIBAgIBATANBgkqhkiG9w0BAQUFADCB")
//					.append("qDESMBAGA1UEBgwJ5Lit5Zu9AAAAMRIwEAYDVQQIDAnljJfkuqwAAAAxFjAUBgNV")
//					.append("BAcMDeWMl+S6rOW4ggAAAAAxNjA0BgNVBAoMLeWMl+S6rOWkqeWcsOiejeenkeaK")
//					.append("gOaciemZkOWFrOWPuAAAAAAAAAAAAAAAADEWMBQGA1UECwwN5rWL6K+V6YOoAAAA")
//					.append("ADEWMBQGA1UEAwwNVGVuLVRlc3QtQ0EyADAeFw0xMzAzMjEwNjA2NTNaFw0yMzAz")
//					.append("MTkwNjA2NTNaMHExCzAJBgNVBAYTAkNOMQswCQYDVQQIEwJCSjESMBAGA1UEChMJ")
//					.append("Z3JhbmRyb2xlMRAwDgYDVQQLEwdkZXZlbG9wMRIwEAYDVQQDEwl0ZW4tdGVzdDEx")
//					.append("GzAZBgkqhkiG9w0BCQEWDHRlc3RAdGVuLmNvbTCBnzANBgkqhkiG9w0BAQEFAAOB")
//					.append("jQAwgYkCgYEAu7kgI5y958PV6ALJ3OKWGWDTPqwEQZBMkBHC0cHosg5UlJq79Qpq")
//					.append("jqRssMpb/h/g1Xe0/fgoSVgJl+yCpo6z7MIelqp8z1DJYOZvOE9sPS3xlXlCu0vG")
//					.append("7NC6riNJKGkc7WLBJL3jAGis190KZvn65rnDVQE6cy08G8XJyWcwho8CAwEAAaOC")
//					.append("AhswggIXMA4GA1UdDwEB/wQEAwIE8DBEBgkqhkiG9w0BCQ8ENzA1MA4GCCqGSIb3")
//					.append("DQMCAgIEADAOBggqhkiG9w0DBAICCAAwBwYFKw4DAgcwCgYIKoZIhvcNAwcwHQYD")
//					.append("VR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMEMA8GA1UdEwEB/wQFMAMCAQEwHQYD")
//					.append("VR0OBBYEFNigoacO643Jy9IW78GDEa8k1h9qMIHVBgNVHSMEgc0wgcqAFEl3PgWk")
//					.append("lG42t+UUom5kLdcNXqYYoYGupIGrMIGoMRIwEAYDVQQGDAnkuK3lm70AAAAxEjAQ")
//					.append("BgNVBAgMCeWMl+S6rAAAADEWMBQGA1UEBwwN5YyX5Lqs5biCAAAAADE2MDQGA1UE")
//					.append("Cgwt5YyX5Lqs5aSp5Zyw6J6N56eR5oqA5pyJ6ZmQ5YWs5Y+4AAAAAAAAAAAAAAAA")
//					.append("MRYwFAYDVQQLDA3mtYvor5Xpg6gAAAAAMRYwFAYDVQQDDA1UZW4tVGVzdC1DQTIA")
//					.append("ggECMDwGCCsGAQUFBwEBBDAwLjAsBggrBgEFBQcwAYYgaHR0cHM6Ly9FVkludGwt")
//					.append("b2NzcC50ZW5keXJvbi5jb20wQAYDVR0fBDkwNzA1oDOgMYYvaHR0cHM6Ly9FVklu")
//					.append("dGwtY3JsLnRlbmR5cm9uLmNvbS8vRVZJbnRsMjAwOS5jcmwwGAYEKgMECQQQFg5J")
//					.append("IGxvdmUgT3BlblNTTDANBgkqhkiG9w0BAQUFAAOCAQEAHYayTC9feOXi3/vezbSJ")
//					.append("fzVkmVWK99G0mHdMX9h1yeRBBpZmaydZgxW1KFaDjiJ9wI/VAWHYKvLC/smXpSOL")
//					.append("Ir+0r8P8ffx/Qh675jqGXVwxpYBqZPurDlr1Yws3C4a4tULWo81dV6CMOhhKkmgl")
//					.append("9KN/HfTT1J3EfeB+CwPG4jsM448xRDzgjZ8XA2Ng9gqbwdQkhy7O7rtBjBzILau2")
//					.append("kaZBczYxBR7M7972b1p5w1NgnckeScQSoSAcD9lmA+GBojqmBPc1ly7xbk3BmWv3")
//					.append("55U45gewCe2U9U/zMDd+8H27Y73NXchlBh+TUCfw9q8bc0Fw9+i0iVKZogEZhHKV")
//					.append("yDGCAVUwggFRAgEBMIGuMIGoMRIwEAYDVQQGDAnkuK3lm70AAAAxEjAQBgNVBAgM")
//					.append("CeWMl+S6rAAAADEWMBQGA1UEBwwN5YyX5Lqs5biCAAAAADE2MDQGA1UECgwt5YyX")
//					.append("5Lqs5aSp5Zyw6J6N56eR5oqA5pyJ6ZmQ5YWs5Y+4AAAAAAAAAAAAAAAAMRYwFAYD")
//					.append("VQQLDA3mtYvor5Xpg6gAAAAAMRYwFAYDVQQDDA1UZW4tVGVzdC1DQTIAAgEBMAkG")
//					.append("BSsOAwIaBQAwDQYJKoZIhvcNAQEBBQAEgYBV8x/LpWc4rW3QIu1x9esmSvnUd3wI")
//					.append("GoFOghZLYmWSsu4rXrj2xS/5bj9ZzR4DuWzqKhHCyAkt5gnAcXAfaSGg7n7+dZ/H")
//					.append("FY2FyXG/0HjNMPCYZJqSJPr4gYuFWCC52maNYQIN1ippAFJ7c+d1ksuuEyR6KfYw")
//					.append("Y2A+GWW53eHvXA==");


//			sign="MIIGvwYJKoZIhvcNAQcCoIIGsDCCBqwCAQExCzAJBgUrDgMCGgUAMAsGCSqGSIb3DQEHAaCCBTIwggUuMIIEFqADAgECAgEBMA0GCSqGSIb3DQEBBQUAMIGoMRIwEAYDVQQGDAnkuK3lm70AAAAxEjAQBgNVBAgMCeWMl+S6rAAAADEWMBQGA1UEBwwN5YyX5Lqs5biCAAAAADE2MDQGA1UECgwt5YyX5Lqs5aSp5Zyw6J6N56eR5oqA5pyJ6ZmQ5YWs5Y+4AAAAAAAAAAAAAAAAMRYwFAYDVQQLDA3mtYvor5Xpg6gAAAAAMRYwFAYDVQQDDA1UZW4tVGVzdC1DQTIAMB4XDTEzMTAxMDA5NTk1M1oXDTIzMTAwODA5NTk1M1owcTELMAkGA1UEBhMCQ04xCzAJBgNVBAgTAkJKMRIwEAYDVQQKEwlncmFuZHJvbGUxEDAOBgNVBAsTB2RldmVsb3AxEjAQBgNVBAMTCXRlbi10ZXN0MTEbMBkGCSqGSIb3DQEJARYMdGVzdEB0ZW4uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBVMZOUOD6DF6vokfHF8InwDNkOLTETNuSvRBh1gGzr96IVPhfYIwT53nJwath5OW\/krnTevWlnfq909wI79YyVVLxeg0UsSVRUiIT7\/t6zXcsMA754gXeetZ+yz6F1EH8jpr0Bo08YhqAczwADRN4PjfUIVw+fCW72YHUxEDJlQIDAQABo4ICGzCCAhcwDgYDVR0PAQH\/BAQDAgTwMEQGCSqGSIb3DQEJDwQ3MDUwDgYIKoZIhvcNAwICAgQAMA4GCCqGSIb3DQMEAgIIADAHBgUrDgMCBzAKBggqhkiG9w0DBzAdBgNVHSUEFjAUBggrBgEFBQcDAgYIKwYBBQUHAwQwDwYDVR0TAQH\/BAUwAwIBATAdBgNVHQ4EFgQUOX3fKAW6w39fUCuoCcM7\/V60+aYwgdUGA1UdIwSBzTCByoAUSXc+BaSUbja35RSibmQt1w1ephihga6kgaswgagxEjAQBgNVBAYMCeS4reWbvQAAADESMBAGA1UECAwJ5YyX5LqsAAAAMRYwFAYDVQQHDA3ljJfkuqzluIIAAAAAMTYwNAYDVQQKDC3ljJfkuqzlpKnlnLDono3np5HmioDmnInpmZDlhazlj7gAAAAAAAAAAAAAAAAxFjAUBgNVBAsMDea1i+ivlemDqAAAAAAxFjAUBgNVBAMMDVRlbi1UZXN0LUNBMgCCAQIwPAYIKwYBBQUHAQEEMDAuMCwGCCsGAQUFBz";
//			sign = sdf.toString();
			sign = FileUtils.getFullContent2(new File("D:\\work\\sign.txt"), SystemHWUtil.CHARSET_UTF,true);

			byte[] signEs = Base64.decode(sign);

			// String
			// sign2="3082079506092a864886f70d010702a082078630820782020101310b300906052b0e03021a05003081e006092a864886f70d010701a081d20481cf303030303030303030303030303030323231534e303130303136d7aac8ebd5cbbba7bac53a5356303130303135323232323232323232323232323231534e303230303139d7aac8ebd5cbbba7d0d5c3fb3a5356303230303036c0eecbc4534e303330303231d7aad5cbbdf0b6ee28d0a1d0b4293a535630333030303831303030302e3030444e303130303136d7aab3f6d5cbbba7bac53a4456303130303135323232323232323232323232323232444e303230303139d7aab3f6d5cbbba7d0d5c3fb3a4456303230303036d5c5c8fda08205323082052e30820416a003020102020101300d06092a864886f70d01010505003081a83112301006035504060c09e4b8ade59bbd0000003112301006035504080c09e58c97e4baac0000003116301406035504070c0de58c97e4baace5b8820000000031363034060355040a0c2de58c97e4baace5a4a9e59cb0e89e8de7a791e68a80e69c89e99990e585ace58fb800000000000000000000000031163014060355040b0c0de6b58be8af95e983a8000000003116301406035504030c0d54656e2d546573742d43413200301e170d3133303332313036303635335a170d3233303331393036303635335a3071310b300906035504061302434e310b300906035504081302424a31123010060355040a13096772616e64726f6c653110300e060355040b1307646576656c6f70311230100603550403130974656e2d7465737431311b301906092a864886f70d010901160c746573744074656e2e636f6d30819f300d06092a864886f70d010101050003818d0030818902818100bbb920239cbde7c3d5e802c9dce2961960d33eac0441904c9011c2d1c1e8b20e54949abbf50a6a8ea46cb0ca5bfe1fe0d577b4fdf82849580997ec82a68eb3ecc21e96aa7ccf50c960e66f384f6c3d2df1957942bb4bc6ecd0baae234928691ced62c124bde30068acd7dd0a66f9fae6b9c355013a732d3c1bc5c9c96730868f0203010001a382021b30820217300e0603551d0f0101ff0404030204f0304406092a864886f70d01090f04373035300e06082a864886f70d030202020400300e06082a864886f70d030402020800300706052b0e030207300a06082a864886f70d0307301d0603551d250416301406082b0601050507030206082b06010505070304300f0603551d130101ff04053003020101301d0603551d0e04160414d8a0a1a70eeb8dc9cbd216efc18311af24d61f6a3081d50603551d230481cd3081ca801449773e05a4946e36b7e514a26e642dd70d5ea618a181aea481ab3081a83112301006035504060c09e4b8ade59bbd0000003112301006035504080c09e58c97e4baac0000003116301406035504070c0de58c97e4baace5b8820000000031363034060355040a0c2de58c97e4baace5a4a9e59cb0e89e8de7a791e68a80e69c89e99990e585ace58fb800000000000000000000000031163014060355040b0c0de6b58be8af95e983a8000000003116301406035504030c0d54656e2d546573742d43413200820102303c06082b060105050701010430302e302c06082b06010505073001862068747470733a2f2f4556496e746c2d6f6373702e74656e6479726f6e2e636f6d30400603551d1f043930373035a033a031862f68747470733a2f2f4556496e746c2d63726c2e74656e6479726f6e2e636f6d2f2f4556496e746c323030392e63726c301806042a0304090410160e49206c6f7665204f70656e53534c300d06092a864886f70d010105050003820101001d86b24c2f5f78e5e2dffbdecdb4897f356499558af7d1b498774c5fd875c9e4410696666b27598315b52856838e227dc08fd50161d82af2c2fec997a5238b22bfb4afc3fc7dfc7f421ebbe63a865d5c31a5806a64fbab0e5af5630b370b86b8b542d6a3cd5d57a08c3a184a926825f4a37f1df4d3d49dc47de07e0b03c6e23b0ce38f31443ce08d9f17036360f60a9bc1d424872eceeebb418c1cc82dabb691a641733631051eccefdef66f5a79c353609dc91e49c412a1201c0fd96603e181a23aa604f735972ef16e4dc1996bf7e79538e607b009ed94f54ff330377ef07dbb63bdcd5dc865061f935027f0f6af1b734170f7e8b4895299a20119847295c831820155308201510201013081ae3081a83112301006035504060c09e4b8ade59bbd0000003112301006035504080c09e58c97e4baac0000003116301406035504070c0de58c97e4baace5b8820000000031363034060355040a0c2de58c97e4baace5a4a9e59cb0e89e8de7a791e68a80e69c89e99990e585ace58fb800000000000000000000000031163014060355040b0c0de6b58be8af95e983a8000000003116301406035504030c0d54656e2d546573742d43413200020101300906052b0e03021a0500300d06092a864886f70d010101050004818055f31fcba56738ad6dd022ed71f5eb264af9d4777c081a814e82164b626592b2ee2b5eb8f6c52ff96e3f59cd1e03b96cea2a11c2c8092de609c071701f6921a0ee7efe759fc7158d85c971bfd078cd30f098649a9224faf8818b855820b9da668d61020dd62a6900527b73e77592cbae13247a29f63063603e1965b9dde1ef5c";
			// byte[] sinBytes = hexString2Bytes(sign2);

			InspectionSign is = new InspectionSign();
			boolean yes = is.verify(signEs);
			System.out.println("yes=" + yes);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static byte[] hexString2Bytes(String hexstr) {
		byte[] bytes = null;
		if (hexstr != null) {
			if (hexstr.length() % 2 != 0) {
				hexstr = 0 + hexstr;
			}
			bytes = new byte[hexstr.length() / 2];
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = (byte) Integer.parseInt(hexstr.substring(2 * i, 2 * i + 2), 16);
			}
		}
		return bytes;
	}

	public static String bytes2HexString(byte[] bytes) {

		StringBuilder buffer = new StringBuilder();
		if (bytes != null) {
			for (int i = 0; i < bytes.length; i++) {
				buffer.append(byte2HexString(bytes[i]));
			}
		}
		return buffer.toString();
	}

	public static String SHA1Encode(String sourceString) {
		if (ValueWidget.isNullOrEmpty(sourceString)){
			return null;
		}
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			resultString = byte2hexString(md.digest(sourceString.getBytes("utf-8")));
		} catch (Exception ignored) {
		}
		if(resultString==null){
			return null;
		}
		return resultString.toLowerCase();
	}

	public static String byte2HexString(byte b) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(HexCode[(b >>> 4) & 0x0f]);
		buffer.append(HexCode[b & 0x0f]);
		return buffer.toString();
	}

	public static String byte2hexString(byte[] bytes) {
		StringBuilder buf = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}

	/**
	 * 验证pkcs7格式的签名数据
	 *
	 * @param signedData pkcs7格式的签名数据
	 * @return 验证结果
	 * @throws Exception
	 */
	public boolean verify(byte[] signedData) {
		boolean bresult = true;
		try {
			CMSSignedData sign = new CMSSignedData(signedData);
//			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			CertStore certs = sign.getCertificatesAndCRLs("Collection", "BC");
			SignerInformationStore signers = sign.getSignerInfos();
			Collection c = signers.getSigners();
			Iterator it = c.iterator();

			System.out.println("---------------------------------");

			String deSignSha1 = "";
			// 当有多个签名者信息时需要进行全部验证
			while (it.hasNext()) {
				System.out.println("111111111111111111");
				SignerInformation signer = (SignerInformation) it.next();
				System.out.println("signer=" + signer.getSID());
				Collection certCollection = certs.getCertificates(signer.getSID());
				System.out.println("certCollection=" + certCollection.getClass());
				Iterator certIt = certCollection.iterator();
				X509Certificate cert = (X509Certificate) certIt.next();// 证书链
				byte[] data = signer.getSignature();
				System.out.println("签名后数据:" + new String(Hex.encode(data)));

				PublicKey pKey = cert.getPublicKey();

				System.out.println("pKey:" + pKey.toString());

				try {
					// 1、公钥解密
					// byte [] temp =
					// RSATest.encryptByPublickey(data,pKey.getEncoded());
					// byte [] temp = RSATest.encryptByPublickey2(data,pKey);
					byte[] temp = RSAUtils.decryptByPublicKey(data, Base64Utils.encode(pKey.getEncoded()));
					deSignSha1 = new String(Hex.encode(temp)).toLowerCase();
					System.out.println("公钥解密签名数据后deSignSha1:" + deSignSha1);

				} catch (Exception ex) {
					ex.printStackTrace();
				}

				// 1、公钥解密 2、摘要比较

				/*
				 * if (signer.verify(pKey, "BC")){//验证过程
				 * logger.info(" pkcs7 verifed success!"); }else{ bresult =
				 * false; } if (signer.verify(cert, "BC")){//验证过程
				 * logger.info(" pkcs7 verifed success!"); }else{ bresult =
				 * false; }
				 */

				// 验证证书模块
				try {
					// PublicKey publicKey = getCert();
					// cert.verify(publicKey);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			// 获取明文模块
			CMSProcessable content = sign.getSignedContent();

			try {
				PipedInputStream pis = new PipedInputStream();
				PipedOutputStream pos = new PipedOutputStream(pis);
				if (!ValueWidget.isNullOrEmpty(content)) {
					content.write(pos);
					pos.flush();
					pos.close();
				}

				byte[] data = new byte[1024];// 1024
				int readlength = 0;
				int offset = 0;
				byte[] des = new byte[4096];// 4096

				while ((readlength = pis.read(data, 0, 1024)) != -1) {// 1024
					// 从data中copy道des中;
					System.arraycopy(data, 0, des, offset, readlength);
					offset += readlength;
					if (offset >= des.length) {
						int i = offset / des.length;
						byte[] temp = new byte[des.length];
						// 从des中copy到temp中
						System.arraycopy(des, 0, temp, 0, des.length);
						des = new byte[4096 * (i + 1)];
						System.arraycopy(temp, 0, des, 0, offset);
					}
				}
				pis.close();

				srcData = new String(des, 0, offset, "utf-8");
				// srcData = new String(des, 0, offset);
				logger.debug("明文:" + srcData);
				System.out.println("明文：=" + srcData);

				String sha1Str = InspectionSign.SHA1Encode(srcData);
				System.out.println("sha1Str=" + sha1Str);

				StringBuilder sdf = new StringBuilder();
				sdf.append("3039300906052b0e03021a0500302c0414").append(sha1Str).append("0414").append(sha1Str);
				// .append("04142f272d9fccc0e81ee5d8b20108519e8187063e00");

				String enSignSha1 = sdf.toString();
				System.out.println("enSignSha1=" + enSignSha1);

				if (deSignSha1.equals(enSignSha1)) {
					System.out.println("------------");
					bresult = true;
				} else {
					bresult = false;
				}

			} catch (Exception ex) {
				bresult = false;
				ex.printStackTrace();
			}
		} catch (Exception ex1) {
			bresult = false;
			ex1.printStackTrace();
		}
		logger.debug("bresult:" + bresult);
		return bresult;
	}

	/**
	 * 获取根证书公钥
	 *
	 * @return
	 * @throws Exception
	 */
	private PublicKey getCert() {
		try {
			FileInputStream fis = new FileInputStream("D:\\CAKey\\Ten-Test-CA2.cer");
			BufferedInputStream bis = new BufferedInputStream(fis);

			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(bis);
			bis.close();
			PublicKey pKey = cert.getPublicKey();
			return pKey;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public String getSrcData() {
		return srcData;
	}

	// String signData = "000000000000000221" +
	// "SN010016转入账户号:SV010015222222222222221" +
	// "SN020019转入账户姓名:SV020006李四" +
	// "SN030021转账金额(小写):SV03000810000.00" +
	// "DN010016转出账户号:DV010015222222222222222" +
	// "DN020019转出账户姓名:DV020006张三";
	//
	// String tmp = "SN010016转入账户号:SV010015222222222222221" +
	// "SN020019转入账户姓名:SV020006李四" +
	// "SN030021转账金额(小写):SV03000810000.00" +
	// "DN010016转出账户号:DV010015222222222222222" +
	// "DN020019转出账户姓名:DV020006张三";
	//
	// System.out.println("总长度:" + tmp.getBytes("utf-8").length);
	//
	// String test1="转入账户号:";
	// String test11="222222222222221";
	// System.out.println(test1.getBytes("utf-8").length + "-" +
	// test11.getBytes("utf-8").length);
	//
	// String test2 = "转入账户姓名:";
	// String test21 = "李四";
	// System.out.println(test2.getBytes("utf-8").length + "-" +
	// test21.getBytes("utf-8").length);
	//
	// String test3="转账金额(小写):";
	// String test31="10000.00";
	// System.out.println(test3.getBytes("utf-8").length + "-" +
	// test31.getBytes("utf-8").length);
	//
	//
	// String test4="转出账户号:";
	// String test41="222222222222222";
	// System.out.println(test4.getBytes("utf-8").length + "-" +
	// test41.getBytes("utf-8").length);
	//
	// String test5="转出账户姓名:";
	// String test51="张三";
	// System.out.println(test5.getBytes("utf-8").length + "-" +
	// test51.getBytes("utf-8").length);

}
