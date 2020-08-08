package team.j2e8.findcateserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "type")
public class Type {

    @Id//主键
    @Column(name = "type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer typeId;

    @Column
    private String typeName;

    @ManyToMany(targetEntity = Shop.class,fetch = FetchType.LAZY)
    @JoinTable(name = "type_shop", joinColumns = @JoinColumn(name = "type_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_type_id"))
    private Set<Shop> shops= new HashSet<Shop>();


    @OneToMany(targetEntity = Food.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "type")
    private Set<Food> foods= new HashSet<Food>();


    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<Shop> getShops() {
        return shops;
    }

    public void setShops(Set<Shop> shops) {
        this.shops = shops;
    }

    public Set<Food> getFoods() {
        return foods;
    }

    public void setFoods(Set<Food> foods) {
        this.foods = foods;
    }

}
