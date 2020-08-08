package team.j2e8.findcateserver.infrastructure.usercontext;

import com.fasterxml.jackson.databind.ObjectMapper;
import team.j2e8.findcateserver.exceptions.UnAuthorizedException;
import team.j2e8.findcateserver.models.User;
import team.j2e8.findcateserver.utils.JwtHelper;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Administrator on 2017/9/1.
 */

@Component(value = "tokenIdentityContext")
public class TokenIdentityContext implements IdentityContext {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private JwtHelper jwtHelper;
    @Value("${token.key}")
    private String key;

    //获取当前token的payload
    @Override
    public Object getIdentity() {
        isAuthenticated();
        Object object;
        String token = httpServletRequest.getHeader("Jwt-Token");
        if (token == null) {
            token = httpServletRequest.getParameter("Jwt_Token");
        }
        try {
            object = new ObjectMapper().readValue(jwtHelper.convertTokenToJsonNode(token).path("identity").traverse(), User.class);
        } catch (IOException e) {
            throw new UnAuthorizedException(ErrorMessage.INVALID_TOKEN);
        }
        return object;
    }

    //认证当前请求的token
    @Override
    public boolean isAuthenticated() {
        String token = httpServletRequest.getHeader("Jwt-Token");
        if (token == null) {
            token = httpServletRequest.getParameter("Jwt_Token");
        }
        jwtHelper.parseJWT(token);
        return true;
    }

    @Override
    public String getAuthKey() {
        return key;
    }


}
