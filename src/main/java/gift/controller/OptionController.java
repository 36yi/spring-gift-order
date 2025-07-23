package gift.controller;

import gift.dto.OptionInfoResponseDto;
import gift.dto.OptionRequestDto;
import gift.dto.OptionSubtractRequestDto;
import gift.service.OptionService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

  private final OptionService optionService;

  public OptionController(OptionService optionService) {
    this.optionService = optionService;
  }

  @GetMapping
  public ResponseEntity<List<OptionInfoResponseDto>> getOptions(@PathVariable Long productId) {
    List<OptionInfoResponseDto> options = optionService.getOptionsByProductId(productId);
    return ResponseEntity.ok(options);
  }

  @PostMapping
  public ResponseEntity<OptionInfoResponseDto> addOption(
      @PathVariable Long productId,
      @Valid @RequestBody OptionRequestDto dto) {

    OptionInfoResponseDto createdOption = optionService.addOptionToProduct(productId, dto);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(createdOption.id())
        .toUri();

    return ResponseEntity.created(location).body(createdOption);
  }

  @PatchMapping("/subtract")
  public ResponseEntity<OptionInfoResponseDto> subtractQuantity(
      @PathVariable Long productId,
      @Valid @RequestBody OptionSubtractRequestDto dto) {

    OptionInfoResponseDto updatedOption = optionService.subtractQuantity(productId, dto);

    return ResponseEntity.ok(updatedOption);
  }
}
