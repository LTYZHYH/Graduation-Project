package team.j2e8.findcateserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.j2e8.findcateserver.infrastructure.ObjectSelector;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.City;
import team.j2e8.findcateserver.models.TravelStrategy;
import team.j2e8.findcateserver.services.TravelStrategyService;
import team.j2e8.findcateserver.utils.FileUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
@Controller
@RequestMapping(value = "/travelstrategy")
public class TravelStrategyController {
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;
    @Autowired
    private TravelStrategyService travelStrategyService;
    private FileUtil fileUtil = new FileUtil();
    @RequestMapping(method = RequestMethod.GET, value = "/picture/{pictureId}")
    public void getPic(@PathVariable("pictureId") String pictureId, HttpServletResponse response) throws IOException {
        if (pictureId != null) {
            response.setContentType("image/jpeg");
            String path = "/strategyPicture/";
            String contentType = "image/jpeg";
            fileUtil.getPictureFile(path,contentType,pictureId,response);
//            FileInputStream is = new FileInputStream();
//
//            if (is != null) {
//                int i = is.available(); // 得到文件大小
//                byte data[] = new byte[i]; // data是字节
//                is.read(data); // 从输入流里，把图片读到data变量里。
//                is.close(); // 输入流读完要关闭，否则内存会一直占用
//                response.setContentType("image/jpeg");  // 设置返回的文件类型
//                OutputStream toClient = response.getOutputStream(); // toClient就是response的输出流，就是响应的意思
//                toClient.write(data); // 把data写到toClient
//                toClient.close();
//            }
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/issueStrategy")
    public ResponseEntity<?> issueStrategy(@RequestParam(value = "file1", required = false) MultipartFile file1,
                                           @RequestParam(value = "file2", required = false) MultipartFile file2,
                                           @RequestParam(value = "file3", required = false) MultipartFile file3,
                                           TravelStrategy travelStrategy, String issueTime1, City city) throws IOException {
        String path = "/strategyPicture";
        FileUtil fileUtil = new FileUtil();
        String strategyPicture1;
        String strategyPicture2;
        String strategyPicture3;
        if (file1 != null){
            strategyPicture1 = fileUtil.FileUtil(file1,path);
        } else {
            strategyPicture1 = "null";
        }
        if (file2 != null){
            strategyPicture2 = fileUtil.FileUtil(file2,path);
        } else {
            strategyPicture2 = "null";
        }
        if (file3 != null){
            strategyPicture3 = fileUtil.FileUtil(file3,path);
        } else {
            strategyPicture3 = "null";
        }
        String theme = travelStrategy.getTheme();
        String area = travelStrategy.getArea();
        String strategyContent = travelStrategy.getStrategyContent();
        String overheadCost = travelStrategy.getOverheadCost();
        Integer travelDays = travelStrategy.getTravelDays();
        String issueTime = issueTime1;
        Integer scenicNumber = travelStrategy.getScenicNumber();
        travelStrategyService.issueTravelStrategy(theme, area, travelDays, overheadCost, strategyContent, issueTime,scenicNumber,
                strategyPicture1, strategyPicture2, strategyPicture3, city);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getStrategyByCityId")
    public ResponseEntity<?> getStrategyByCityId(@RequestParam Integer cityId,
                                                 @RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                                 @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                                 @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "strategyId,desc") String sort) throws Exception {

        Page<TravelStrategy> travelStrategies = travelStrategyService.getPageTravelStrategyByCityId(cityId, sort, pageNum, pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(travelStrategies, "(strategyId,area,theme,strategyPicture1)"));
    }
    //根据省份模糊搜索攻略
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getStrategyByLike")
    public ResponseEntity<?> getStrategyByLike(@RequestParam Integer cityId,
                                               @RequestParam String like,
                                               @RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                               @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                               @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "strategyId,desc") String sort) throws Exception {
        Page<TravelStrategy> travelStrategies = travelStrategyService.getPageTravelStrategyByLike(cityId, like, sort, pageNum, pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(travelStrategies, "(strategyId,area,theme,strategyPicture1)"));
    }
    //获取未审核的攻略
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,value = "getDisactiveStrategy")
    public ResponseEntity<?> getDisactiveStrategy(){
        Iterable<TravelStrategy> travelStrategies = travelStrategyService.getDisactiveStrategy();
        return ResponseEntity.ok(new ObjectSelector().mapObjects(travelStrategies, "(strategyId,theme,area,issueTime,city(cityId,cityTitle),strategyAudit)"));
    }
    //审核攻略
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/auditStrategy")
    public ResponseEntity<?> auditStrategy(@RequestBody JsonNode jsonNode){
        Integer strategyId = Integer.valueOf(jsonNode.path("strategyId").textValue());
        String auditResult = jsonNode.path("auditResult").textValue();
        travelStrategyService.auditStrategy(strategyId,auditResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    //获取攻略详情
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "getStrategyDetila")
    public ResponseEntity<?> getStrategyDetila(@RequestParam Integer strategyId){
        Optional<TravelStrategy> travelStrategyOptional = travelStrategyService.getStrategyDetail(strategyId);
        return ResponseEntity.ok(new ObjectSelector().mapObject(travelStrategyOptional.get(),"(strategyId,area,theme,travelDays,strategyContent,overheadCost,scenicNumber,issueTime,strategyPicture1,strategyPicture2,strategyPicture3,user(id,userName))"));
    }
    //获取所有攻略
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,value = "getAllStrategy")
    public ResponseEntity<?> getAllStrategy(@RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                            @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                            @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "strategyId,desc") String sort) throws Exception{
        Page<TravelStrategy> travelStrategyPage = travelStrategyService.getAllStrategy(sort,pageNum,pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(travelStrategyPage,"(strategyId,area,theme,user(id,userName),issueTime)"));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "retainStrategy")
    public ResponseEntity<?> retainStrategy(@RequestBody JsonNode jsonNode){
        Integer strategyId = Integer.valueOf(jsonNode.path("strategyId").textValue());
        travelStrategyService.retainStrategy(strategyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "blockStrategy")
    public ResponseEntity<?> blockStrategy(@RequestBody JsonNode jsonNode){
        Integer strategyId = Integer.valueOf(jsonNode.path("strategyId").textValue());
        travelStrategyService.blockStrategy(strategyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
