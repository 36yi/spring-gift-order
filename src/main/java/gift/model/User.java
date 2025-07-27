package gift.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userid;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    protected User() {
    }
    public User(String userid, String password, String role) {
        this.userid = userid;
        this.password = password;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getUserid() { return userid; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setId(Long id) { this.id = id; }
    public void setUserid(String userid) { this.userid = userid; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}