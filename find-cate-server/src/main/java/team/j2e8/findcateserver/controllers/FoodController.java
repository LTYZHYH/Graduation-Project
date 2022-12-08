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
import team.j2e8.findcateserver.models.Food;
import team.j2e8.findcateserver.services.FoodService;
import team.j2e8.findcateserver.services.UserService;
import team.j2e8.findcateserver.utils.FileUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/food")
public class FoodController {
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;
    @Autowired
    private UserService userService;
    @Autowired
    private FoodService foodService;

    private FileUtil fileUtil = new FileUtil();
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "serve")
    public ResponseEntity<?> serveFood(@RequestParam(value = "foodPhoto",required = true) MultipartFile file,
                                       @RequestParam(value = "foodName") String foodName,
                                       @RequestParam(value = "foodIntroduction") String foodIntroduction,
                                       @RequestParam(value = "areaName") String areaName) throws Exception{
        String path = "/FoodPicture";
        String foodPhoto = fileUtil.FileUtil(file,path);
        foodService.addFood(foodName,foodPhoto,foodIntroduction,areaName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "unserve")
    public ResponseEntity<?> unServeFood(@RequestBody JsonNode jsonNode) {
        int foodId = jsonNode.path("food_id").asInt();
        int shopId = jsonNode.path("shop_id").asInt();
        foodService.unServeDish(foodId, shopId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "gets")
    public ResponseEntity<?> getFoodsByShop(@RequestParam(required = true, defaultValue = "") Integer shopId,
                                            @RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                            @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                            @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "foodId,desc") String sort) {
        Page<Food> foods = foodService.getFoodsByShop(shopId, sort, pageNum, pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(foods, "(foodId, foodName, foodPrice, foodPhoto, type(typeId, typeName))"));

    }

    @RequestMapping(method = RequestMethod.GET, value = "/FoodPicture/{pictureName}")
    public void getFoodPicture(@PathVariable("pictureName") String pictureName, HttpServletResponse response )throws IOException {
        if (pictureName != null) {
            String path = "/FoodPicture/";
            String contentType = "image/jpeg";
            fileUtil.getPictureFile(path,contentType,pictureName,response);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "getAllFood")
    public ResponseEntity<?> getAllFood(@RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                        @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                        @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "foodId,desc") String sort) throws Exception{
        Page<Food> foods = foodService.getAllFood(sort,pageNum,pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(foods,"(foodId, foodName, foodPhoto,foodIntroduction,area(areaId,areaName))"));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "getFoodByAreaName")
    public ResponseEntity<?> getFoodByAreaName(@RequestParam(required = false, defaultValue = "") Integer areaId,
                                            @RequestParam(required = true, defaultValue = "") String areaName,
                                            @RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                            @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                            @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "foodId,desc") String sort) {
        Page<Food> foods = foodService.getFoodByAreaName(areaId, sort, pageNum, pageSize,areaName);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(foods, "(foodId, foodName, foodPhoto,area(areaId,areaName))"));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "getFoodBySearch")
    public ResponseEntity<?> getFoodBySearch(@RequestParam(required = false, defaultValue = "") Integer areaId,
                                             @RequestParam(required = false,defaultValue = "") String foodName,
                                             @RequestParam(required = false,defaultValue = "") String areaName,
                                             @RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                             @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                             @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "foodId,desc") String sort) {
        Page<Food> foods = foodService.getFoodBySearch(areaId, sort, pageNum, pageSize,foodName,areaName);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(foods, "(foodId, foodName, foodPhoto,foodIntroduction,area(areaId,areaName))"));
    }
}
