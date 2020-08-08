package team.j2e8.findcateserver.models;

import javax.persistence.*;

//省份（当初起名字的时候没有想好。。。所以写成了city）
@Entity
@Table(name = "city")
public class City {
    @Id
    @Column(name = "city_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer cityId;
    @Column
    private String cityTitle;
    @Column
    private String cityPicture;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getCityPicture() {
        return cityPicture;
    }

    public void setCityPicture(String cityPicture) {
        this.cityPicture = cityPicture;
    }
}
