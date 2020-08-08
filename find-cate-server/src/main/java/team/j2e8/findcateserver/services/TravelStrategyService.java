package team.j2e8.findcateserver.services;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import team.j2e8.findcateserver.exceptions.BusinessException;
import team.j2e8.findcateserver.exceptions.ResourceNotFoundException;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.*;
import team.j2e8.findcateserver.repositories.AdminRepository;
import team.j2e8.findcateserver.repositories.TravelStrategyRepository;
import team.j2e8.findcateserver.repositories.UserRepository;
import team.j2e8.findcateserver.utils.HttpResponseDataUtil;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static team.j2e8.findcateserver.valueObjects.ErrorCode.INVALID_TOKEN;

@Service
public class TravelStrategyService {

    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TravelStrategyRepository travelStrategyRepository;

    private QUser qUser = QUser.user;
    private QCity qCity = QCity.city;
    private QAdmin qAdmin = QAdmin.admin;
    private QTravelStrategy qTravelStrategy = QTravelStrategy.travelStrategy;

    //将token转为完整的user对象
    private User TokenToUser() {
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

    public void issueTravelStrategy(String theme, String area, Integer travelDays, String overheadCost,
                                    String strategyContent, String issueTime1, Integer scenicNumber,String strategyPicture1, String strategyPicture2,
                                    String strategyPicture3, City city) {
        TravelStrategy travelStrategy = new TravelStrategy();
        travelStrategy.setArea(area);
        travelStrategy.setTheme(theme);
        travelStrategy.setTravelDays(travelDays);
        travelStrategy.setOverheadCost(overheadCost);
        travelStrategy.setStrategyContent(strategyContent);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sf.parse(issueTime1);
            travelStrategy.setIssueTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        travelStrategy.setScenicNumber(scenicNumber);
        travelStrategy.setStrategyAudit(0);//传入0表示默认未审核状态
        travelStrategy.setIsReport(0);//默认是没有被举报的攻略
        if (!strategyPicture1.equals("null")){
            travelStrategy.setStrategyPicture1(strategyPicture1);
        }
        if (!strategyPicture2.equals("null")){
            travelStrategy.setStrategyPicture2(strategyPicture2);
        }
        if (!strategyPicture3.equals("null")){
            travelStrategy.setStrategyPicture3(strategyPicture3);
        }
        travelStrategy.setUser(TokenToUser());
        travelStrategy.setCity(city);
        travelStrategyRepository.save(travelStrategy);
    }

    //根据省份获取攻略
    public Page<TravelStrategy> getPageTravelStrategyByCityId(Integer cityId, String sort, int pageNum, int pageSize) {
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qTravelStrategy.city.cityId.eq(cityId)).and(qTravelStrategy.strategyAudit.eq(2));
        return travelStrategyRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }

    //在某一省份下模糊查询攻略
    public Page<TravelStrategy> getPageTravelStrategyByLike(Integer cityId, String like, String sort, int pageNum, int pageSize) {
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qTravelStrategy.city.cityId.eq(cityId))
                                                            .and(qTravelStrategy.theme.like("%" + like + "%"));
        return travelStrategyRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }
    //获取未审核的攻略
    public Iterable<TravelStrategy> getDisactiveStrategy(){
        User user = TokenToUser();
        if(!judgeAdmin(user)){
            throw new ResourceNotFoundException(ErrorMessage.ACCOUNT_NOT_EXIST);
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qTravelStrategy.strategyAudit.eq(0));
        Iterable<TravelStrategy> travelStrategies = travelStrategyRepository.findAll(booleanBuilder);
        return travelStrategies;
    }

    //审核攻略
    public void auditStrategy(Integer strategyId,String auditResult){
        User user = TokenToUser();
        if(!judgeAdmin(user)){
            throw new ResourceNotFoundException(ErrorMessage.ACCOUNT_NOT_EXIST);
        }
        Optional<TravelStrategy> strategyOptional = travelStrategyRepository.findById(strategyId);
        TravelStrategy travelStrategy = strategyOptional.get();
        if (auditResult.equals("pass")){
            travelStrategy.setStrategyAudit(2);//通过审核
        } else {
            travelStrategy.setStrategyAudit(1);//不通过审核
        }
        travelStrategyRepository.save(travelStrategy);
    }
    //获取攻略全文
    public Optional<TravelStrategy> getStrategyDetail(Integer strategyId){
        return travelStrategyRepository.findById(strategyId);
    }
    //根据用户id获取他的发布
    public Page<TravelStrategy> getStrategyByUserId(String sort, int pageNum, int pageSize){
        User user = (User) identityContext.getIdentity();
        if (user == null){
            throw new BusinessException(INVALID_TOKEN, ErrorMessage.INVALID_TOKEN);
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qTravelStrategy.user.id.eq(user.getId()));
        return travelStrategyRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }
    //获取全部攻略
    public Page<TravelStrategy> getAllStrategy(String sort, int pageNum, int pageSize){
        User user = (User) identityContext.getIdentity();
        if (user == null){
            throw new BusinessException(INVALID_TOKEN, ErrorMessage.INVALID_TOKEN);
        }
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.get().getAdmin() == null){
            throw new BusinessException(INVALID_TOKEN, ErrorMessage.INVALID_TOKEN);
        } else {
            return travelStrategyRepository.findAll(HttpResponseDataUtil.sortAndPaging(sort,pageNum,pageSize));
        }
    }
    //保留攻略
    public void retainStrategy(Integer strategyId){
        Optional<TravelStrategy> strategyOptional = travelStrategyRepository.findById(strategyId);
        TravelStrategy travelStrategy = strategyOptional.get();
        travelStrategy.setIsReport(0);
        travelStrategy.setReportReason("无");
        travelStrategyRepository.save(travelStrategy);
    }
    //屏蔽攻略
    public void blockStrategy(Integer strategyId){
        Optional<TravelStrategy> strategyOptional = travelStrategyRepository.findById(strategyId);
        TravelStrategy travelStrategy = strategyOptional.get();
        travelStrategy.setStrategyAudit(1);
        travelStrategy.setIsReport(0);
        travelStrategyRepository.save(travelStrategy);
    }
}
