package gift.option.controller;

import gift.option.dto.OptionResponse;
import gift.option.service.OptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class OptionApiController {

    private final OptionService optionService;

    public OptionApiController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{productId}/options")
    public List<OptionResponse> getOptions(@PathVariable Long productId) {
        return optionService.getOptions(productId);
    }
}
