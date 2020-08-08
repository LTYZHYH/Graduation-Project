package team.j2e8.findcateserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.security.util.Password;
import team.j2e8.findcateserver.infrastructure.ObjectSelector;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.Response;
import team.j2e8.findcateserver.models.TravelStrategy;
import team.j2e8.findcateserver.models.User;
import team.j2e8.findcateserver.repositories.UserRepository;
import team.j2e8.findcateserver.services.TravelStrategyService;
import team.j2e8.findcateserver.services.UserService;
import team.j2e8.findcateserver.utils.FileUtil;
import team.j2e8.findcateserver.utils.JwtHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;
    @Autowired
    private UserService userService;
    @Autowired
    private TravelStrategyService travelStrategyService;
    @Autowired
    private JwtHelper jwtHelper;
    private FileUtil fileUtil = new FileUtil();
    @Value("${token.issuer}")
    private String issuer;
    @Value("${token.expired_in}")
    private int expiredIn;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<?> login(@RequestBody JsonNode jsonNode) throws Exception {
        //用户认证
        String email = jsonNode.path("user_email").textValue();
        String password = jsonNode.path("password").textValue();
        User user = userService.verifyUserByEmail(email, password);
        userService.verifyUserByEmail(email,password);
        //token颁发
        return ResponseEntity.ok(jwtHelper.getToken(issuer, expiredIn, user));
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<?> register(@RequestBody JsonNode jsonNode) throws Exception {
        String email = jsonNode.path("user_email").textValue();
        String userName = jsonNode.path("user_name").textValue();
        String userTelenumber = jsonNode.path("user_telenumber").textValue();
//        String userPhoto = jsonNode.path("user_photo").textValue();
        String password = jsonNode.path("password").textValue();
        userService.registerUserByEmail(email, userName, userTelenumber, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "addUserPic")
    public ResponseEntity<?> addUserPic(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        String path = "/Users/yhh/Desktop/毕业设计/Find-Cate-master/find-cate-server/src/main/resources/static/userPicture";
        String fileName;
        if (file.isEmpty()){
            fileName = "n";
        } else {
            fileName = fileUtil.FileUtil(file,path);
        }
        userService.addUserPic(fileName);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/upData")
    public ResponseEntity<?> upData(@RequestBody JsonNode jsonNode) throws Exception {
        String newUserName = jsonNode.path("new_user_name").textValue();
        String newUserEmail = jsonNode.path("new_user_email").textValue();
        String newUserPhone = jsonNode.path("new_user_phone").textValue();
        userService.updateUserInformation(newUserName, newUserEmail,newUserPhone);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/adminInfo")
    public ResponseEntity<?> getAdminInformation(@RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                                @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                                @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "id")String sort) throws Exception {
        Page<User> user = userService.getLoginAdminInformation(sort, pageNum, pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(user,
                "(id, userName, userTelenumber, userPhoto, userEmail, admin(adminId))"));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    public ResponseEntity<?> getUserInformation() throws Exception {
        Optional<User> user = userService.getLoginUserInformation();
//        return ResponseEntity.ok(new ObjectSelector().mapObject(user.get(), "(id, userName, userTelenumber, userPhoto, userEmail,admin(adminId)"));
        return ResponseEntity.ok(new ObjectSelector().mapObject(user.get(), "(id, userName, userTelenumber, userPhoto, userEmail)"));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/userPicture/{pictureName}")
    public void getUserPicture(@PathVariable("pictureName") String pictureName, HttpServletResponse response )throws IOException {
        if (pictureName != null) {
            String path = "/Users/yhh/Desktop/毕业设计/Find-Cate-master/find-cate-server/src/main/resources/static/userPicture/";
            String contentType = "image/jpeg";
            fileUtil.getPictureFile(path,contentType,pictureName,response);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,value = "getMyStrategy")
    public ResponseEntity<?> getMyStrategy(@RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                           @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                           @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "strategyId,desc")String sort )throws Exception{
        Page<TravelStrategy> travelStrategies = travelStrategyService.getStrategyByUserId(sort,pageNum,pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(travelStrategies,"(strategyId,theme,strategyAudit)"));
    }
}
