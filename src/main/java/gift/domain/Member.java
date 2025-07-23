package gift.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WishList> wishLists = new ArrayList<>();

  protected Member() {}

  public Member(Long id, String email, String password, Role role) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public Member(String email, String password, Role role) {
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public Long getId() { return id; }
  public String getEmail() { return email; }
  public String getPassword() { return password; }
  public Role getRole() { return role; }
  public List<WishList> getWishLists() { return wishLists; }

  public void update(String email, String password, Role role) {
    this.email = email;
    this.password = password;
    this.role = role;
  }
}
