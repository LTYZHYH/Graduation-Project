package team.j2e8.findcateserver.services;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.*;
import team.j2e8.findcateserver.repositories.CommityRepository;
import team.j2e8.findcateserver.repositories.ReplyRepository;
import team.j2e8.findcateserver.repositories.TravelStrategyRepository;
import team.j2e8.findcateserver.utils.HttpResponseDataUtil;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    private TravelStrategyRepository travelStrategyRepository;
    @Autowired
    private CommityRepository commityRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;
    private QTravelStrategy qTravelStrategy = QTravelStrategy.travelStrategy;
    private QCommity qCommity = QCommity.commity;
    private QReply qReply = QReply.reply;

    //举报不正常攻略
    public void reportStrategy(Integer strategyId,String reportReason){
        Optional<TravelStrategy> travelStrategyOptional = travelStrategyRepository.findById(strategyId);
        if (travelStrategyOptional.isPresent()){
            TravelStrategy travelStrategy = travelStrategyOptional.get();
            travelStrategy.setIsReport(1);//'1'代表被举报
            travelStrategy.setReportReason(reportReason);
            travelStrategyRepository.save(travelStrategy);
        }
    }
    //举报不正常评论
    public void reportCommmit(Integer commitId){
        Optional<Commity> commityOptional = commityRepository.findById(commitId);
        if (commityOptional.isPresent()){
            Commity commity = commityOptional.get();
            commity.setIsReport(1);
            commityRepository.save(commity);
        }
    }
    //举报不正常回复
    public void reportReply(Integer replyId){
        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if (replyOptional.isPresent()){
            Reply reply = replyOptional.get();
            reply.setIsReport(1);
            replyRepository.save(reply);
        }
    }
    //获取所有被举报的攻略
    public Page<TravelStrategy> getAllReportStrategy(String sort, int pageNum, int pageSize){
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qTravelStrategy.isReport.eq(1));
        return travelStrategyRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }
    //获取所有被举报的评论
    public Page<Commity> getAllReportCommit(String sort,int pageNum,int pageSize){
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qCommity.isReport.eq(1));
        return commityRepository.findAll(booleanBuilder,HttpResponseDataUtil.sortAndPaging(sort,pageNum,pageSize));
    }
    //获取所有被举报的回复
    public Page<Reply> getAllReportReply(String sort,int pageNum,int pageSize){
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qReply.isReport.eq(1));
        return replyRepository.findAll(booleanBuilder,HttpResponseDataUtil.sortAndPaging(sort,pageNum,pageSize));
    }
}
