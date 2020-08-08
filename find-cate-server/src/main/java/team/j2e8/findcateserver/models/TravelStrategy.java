package team.j2e8.findcateserver.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/*
    单一省份地区旅游攻略
 */
@Entity
@Table(name = "uncorrelated_travel_strategy")
public class TravelStrategy {
    @Id
    @Column(name = "strategy_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer strategyId;
    @Column
    private String area;//地区
    @Column
    private String theme;//主题
    @Column
    private Integer travelDays;//几日游
    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(columnDefinition="TEXT",nullable=true)
    private String strategyContent;//攻略
    @Column
    private String overheadCost;//开销
    @Column(name = "scenic_number")
    private Integer scenicNumber;
    @Column(name = "strategy_audit")
    private Integer strategyAudit;//判断该攻略是否过审
    @Column(name = "is_report")
    private Integer isReport;//判断是否被举报
    @Column(name = "report_reason")
    private String reportReason;
    @Column
    @JsonProperty(value = "issueTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueTime;
    @Column
    private String strategyPicture1;
    @Column
    private String strategyPicture2;
    @Column
    private String strategyPicture3;
    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_user_id")
    private User user;
    @ManyToOne(targetEntity = City.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliation_city_id")//归属省份
    private City city;

    @OneToMany(targetEntity = Commity.class,fetch = FetchType.LAZY,
            mappedBy = "travelStrategy")
    private Set<Commity> commity= new HashSet<>();

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Integer getTravelDays() {
        return travelDays;
    }

    public void setTravelDays(Integer travelDays) {
        this.travelDays = travelDays;
    }

    public String getStrategyContent() {
        return strategyContent;
    }

    public void setStrategyContent(String strategyContent) {
        this.strategyContent = strategyContent;
    }

    public String getOverheadCost() {
        return overheadCost;
    }

    public void setOverheadCost(String overheadCost) {
        this.overheadCost = overheadCost;
    }

    public Integer getScenicNumber() {
        return scenicNumber;
    }

    public void setScenicNumber(Integer scenicNumber) {
        this.scenicNumber = scenicNumber;
    }

    public Integer getStrategyAudit() {
        return strategyAudit;
    }

    public void setStrategyAudit(Integer strategyAudit) {
        this.strategyAudit = strategyAudit;
    }

    public Integer getIsReport() {
        return isReport;
    }

    public void setIsReport(Integer isReport) {
        this.isReport = isReport;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public String getStrategyPicture1() {
        return strategyPicture1;
    }

    public void setStrategyPicture1(String strategyPicture1) {
        this.strategyPicture1 = strategyPicture1;
    }

    public String getStrategyPicture2() {
        return strategyPicture2;
    }

    public void setStrategyPicture2(String strategyPicture2) {
        this.strategyPicture2 = strategyPicture2;
    }

    public String getStrategyPicture3() {
        return strategyPicture3;
    }

    public void setStrategyPicture3(String strategyPicture3) {
        this.strategyPicture3 = strategyPicture3;
    }

    public Set<Commity> getCommity() {
        return commity;
    }

    public void setCommity(Set<Commity> commity) {
        this.commity = commity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<Commity> getCommities() {
        return commity;
    }

    public void setCommities(Set<Commity> commities) {
        this.commity = commities;
    }
}
