package com.test;

import com.common.bean.PrivPubKeyBean;
import com.common.bean.Woman;
import com.common.bean.ZipFileBean;
import com.common.enu.SignatureAlgorithm;
import com.common.util.CompressZipUtil;
import com.common.util.ReflectHWUtils;
import com.common.util.SystemHWUtil;
import com.common.util.ZipUtil;
import com.io.hw.file.util.FileUtils;
import com.path.hw.PathUtils;
import com.string.widget.util.ValueWidget;
import com.test.bean.Person2;
import com.test.bean.Student2;
import com.time.util.TimeHWUtil;
import junit.framework.Assert;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UnitTest {
    static PublicKey publicKey = null;
    static PrivateKey privateKey = null;
    static String algorithm2 = SystemHWUtil.KEY_ALGORITHM_RSA;

    @BeforeClass
    public static void before() throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        KeyPair kp = SystemHWUtil.getKeyPair("whuang", algorithm2);
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

    }

    public static PrivateKey getPrivateKey2(byte[] m, byte[] privateExponent)
            throws Exception {
        return SystemHWUtil.getPrivateKey(SystemHWUtil.getBigIntegerByByteArr(m),
                SystemHWUtil.getBigIntegerByByteArr(privateExponent));
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {

        if (hexString == null || hexString.equals("")) {
            return null;
        }
        if (hexString.length() % 2 != 0) {
            throw new RuntimeException("Hex  bit string length must be even");
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte 'F'-->15
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        int index = "0123456789ABCDEF".indexOf(c);
        if (index == -1) {
            index = "0123456789abcdef".indexOf(c);
        }
        return (byte) index;
    }

    @Test
    public void test_sign() throws Exception {
        String message = "whuang3";
        SignatureAlgorithm algorithm = SignatureAlgorithm.SIGNATURE_ALGORITHM_SHA256withRSA;
        byte[] signResult = SystemHWUtil.sign(message, privateKey, algorithm);
        System.out.println("sign result hex:"
                + SystemHWUtil.toHexString(signResult));
        boolean isSuccess = SystemHWUtil.verifySign(
                message.getBytes(SystemHWUtil.CHARSET_ISO88591), signResult,
                publicKey, algorithm);
        System.out.println("sign1 :" + isSuccess);
        Assert.assertEquals(isSuccess, true);
    }

    @Test
    public void testPrint() throws Exception {
        System.out.println("pub:"
                + SystemHWUtil.toHexString(publicKey.getEncoded()));
        byte[] message = new byte[]{0, 111, 0, 0, 0, 0, 85, 85, 2, -107, 5,
                0, -128, 4};
        System.out.println(SystemHWUtil.formatBytes(SystemHWUtil.encrypt(message,
                publicKey)));
        System.out.println("pri:"
                + SystemHWUtil.toHexString(privateKey.getEncoded()));
    }

    @Test
    public void testHex2ByteArr() {
        System.out.println("testHex2ByteArr");
        byte[] bytes = new byte[]{(byte) -1, (byte) 1, 'a'};
        String hexString = SystemHWUtil.toHexString(bytes);
        BigInteger bigI = new BigInteger(hexString, 16);
        Assert.assertEquals(hexString, bigI.toString(16));
        Assert.assertEquals(hexString, "ff0161");
    }

    @Test
    public void tsetBigInt() {
        BigInteger bigI = new BigInteger("1214");
        System.out.println(bigI.toString(16));
        System.out.println(bigI.toString(2));
    }

    @Test
    public void testHex2ByteArr2() {
        System.out.println("testHex2ByteArr..2");
        byte[] bytes = new byte[]{(byte) 0, (byte) 127, 'b', (byte) -128};
        String hexString = SystemHWUtil.toHexString(bytes);
        Assert.assertEquals(hexString, "007f6280");
    }

    @Test
    public void testEncrpytByPublicKey() throws Exception {
        System.out.println("testEncrpytByPublicKey");
        String message = "whuang2";
        // 使用公钥加密
        byte[] encryptBytes = SystemHWUtil.encrypt(message, publicKey);
        System.out.println("encryptBytes length:" + encryptBytes.length);

        // 使用私钥解密
        byte[] decryptBytes = SystemHWUtil.decrypt(encryptBytes, privateKey);
        Assert.assertEquals(message, new String(decryptBytes,
                SystemHWUtil.CHARSET_ISO88591));
    }

    @Test
    public void testEncrpytByPrivateKey() throws Exception {
        System.out.println("testEncrpytByPrivateKey");
        String message = "whuang2";
        // 使用私钥加密
        byte[] encryptBytes = SystemHWUtil.encrypt(message, privateKey);
        System.out.println("encryptBytes length:" + encryptBytes.length);

        // 使用公钥解密
        byte[] decryptBytes = SystemHWUtil.decrypt(encryptBytes, publicKey);
        Assert.assertEquals(message, new String(decryptBytes,
                SystemHWUtil.CHARSET_ISO88591));
    }

    @Test
    public void testEncode() throws Exception {
        System.out.println("testEncode");
        byte[] keyBytes = privateKey.getEncoded();
        String message = "whuang2";
        // 使用公钥加密
        byte[] encryptBytes = SystemHWUtil.encrypt(message, publicKey);
        PrivateKey priKey = SystemHWUtil.convert2PrivateKey(keyBytes, algorithm2);
        byte[] decryptBytes = SystemHWUtil.decrypt(encryptBytes, priKey);
        Assert.assertEquals(message, new String(decryptBytes,
                SystemHWUtil.CHARSET_ISO88591));
        System.out.println(SystemHWUtil.verifyPrivPubKey(publicKey, priKey));
    }

    @Test
    public void testEncode2() throws Exception {
        System.out.println("testEncode..2");
        byte[] keyBytes = publicKey.getEncoded();
        String hexKey = SystemHWUtil.toHexString(keyBytes);
        PublicKey pubKey = SystemHWUtil.convert2PublicKey(hexKey);
        String message = "whuang2";
        byte[] encryptBytes = SystemHWUtil.encrypt(message, pubKey);
        byte[] decryptBytes = SystemHWUtil.decrypt(encryptBytes, privateKey);
        Assert.assertEquals(message, new String(decryptBytes,
                SystemHWUtil.CHARSET_ISO88591));
    }

    @Test
    public void testByte2Hex() throws UnsupportedEncodingException {
        System.out.println("testByte2Hex");
        String hexString = "30820155020100300d06092a864886f70d01010105000482013f3082013b02010002410090ce433c33cc772cc679f667e06382bbc3391571e891187162607a0b5e0bb21331500f5aa268cd88a93e827adb8e646d87e2e2a6f1000d593c4220d7e9e416f10203010001024005012befb3b69853d2d43a40e2cf6da041a2026cba65f15b51d415c3b8cf5cab21d30842ed43779bd8d5743a1dd4d7b0da8aff5a44f23db4447f7f7f0b81d801022100c77e1af6e145b50a2c23f28949c50b4fb21cda4046877bc48fc3e7c5718b4a81022100b9d29f83578722f84762198aefd3cce34315ba0b88d856fa08a2642fa1463471022100b07d4465be0ee987c25d5b2952765cf778e045c62f9f9543e4dcb0a3e13c1601022036e27404da2c6af2c43ac06a9e0d83ddb829c9aab07f9da8fa35e4bb80b9f8b102210084afe21fc1a9718acc10df54c9a17319af70b8655abc469b528d2e8a1f64e9ce";
        byte[] bytes = SystemHWUtil.hexStrToBytes(hexString);
        Assert.assertEquals(hexString, SystemHWUtil.toHexString(bytes));
    }

    @Test
    public void test2String() throws UnsupportedEncodingException {
        System.out.println("test2String");
        String str = "中国";
        byte[] bytes = str.getBytes(SystemHWUtil.CHARSET_UTF);
        Assert.assertEquals(str, new String(bytes, SystemHWUtil.CHARSET_UTF));
    }

    @Test
    public void testHMAC1() throws Exception {
        System.out.println("testHMAC1");
        byte[] result = SystemHWUtil
                .getHMAC_SHA1("aa",
                        "87ba09a84148a2a586757d922825419cd17eec0b0647711c32fad901df871157");

        // System.out.println(result.length);
    }

    @Test
    public void testHMAC2() throws Exception {
        System.out.println("testHMAC2");
        byte[] secretKey = "whuang".getBytes(SystemHWUtil.CHARSET_ISO88591);
        byte[] input = SystemHWUtil
                .hexStrToBytes("22bee943e691427dd1ab8931865403db60cdf6b0e80f6b678ce1e628462972cb");
        byte[] result = SystemHWUtil.getHMAC_SHA256(secretKey, input);
        System.out.println("HMAC length:" + result.length);

    }

    @Test
    public void testEncryptSHA() throws Exception {
        System.out.println("testEncryptSHA");
        byte[] input = SystemHWUtil
                .hexStrToBytes("22bee943e691427dd1ab8931865403db60cdf6b0e80f6b678ce1e628462972cb");
        byte[] result = SystemHWUtil.encryptSHA(input);
        System.out.println("SHA length:" + result.length);
    }

    @Test
    public void testEncryptSHA1() throws Exception {
        System.out.println("testEncryptSHA1..");
        byte[] input = SystemHWUtil
                .hexStrToBytes("22bee943e691427dd1ab8931865403db60cdf6b0e80f6b678ce1e628462972cb");
        byte[] result = SystemHWUtil.encryptSHA1(input);
        System.out.println("SHA length:" + result.length);
    }

    @Test
    public void testPrivateExponent() throws Exception {
        RSAPublicKey rsaPublKey = (RSAPublicKey) publicKey;
        BigInteger modulus = rsaPublKey.getModulus();
        RSAPrivateKey rsaPriKey = (RSAPrivateKey) privateKey;
        BigInteger privateExponent = rsaPriKey.getPrivateExponent();

        PrivateKey priKey = SystemHWUtil.getPrivateKey(modulus, privateExponent);
        // System.out.println(SystemUtil.getBigIntegerByByteArr(privateKey
        // .getEncoded()));
        PrivateKey pk = SystemHWUtil.convert2PrivateKey(priKey.getEncoded(),
                algorithm2);
        // System.out.println(SystemUtil.getBigIntegerByByteArr(pr.getEncoded()));
        // Assert.assertEquals(SystemUtil.toHexString(privateKey.getEncoded()),
        // SystemUtil.toHexString(priKey.getEncoded()));
        System.out.println(SystemHWUtil.verifyPrivPubKey(publicKey, pk));
    }

    @Test
    public void testPriPubKey() throws Exception {
        System.out.println("testPriPubKey");
        RSAPublicKey rsaPublKey = (RSAPublicKey) publicKey;
        BigInteger modulus = rsaPublKey.getModulus();
        System.out.println("modulus:" + modulus);
        RSAPrivateKey rsaPriKey = (RSAPrivateKey) privateKey;
        BigInteger privateExponent = rsaPriKey.getPrivateExponent();
        BigInteger publicExponent = rsaPublKey.getPublicExponent();
        System.out.println("privateExponent:" + privateExponent);
        System.out.println("publicExponent:" + publicExponent);
        PrivateKey pr = SystemHWUtil.getPrivateKey(modulus, privateExponent);

        Assert.assertEquals(SystemHWUtil.verifyPrivPubKey(modulus,
                publicExponent, privateExponent), true);
    }

    @Test
    public void testMudulus() throws Exception {
        RSAPublicKey rsaPublKey = (RSAPublicKey) publicKey;
        BigInteger modulus = rsaPublKey.getModulus();
        RSAPrivateKey rsaPriKey = (RSAPrivateKey) privateKey;
        BigInteger privateExponent = rsaPriKey.getPrivateExponent();
        PrivateKey pr = SystemHWUtil.getPrivateKey(modulus, privateExponent);
        System.out.println(SystemHWUtil.verifyPrivPubKey(publicKey, pr));
    }

    @Test
    public void testNDE() throws Exception {
        BigInteger N = new BigInteger(
                "7584090596837072109850306405800588685807625507483213743023910161671363583771355704127065119314902759565833543453554662825571084296316159730914850569983729");
        BigInteger D = new BigInteger(
                "262110948042113131953109602348876716562465046835367489020692990466234928624162319280226870704380963866419366511059904389044269226504204002650417654781953");
        BigInteger E = new BigInteger("65537");
        Assert.assertEquals(SystemHWUtil.verifyPrivPubKey(N, E, D), true);

    }

    @Test
    public void testPrime() {
        System.out.println(SystemHWUtil.generatePrime(400005));
    }

    /***
     * RSA
     *
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    @Test
    public void testDe() throws UnsupportedEncodingException, Exception {
        // public static final String
        // pub_key="305c300d06092a864886f70d0101010500034b00304802410090ce433c33cc772cc679f667e06382bbc3391571e891187162607a0b5e0bb21331500f5aa268cd88a93e827adb8e646d87e2e2a6f1000d593c4220d7e9e416f10203010001";
        // public static final String
        // pri_key="30820155020100300d06092a864886f70d01010105000482013f3082013b02010002410090ce433c33cc772cc679f667e06382bbc3391571e891187162607a0b5e0bb21331500f5aa268cd88a93e827adb8e646d87e2e2a6f1000d593c4220d7e9e416f10203010001024005012befb3b69853d2d43a40e2cf6da041a2026cba65f15b51d415c3b8cf5cab21d30842ed43779bd8d5743a1dd4d7b0da8aff5a44f23db4447f7f7f0b81d801022100c77e1af6e145b50a2c23f28949c50b4fb21cda4046877bc48fc3e7c5718b4a81022100b9d29f83578722f84762198aefd3cce34315ba0b88d856fa08a2642fa1463471022100b07d4465be0ee987c25d5b2952765cf778e045c62f9f9543e4dcb0a3e13c1601022036e27404da2c6af2c43ac06a9e0d83ddb829c9aab07f9da8fa35e4bb80b9f8b102210084afe21fc1a9718acc10df54c9a17319af70b8655abc469b528d2e8a1f64e9ce";

        String privateKeyStr1 = "30820155020100300d06092a864886f70d01010105000482013f3082013b02010002410090ce433c33cc772cc679f667e06382bbc3391571e891187162607a0b5e0bb21331500f5aa268cd88a93e827adb8e646d87e2e2a6f1000d593c4220d7e9e416f10203010001024005012befb3b69853d2d43a40e2cf6da041a2026cba65f15b51d415c3b8cf5cab21d30842ed43779bd8d5743a1dd4d7b0da8aff5a44f23db4447f7f7f0b81d801022100c77e1af6e145b50a2c23f28949c50b4fb21cda4046877bc48fc3e7c5718b4a81022100b9d29f83578722f84762198aefd3cce34315ba0b88d856fa08a2642fa1463471022100b07d4465be0ee987c25d5b2952765cf778e045c62f9f9543e4dcb0a3e13c1601022036e27404da2c6af2c43ac06a9e0d83ddb829c9aab07f9da8fa35e4bb80b9f8b102210084afe21fc1a9718acc10df54c9a17319af70b8655abc469b528d2e8a1f64e9ce";
        String dataHex1 = "1cbe1de02163a864e6e22837fb5cb7eb4e064ee71dd91df756258330e378f0b8e5b7ccd217d37165fe95fef4495663da9b42e35b2c26d0a6984ea7b3056422ad";
        String dataHex2 = "36637efc341cb4a122653e04c6b557eb6fcc64268c2ecf777a93dc91e1c588cd2f519dea3462fcb3711ab44b2df458edaebf80aeb618b30d2dea166c9ee52e67";
        System.out.println(new String(SystemHWUtil.decryptByPrivateKey(dataHex1,
                privateKeyStr1, algorithm2), SystemHWUtil.CHARSET_ISO88591));
        System.out.println(new String(SystemHWUtil.decryptByPrivateKey(dataHex2,
                privateKeyStr1, algorithm2), SystemHWUtil.CHARSET_ISO88591));
    }

    @Test
    public void testDES() throws Exception {
        // byte[]input=new
        // byte[]{73,100,8,-49,-63,-90,-27,74,-120,-128,60,-41,-28,-16,55,-112,-112,15};
        // byte[]encKey=new byte[]{-101, 111, -6, 18, 109, 43, 119, 18, 9, -28,
        // 63, 51, -97, 84, -116, -29};
        // int leng=input.length;
        // byte[]toDebytes=SystemUtil.getFrontBytes(input, leng-10);
        // byte[]result=SystemUtil.decryptDES(toDebytes, encKey) ;//
        // testdll.DESDEC(encKey, toDebytes, toDebytes.length);
        // System.out.println(SystemUtil.printBytes(result));
        String data = "whuang";
        String key = "jingning!@#$%";
        String result = SystemHWUtil.encryptDES(data, key);
        Assert.assertEquals(SystemHWUtil.decryptDES(result, key), data);
        System.out.println(result);
    }

    @Test
    public void testDES2() throws Exception {
        System.out.println("testDES2");
        String data = "whuang";
        String key = "jingning!@#$%";
        String result = SystemHWUtil.encryptDES(data, key);
        Assert.assertEquals(SystemHWUtil.decryptDES(result, key), data);
        System.out.println(result);
    }

    @Test
    public void testGetSerlvetName() {
        System.out.println("testGetSerlvetName");
        String url = "http://localhost:8081/SSLServer/addUser.security?username=whuang&password=root";
        String servletName = SystemHWUtil.getSerlvetName(url);
        Assert.assertEquals("addUser", servletName);
    }

    @Test
    public void testGetHttpPort() {
        System.out.println("testGetHttpPort");
        String url = "http://localhost:8081/SSLServer/addUser.security?username=whuang&password=root";
        String port = SystemHWUtil.getHttpPort(url);
        Assert.assertEquals("8081", port);
    }

    @Test
    public void testGetProjectName() {
        System.out.println("testGetProjectName");
        String url = "http://localhost:8081/SSLServer/addUser.security?username=whuang&password=root";
        String projectName = SystemHWUtil.getProjectName(url);
        Assert.assertEquals("SSLServer", projectName);
    }

    @Test
    public void testGetHttpIp() {
        System.out.println("testGetHttpIp");
        String url = "http://localhost:8081/SSLServer/addUser.security?username=whuang&password=root";
        String ip = SystemHWUtil.getHttpIp(url);
        System.out.println("ip:" + ip);
        // Assert.assertEquals("localhost", ip);
    }

    @Test
    public void test03() throws Exception {
        byte[] N = new byte[]{(byte) 0xD6, (byte) 0x21, (byte) 0x58,
                (byte) 0xA4, (byte) 0x8B, (byte) 0xA0, (byte) 0x2B,
                (byte) 0x5D, (byte) 0x1E, (byte) 0x94, (byte) 0x72,
                (byte) 0xB0, (byte) 0x95, (byte) 0xFA, (byte) 0xAA,
                (byte) 0xED, (byte) 0x2A, (byte) 0x05, (byte) 0x10,
                (byte) 0x11, (byte) 0x76, (byte) 0xE2, (byte) 0xBB,
                (byte) 0xF3, (byte) 0xFC, (byte) 0x01, (byte) 0x19,
                (byte) 0x41, (byte) 0x01, (byte) 0xCC, (byte) 0xDA,
                (byte) 0xB1, (byte) 0x37, (byte) 0x3C, (byte) 0x58,
                (byte) 0x29, (byte) 0xE8, (byte) 0xEA, (byte) 0x36,
                (byte) 0x01, (byte) 0x9C, (byte) 0x82, (byte) 0xD9,
                (byte) 0x5B, (byte) 0xC7, (byte) 0xE7, (byte) 0x56,
                (byte) 0x6B, (byte) 0x50, (byte) 0x96, (byte) 0x27,
                (byte) 0x2F, (byte) 0x83, (byte) 0x37, (byte) 0x26,
                (byte) 0xE2, (byte) 0x62, (byte) 0x35, (byte) 0xE9,
                (byte) 0x80, (byte) 0x57, (byte) 0x9F, (byte) 0xF0,
                (byte) 0xE8, (byte) 0x78, (byte) 0x6A, (byte) 0xBC,
                (byte) 0x4C, (byte) 0x1B, (byte) 0xE6, (byte) 0x56,
                (byte) 0xE8, (byte) 0xBB, (byte) 0x91, (byte) 0x2C,
                (byte) 0x51, (byte) 0x14, (byte) 0x4E, (byte) 0x33,
                (byte) 0xDC, (byte) 0xAE, (byte) 0xA6, (byte) 0xDB,
                (byte) 0xE6, (byte) 0xA1, (byte) 0x2F, (byte) 0x83,
                (byte) 0xF7, (byte) 0x82, (byte) 0x29, (byte) 0x14,
                (byte) 0x86, (byte) 0x47, (byte) 0x8E, (byte) 0xC5,
                (byte) 0x3F, (byte) 0x59, (byte) 0x64, (byte) 0xF3,
                (byte) 0xE1, (byte) 0xC9, (byte) 0xA7, (byte) 0x00,
                (byte) 0xBD, (byte) 0x4E, (byte) 0xA6, (byte) 0xFE,
                (byte) 0x6A, (byte) 0x5C, (byte) 0x5C, (byte) 0x20,
                (byte) 0xE7, (byte) 0xAA, (byte) 0x5B, (byte) 0x83,
                (byte) 0x48, (byte) 0x0D, (byte) 0xDB, (byte) 0xCB,
                (byte) 0xA7, (byte) 0x2E, (byte) 0xF1, (byte) 0x2A,
                (byte) 0x98, (byte) 0x8A, (byte) 0xA5, (byte) 0xDA, (byte) 0x79};
        byte[] D = new byte[]{(byte) 0xA1, (byte) 0x00, (byte) 0x19,
                (byte) 0xA2, (byte) 0x3E, (byte) 0xC9, (byte) 0x83,
                (byte) 0x51, (byte) 0x54, (byte) 0xB8, (byte) 0xD1,
                (byte) 0x33, (byte) 0x2C, (byte) 0xD1, (byte) 0x78,
                (byte) 0xCB, (byte) 0x31, (byte) 0x98, (byte) 0xF4,
                (byte) 0xE7, (byte) 0xEC, (byte) 0xB7, (byte) 0xB6,
                (byte) 0x12, (byte) 0x51, (byte) 0x1C, (byte) 0x92,
                (byte) 0x5C, (byte) 0x6A, (byte) 0x7A, (byte) 0x18,
                (byte) 0xC4, (byte) 0xBE, (byte) 0x7F, (byte) 0x44,
                (byte) 0xDA, (byte) 0xDE, (byte) 0x21, (byte) 0x9B,
                (byte) 0xD1, (byte) 0xC2, (byte) 0x58, (byte) 0x30,
                (byte) 0x0D, (byte) 0xEE, (byte) 0x54, (byte) 0x66,
                (byte) 0x89, (byte) 0xF1, (byte) 0xE1, (byte) 0xB4,
                (byte) 0x3C, (byte) 0xE9, (byte) 0x30, (byte) 0x02,
                (byte) 0xB0, (byte) 0x9C, (byte) 0x9F, (byte) 0x64,
                (byte) 0x9D, (byte) 0xF7, (byte) 0x44, (byte) 0x6E,
                (byte) 0xE1, (byte) 0x3A, (byte) 0xA5, (byte) 0xB1,
                (byte) 0x93, (byte) 0x08, (byte) 0x6E, (byte) 0x50,
                (byte) 0xCA, (byte) 0xA7, (byte) 0xC3, (byte) 0x99,
                (byte) 0x0D, (byte) 0x70, (byte) 0xB5, (byte) 0x9F,
                (byte) 0x12, (byte) 0x67, (byte) 0xE7, (byte) 0xEB,
                (byte) 0x8F, (byte) 0x0D, (byte) 0xDF, (byte) 0x0A,
                (byte) 0x72, (byte) 0x7B, (byte) 0x29, (byte) 0xDA,
                (byte) 0x8E, (byte) 0xF3, (byte) 0x90, (byte) 0x81,
                (byte) 0x5E, (byte) 0xA3, (byte) 0x38, (byte) 0xA0,
                (byte) 0xD3, (byte) 0x27, (byte) 0xB0, (byte) 0x9A,
                (byte) 0x7A, (byte) 0x93, (byte) 0xBB, (byte) 0x5C,
                (byte) 0x27, (byte) 0x44, (byte) 0x22, (byte) 0x1D,
                (byte) 0x84, (byte) 0x21, (byte) 0x34, (byte) 0xC6,
                (byte) 0x2F, (byte) 0x4D, (byte) 0xE0, (byte) 0xD6,
                (byte) 0xDF, (byte) 0x5B, (byte) 0x8E, (byte) 0xFD,
                (byte) 0xF7, (byte) 0x3F, (byte) 0xE9, (byte) 0x2C, (byte) 0x71};
        // PrivateKey priKey=SystemUtil.getPrivateKey(m, d)
        BigInteger E = new BigInteger("65537");
        SystemHWUtil.verifyPrivPubKey(SystemHWUtil.getBigIntegerByByteArr(N), E,
                SystemHWUtil.getBigIntegerByByteArr(D));

    }

    @Test
    public void test_filterFrontBytes() {
        System.out.println("test_filterFrontBytes");
        byte[] bytes = new byte[]{90, 8, 98, 37, 0};
        byte[] result = SystemHWUtil.filterFrontBytes(2, bytes);
        System.out.println(SystemHWUtil.formatBytes(result));
        Assert.assertEquals(true,
                SystemHWUtil.arrayIsEqual(result, new byte[]{98, 37, 0}));

    }

    @Test
    public void test_arrayIsEqual() {
        System.out.println("test_filterFrontBytes");
        byte[] a = new byte[]{1, 2, 3, 4};
        byte[] b = new byte[]{1, 2};
        Assert.assertEquals(false, SystemHWUtil.arrayIsEqual(a, b));
        Assert.assertEquals(true, SystemHWUtil.arrayIsEqual(a, a));
        Assert.assertEquals(true, SystemHWUtil.arrayIsEqual(b, b));
    }

    @Test
    public void test_decryptByPrivateKey() throws Exception {
        byte[] message = new byte[]{0, 111, 0, 0, 0, 0, 85, 85, 2, -107, 5,
                0, -128, 4};
        byte[] encrpytResult = new byte[]{86, -94, 5, 84, 114, 82, -58, -102,
                57, -79, -10, -18, 62, -65, 41, 107, 112, 119, 126, -32, 11,
                65, -86, -50, -74, -101, 41, 15, -58, -36, -124, -10, -56, 15,
                -47, -65, 105, 41, -15, -96, 94, 10, -42, -114, 4, 127, -109,
                -96, 45, 75, 98, -44, 77, 119, 27, 105, 88, 44, 35, -52, -44,
                88, -102, -14};
        String N_hex = "7584090596837072109850306405800588685807625507483213743023910161671363583771355704127065119314902759565833543453554662825571084296316159730914850569983729";
        String D_hex = "262110948042113131953109602348876716562465046835367489020692990466234928624162319280226870704380963866419366511059904389044269226504204002650417654781953";
        System.out.println("length of D_hex:" + D_hex.length());
        byte[] N = new byte[]{117, -124, 9, 5, -106, -125, 112, 114, 16,
                -104, 80, 48, 100, 5, -128, 5, -120, 104, 88, 7, 98, 85, 7, 72,
                50, 19, 116, 48, 35, -111, 1, 97, 103, 19, 99, 88, 55, 113, 53,
                87, 4, 18, 112, 101, 17, -109, 20, -112, 39, 89, 86, 88, 51,
                84, 52, 83, 85, 70, 98, -126, 85, 113, 8, 66, -106, 49, 97, 89,
                115, 9, 20, -123, 5, 105, -104, 55, 41};
        byte[] D = new byte[]{38, 33, 16, -108, -128, 66, 17, 49, 49, -107,
                49, 9, 96, 35, 72, -121, 103, 22, 86, 36, 101, 4, 104, 53, 54,
                116, -119, 2, 6, -110, -103, 4, 102, 35, 73, 40, 98, 65, 98,
                49, -110, -128, 34, 104, 112, 112, 67, -128, -106, 56, 102, 65,
                -109, 102, 81, 16, 89, -112, 67, -119, 4, 66, 105, 34, 101, 4,
                32, 64, 2, 101, 4, 23, 101, 71, -127, -107};
        // PrivateKey
        // privateKey=SystemUtil.getPrivateKey(SystemUtil.getBigIntegerByByteArr(SystemUtil.hexStrToBytes(N_hex)),
        // SystemUtil.getBigIntegerByByteArr(SystemUtil.hexStrToBytes(D_hex)));
        // System.out.println("N:"+SystemUtil.toHexString(N));
        // System.out.println("N:"+SystemUtil.toHexString(D));
        // System.out.println("d:"+D_hex.equals(SystemUtil.toHexString(SystemUtil.hexStrToBytes(D_hex))));
        // PrivateKey privateKey =
        // SystemUtil.getPrivateKey(SystemUtil.toHexString(N),
        // SystemUtil.toHexString(D));
        PrivateKey privateKey = SystemHWUtil.getPrivateKey(N_hex, D_hex);// ok
        // PrivateKey
        // privateKey=SystemUtil.convert2PrivateKey("30820155020100300d06092a864886f70d01010105000482013f3082013b02010002410090ce433c33cc772cc679f667e06382bbc3391571e891187162607a0b5e0bb21331500f5aa268cd88a93e827adb8e646d87e2e2a6f1000d593c4220d7e9e416f10203010001024005012befb3b69853d2d43a40e2cf6da041a2026cba65f15b51d415c3b8cf5cab21d30842ed43779bd8d5743a1dd4d7b0da8aff5a44f23db4447f7f7f0b81d801022100c77e1af6e145b50a2c23f28949c50b4fb21cda4046877bc48fc3e7c5718b4a81022100b9d29f83578722f84762198aefd3cce34315ba0b88d856fa08a2642fa1463471022100b07d4465be0ee987c25d5b2952765cf778e045c62f9f9543e4dcb0a3e13c1601022036e27404da2c6af2c43ac06a9e0d83ddb829c9aab07f9da8fa35e4bb80b9f8b102210084afe21fc1a9718acc10df54c9a17319af70b8655abc469b528d2e8a1f64e9ce");
        byte[] deResult = SystemHWUtil.decrypt(encrpytResult, privateKey);
        Assert.assertEquals(true, SystemHWUtil.arrayIsEqual(deResult, message));
        // System.out.println(SystemUtil.printBytes(SystemUtil.hexStrToBytes(N_hex)));
        // System.out.println(SystemUtil.printBytes(SystemUtil.hexStrToBytes(D_hex)));
    }

    @Test
    public void test_isDate() {
        String input = "2014-2-56";
        Assert.assertEquals(true, SystemHWUtil.isDate(input));
        input = "14-2-56";
        Assert.assertEquals(false, SystemHWUtil.isDate(input));
        input = "2214-2-6";
        Assert.assertEquals(true, SystemHWUtil.isDate(input));
    }

    @Test
    public void test_randoms() {
        // SystemUtil.printArray(SystemUtil.randoms(5, 3));

        // Random r = new Random();
        // System.out.println(r.nextInt(9));
    }

    @Test
    public void test_getParentDir() {
        String filePath = "d:\\a\\b\\c\\c1";
        Assert.assertEquals("d:\\a\\b\\c", SystemHWUtil.getParentDir(filePath));
        System.out.println(PathUtils.windowsPath(filePath, 1));
    }

    @Test
    public void test_windowsPath() {
        String filePath = "d:\\a\\b\\c\\c1";
        Assert.assertEquals("d:\\\\a\\\\b\\\\c\\\\c1",
                PathUtils.windowsPath(filePath, 1));
    }

    @Test
    public void test_getDateAfter() throws ParseException {
        String input = "2013-07-09 12:11:11";
        Date date = TimeHWUtil.getDate4Str(input);
        Date newDate = TimeHWUtil.getTimestampBefore(date, 14);
        System.out.println(TimeHWUtil.formatDate(newDate));
    }

    // @Test
    // public void test_splitAndFilterString() {
    // String content = FileUtils.getFullContent("D:\\Temp\\margin.html");
    // String result = SystemUtil.splitAndFilterString(content);
    // System.out.println(result);
    // }

    // @Test
    // public void test_getFileMD5(){
    // System.out.println(SystemUtil.getFileMD5("D:\\Temp\\ccc.zip"));
    // }
    //
    // @Test
    // public void test_readBytes4file() throws IOException {
    // byte[] bytes = FileUtils
    // .readBytes4file("D:\\share\\staruml-5.0-with-cm.exe");
    // FileUtils.writeBytesToFile(bytes,
    // "D:\\Temp\\a\\staruml-5.0-with-cm.exe");
    // }
    // @Test
    // public void test_readBytes() throws IOException {
    // InputStream in = new FileInputStream(
    // "D:\\share\\staruml-5.0-with-cm.exe");
    // byte[] bytes = FileUtils.readBytes(in);
    // FileUtils.writeBytesToFile(bytes,
    // "D:\\Temp\\a\\staruml-5.0-with-cm.exe");
    // }

    @Test
    public void test_getLastNameByPeriod() {
        String input = "java.lang.String";
        Assert.assertEquals("String", SystemHWUtil.getLastNameByPeriod(input));
    }

    @Test
    public void test005_isDate() {
        Assert.assertEquals(false, TimeHWUtil.isDate("2013-02-29"));
        Assert.assertEquals(true, TimeHWUtil.isDate("2012-02-29"));
        Assert.assertEquals(true, TimeHWUtil.isDate("2012-02-1"));
        Assert.assertEquals(false, TimeHWUtil.isDate("2012-05-32"));
    }

    // @Test
    // public void test_http() throws Exception {
    // String urlStr =
    // "http://localhost:8083/shop_goods/downloadOneFile?filename=BaiduYunGuanjia_2.1.022.exe&filepath=D:\\eclipse\\workspace\\shop_goods\\upload\\BaiduYunGuanjia_2.1.022.exe";
    // String destFile = "d:\\Temp\\a\\a.exe";
    // HttpSocketUtil.commResult(urlStr, null, null, null, null, false,
    // destFile);
    // }
//	@Test
    public void test_getFileMD5() throws Exception {
        String filepath = "D:\\download\\3_尚学堂_UML概览.avi";
        // File file=new File(filepath);
        String md5_1 = SystemHWUtil.getFileMD5(filepath);
        // System.out.println(md5_1);

        byte[] bytes = FileUtils.readBytes4file(filepath);
        byte[] md5 = SystemHWUtil.encryptMD5(bytes);
        String md5_2 = SystemHWUtil.toHexString(md5);
        // System.out.println(md5_2);
        Assert.assertEquals(md5_1, md5_2);
    }

    // @Test
    // public void test_GenericReadPropsUtil() throws IOException{
    // Properties
    // pro=GenericReadPropsUtil.getProperties("com/wh/test_switch.properties");
    // System.out.println(pro.get("endpoint.isSelfCheck"));
    // }
    @Test
    public void test_isEqualBytes() {
        byte[] bytes1 = new byte[]{1, 2, 3};
        byte[] bytes2 = new byte[]{1, 2, 3};
        byte[] bytes3 = new byte[]{1, 12, 3};
        Assert.assertEquals(true, SystemHWUtil.isEqualBytes(bytes1, bytes2));
        Assert.assertEquals(false, SystemHWUtil.isEqualBytes(bytes1, bytes3));
    }

    @Test
    public void test_isEquals() {
        byte[] bytes1 = new byte[]{1, 2, 3, 4};
        byte[] bytes2 = new byte[]{1, 2, 3};
        byte[] bytes3 = new byte[]{1, 2, 3};
        byte[] bytes4 = new byte[]{1, 12, 3};
        Assert.assertEquals(false, SystemHWUtil.isEquals(bytes1, bytes2));
        Assert.assertEquals(true, SystemHWUtil.isEquals(bytes2, bytes3));
        Assert.assertEquals(false, SystemHWUtil.isEquals(bytes3, bytes4));
    }

    @Test
    public void test_isNullOrEmpty() {
        String[] strs1 = new String[]{"a"};
        String[] strs2 = new String[]{"", ""};
        String[] strs3 = new String[]{"", "c"};
        String[] strs4 = new String[]{};
        String str5;
        Assert.assertEquals(false, SystemHWUtil.isNullOrEmpty(strs1));
        Assert.assertEquals(true, SystemHWUtil.isNullOrEmpty(strs2));
        Assert.assertEquals(false, SystemHWUtil.isNullOrEmpty(strs3));
        Assert.assertEquals(true, SystemHWUtil.isNullOrEmpty(strs4));
//		Assert.assertEquals(true, SystemHWUtil.isNullOrEmpty(str5));
//		Assert.assertEquals(true, SystemHWUtil.isNullOrEmpty(str5));
    }

    //	@Test
    public void test_getFilesByPath() {
        File path = new File("d:\\Temp\\a");
        String suffix = "mpc";
        File[] files = FileUtils.getFilesByPathAndSuffix(path, suffix);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            System.out.println(file.getAbsolutePath());
            Assert.assertEquals(true, file.getAbsolutePath().endsWith(suffix));
        }

    }

    @Test
    public void test_toLinuxPath() {
        String windowPath = "d:\\Test\\a";
        Assert.assertEquals("d:/Test/a", PathUtils.toLinuxPath(windowPath));
    }

    //	 @Test
    public void test_reduceFile() {
        List<String> filePaths = new ArrayList<String>();
        filePaths.add("D:\\doc\\android\\app\\package\\2014-4-9\\2014-4-9\\editor_360llq\\icon.jpg");
        filePaths.add("D:\\doc\\android\\app\\package\\2014-4-9\\2014-4-9\\editor_360llq\\desc.txt");
//	 filePaths.add("D:\\Temp\\BaiduYunGuanjia_2.1.022.exe");
//	 filePaths.add("D:\\Temp\\a\\编码转换器.jar");

        List<String> fileNames = new ArrayList<String>();
        fileNames.add("editor_360llq\\icon.jpg");
        fileNames.add("editor_360llq\\desc.txt");
//	 fileNames.add("BaiduYunGuanjia_2.1.022333.exe");
//	 fileNames.add("a\\编码转换器55.jar");

        String outPutFileStr = "d:\\Temp\\a\\c\\test.zip";
        File outPutFile = new File(outPutFileStr);
        if (outPutFile.exists()) {
            outPutFile.delete();
        }
        long oldTime = System.currentTimeMillis();
        ZipUtil.compressFiles(filePaths, fileNames, outPutFileStr);
        System.out.println(System.currentTimeMillis() - oldTime);
    }

    // @Test
    // public void test_getContent() throws ZipException, IOException{
    // String outPutFileStr="d:\\Temp\\a\\test.zip";
    // File file=new File(outPutFileStr);
    // ZipFile zipFile = new ZipFile(file);
    // ZipEntry zipEntry = zipFile.getEntry("BaiduYunGuanjia_2.1.022333.exe");
    // byte[]bytes=ZipUtil.getContent(zipFile, zipEntry);
    // FileUtils.writeBytesToFile(bytes, "d:\\Temp\\a\\baidu.exe");
    // }
    @Test
    public void test_isDouble() {
        String input = "223.4";
        Assert.assertEquals(true, ValueWidget.isDouble(input));

        input = "2234";
        Assert.assertEquals(true, ValueWidget.isDouble(input));

        input = "223a4";
        Assert.assertEquals(false, ValueWidget.isDouble(input));

        input = "2.234";
        Assert.assertEquals(true, ValueWidget.isDouble(input));

        input = "2c234";
        Assert.assertEquals(false, ValueWidget.isDouble(input));
    }

//	@Test
/*	public void test_convertEmpty2Null() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Person2 p=new Person2();
		p.setAddress("");
		p.setIdentitify("");
		ReflectHWUtils.convertEmpty2Null(p);
		System.out.println(p.getAddress());
	}*/

    @Test
    public void test_getAllFieldList1() {
        List<Field> fieldList = ReflectHWUtils.getAllFieldList(Person2.class);
        int size = fieldList.size();
        System.out.println("total num:" + size);
        System.out.println("--------------------------------");
        for (int i = 0; i < size; i++) {
            Field array_element = fieldList.get(i);
            System.out.println(array_element.getName());
        }
    }

    @Test
    public void test_getAllFieldList2() {
        List<Field> fieldList = ReflectHWUtils.getAllFieldList(Student2.class);
        int size = fieldList.size();
        System.out.println("total num:" + size);
        System.out.println("--------------------------------");
        for (int i = 0; i < size; i++) {
            Field array_element = fieldList.get(i);
            System.out.println(array_element.getName());
        }
    }

    @Test
    public void test_isNullObject() throws SecurityException,
            IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        Person2 p = new Person2();
        org.junit.Assert.assertEquals(true, ReflectHWUtils.isNullObject(p, false, true));
        org.junit.Assert.assertEquals(false, ReflectHWUtils.isNullObject(p, false, false));

        p.setAddress("beijing");
        org.junit.Assert.assertEquals(false, ReflectHWUtils.isNullObject(p, false, true));
        org.junit.Assert.assertEquals(false, ReflectHWUtils.isNullObject(p, false, false));

        p.setAddress(null);
        p.setId(0);
        org.junit.Assert.assertEquals(true, ReflectHWUtils.isNullObject(p, false, true));
        org.junit.Assert.assertEquals(false, ReflectHWUtils.isNullObject(p, false, false));

        p = new Person2();
        p.setId(0);
        org.junit.Assert.assertEquals(true, ReflectHWUtils.isNullObject(p, true, true));
        org.junit.Assert.assertEquals(true, ReflectHWUtils.isNullObject(p, true, false));

        p = new Person2();
        p.setId(2);
        org.junit.Assert.assertEquals(true, ReflectHWUtils.isNullObject(p, true, true));
        org.junit.Assert.assertEquals(true, ReflectHWUtils.isNullObject(p, true, false));

        p = new Person2();
        p.setId(20000);
        org.junit.Assert.assertEquals(true, ReflectHWUtils.isNullObject(p, true, true));
        org.junit.Assert.assertEquals(true, ReflectHWUtils.isNullObject(p, true, false));

        p = new Person2();
        p.setId(20000);
        p.setBirthdate(TimeHWUtil.getCurrentTimestamp());
        ;
        org.junit.Assert.assertEquals(false, ReflectHWUtils.isNullObject(p, true, true));
        org.junit.Assert.assertEquals(false, ReflectHWUtils.isNullObject(p, true, false));
    }

    // @Test
    // public void test_getDeclaredFields() {
    // Field[]fields=Person2.class.getFields();
    // for (int i = 0; i < fields.length; i++) {
    // Field field = fields[i];
    // System.out.println(field.getName());
    // }
    // }
    @Test
    public void test_isInteger() {
        String input = "123";
        // System.out.println(input+":"+ValueWidget.isInteger(input) );
        org.junit.Assert.assertEquals(true, ValueWidget.isInteger(input));

        input = "000000000000009";
        // System.out.println(input+":"+ValueWidget.isInteger(input) );
        org.junit.Assert.assertEquals(true, ValueWidget.isInteger(input));

        input = "-9";
        // System.out.println(input+":"+ValueWidget.isInteger(input) );
        org.junit.Assert.assertEquals(true, ValueWidget.isInteger(input));

        input = "-09";
        // System.out.println(input+":"+ValueWidget.isInteger(input) );
        org.junit.Assert.assertEquals(true, ValueWidget.isInteger(input));

        input = "--9";
        // System.out.println(input+":"+ValueWidget.isInteger(input) );
        org.junit.Assert.assertEquals(false, ValueWidget.isInteger(input));
    }

    @Test
    public void test_getSpecifiedField() {
        Field field = ReflectHWUtils.getSpecifiedField(Student2.class, "id");
        Assert.assertEquals("id", field.getName());

    }

    //	@Test
    public void test_setObjectValue() throws SecurityException,
            IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        Student2 p = new Student2();
        ReflectHWUtils.setObjectValue(p, "personName", "1");
        System.out.println(ReflectHWUtils.getObjectValue(p, "personName"));
    }

    // @Test
    // public void test_html(){
    // String input=" <html>fdf </html> ";
    // System.out.println(input.matches("^[\\s]*<html>.*</html>[\\s]*$"));
    // }

    //	@Test
    public void test_getObjectValue() throws SecurityException,
            IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        Student2 p = new Student2();
        ReflectHWUtils.setObjectValue(p, "classroom", "三六班");
        System.out.println(p.getClassroom());
    }

    // @Test
    // public void test_getListFiles(){
    // ArrayList<File> files=FileUtils.getListFiles("d:\\Temp\\a\\a");
    // SystemUtil.printFilesFilePath(files);
    // }
    @Test
    public void test_clone() throws IOException {
        Woman w = new Woman();
        w.setAge(23);
        w.setCheerful(true);
        w.setName("jn");

        Woman w2 = FileUtils.clone(w);
        System.out.println(w2.getAge());
        System.out.println(w2.getName());
        System.out.println(w == w2);

    }

    @Test
    public void test_appandByte() {
        byte[] bytes = new byte[]{1, 2, 3};
        byte[] resultBytes = SystemHWUtil.appandByte(bytes, (byte) 32);
        SystemHWUtil.isEquals(resultBytes, new byte[]{1, 2, 3, 32});
    }

    @Test
    public void test_hexStringToBytes() {
        String hexString = "12";
        byte[] result = hexStringToBytes(hexString);
        SystemHWUtil.printBytes(result);
    }

    @Test
    public void test_arrayIsEqual2() {
        System.out.println("test_filterFrontBytes");
        byte[] a = null;
        byte[] b = new byte[]{1, 2, 3, 4};
        // System.out.println( SystemUtil.arrayIsEqual(a, b));
        Assert.assertEquals(false, SystemHWUtil.arrayIsEqual(a, b));
        Assert.assertEquals(true, SystemHWUtil.arrayIsEqual(a, a));
        Assert.assertEquals(true, SystemHWUtil.arrayIsEqual(b, b));
    }

    //	@Test
    public void test_parseCA() throws CertificateException,
            FileNotFoundException {
        File file = new File("d:\\work\\sslserver.der");// ssl服务端的证书
        PublicKey pk = SystemHWUtil.getPublicKey(file);
        System.out.println(pk);
    }

    public void test_verifySing() {

    }

    @Test
    public void test_01() {
        try {
            String keyStorePath = "d:\\Temp\\a\\a\\ca\\zlex.keystore";
            if (FileUtils.isFile(keyStorePath)) {
                String password = "abcdefg";
                String alias = "www.kunlunsoft.com";
                PrivPubKeyBean privPubKeyBean = SystemHWUtil.getPrivPubKeyBean(
                        keyStorePath, password, alias);
                System.out.println(privateKey);

                Assert.assertEquals(true, SystemHWUtil.verifyPrivPubKey(
                        privPubKeyBean.getPublKey(),
                        privPubKeyBean.getPrivKey()));
                System.out.println(privPubKeyBean.getSigAlgName());// SHA1withRSA
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //	@Test
    public void test_03() {
        try {
            String message = "whuang";
            String keyStorePath = "d:\\Temp\\a\\a\\ca\\zlex.keystore";
            if (FileUtils.isFile(keyStorePath)) {
                String password = "abcdefg";
                String alias = "www.kunlunsoft.com";
                PrivPubKeyBean privPubKeyBean = SystemHWUtil.getPrivPubKeyBean(
                        keyStorePath, password, alias);
                byte[] result = SystemHWUtil.encrypt(message,
                        privPubKeyBean.getPublKey());
                byte[] deResult = SystemHWUtil.decrypt(result,
                        privPubKeyBean.getPrivKey());
                System.out.println(new String(deResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test_bytes_same() throws UnsupportedEncodingException {
        String message = "jingning";
        byte[] bytes1 = message.getBytes(SystemHWUtil.CHARSET_ISO88591);
        byte[] bytes2 = message.getBytes(SystemHWUtil.CHARSET_UTF);
        Assert.assertEquals(true, SystemHWUtil.isSame(bytes1, bytes2));
    }

    @Test
    public void test_bytes_same2() throws UnsupportedEncodingException {
        String message = "jingning中国";
        byte[] bytes1 = message.getBytes(SystemHWUtil.CHARSET_ISO88591);
        byte[] bytes2 = message.getBytes(SystemHWUtil.CHARSET_UTF);
        Assert.assertEquals(false, SystemHWUtil.isSame(bytes1, bytes2));
    }

    //	@Test
    public void test_decompress() {
        String zipFile = "D:\\bin\\conf\\conf.zip";
        String decompressLocStr = "D:\\Temp\\a\\a\\b";
        boolean isSuccess;
//		try {
//			isSuccess = CompressZipUtil.decompress(zipFile, decompressLocStr,"");
//			System.out.println(isSuccess);
//		} catch (ArchiveException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        String str = System.getProperty("java.io.tmpdir");
        ;
        System.out.println(str);
    }

//	@Test
//	public void test_getZipFileList(){
//		String directory="D:\\Temp\\a\\c";
//		try {
//			CompressZipUtil.compressZipRecursion("D:\\Temp\\a\\a\\b\\cc.zip", directory);
//		} catch (ArchiveException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

    //	@Test
    public void test_deCompressRecursionFileList() {
        String zipFile = "D:\\bin\\conf\\conf.zip";
        try {
            List<ZipFileBean> zipFiles = CompressZipUtil.deCompressRecursionFileList(zipFile, "", true);
            CompressZipUtil.printZipFileBeans(zipFiles);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ArchiveException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void test_isSamePropertyValue() {
        Woman w1 = new Woman();
        Woman w2 = new Woman();

        w1.setAge(1);
        w2.setAge(1);

        w1.setName("whuang");
        w2.setName("whuang");
        try {
            boolean isSame = ReflectHWUtils.isSamePropertyValue(w1, w2);
            System.out.println(isSame);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_split() {
//		String input="abc,www\\,aaa";
//		String[]strs=input.split(",");
//		for (int i = 0; i < strs.length; i++) {
//			String string = strs[i];
//			System.out.println(string);
//		}
    }

    @Test
    public void test_byte() {
//		byte type = Byte.valueOf("-10",16);
//		System.out.println(type);
//		System.out.println((byte)0x80 ^ (byte)0x82);
    }

    @Test
    public void test_formatArr() {
        String[] strs = new String[]{"a", "b", "c", "d"};
        Assert.assertEquals("a,b,c,d", SystemHWUtil.formatArr(strs, ","));
    }

    @Test
    public void test_compareText() {
        String text1 = "";
        String text2 = "aabcb";
        System.out.println();
        Assert.assertEquals(-1, SystemHWUtil.compareText(text1, text2));
    }

    //	@Test
    public void test_compressZip() {
        String folder = "D:\\doc\\android\\app\\package\\2014-4-9\\2014-4-9\\a";
        String zipFile = "D:\\doc\\android\\app\\package\\2014-4-9\\cccc.zip";
        try {
            CompressZipUtil.compressZip(zipFile, folder);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArchiveException e) {
            e.printStackTrace();
        }
    }
	/*public void test_priv_pub_fromStr(){
		SystemHWUtil.getPrivateKey(modulus, privateExponent)
	}*/

}
