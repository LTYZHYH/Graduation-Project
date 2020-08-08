package team.j2e8.findcateserver.services;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import team.j2e8.findcateserver.exceptions.BusinessException;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.*;
import team.j2e8.findcateserver.repositories.CommityRepository;
import team.j2e8.findcateserver.repositories.ReplyRepository;
import team.j2e8.findcateserver.repositories.UserRepository;
import team.j2e8.findcateserver.utils.EnsureDataUtil;
import team.j2e8.findcateserver.utils.GetNowTimeUtil;
import team.j2e8.findcateserver.utils.HttpResponseDataUtil;
import team.j2e8.findcateserver.valueObjects.ErrorCode;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * @auther Heyanhu
 * @date 2018/12/11 21:40
 */
@Service
public class ReplyService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommityRepository commityRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;

    private QUser qUser = QUser.user;
    private QCommity qCommity = QCommity.commity;
    private QReply qReply = QReply.reply;
    private GetNowTimeUtil getNowTimeUtil = new GetNowTimeUtil();

    public void insertReply(int commityId,String replyContent){
        EnsureDataUtil.ensureNotEmptyData(replyContent, ErrorMessage.EMPTY_LOGIN_NAME.getMessage());
        User user = (User) identityContext.getIdentity();
        BooleanBuilder booleanBuilder1 = new BooleanBuilder().and(qCommity.commityId.eq(commityId));
        //查询
        Optional<Commity> optionalCommity = commityRepository.findOne(booleanBuilder1);
        Reply reply = new Reply();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sf.parse(getNowTimeUtil.refFormatNowDate());
            reply.setReplyTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reply.setUser(user);
        reply.setCommity(optionalCommity.get());
        reply.setReplyContent(replyContent);
        reply.setIsReport(0);
        replyRepository.save(reply);
    }

    //通过评论id获取回复
    public Page<Reply> getReplyByCommityId(Integer commityId, String sort, int pageNum, int pageSize){
        if (commityId == null) throw new NullPointerException("需要commityId");
        Optional<Commity> optionalShop = commityRepository.findOne(qCommity.commityId.eq(commityId));
        if (!optionalShop.isPresent()) throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, ErrorMessage.NOT_FOUND);
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qCommity.commityId.eq(commityId));

        return replyRepository.findAll(booleanBuilder, HttpResponseDataUtil.sortAndPaging(sort, pageNum, pageSize));
    }
    public void deleteReply(Integer replyId){
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qReply.replyId.eq(replyId));
        Optional<Reply> optionalReply = replyRepository.findOne(booleanBuilder);
        if (optionalReply.isPresent()){
            replyRepository.delete(optionalReply.get());
        }
    }
}
