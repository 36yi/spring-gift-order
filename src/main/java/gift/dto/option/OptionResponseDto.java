package gift.dto.option;

import gift.entity.Option;

public class OptionResponseDto {

  private Long id;
  private String name;
  private int quantity;

  public OptionResponseDto(Long id, String name, int quantity) {
    this.id = id;
    this.name = name;
    this.quantity = quantity;
  }

  public OptionResponseDto(Option option) {
    this(option.getId(), option.getName(), option.getQuantity());
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
}
