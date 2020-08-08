package team.j2e8.findcateserver.services;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import team.j2e8.findcateserver.exceptions.BusinessException;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.*;
import team.j2e8.findcateserver.repositories.CommityRepository;
import team.j2e8.findcateserver.repositories.ShopRepository;
import team.j2e8.findcateserver.repositories.TravelStrategyRepository;
import team.j2e8.findcateserver.repositories.UserRepository;
import team.j2e8.findcateserver.utils.EnsureDataUtil;
import team.j2e8.findcateserver.utils.GetNowTimeUtil;
import team.j2e8.findcateserver.utils.HttpResponseDataUtil;
import team.j2e8.findcateserver.valueObjects.ErrorCode;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static team.j2e8.findcateserver.valueObjects.ErrorCode.INVALID_TOKEN;

/**
 * @auther Heyanhu
 * @date 2018/12/11 18:15
 */
@Service
public class CommityService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private TravelStrategyRepository travelStrategyRepository;
    @Autowired
    private CommityRepository commityRepository;
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;
    private QTravelStrategy qTravelStrategy = QTravelStrategy.travelStrategy;
    private QShop qShop = QShop.shop;
    private QCommity qCommity = QCommity.commity;
    private GetNowTimeUtil getNowTimeUtil = new GetNowTimeUtil();

    public void insertComments (int strategyId,String comments)throws Exception{
        EnsureDataUtil.ensureNotEmptyData(comments, ErrorMessage.EMPTY_LOGIN_NAME.getMessage());
        User user = (User) identityContext.getIdentity();
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qTravelStrategy.strategyId.eq(strategyId));
        //查询
        Optional<TravelStrategy> optionalTravelStrategy = travelStrategyRepository.findOne(booleanBuilder);
        Commity commity = new Commity();
        commity.setCommityContent(comments.trim());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sf.parse(getNowTimeUtil.refFormatNowDate());
            commity.setCommityTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        commity.setUser(user);
        commity.setTravelStrategy(optionalTravelStrategy.get());
        commity.setIsReport(0);
        commityRepository.save(commity);
    }

    public Page<Commity> getCommentsByTravelStrategyId(Integer travelStrategyId,String sort, int pageNum, int pageSize){
        if (travelStrategyId == null) throw new NullPointerException("需要TravelStrategyId");
        Optional<TravelStrategy> optionalTravelStrategy = travelStrategyRepository.findOne(qTravelStrategy.strategyId.eq(travelStrategyId));
        if (!optionalTravelStrategy.isPresent()) throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,ErrorMessage.NOT_FOUND);
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qCommity.travelStrategy.strategyId.eq(travelStrategyId));
        return commityRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }

    public Optional<Commity> getComment(Integer commentId){
        return commityRepository.findById(commentId);
    }

    public void deleteCommity(Integer commityId){
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qCommity.commityId.eq(commityId));
        Optional<Commity> optionalCommity = commityRepository.findOne(booleanBuilder);
        if (optionalCommity.isPresent()){
            commityRepository.delete(optionalCommity.get());
        }
    }
}
