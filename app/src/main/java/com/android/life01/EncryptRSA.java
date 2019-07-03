package com.android.life01;



import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;


/**
 * Created by wentao *_*
 * on 2019-06-26
 */
public class EncryptRSA {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final String ANDROID_RSA = "RSA/None/NoPadding";
    public static final String JDK_RSA = "RSA/None/PKCS1Padding";
    /**
     * RSA算法
     */
    public static final String RSA = "RSA";

    /**
     * 用公钥对字符串进行加密
     *
     * @param content 原文
     */
    public static String encryptByPublicKey(String content, String publicKey) {
        try {


            PublicKey keyPublic = getPublicKeys(publicKey);


            Cipher cp = Cipher.getInstance(JDK_RSA);
            cp.init(Cipher.ENCRYPT_MODE, keyPublic);
            byte[] bytes = cp.doFinal(content.getBytes(UTF_8));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               return Base64.getEncoder().encodeToString(bytes);
            }
            return "";
//            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 得到公钥
     *
     * @param publicKeyStr 密钥字符串（经过base64编码）
     * @return PublicKey
     */
    private static PublicKey getPublicKey(String publicKeyStr) throws Exception {
//        byte[] keyByte = Base64.decode(publicKeyStr.getBytes(), Base64.NO_WRAP);
        byte[] keyByte = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyByte =   Base64.getDecoder().decode(publicKeyStr);
        }

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyByte);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 得到公钥
     * @param publicKeyStr 密钥字符串（经过base64编码）
     * @return PublicKey
     */
    private static PublicKey getPublicKeys(String publicKeyStr) {

        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        PublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);


            publicKey = keyFactory.generatePublic(keySpec);

        } catch (Exception e) {
            outException(e);
            throw new RuntimeException("得到公钥异常");
        }

        return publicKey;
    }

    public static String outException(Exception e) {
        StringPrintWriter pw = new StringPrintWriter();
        e.printStackTrace(pw);
        return pw.out();
    }

    public static String outThrowable(Throwable e) {
        StringPrintWriter pw = new StringPrintWriter();
        e.printStackTrace(pw);
        return pw.out();
    }

    static class StringPrintWriter extends PrintWriter {
        public StringPrintWriter() {
            super(new StringWriter());
        }

        public StringPrintWriter(int size) {
            super(new StringWriter(size));
        }

        public String out() {
            flush();
            return ((StringWriter)out).toString();
        }

        public String toString() {
            return out();
        }
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength
     *            密钥长度，范围：512～2048<br>
     *            一般1024
     * @return
     */
    public static KeyPair generateRSAKeyPair(int keyLength)
    {
        try
        {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
