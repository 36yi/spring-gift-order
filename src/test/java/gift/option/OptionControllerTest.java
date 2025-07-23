package gift.option;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.config.TestConfig;
import gift.dto.OptionRequestDto;
import gift.dto.OptionSubtractRequestDto;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
class OptionControllerTest extends TestConfig {
  @Autowired private ProductRepository productRepository;

  private static final Integer DEFAULT_TEST_QUANTITY = 1000;

  private static final Integer DEFAULT_SUBTRACT_QUANTITY = 300;

  private static final Integer TEST_PRODUCT_ID = 1;

  @Test
  void 옵션_이름이_50자_초과할_경우_400() throws Exception {
    String invalidName = "사과3456789012345678901234567890123456789012345678901";  // 51글자
    OptionRequestDto dto = new OptionRequestDto(invalidName, DEFAULT_TEST_QUANTITY);

    mockMvc.perform(post("/api/products/{productId}/options", TEST_PRODUCT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 옵션_이름에_허용되지_않는_특수문자_등록_시도시_400() throws Exception {
    String invalidName = "!사과";  // '!' 허용 안됨
    OptionRequestDto dto = new OptionRequestDto(invalidName, DEFAULT_TEST_QUANTITY);

    mockMvc.perform(post("/api/products/{productId}/options", TEST_PRODUCT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 동일_상품_내_중복된_옵션_이름_등록_시도시_409() throws Exception {
    String name = "중복옵션";
    OptionRequestDto dto = new OptionRequestDto(name, DEFAULT_TEST_QUANTITY);

    // 최초 등록
    mockMvc.perform(post("/api/products/{productId}/options", TEST_PRODUCT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated());

    // 중복 등록
    mockMvc.perform(post("/api/products/{productId}/options", TEST_PRODUCT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isConflict());
  }

  @Test
  void 옵션_수량_차감_테스트_1000개_에서_300개_뺄시_700개가_되나() throws Exception {
    String optionName = "사과 한 박스 10kg";

    // 옵션 등록
    OptionRequestDto createDto = new OptionRequestDto(optionName, DEFAULT_TEST_QUANTITY); // 1000개
    mockMvc.perform(post("/api/products/{productId}/options", TEST_PRODUCT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isCreated());

    // 차감 요청
    OptionSubtractRequestDto subtractDto = new OptionSubtractRequestDto(optionName, DEFAULT_SUBTRACT_QUANTITY); //300개
    mockMvc.perform(patch("/api/products/{productId}/options/subtract", TEST_PRODUCT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(subtractDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.quantity").value(DEFAULT_TEST_QUANTITY - DEFAULT_SUBTRACT_QUANTITY));
  }
}
