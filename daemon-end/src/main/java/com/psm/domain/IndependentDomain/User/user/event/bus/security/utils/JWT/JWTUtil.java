package com.psm.domain.IndependentDomain.User.user.event.bus.security.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtil {
    @Autowired
    JWTUtilProperties jwtUtilProperties;

    /**
     * 生成uuid
     *
     * @return String uuid
     */
    public String getUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-","");

    }

    /**
     * 生成jwt
     *
     * @param subject
     * @return String jwt密文
     */
    public String createJWT(String subject){
        return createJWT(subject, jwtUtilProperties.getExpiration(), getUUID());// 设置过期时间
    }

    /**
     * 生成jwt
     *
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return String jwt密文
     */
    public String createJWT(String subject, Long ttlMillis){
        return createJWT(subject, ttlMillis, getUUID());
    }

    /**
     * 生成jwt
     *
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return String jwt密文
     */
    public String createJWT(String subject, Long ttlMillis, String id){
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    private JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid){
        //指定加密算法
        SecureDigestAlgorithm<SecretKey, SecretKey> algorithm = Jwts.SIG.HS256;

        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null){
            ttlMillis = jwtUtilProperties.getExpiration();
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid) // 设置唯一id
                .setSubject(subject) //主题 可以是json数据
                .setIssuer(jwtUtilProperties.getIssuer()) // 签发者
                .setIssuedAt(now) // 签发时间
                .signWith(secretKey, algorithm)// 加密算法以及秘钥
                .setExpiration(expDate);
    }

    public SecretKey generalKey(){
        String originalKey = jwtUtilProperties.getSecret();
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
    public Claims parseJWT(String jwt) throws Exception{
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
    private Date getJwtExpiration(String jwt) {
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
    private long getRemainingTime(Date expirationDate) {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTime = expirationDate.getTime();
        return (expirationTime - currentTimeMillis) / 1000;//返回以秒为单位
    }
}
