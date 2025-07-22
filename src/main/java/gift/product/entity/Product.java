package gift.product.entity;

import gift.option.entity.Option;
import gift.product.dto.ProductResponse;
import gift.wish.entity.Wish;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    protected Product() {
    }

    public Product(String name, BigDecimal price, String imgUrl) {
        validateName(name);
        validatePrice(price);
        validateImgUrl(imgUrl);
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public Product(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imgUrl = product.getImgUrl();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품 이름은 비어 있을 수 없습니다");
        }
        if (name.length() > 15) {
            throw new IllegalArgumentException("상품 이름은 최대 15자까지 입력할 수 있습니다");
        }
        if (!name.matches("^[\\p{L}\\p{N} ()\\[\\]+\\-&/_]*$")) {
            throw new IllegalArgumentException("상품 이름에는 허용되지 않은 특수문자가 포함되어 있습니다");
        }
        if (name.contains("카카오") && !name.endsWith("카테캠")) {
            throw new IllegalArgumentException("\"카카오\"가 포함된 상품 이름은 MD와 협의 후 사용해 주세요");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다");
        }
    }

    private void validateImgUrl(String imgUrl) {
        if (imgUrl == null || imgUrl.isBlank()) {
            throw new IllegalArgumentException("이미지 URL은 비어 있을 수 없습니다");
        }
    }

    public void addWish(Wish wish) {
        wishes.add(wish);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    public void updatePrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public void updateImgUrl(String imgUrl) {
        validateImgUrl(imgUrl);
        this.imgUrl = imgUrl;
    }

    public void addOption(Option option) {
        options.add(option);
        option.updateProduct(this);
    }
}
