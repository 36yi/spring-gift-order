package gift.option.entity;

import gift.product.entity.Product;
import jakarta.persistence.*;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected Option() {
    }

    public Option(String name, int quantity, Product product) {
        validateName(name);
        validateQuantity(quantity);
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    private void validateName(String name) {
        if (!name.matches("[a-zA-Z0-9 ()\\[\\]\\+\\-\\&/_]{1,50}")) {
            throw new IllegalArgumentException("옵션 이름 형식이 잘못되었습니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1 || quantity >= 100_000_000) {
            throw new IllegalArgumentException("수량은 1 이상 1억 미만이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int newQuantity) {
        validateQuantity(newQuantity);
        this.quantity = newQuantity;
    }

    public void subtractQuantity(int amount) {
        if (amount <= 0 || amount > quantity) {
            throw new IllegalArgumentException("차감 수량이 잘못되었습니다.");
        }
        this.quantity -= amount;
    }

    public String getName() {
        return name;
    }

    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    public Product getProduct() {
        return product;
    }

    public void updateProduct(Product newProduct) {
        this.product = newProduct;
    }
}
