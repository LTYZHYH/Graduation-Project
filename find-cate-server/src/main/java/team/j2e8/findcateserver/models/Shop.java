package team.j2e8.findcateserver.models;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shop")
public class Shop {

    @Id//主键
    @Column(name = "shop_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer shopId;

    @Column
    private String shopName;
    @Column
    private String shopTelenumber;
    @Column
    private String shopAddress;
    @Column
    private String shopPhoto;


    @Column
    private Integer shopLike;
    @Column
    private Integer shopDislike;
    @Column
    private Integer shopActive;  //判断shop是否通过审核
    @Column
    private Double shopLng;
    @Column
    private Double shopLat;


    @ManyToMany(targetEntity = Type.class,fetch = FetchType.LAZY)
    @JoinTable(name = "type_shop", joinColumns = @JoinColumn(name = "shop_type_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    private Set<Type> types= new HashSet<Type>();


    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_shopper_id")
    private User user;

    @OneToMany(targetEntity = Food.class, fetch = FetchType.LAZY,  mappedBy = "shop")
    private Set<Food> foods= new HashSet<>();


    @OneToMany(targetEntity = Commity.class,fetch = FetchType.LAZY,
            mappedBy = "shop")
    private Set<Commity> commities= new HashSet<>();

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopTelenumber() {
        return shopTelenumber;
    }

    public void setShopTelenumber(String shopTelenumber) {
        this.shopTelenumber = shopTelenumber;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopPhoto() {
        return shopPhoto;
    }

    public void setShopPhoto(String shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    public Integer getShopLike() {
        return shopLike;
    }

    public void setShopLike(Integer shopLike) {
        this.shopLike = shopLike;
    }

    public Integer getShopDislike() {
        return shopDislike;
    }

    public void setShopDislike(Integer shopDislike) {
        this.shopDislike = shopDislike;
    }

    public Integer getShopActive() {
        return shopActive;
    }

    public void setShopActive(Integer shopActive) {
        this.shopActive = shopActive;
    }

    public Double getShopLng() {
        return shopLng;
    }

    public void setShopLng(Double shopLng) {
        this.shopLng = shopLng;
    }

    public Double getShopLat() {
        return shopLat;
    }

    public void setShopLat(Double shopLat) {
        this.shopLat = shopLat;
    }

    public Set<Type> getTypes() {
        return types;
    }

    public void setTypes(Set<Type> types) {
        this.types = types;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Food> getFoods() {
        return foods;
    }

    public void setFoods(Set<Food> foods) {
        this.foods = foods;
    }

    public Set<Commity> getCommities() {
        return commities;
    }

    public void setCommities(Set<Commity> commities) {
        this.commities = commities;
    }
}
