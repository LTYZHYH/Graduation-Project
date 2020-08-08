package team.j2e8.findcateserver.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commity")
public class Commity {
    @Id//主键
    @Column(name = "commity_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer commityId;

    @Column
    private String commityContent;
    @Column
    @JsonProperty(value = "commity_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date commityTime;
    @Column
    private Integer commityLike;
    @Column
    private Integer commityDislike;
    @Column(name = "is_report")
    private Integer isReport;//判断是否被举报
    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "commity_user_id")
    private User user;

    @ManyToOne(targetEntity = Shop.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "commity_shop_id")
    private Shop shop;
    @ManyToOne(targetEntity = TravelStrategy.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "commity_uncorrelated_travel_strategy_id")
    private TravelStrategy travelStrategy;

    @OneToMany(targetEntity = Reply.class,fetch = FetchType.LAZY,mappedBy = "commity",cascade = CascadeType.REMOVE)
    private List<Reply> replyList = new ArrayList<>();
    public Integer getCommityId() {
        return commityId;
    }

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

    public void setCommityId(Integer commityId) {
        this.commityId = commityId;
    }

    public String getCommityContent() {
        return commityContent;
    }

    public void setCommityContent(String commityContent) {
        this.commityContent = commityContent;
    }

    public Date getCommityTime() {
        return commityTime;
    }

    public void setCommityTime(Date commityTime) {
        this.commityTime = commityTime;
    }
    //__________
    public Integer getCommityLike() {
        return commityLike;
    }

    public void setCommityLike(Integer commityLike) {
        this.commityLike = commityLike;
    }

    public Integer getCommityDislike() {
        return commityDislike;
    }

    public void setCommityDislike(Integer commityDislike) {
        this.commityDislike = commityDislike;
    }

    public Integer getIsReport() {
        return isReport;
    }

    public void setIsReport(Integer isReport) {
        this.isReport = isReport;
    }

    //__________
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
//__________
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
//__________

    public TravelStrategy getTravelStrategy() {
        return travelStrategy;
    }

    public void setTravelStrategy(TravelStrategy travelStrategy) {
        this.travelStrategy = travelStrategy;
    }
}
