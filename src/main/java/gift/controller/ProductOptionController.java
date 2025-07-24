package gift.controller;

import gift.service.ProductOptionService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class ProductOptionController {
    private final ProductOptionService productOptionService;

    public ProductOptionController(ProductOptionService productOptionService){
        this.productOptionService = productOptionService;
    }

    @PatchMapping("/options/{optionId}/decrease")
    public void decreaseQuantity(
            @PathVariable Long optionId,
            @RequestParam int amount
    ) {
        productOptionService.decreaseQuantity(optionId, amount);
    }

    @PatchMapping("/options/{optionId}/increase")
    public void increaseQuantity(
            @PathVariable Long optionId,
            @RequestParam int amount
    ) {
        productOptionService.increaseQuantity(optionId, amount);
    }

}