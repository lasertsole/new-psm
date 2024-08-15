package com.psm.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JWTUtil {
    /**
     * 默认有效期为
     */
    public static final Long JWT_TTL = 60 * 60 * 1000L;//一个小时

    /**
     * 设置密钥
     */
    public static final String JWT_KEY = "moyesprite";

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 生成jwt
     *
     * @param subject
     * @return String jwt密文
     */
    public static String createJWT(String subject){
        return createJWT(subject, JWT_TTL, getUUID());// 设置过期时间
    }

    /**
     * 生成jwt
     *
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return String jwt密文
     */
    public static String createJWT(String subject, Long ttlMillis){
        return createJWT(subject, ttlMillis, getUUID());
    }

    /**
     * 生成jwt
     *
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return String jwt密文
     */
    public static String createJWT(String subject, Long ttlMillis, String id){
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid){
        //指定加密算法
        SecureDigestAlgorithm<SecretKey, SecretKey> algorithm = Jwts.SIG.HS256;

        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null){
            ttlMillis = JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid) // 设置唯一id
                .setSubject(subject) //主题 可以是json数据
                .setIssuer("moye") // 签发者
                .setIssuedAt(now) // 签发时间
                .signWith(secretKey, algorithm)// 加密算法以及秘钥
                .setExpiration(expDate);
    }

    public static SecretKey generalKey(){
        String originalKey = JWT_KEY;
        int requiredLength = 43; // Base64 encoded length for 256 bits (32 bytes)

        // 将原始密钥重复填充到 43 字符
        StringBuilder sb = new StringBuilder();
        while (sb.length() < requiredLength) {
            sb.append(originalKey);
        }

        // 截取前 43 字符
        String base64EncodedKey = sb.substring(0, requiredLength);

        byte[] encodedKey = Base64.getDecoder().decode(base64EncodedKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA256");
        return key;
    }

    /**
     * 解析jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception{
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 获取jwt过期时间
     *
     * @param jwt
     * @return
     */
    private static Date getJwtExpiration(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getExpiration();
    }

    /**
     * 获取jwt剩余过期时间
     *
     * @param expirationDate
     * @return
     */
    private static long getRemainingTime(Date expirationDate) {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTime = expirationDate.getTime();
        return (expirationTime - currentTimeMillis) / 1000;//返回以秒为单位
    }
}
