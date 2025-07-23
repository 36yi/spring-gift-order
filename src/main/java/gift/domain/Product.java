package gift.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 15)
  private String name;

  @Column(nullable = false)
  private Integer price;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WishList> wishLists = new ArrayList<>();

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Option> options = new ArrayList<>();

  protected Product() {}

  public Product(Long id, String name, Integer price, String imageUrl){
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public Product(String name, Integer price, String imageUrl) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public void update(String name, Integer price, String imageUrl) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public Long getId() { return id; }
  public String getName() { return name; }
  public Integer getPrice() { return price; }
  public String getImageUrl() { return imageUrl; }

  public List<Option> getOptions() {
    return options;
  }
  public void addOption(Option option) {
    options.add(option);
  }
}
