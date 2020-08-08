package team.j2e8.findcateserver.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.User;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;
import team.j2e8.findcateserver.valueObjects.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;
import team.j2e8.findcateserver.exceptions.UnAuthorizedException;
import team.j2e8.findcateserver.infrastructure.ObjectSelector;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//生成安全凭据
@Service
public class JwtHelper {
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;

    //生成token的帮助函数
    public Map<String, Object> createJWT(String issuer, long expiredMillis, User user)
            throws InvocationTargetException, IntrospectionException, NoSuchFieldException, IllegalAccessException {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String secret = identityContext.getAuthKey();
        long nowMillis = new Date().getTime();
        Date now = new Date(nowMillis);
        secret = new BASE64Encoder().encode(secret.getBytes());
        //添加Token过期时间
        long expMillis = nowMillis + expiredMillis;
        Date exp = new Date(expMillis);
        //序列化待优化
        String select = "user(id,userName,userTelenumber,userPhoto,userEmail)";
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setIssuer(issuer)
                .claim("identity", new ObjectSelector().mapObject(user, select))
                .signWith(signatureAlgorithm, secret);
        builder.setExpiration(exp).setNotBefore(now);
        Map<String, Object> jwtMap = new HashMap<>();
        jwtMap.put("token", builder.compact());
        jwtMap.put("expires_time", expMillis / 1000);
        return jwtMap;
    }

    //解析token的帮助函数
    public Claims parseJWT(String jsonWebToken) {
        return Jwts.parser()
                .setSigningKey(new BASE64Encoder().encode(identityContext.getAuthKey().getBytes()))
                .parseClaimsJws(jsonWebToken).getBody();
    }
    //获取token的payload函数
    public Map getPayload(String jsonWebToken) throws IOException {
        if (jsonWebToken == null || jsonWebToken.isEmpty()) {
            throw new UnAuthorizedException(ErrorMessage.INVALID_TOKEN);
        }
        String key = identityContext.getAuthKey();
        Claims claims = Jwts.parser()
                .setSigningKey(new BASE64Encoder().encode(key.getBytes()))
                .parseClaimsJws(jsonWebToken).getBody();
        Map<String, Object> map = new HashMap<>();
        map.put("identity", claims.get("identity"));
        return map;
    }

    public JsonNode convertTokenToJsonNode(String token) throws IOException {
        Map map = getPayload(token);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(objectMapper.writeValueAsString(map), JsonNode.class);
    }
    //返回token的帮助类
    public Token getToken(String issuer, long expiredIn, User user) throws IntrospectionException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        Map<String, Object> jwtMap = createJWT(issuer, expiredIn * 1000, user);
        Token token = new Token();
        token.setToken(jwtMap.get("token").toString());
        token.setExpiresIn(expiredIn);
        token.setExpiresTime(Long.parseLong(jwtMap.get("expires_time").toString()));
        return token;
    }
}
