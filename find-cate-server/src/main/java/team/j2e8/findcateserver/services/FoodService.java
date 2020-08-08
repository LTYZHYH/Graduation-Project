package team.j2e8.findcateserver.services;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import team.j2e8.findcateserver.exceptions.BusinessException;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.*;
import team.j2e8.findcateserver.repositories.AreaRepository;
import team.j2e8.findcateserver.repositories.FoodRepository;
import team.j2e8.findcateserver.repositories.ShopRepository;
import team.j2e8.findcateserver.repositories.TypeRepository;
import team.j2e8.findcateserver.utils.EnsureDataUtil;
import team.j2e8.findcateserver.utils.HttpResponseDataUtil;
import team.j2e8.findcateserver.valueObjects.ErrorCode;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

@Service
public class FoodService {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;
    private QArea qArea = QArea.area;
    private QShop qShop = QShop.shop;
    private QFood qFood = QFood.food;

    public void addFood(String foodName,String foodPhoto,String foodIntroduction,String areaName){
        if (foodName == null || foodPhoto == null || foodIntroduction == null)
            throw new NullPointerException("菜名或菜图或介绍不可为空");
//        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qFood.area.areaName.eq(areaName));
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qArea.areaName.eq(areaName));
        Optional<Area> optionalArea = areaRepository.findOne(booleanBuilder);
        if (optionalArea.isPresent()){
            Food food = new Food();
            food.setFoodName(foodName);
            food.setFoodPhoto(foodPhoto);
            food.setFoodIntroduction(foodIntroduction);
            food.setArea(optionalArea.get());
            foodRepository.save(food);
        } else {
            throw new NullPointerException("输入的地区名有错!");
        }
    }

    public void unServeDish(int foodId, int shopId){
        if (foodId == 0 || shopId ==0) throw new NullPointerException("店铺或者菜不为空");
        User user = (User) identityContext.getIdentity();
        Optional<Shop> optionalShop = shopRepository.findOne(qShop.shopId.eq(shopId));
        Shop shop = null;
        Food food = null;
        if (optionalShop.isPresent()){
            shop = optionalShop.get();
        } else {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, ErrorMessage.NOT_FOUND);
        }
        if (shop.getUser().getId().intValue() != user.getId().intValue()){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS, ErrorMessage.ACCOUNT_NOT_EXIST);
        }
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if (optionalFood.isPresent()){
            food = optionalFood.get();
        } else {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, ErrorMessage.NOT_FOUND);
        }
        foodRepository.delete(food);
    }

    public Page<Food> getFoodsByShop(int shopId, String sort, int pageNum, int pageSize){
        if (shopId ==0) throw new NullPointerException("店铺或者类型不为空");
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qFood.shop.shopId.eq(shopId));
        return foodRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }
    //获取全部美食
    public Page<Food> getAllFood(String sort,int pageNum, int pageSize){
        return foodRepository.findAll(HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }
    //依据地区名字搜索美食
    public Page<Food> getFoodByAreaName(Integer areaId,String sort,int pageNum, int pageSize,String areaName){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qFood.area.areaName.like("%"+areaName+"%"));
        return foodRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort,pageNum,pageSize));
    }
    //搜索美食
    public Page<Food> getFoodBySearch(Integer areaId, String sort,int pageNum, int pageSize,String foodName, String areaName){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (foodName != null && areaName == null){
            booleanBuilder.and(qFood.foodName.like("%" + foodName + "%"));
        } else if (foodName == null && areaName != null){
            booleanBuilder.and(qFood.area.areaName.like("%" + areaName + "%"));
        } else if (foodName != null && areaName != null){
            booleanBuilder.and(qFood.foodName.like("%" + foodName + "%")).and(qFood.area.areaName.like("%"+areaName+"%"));
        }
        return foodRepository.findAll(booleanBuilder,HttpResponseDataUtil.sortAndPaging(sort,pageNum,pageSize));
    }
}
