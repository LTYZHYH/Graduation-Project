package team.j2e8.findcateserver.models;

import javax.persistence.*;

@Entity
@Table(name = "admin")
public class Admin {


    @Id//主键
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer adminId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="admin_id")
    private User user;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
