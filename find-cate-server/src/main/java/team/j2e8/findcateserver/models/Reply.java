package team.j2e8.findcateserver.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reply")
public class Reply {
    @Id//主键
    @Column(name = "reply_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer replyId;
    @Column
    private String replyContent;
    @Column
    private Date replyTime;
    @Column(name = "is_report")
    private Integer isReport;//判断是否被举报
    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_user_id")
    private User user;

    @ManyToOne(targetEntity = Commity.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_commity_id")
    private Commity commity;

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Integer getIsReport() {
        return isReport;
    }

    public void setIsReport(Integer isReport) {
        this.isReport = isReport;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Commity getCommity() {
        return commity;
    }

    public void setCommity(Commity commity) {
        this.commity = commity;
    }
}
