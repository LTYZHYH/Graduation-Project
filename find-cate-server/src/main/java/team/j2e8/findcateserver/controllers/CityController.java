package team.j2e8.findcateserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.j2e8.findcateserver.infrastructure.ObjectSelector;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.City;
import team.j2e8.findcateserver.models.Shop;
import team.j2e8.findcateserver.services.CityService;

import javax.annotation.Resource;
import java.io.File;
import java.util.UUID;

@Controller
@RequestMapping(value = "/city")
public class CityController {
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;
    @Autowired
    private CityService cityService;

    //添加首页城市图片
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "addCity")
    public ResponseEntity<?> addCity(@RequestParam(value = "file") MultipartFile file, City city) throws Exception{
        File pictureFile = new File("/Users/yhh/Desktop/毕业设计/Find-Cate-master/find-cate-server/src/main/resources/static/indexPicture");
        if (!pictureFile.exists()){
            pictureFile.mkdir();
        }
        //生成文件在服务器的存储名字
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileSuffix;
        File files = new File(pictureFile + "/" + fileName);
        //上传
        file.transferTo(files);
        String cityTitle = city.getCityTitle();
        String cityPicture = "/indexPicture/" + fileName;

        cityService.addCity(cityTitle,cityPicture);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "initIndex")
    public ResponseEntity<?> getCity(){
        Iterable<City> city = cityService.getCityPicture();
        return ResponseEntity.ok(new ObjectSelector().mapObjects(city,"(cityId,cityTitle,cityPicture)"));
    }
}
