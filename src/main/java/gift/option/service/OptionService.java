package gift.option.service;

import gift.global.exception.OptionAlreadyExistsException;
import gift.global.exception.ProductNotFoundException;
import gift.option.dto.OptionResponse;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionResponse> getOptions(Long productId) {
        List<Option> options = optionRepository.findByProductId(productId);
        return options.stream()
                .map(OptionResponse::from)
                .toList();
    }

    public void addOption(Long productId, String name, int quantity) {
        if (optionRepository.existsByProductIdAndName(productId, name)) {
            throw new OptionAlreadyExistsException(name, productId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OptionAlreadyExistsException(name, productId));

        Option option = new Option(name, quantity, product);
        optionRepository.save(option);
    }
}
