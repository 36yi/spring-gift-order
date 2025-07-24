package gift.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "wish",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userid", "productid"})
)
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long count;

    protected Wish() {
    }

    public Wish(User user, Product product, Long count) {
        this.user = user;
        this.product = product;
        this.count = count;
    }
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Product getProduct() { return product; }
    public Long getCount() { return count; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setProduct(Product product) { this.product = product; }
    public void setCount(Long count) { this.count = count; }
}