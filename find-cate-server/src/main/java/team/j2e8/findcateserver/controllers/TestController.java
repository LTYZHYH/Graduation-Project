package team.j2e8.findcateserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import team.j2e8.findcateserver.models.QShop;
import team.j2e8.findcateserver.models.QUser;
import team.j2e8.findcateserver.models.Shop;
import team.j2e8.findcateserver.models.User;
import team.j2e8.findcateserver.repositories.ShopRepository;
import team.j2e8.findcateserver.repositories.UserRepository;
import team.j2e8.findcateserver.utils.HttpResponseDataUtil;

import java.util.Optional;

@Controller
@ResponseBody
public class TestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    private final QUser qUser = QUser.user;

    private final QShop qShop = QShop.shop;
    @RequestMapping(method = RequestMethod.GET, value = "/test")
    public void test() {
//        User user = new User();
//        user.setId(8);
//        user.setUserEmail("adsss@qq.com");
//        user.setUserName("zwssss");
//        user.setUserTelenumber("31231");
//        user.setUserPhoto("sadaasasd");
        Optional<User> user = userRepository.findOne(new BooleanBuilder().and(qUser.id.eq(8)));
        Shop shop = new Shop();
        shop.setShopAddress("sadsad");
        shop.setUser(user.get());
        shopRepository.save(shop);
    }
}
