package gift.service;

import gift.model.ProductOption;
import gift.repository.ProductOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductOptionService {
    private final ProductOptionRepository productOptionRepository;

    public ProductOptionService(ProductOptionRepository productOptionRepository) {
        this.productOptionRepository = productOptionRepository;
    }

    public void increaseQuantity(Long id, int amount) {
        ProductOption option = productOptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 옵션이 존재하지 않습니다."));

        option.setQuantity(option.getQuantity() + amount);
        productOptionRepository.save(option); // flush 시 update 발생
    }

    public void decreaseQuantity(Long id, int amount) {
        ProductOption option = productOptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 옵션이 존재하지 않습니다."));

        if (option.getQuantity() < amount) {
            throw new IllegalStateException("재고 부족: 현재 수량 = " + option.getQuantity());
        }

        option.setQuantity(option.getQuantity() - amount);
        productOptionRepository.save(option);
    }
}