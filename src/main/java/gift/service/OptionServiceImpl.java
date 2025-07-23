package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.OptionInfoResponseDto;
import gift.dto.OptionRequestDto;
import gift.dto.OptionSubtractRequestDto;
import gift.exception.DuplicateOptionNameException;
import gift.exception.InvalidOptionQuantityException;
import gift.exception.OptionNotFoundException;
import gift.exception.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionServiceImpl implements OptionService {

  private static final String DEFAULT_OPTION_SUFFIX = " 단품";

  private static final Integer DEFAULT_QUANTITY = 100;

  private final OptionRepository optionRepository;
  private final ProductRepository productRepository;

  public OptionServiceImpl(OptionRepository optionRepository, ProductRepository productRepository) {
    this.optionRepository = optionRepository;
    this.productRepository = productRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<OptionInfoResponseDto> getOptionsByProductId(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));

    List<Option> options = optionRepository.findByProductId(productId);

    return options.stream()
        .map(option -> new OptionInfoResponseDto(option.getId(), option.getName(), option.getQuantity()))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public OptionInfoResponseDto addOptionToProduct(Long productId, OptionRequestDto dto) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));

    optionRepository.findByProductIdAndName(productId, dto.name())
        .ifPresent(option -> {
          throw new DuplicateOptionNameException(productId, dto.name());
        });

    Option option = new Option(dto.name(), dto.quantity(), product);
    product.addOption(option);
    Option savedOption = optionRepository.save(option);

    return new OptionInfoResponseDto(savedOption.getId(), savedOption.getName(), savedOption.getQuantity());
  }

  @Override
  @Transactional
  public OptionInfoResponseDto subtractQuantity(Long productId, OptionSubtractRequestDto dto) {
    Option option = optionRepository.findByProductIdAndName(productId, dto.name())
        .orElseThrow(() -> new OptionNotFoundException(productId, dto.name()));

    int currentQuantity = option.getQuantity();
    int subtractAmount = dto.quantity();

    if (currentQuantity - subtractAmount < 0) {
      throw new InvalidOptionQuantityException(currentQuantity, subtractAmount);
    }

    option.updateQuantity(currentQuantity - subtractAmount);

    return new OptionInfoResponseDto(option.getId(), option.getName(), option.getQuantity());
  }

  @Override
  @Transactional
  public OptionInfoResponseDto addDefaultOption(Long productId, String baseName){
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));

    String defaultOptionName = baseName + DEFAULT_OPTION_SUFFIX;

    Option option = new Option(defaultOptionName, DEFAULT_QUANTITY, product);
    product.addOption(option);
    Option savedOption = optionRepository.save(option);

    return new OptionInfoResponseDto(savedOption.getId(), savedOption.getName(), savedOption.getQuantity());
  }
}
