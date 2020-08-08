package team.j2e8.findcateserver.services;

import com.google.common.collect.Iterables;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.*;
import team.j2e8.findcateserver.repositories.*;
import team.j2e8.findcateserver.utils.HttpResponseDataUtil;
import team.j2e8.findcateserver.exceptions.ResourceNotFoundException;
import team.j2e8.findcateserver.utils.Encryption;
import team.j2e8.findcateserver.utils.EnsureDataUtil;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;

import javax.annotation.Resource;
import java.util.*;
import javax.annotation.Resource;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private UserRepository userRepository;
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;

    private QShop qShop = QShop.shop;
    private QType qType = QType.type;
    private QFood qFood = QFood.food;
    private QAdmin qAdmin = QAdmin.admin;
    private QUser  qUser = QUser.user;

    public Page<Shop> getAllShopByUser( String sort, int pageNum, int pageSize){
        User user = (User) identityContext.getIdentity();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qShop.user.id.eq(user.getId()));
        return shopRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }

    public Page<Shop> getShopById(Integer shopId, String sort, int pageNum, int pageSize){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qShop.shopId.eq(shopId));
        return shopRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }
    //将token转为完整的user对象
    private User TokenToUser(){
        User user = (User) identityContext.getIdentity();
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qUser.id.eq(user.getId()));
        Optional<User> user1 = userRepository.findOne(booleanBuilder);
        return user1.get();
    }
    private Boolean judgeAdmin(User user){
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qAdmin.adminId.eq(user.getId()));
        //查询
        Optional<Admin> optionalAdmin = adminRepository.findOne(booleanBuilder);
        return optionalAdmin.isPresent();
    }

    //判断密码是否正确
    private Boolean judgePassword(String password)throws Exception{
        User user = TokenToUser();
        Encryption encryption = new Encryption();
        String pass = encryption.getPassword(password.trim(), user.getUserSalt());
        return user.getUserPassword().equals(pass);
    }
    //暂时删掉参数中的"String password,"
    public void registerShop(String shopName,String shopAddr,String shopTelenumber,String shopPhoto,
                             Double shopLng,Double shopLat) throws Exception{
//        EnsureDataUtil.ensureNotEmptyData(password, ErrorMessage.EMPTY_PASSWORD.getMessage());
//        if(!judgePassword(password)){
//            throw new ResourceNotFoundException(ErrorMessage.ERROR_LOGIN__NAME_OR_PASSWORD);
//        }

        Shop shop = new Shop();
        shop.setShopTelenumber(shopTelenumber);
        shop.setShopName(shopName);
        shop.setShopAddress(shopAddr);
        shop.setShopPhoto(shopPhoto);
        shop.setUser(TokenToUser());
        shop.setShopActive(0);
        shop.setShopLng(shopLng);
        shop.setShopLat(shopLat);
        shopRepository.save(shop);
    }


    public Iterable<Shop> getDisactiveShops(){
        User user = TokenToUser();
        if(!judgeAdmin(user)){
            throw new ResourceNotFoundException(ErrorMessage.ACCOUNT_NOT_EXIST);
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qShop.shopActive.eq(0));
        Iterable<Shop> shops = shopRepository.findAll(booleanBuilder);
//        Iterable<Shop> shops = shopRepository.findAll(booleanBuilder,PageRequest.of(0, 1));
//        List<Shop> lShops = new ArrayList<>();
//        shops.forEach(single->{lShops.add(single);});
        return shops;
    }

    public void activeShop(int id){
        User user = TokenToUser();
        if(!judgeAdmin(user)){
            throw new ResourceNotFoundException(ErrorMessage.ACCOUNT_NOT_EXIST);
        }
        Optional<Shop> shops = shopRepository.findById(id);
        Shop shop = shops.get();
        shop.setShopActive(1);
        shopRepository.save(shop);
    }

    public Page<Shop> getShopsByCondition(Double lng, Double lat, String name, String type, int pageNum, int pageSize){
        if (lng == null || lat == null) throw  new NullPointerException("经纬度不为空");
        pageNum = pageNum * pageSize;
//        JPAQueryFactory queryFactory = new JPAQueryFactory();
//        BooleanBuilder booleanBuilder = new BooleanBuilder()
//                .and(qShop.shopActive.eq(1))
//                .and(qShop.shopLng.between(lng, lng+1))
//                .and(qShop.shopLng.between(lng-1, lng))
//                .and(qShop.shopLat.between(lat, lat+1))
//                .and(qShop.shopLat.between(lat-1, lat));
//        if (name != null) booleanBuilder.and(qShop.shopName.like("%"+name+"%"));
//        if (type != null) booleanBuilder.and(qSho)  //类型

//        return shopRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPagingByLatAndLng(lng, lat, pageNum, pageSize));
        List<Shop> shops;
        Integer shopsSize;
        if (name == null){
            shops=shopRepository.findByLngAndLat(lng, lat,pageNum,pageSize);
            shopsSize=shopRepository.findByLngAndLatTest(lng, lat);
        }
        else {
            shops=shopRepository.findByLngAndLatAndShopName(lng, lat,pageNum,pageSize, name);
            shopsSize=shopRepository.findByLngAndLatAndShopNameTest(lng, lat, name);
        }
        return listToPage(shops, pageNum, pageSize,shopsSize);
    }

    public Iterable<Shop> getShopByRandom(Integer number){
        Iterable<Shop> shops = shopRepository.findAll(new BooleanBuilder().and(qShop.shopActive.eq(1)));
        if (Iterables.size(shops)<number){
            return null;
        }
        List<Shop> shopList = new ArrayList<>();
        Random random =new Random(new Date().getTime());
        Set<Integer> randoms = new HashSet<>();
        while (randoms.size()<number){
            randoms.add(random.nextInt(Iterables.size(shops)));
        }


        for (Integer n:randoms){
            Iterator<Shop> it = shops.iterator();
            int j = 0;
            while (it.hasNext()) {
                if (j==n){
                    shopList.add(it.next());
                    break;
                } else {
                    it.next();
                }

                j++;
            }
        }

        return shopList;
    }

//    public Iterable<Shop> getCityPicture(String photo){
//        Iterable<Shop> city = shopRepository.findAll(photo);
//        return city;
//    }

    private Page<Shop> listToPage(List<Shop> shops, int pageNum, int pageSize, int totalSize){
        return new PageImpl<Shop>(shops,PageRequest.of(pageNum, pageSize),totalSize);
    }
}
