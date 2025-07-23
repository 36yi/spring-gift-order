package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "wish_list")
public class WishList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_wishlist_member"))
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_wishlist_product"))
  private Product product;

  @Column(nullable = false)
  private Integer quantity = 1;

  protected WishList() {}

  public WishList(Long id, Member member, Product product, Integer quantity) {
    this.id = id;
    this.member = member;
    this.product = product;
    this.quantity = quantity;
  }

  public WishList(Member member, Product product, Integer quantity) {
    this.member = member;
    this.product = product;
    this.quantity = quantity;
  }

  public Long getId() { return id; }
  public Member getMember() { return member; }
  public Product getProduct() { return product; }
  public Integer getQuantity() { return quantity; }

  public void updateQuantity(Integer quantity) {
    this.quantity = quantity;
  }
}
