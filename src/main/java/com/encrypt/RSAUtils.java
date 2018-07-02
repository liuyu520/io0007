package com.encrypt;

import com.common.bean.PrivPubKeyBean;
import com.common.bean.RSAFormatConf;
import com.common.enu.SignatureAlgorithm;
import com.common.util.SystemHWUtil;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import com.swing.dialog.toast.ToastMessage;
import sun.security.rsa.RSAPrivateCrtKeyImpl;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程分析： 
 *  1、甲方利用加密算法对原数据生成消息摘要
 *  1、甲方构建密钥对儿，将公钥公布给乙方，将私钥保留。 
 *  2、甲方使用私钥加密数据，然后用私钥对原数据的消息摘要进行签名，发送给乙方(1)签名以及(2)加密后的数据；
 *     乙方使用公钥、签名来验证待解密数据是否有效，如果有效使用公钥对数据解密。 
 *  3、乙方使用公钥加密数据，向甲方发送经过加密后的数据；甲方获得加密数据，通过私钥解密。 
 *  @author tdr-20121012
 *
 */
public class RSAUtils {

    /**
     * 加密算法-RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    
    /**
     * 签名算法-MD5withRSA
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    public static final String comment_begin_publicKey = "-----BEGIN PUBLIC KEY-----";
    public static final String comment_end_publicKey = "-----END PUBLIC KEY-----";
    public static final String comment_begin_privateKey = "-----BEGIN PRIVATE KEY-----";
    public static final String comment_end_privateKey = "-----END PRIVATE KEY-----";
    public static final String publicKey_comment = comment_begin_publicKey + SystemHWUtil.CRLF + "%s" + SystemHWUtil.CRLF + comment_end_publicKey;
    public static final String privateKey_comment = comment_begin_privateKey + SystemHWUtil.CRLF + "%s" + SystemHWUtil.CRLF + comment_end_privateKey;
    /**
     * 生成密钥对(公钥和私钥)
     * @return  Map<String, Object>
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }
    
    /**
     * 用私钥对信息生成数字签名
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
    	// 解密由base64编码的私钥 
        byte[] keyBytes = Base64Utils.decode(privateKey);
        // 构造PKCS8EncodedKeySpec对象   
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法 
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象 
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        
        return Base64Utils.encode(signature.sign());
    }

    /**
     * 校验数字签名
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     * 
     * @return
     * @throws Exception
     * 
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
    	// 解密由base64编码的公钥
        byte[] keyBytes = Base64Utils.decode(publicKey);
        // 构造X509EncodedKeySpec对象 
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        
        // 验证签名是否正常
        return signature.verify(Base64Utils.decode(sign));
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     * 
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
    	// 对密钥解密
        byte[] keyBytes = Base64Utils.decode(privateKey);
        
        // 取得私钥 
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        
        // 对数据解密 
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        decrypteFragment(encryptedData, cipher, inputLen, out, offSet, i);
        byte[] decryptedData = out.toByteArray();
        out.close();
        
        return decryptedData;
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     * 
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
    	// 对公钥解密 
        byte[] keyBytes = Base64Utils.decode(publicKey);
        
       // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        decrypteFragment(encryptedData, cipher, inputLen, out, offSet, i);

        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    public static void decrypteFragment(byte[] encryptedData, Cipher cipher, int inputLen, ByteArrayOutputStream out, int offSet, int i) throws IllegalBlockSizeException, BadPaddingException {
        byte[] cache;// 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
    }

    /**
     * 公钥加密
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
    	// 对公钥解密
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        /*while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }*/
        decrypteFragment(data, cipher, inputLen, out, offSet, i);
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     * 
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        /*while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }*/
        decrypteFragment(data, cipher, inputLen, out, offSet, i);
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     * 
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     * 
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64Utils.encode(key.getEncoded());
    }


    /***
     * 去掉:"-----BEGIN PRIVATE KEY-----"<br />
     * "-----BEGIN PUBLIC KEY-----"
     * @param pubKeyTxt
     * @return
     */
    public static String deleteKeyComment(String pubKeyTxt) {
        if (pubKeyTxt.startsWith("-----BEGIN")) {
            pubKeyTxt = RegexUtil.sed(pubKeyTxt, "^----.*----$", SystemHWUtil.EMPTY);
        }
        return pubKeyTxt;
    }

    public static String formatBase64ToMultiple(String inputKey) {
        return formatBase64ToMultiple(inputKey, (RSAFormatConf) null);
    }

    public static String formatBase64ToMultiple(String inputKey, RSAFormatConf rsaFormatConf) {
        if (SystemHWUtil.findStr3(inputKey, SystemHWUtil.CRLF).getCount() > 1) {
            System.out.println("已经执行过换行 :");
            return inputKey;
        }
        inputKey = inputKey.replaceAll("(.{64})", "$1" + SystemHWUtil.CRLF);
        if (null == rsaFormatConf || (!rsaFormatConf.isAppendComment())) {
            return inputKey;
        }
        if (rsaFormatConf.isPrivate()) {
            return String.format(RSAUtils.privateKey_comment, inputKey);
        } else {
            return String.format(RSAUtils.publicKey_comment, inputKey);
        }
    }

    public static PrivPubKeyBean getPrivPubKeyBean(String saltKeyInfo, Integer keysize) {
        if (null == saltKeyInfo) {
            saltKeyInfo = "whuang";//TODO 抽取为常量
        }
        KeyPair kp = null;
        try {
            kp = SystemHWUtil.getKeyPair(saltKeyInfo, SystemHWUtil.KEY_ALGORITHM_RSA, keysize);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
            ToastMessage.toast(e1.getMessage(), 3000, Color.RED);
            return null;
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            ToastMessage.toast(e1.getMessage(), 3000, Color.RED);
            return null;
        }
        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();
        String publicKeyStr = null;
        String privateKeyStr = null;
        PrivPubKeyBean privPubKeyBean = new PrivPubKeyBean();
        privPubKeyBean.setPrivateKeyBytes(privateKey.getEncoded());
        privPubKeyBean.setPrivKey(privateKey);
        privPubKeyBean.setPublicKeyBytes(publicKey.getEncoded());
        privPubKeyBean.setPublKey(publicKey);
        return privPubKeyBean;
    }

    public static Integer getRSAKeySize(int selectedIndex) {
        Integer keysize = 1024;
        switch (selectedIndex) {
            case 0:
                keysize = 512;
                break;
            case 1:
                keysize = 1024;
                break;
            case 2:
                keysize = 2048;
                break;
            case 3:
                keysize = 4096;
                break;
        }
        return keysize;
    }

    public static String rsaEncrypt(String privateKeyBase64, byte[] source) {
        String encryptedStr = null;
        try {
            byte[] encrypted = SystemHWUtil.encryptByPrivateKeyBase64(source, privateKeyBase64, "RSA");
            encryptedStr = org.springframework.util.Base64Utils.encodeToString(encrypted);

            System.out.println("encryptedStr :" + encryptedStr);
        } catch (Exception e1) {
            e1.printStackTrace();
            ToastMessage.toast(e1.getMessage(), 3000, Color.RED);
        }
        return encryptedStr;
    }

    public static String rsaEncrypt(byte[] privateKeyBase64, byte[] source) {
        String encryptedStr = null;
        try {
            Key privateKey = SystemHWUtil.convert2PrivateKey(privateKeyBase64, "RSA");
            byte[] encrypted = SystemHWUtil.encrypt(source, privateKey);
//            byte[] encrypted = SystemHWUtil.encryptByPrivateKeyBase64(source, privateKeyBase64, "RSA");
            encryptedStr = org.springframework.util.Base64Utils.encodeToString(encrypted);

            System.out.println("encryptedStr :" + encryptedStr);
        } catch (Exception e1) {
            e1.printStackTrace();
            ToastMessage.toast(e1.getMessage(), 3000, Color.RED);
        }
        return encryptedStr;
    }

    public static String getPrivateKeyBase64(String privateText) {
        String errorParam = "私钥";
        if (checkKeyNull(privateText, errorParam)) return null;
        privateText = RSAUtils.deleteKeyComment(privateText);
        return SystemHWUtil.deleteAllCRLF(privateText);
    }

    public static boolean checkKeyNull(String privateText, String errorParam) {
        if (ValueWidget.isNullOrEmpty(privateText)) {
            ToastMessage.toast(errorParam + "为空,请检查\"生成秘钥\"页签", 3000, Color.RED);
//            this.tabbedPane1.setSelectedIndex(1);
            return true;
        }
        return false;
    }

    /***
     * 校验签名,验签
     * @param sourceTxt
     * @param algorithm
     * @param result
     * @param keyBytes
     */
    public static boolean verifySign(String sourceTxt, SignatureAlgorithm algorithm, String result, byte[] keyBytes) {
        // 取得公钥
        PublicKey publicKey = SystemHWUtil.convert2PublicKey(keyBytes);
        boolean verifyResult = false;
        try {
            verifyResult = SystemHWUtil.verifySign(sourceTxt.getBytes(SystemHWUtil.CHARSET_UTF), SystemHWUtil.decodeBase64(result), publicKey, algorithm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (verifyResult) {
            ToastMessage.toast("校验签名成功", 2000);
        } else {
            ToastMessage.toast("校验签名失败", 3000, Color.RED);
        }
        return verifyResult;
    }

    public static StringBuffer getPrivateKeyDetailStringBuffer(RSAPrivateCrtKeyImpl privateKey) {
        BigInteger modulus = privateKey.getModulus();
        BigInteger privateExponent = privateKey.getPrivateExponent();
        BigInteger exponent2 = privateKey.getPrimeExponentQ();
        BigInteger exponent1 = privateKey.getPrimeExponentP();
        BigInteger coefficient = privateKey.getCrtCoefficient();

        String modulusHex = formatModulusHex(modulus);
        String privateExponentHex = formatModulusHex(privateExponent);
        String exponent2Hex = formatModulusHex(exponent2);//PrimeExponentQ
        String exponent1Hex = formatModulusHex(exponent1);//PrimeExponentP
        String coefficientHex = formatModulusHex(coefficient);
//        privateKey.getEncoded();

        System.out.println("modulusHex :" + SystemHWUtil.CRLF + modulusHex);
        System.out.println("privateExponentHex :" + SystemHWUtil.CRLF + privateExponentHex);
        System.out.println("exponent2 :" + SystemHWUtil.CRLF + exponent2Hex);
        System.out.println("exponent1 :" + SystemHWUtil.CRLF + exponent1Hex);//PrimeExponentP
        StringBuffer resultBuffer = new StringBuffer();
        resultBuffer.append("modulus:").append(SystemHWUtil.CRLF)
                .append(ValueWidget.deltaSpace)
                .append("00:")
                .append(modulusHex).append(SystemHWUtil.CRLF)

                .append("privateExponent:").append(SystemHWUtil.CRLF)
                .append(ValueWidget.deltaSpace)
                .append(privateExponentHex).append(SystemHWUtil.CRLF)

                .append("prime1:").append(SystemHWUtil.CRLF)
                .append("").append(SystemHWUtil.CRLF)
                .append("prime2:").append(SystemHWUtil.CRLF)
                .append("").append(SystemHWUtil.CRLF)

                .append("exponent1:").append(SystemHWUtil.CRLF)//PrimeExponentP
                .append(ValueWidget.deltaSpace)
                .append(exponent1Hex).append(SystemHWUtil.CRLF)

                .append("exponent2:").append(SystemHWUtil.CRLF)//PrimeExponentQ
                .append(ValueWidget.deltaSpace)
                .append(exponent2Hex).append(SystemHWUtil.CRLF)

                .append("coefficient:").append(SystemHWUtil.CRLF)
                .append(ValueWidget.deltaSpace)
                .append("00:")
                .append(coefficientHex).append(SystemHWUtil.CRLF);
        return resultBuffer;
    }

    public static String formatModulusHex(BigInteger modulus) {
        return ValueWidget.formStrByBr(ValueWidget.formatHex(modulus.toString(16)), 45);
    }

}