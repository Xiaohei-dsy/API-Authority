package com.aiit.authority.utils;


import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;


public class JwtUtils {

    // 唯一密钥存储在服务端
    private static final String SECRET_KEY = "XX#$%()(#*!()!KL<><MQLMNQNQJQKsdfkjsdrow32234545fdf>?N<:{LWPW";

    /**
     * 生成token
     *
     * @return
     */
    public static String createToken(String username) {
        try {
            // 设置加密算法（密钥采用固定值+用户名的格式，这样密钥是动态的，进一步加强安全性）
            Algorithm algorithm = Algorithm.HMAC256(username + SECRET_KEY);
            // 生成token(不带前缀)
            return JWT.create()
                    // 设置封入jwt的数据
                    .withClaim("username", username)
                    .withClaim("saveTime", System.currentTimeMillis())
                    // 采用算法
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证jwt携带签名的合法性
     *
     * @param token
     * @param username
     * @return
     * @throws JWTVerificationException
     * @throws IllegalArgumentException
     */
    public static boolean verifyToken(String token, String username)
            throws JWTVerificationException, IllegalArgumentException {

        // 指定解密算法，使用加密数字签名时的密钥
        Algorithm algorithm = Algorithm.HMAC256(username + SECRET_KEY);
        // 验证器使用指定解密算法构建，用于解密（还可以添加其他待验证信息，比如签发机构）
        JWTVerifier verifier = JWT.require(algorithm).build();
        // 验证token（是否过期，payload是否被篡改）,如果这一步有问题会抛JWTVerificationException异常
        // 考虑到jwt无法续签，本项目结合redis进行续签，顾生成与验证token时均忽略jwt的expire time
        verifier.verify(token);
        // 非法抛异常，合法返回true
        return true;
    }

    /**
     * 解密token为string
     *
     * @param token
     * @return
     */
    public static String decodeToken(String token) throws JWTVerificationException {
        /**
         * jwt分为3部分：
         * header：包含加密算法，采用base64算法加密；
         * payload：包含我们藏入的用户信息（比如username），签发时间，签发者等，采用base64算法加密
         * signature：根据前两部分 + 密钥（可以选择加盐）经过算法加密生成。JWTVerifier验证的主体就是这部分
         * 任何我们藏入的信息都封在payload内，因此无需密钥直接根据base64算法解密即可。
         */
        DecodedJWT decodedToken = JWT.decode(token);
        String username = decodedToken.getClaim("username").asString();
        return username;
    }

    public static String getTokenWithoutPrefix(String bearerToken) {

        // 先检查token是否有前缀
        if (!bearerToken.startsWith("Bearer ")) {
            throw new AuthenticateException(ResultCodeEnum.IDENTITY_VERIFICATION_FAILED);
        }

        // 返回去除前缀的token
        return bearerToken.split(" ")[1];
    }

}

