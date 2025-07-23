package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(
    name = "product_option",
    uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "name"})
)
public class Option {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50, nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer quantity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  protected Option() {}

  public Option(String name, Integer quantity, Product product) {
    this.name = name;
    this.quantity = quantity;
    this.product = product;
  }

  public Long getId() { return id; }
  public String getName() { return name; }
  public Integer getQuantity() { return quantity; }
  public Product getProduct() { return product; }

  public void updateQuantity(Integer quantity) {
    this.quantity = quantity;
  }
}
