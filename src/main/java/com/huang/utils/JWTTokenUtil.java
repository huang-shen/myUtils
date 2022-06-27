package com.huang.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.huang.utils.entity.to.TokenValResultTO;

import java.util.Calendar;

/**
 * @author HuangShen
 * @Description token 生成token
 * @create 2021-09-25 15:47
 */
public class JWTTokenUtil {
    /**
     * 生成token 账号密文密码做密钥
     *
     * @param tokenTO 用户信息
     * @param secret  密码密文
     * @return String token
     */
    public static String creatToken(String tokenTO, String secret, Long often) {
        Calendar instance = Calendar.getInstance();
        // token often 小时过期
        instance.add(Calendar.HOUR, Math.toIntExact(often));
        String token = JWT.create()
                .withClaim("tokenInfo", tokenTO)//添加payload
                .withExpiresAt(instance.getTime())//设置过期时间
                .sign(Algorithm.HMAC256(secret));
        return AESUtils.encode(token, AESUtils.parseHexStr2Byte(secret));
    }

    public static TokenValResultTO validationToken(String token, String secret) {


        TokenValResultTO tokenValResultTO = new TokenValResultTO();
        DecodedJWT decodedJWT = null;
        try {
            token = AESUtils.decode(token, AESUtils.parseHexStr2Byte(secret));
            //创建验证对象,这里使用的加密算法和密钥必须与生成TOKEN时的相同否则无法验证
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            //验证JWT
            decodedJWT = jwtVerifier.verify(token);
        } catch (TokenExpiredException err) {
            tokenValResultTO.setCode(100);
            tokenValResultTO.setMsg("token 过期");
            return tokenValResultTO;
        } catch (Exception err) {
            tokenValResultTO.setCode(500);
            tokenValResultTO.setMsg("token 无效");
            return tokenValResultTO;
        }

        String userInfo = decodedJWT.getClaim("tokenInfo").asString();
        tokenValResultTO.setTokenInfo(userInfo);
        tokenValResultTO.setExpiresAt(decodedJWT.getExpiresAt().getTime());
        tokenValResultTO.setMsg("token 验证成功");
        tokenValResultTO.setCode(200);
        return tokenValResultTO;
    }
}
