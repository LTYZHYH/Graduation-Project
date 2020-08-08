package team.j2e8.findcateserver.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ErrorMessageBuilder {

    @Autowired
    private HttpServletRequest request;

    public Map<String, Object> build(int status, String error, String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timestamp", new Date().getTime());
        map.put("status", status);
        map.put("path", request.getServletPath());
        map.put("error", error);
        map.put("message", message);
        return map;
    }
}