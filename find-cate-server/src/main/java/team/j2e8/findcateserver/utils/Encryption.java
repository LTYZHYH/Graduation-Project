package team.j2e8.findcateserver.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Encryption {

    private static final Integer SALT_LENGTH = 12;
    //获取加密密码的帮助函数
    public String getPassword(String password, String salt) throws Exception {
        MessageDigest messageDigest;
        try {
            //加密方式
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(e);
        }
        messageDigest.update((password + salt).getBytes());
        byte[] byteBuffer = messageDigest.digest();

        return new String(Base64.getEncoder().encode(byteBuffer));
    }
    //获取随机加密盐
    public String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] mySalt = new byte[SALT_LENGTH];
        random.nextBytes(mySalt);

        return new String(Base64.getEncoder().encode(mySalt));
    }

    public String getSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] mySalt = new byte[length];
        random.nextBytes(mySalt);
        return new String(Base64.getEncoder().encode(mySalt));
    }
}
