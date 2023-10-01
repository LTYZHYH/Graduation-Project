package team.j2e8.findcateserver.services;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import team.j2e8.findcateserver.exceptions.BusinessException;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.*;
import team.j2e8.findcateserver.repositories.FavoriteRepository;
import team.j2e8.findcateserver.repositories.TravelStrategyRepository;
import team.j2e8.findcateserver.repositories.UserRepository;
import team.j2e8.findcateserver.utils.HttpResponseDataUtil;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;

import javax.annotation.Resource;

import java.util.Optional;

import static team.j2e8.findcateserver.valueObjects.ErrorCode.INVALID_TOKEN;

@Service
public class FavoriteService {
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TravelStrategyRepository travelStrategyRepository;

    private QFavorite qFavorite = QFavorite.favorite;
    private QUser qUser = QUser.user;
    private QTravelStrategy qTravelStrategy = QTravelStrategy.travelStrategy;

    public Page<Favorite> getMyFavoriteStrategy(String sort,int pageNum,int pageSize){
        User user = (User) identityContext.getIdentity();
        if (user == null){
            throw new BusinessException(INVALID_TOKEN, ErrorMessage.INVALID_TOKEN);
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qFavorite.user.id.eq(user.getId()));
        return favoriteRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }

    public int addFavorite(Integer strategyId){
        int key;
        User user = (User) identityContext.getIdentity();
        if (user == null){
            throw new BusinessException(INVALID_TOKEN, ErrorMessage.INVALID_TOKEN);
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qTravelStrategy.strategyId.eq(strategyId));
        Optional<TravelStrategy> optionalTravelStrategy = travelStrategyRepository.findOne(booleanBuilder);

        BooleanBuilder booleanBuilder2 = new BooleanBuilder().and(qTravelStrategy.strategyId.eq(strategyId)).and(qUser.id.eq(user.getId()));
        Optional<Favorite> optionalFavorite = favoriteRepository.findOne(booleanBuilder2);
        //判断是否已经收藏过，防止重复收藏
        if (optionalFavorite.isPresent()){
            key = 2;
            return key;
        } else {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            TravelStrategy travelStrategy = optionalTravelStrategy.get();
            int favoriteNum = travelStrategy.getFavoriteNum() + 1;
            if (favoriteNum >= 0){
                travelStrategy.setFavoriteNum(favoriteNum);
                travelStrategyRepository.save(travelStrategy);
            }
            favorite.setTravelStrategy(optionalTravelStrategy.get());
            favoriteRepository.save(favorite);
            key = 1;
            return key;
        }
    }
    //取消收藏
    public void cancelFavoriteAgain(Integer strategyId){
        User user = (User) identityContext.getIdentity();
        if (user == null){
            throw new BusinessException(INVALID_TOKEN, ErrorMessage.INVALID_TOKEN);
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qUser.id.eq(user.getId())).and(qFavorite.travelStrategy.strategyId.eq(strategyId));
        Optional<Favorite> optionalFavorite = favoriteRepository.findOne(booleanBuilder);
        if (optionalFavorite.isPresent()){
            favoriteRepository.delete(optionalFavorite.get());
        }
        BooleanBuilder booleanBuilder2 = new BooleanBuilder().and(qTravelStrategy.strategyId.eq(strategyId));
        Optional<TravelStrategy> optionalTravelStrategy = travelStrategyRepository.findOne(booleanBuilder2);
        TravelStrategy travelStrategy = optionalTravelStrategy.get();
        int favoriteNum = travelStrategy.getFavoriteNum() - 1;
        if (favoriteNum >= 0){
            travelStrategy.setFavoriteNum(favoriteNum);
            travelStrategyRepository.save(travelStrategy);
        }
    }
}
