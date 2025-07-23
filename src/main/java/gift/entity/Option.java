package gift.entity;

import gift.exception.CantSubtractException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Option {

  @Id
  @GeneratedValue
  Long id;

  String name;

  int quantity;

  @ManyToOne
  @JoinColumn(name = "product_id")
  Product product;

  public Option() {
  }

  public Option(Long id, String name, int quantity, Product product) {
    if (quantity < 0 || quantity > 100000000) {
      throw new IllegalStateException("수량은 1개이상 1억개 미만입니다.");
    }
    this.id = id;
    this.name = name;
    this.quantity = quantity;
    this.product = product;
  }

  public Option(String name, int quantity, Product product) {
    this(null, name, quantity, product);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
  }

  public Product getProduct() {
    return product;
  }

  protected void setProduct(Product product) {
    this.product = product;
  }

  public int subtractQuantity(int quantity) {
    if (this.quantity < quantity) {
      throw new CantSubtractException("옵션 수량보다 더 큰 수량은 불가능합니다.");
    }
    return this.quantity -= quantity;
  }
}
