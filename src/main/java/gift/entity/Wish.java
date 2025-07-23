package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Wish {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "PRODUCT_ID")
  private Product product;

  private Long quantity;

  public Wish() {

  }

  public Wish(Long id, Member member, Product product, Long quantity) {
    this.id = id;
    this.member = member;
    this.product = product;
    this.quantity = quantity;
  }

  public Wish(Member member, Product product, Long quantity) {
    this(null, member, product, quantity);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public Member getMember() {
    return member;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void updateQuantity(Long quantity) {
    this.quantity = quantity;
  }
}
