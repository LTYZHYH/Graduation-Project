package team.j2e8.findcateserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.j2e8.findcateserver.infrastructure.ObjectSelector;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.Shop;
import team.j2e8.findcateserver.services.FoodService;
import team.j2e8.findcateserver.services.ShopService;
import team.j2e8.findcateserver.services.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/shop")
public class ShopController {
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;
    @Autowired
    private UserService userService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private ShopService shopService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "getsbyuser")
    public ResponseEntity<?> getShopsByUser(@RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                            @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                            @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "shopId") String sort) {
        Page<Shop> shops = shopService.getAllShopByUser(sort, pageNum, pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(shops, "(shopId, shopName, shopTelenumber, shopAddress, shopPhoto, shopLng, shopLat, types(typeId, typeName))"));

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "getone")
    public ResponseEntity<?> getShopById(@RequestParam(required = true, defaultValue = "") Integer shopId,
                                         @RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                         @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                         @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "shopId") String sort) {
        Page<Shop> shops = shopService.getShopById(shopId, sort, pageNum, pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(shops, "(shopId, shopName, shopTelenumber, shopAddress, shopPhoto, shopLng, shopLat, types(typeId, typeName), user(id, userName, userTelenumber,userPhoto))"));

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/open")
    public ResponseEntity<?> openShop(@RequestParam(value = "file") MultipartFile file, Shop shop) throws Exception {
        //创建文件夹在服务器端的存放路径
       File pictureFile = new File("/Users/yhh/Desktop/picture");
        if (!pictureFile.exists()){
            pictureFile.mkdir();
        }
        //生成文件在服务器的存储名字
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileSuffix;
        File files = new File(pictureFile + "/" + fileName);
        //上传
        file.transferTo(files);
        String shopName = shop.getShopName();
        String shopAddr = shop.getShopAddress();
        String shopTelenumber = shop.getShopTelenumber();
        String shopPhoto = pictureFile + "/" + fileName;
        shopService.registerShop(shopName, shopAddr, shopTelenumber, shopPhoto, null,null);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/upPicture",method = RequestMethod.POST)
    public ResponseEntity<?> upCityPicture(MultipartFile file, HttpServletRequest request) throws Exception{
        //创建文件夹在服务器端的存放路径
//        String pictureUrl = request.getServletContext().getRealPath("/User/yhh/Desktop/picture");
        File pictureFile = new File("/Users/yhh/Desktop/picture");
        if (!pictureFile.exists()){
            pictureFile.mkdir();
        }
        //生成文件在服务器的存储名字
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileSuffix;
        File files = new File(pictureFile + "/" + fileName);
        //上传
        file.transferTo(files);

        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/unactive")
    public ResponseEntity<?> showUnactiveShop() throws Exception {
        Iterable<Shop> shops = shopService.getDisactiveShops();

        return ResponseEntity.ok(new ObjectSelector().mapObjects(shops,
                "(shopId, shopName, shopTelenumber, shopAddress," +
                        " shopPhoto, shopLng, shopLat, types(typeId, typeName))"));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/active")
    public ResponseEntity<?> activeShop(@RequestBody JsonNode jsonNode) throws Exception {
        int id = jsonNode.path("id").asInt();
        shopService.activeShop(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "search")
    public ResponseEntity<?> getShopById(@RequestParam(required = true) Double lng,
                            @RequestParam(required = true) Double lat,
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) String type,
                            @RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                            @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize) {
        Page<Shop> shops = shopService.getShopsByCondition(lng, lat, name, type, pageNum, pageSize);
        //return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(shops, "(shopId, shopName, shopTelenumber, shopAddress, shopPhoto, shopLng, shopLat, types(typeId, typeName))"));
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(shops,
                "(shopId, shopName, shopTelenumber, shopAddress, shopPhoto, shopLng, shopLat, types(typeId, typeName))"));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "random")
    public ResponseEntity<?> getShopByRandom(@RequestParam(required = false,defaultValue = "3") Integer number) {
        Iterable<Shop> shops = shopService.getShopByRandom(number);
        //return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(shops, "(shopId, shopName, shopTelenumber, shopAddress, shopPhoto, shopLng, shopLat, types(typeId, typeName))"));
        return ResponseEntity.ok(new ObjectSelector().mapObjects(shops,
                "(shopId, shopName, shopTelenumber, shopAddress, shopPhoto, shopLng, shopLat, types(typeId, typeName))"));
    }
}
