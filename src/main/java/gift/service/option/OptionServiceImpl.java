package gift.service.option;

import gift.dto.option.OptionRequestDto;
import gift.dto.option.OptionResponseDto;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.DuplicatedOptionException;
import gift.exception.notfound.ProductNotFoundException;
import gift.repository.option.OptionJpaRepository;
import gift.repository.product.ProductJpaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OptionServiceImpl implements OptionService {

  private final OptionJpaRepository optionRepository;
  private final ProductJpaRepository productRepository;

  public OptionServiceImpl(OptionJpaRepository optionRepository,
      ProductJpaRepository productRepository) {
    this.optionRepository = optionRepository;
    this.productRepository = productRepository;
  }

  @Override
  public List<OptionResponseDto> findByProductId(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException("상품이 존재하지 않습니다."))
        .getOptions().stream().map(OptionResponseDto::new).toList();
  }

  @Transactional
  @Override
  public OptionResponseDto createOption(Long productId, OptionRequestDto requestDto) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException("상품이 존재하지 않습니다."));

    List<Option> options = product.getOptions();
    for (Option option : options) {
      if (requestDto.getName().equals(option.getName())) {
        throw new DuplicatedOptionException("중복된 옵션이 존재합니다.");
      }
    }

    Option option = optionRepository.save(
        new Option(requestDto.getName(), requestDto.getQuantity(), product));
    product.addOption(option);

    return new OptionResponseDto(option);
  }

  @Transactional
  @Override
  public void deleteAllOption(Long productId) {
    productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException("삭제하려는 상품이 존재하지 않습니다."))
        .removeAllOption();
    optionRepository.deleteByProductId(productId);
  }

  @Transactional
  @Override
  public void deleteByOptionId(Long productId, Long optionId) {
    productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException("삭제하려는 상품이 존재하지 않습니다."))
        .removeOption(optionRepository.findById(optionId).orElseThrow());
    optionRepository.deleteByProductIdAndId(productId, optionId);
  }
}
