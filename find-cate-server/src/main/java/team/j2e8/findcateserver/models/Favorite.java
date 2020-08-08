package team.j2e8.findcateserver.models;

import javax.persistence.*;

@Entity
@Table(name = "Favorite")
public class Favorite {
    @Id
    @Column(name = "favorite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer favoriteId;
    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_user_id")
    private User user;
    @ManyToOne(targetEntity = TravelStrategy.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_travelStrategy_id")
    private TravelStrategy travelStrategy;

    public Integer getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Integer favoriteId) {
        this.favoriteId = favoriteId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TravelStrategy getTravelStrategy() {
        return travelStrategy;
    }

    public void setTravelStrategy(TravelStrategy travelStrategy) {
        this.travelStrategy = travelStrategy;
    }
}
