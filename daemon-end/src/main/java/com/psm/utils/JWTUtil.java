package com.psm.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
     * 有效期为
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
        return createJWT(subject, null, getUUID());// 设置过期时间
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
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
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
                .signWith(signatureAlgorithm, secretKey)// 加密算法以及秘钥
                .setExpiration(expDate);
    }

    public static SecretKey generalKey(){
        byte[] encodedKey = Base64.getDecoder().decode(JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
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
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
