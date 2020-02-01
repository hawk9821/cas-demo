package com.hawk;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.ConfigurableHashService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

/**
 * @Author hawk9821
 * @Date 2020-01-23
 */
public class PasswordSaltTest {
    protected String algorithmName = "MD5";
    protected String staticSalt = ".";
    protected String encodedPassword = "123456";
    protected static int hashIterations = 10;


    @Test
    public void test(){
        for (int i = 0; i < 10; i++) {
            buildPWD();
        }
    }


    public void buildPWD(){
//        String uuid = UUID.randomUUID().toString();
//        String salt = getEncodePwd(this.staticSalt,this.algorithmName,null,uuid);

        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String salt = randomNumberGenerator.nextBytes().toHex();
        String res = getEncodePwd(this.staticSalt,this.algorithmName,salt,this.encodedPassword);
        System.out.println("salt   :" + salt);
        System.out.println("password   :" + this.encodedPassword);
        System.out.println("encodedPassword   :" + res);
        System.out.println("===============================");
    }

    public static String getEncodePwd(String staticSalt,String algorithmName,String salt,String encodedPassword){
        ConfigurableHashService hashService = new DefaultHashService();
        if (!StringUtils.isBlank(staticSalt)){
            hashService.setPrivateSalt(ByteSource.Util.bytes(staticSalt));
        }
        hashService.setHashAlgorithmName(algorithmName);
        hashService.setHashIterations(hashIterations);
        HashRequest request = new HashRequest.Builder()
                .setSalt(salt)
                .setSource(encodedPassword)
                .build();
        return hashService.computeHash(request).toHex();
    }

    @Test
    public void encryptPassword(){
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String salt = randomNumberGenerator.nextBytes().toHex();
        String encrPWD = new SimpleHash(
                this.algorithmName,
                this.encodedPassword,
                ByteSource.Util.bytes(salt),
                10).toHex();
        System.out.println("salt   :" + salt);
        System.out.println("password   :" + this.encodedPassword);
        System.out.println("encodedPassword   :" + encrPWD);
        System.out.println("===============================");
//        String encodePwd = getEncodePwd(null, this.algorithmName, salt, this.encodedPassword);
//        System.out.println("encodedPassword   :" + encodePwd);
//        System.out.println(encodePwd.equals(encrPWD));

    }









}

