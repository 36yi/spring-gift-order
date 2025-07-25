package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoUserDTO;
import gift.model.User;
import gift.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class KakaoOAuthService {
    private final UserRepository userRepository;
    @Value("${kakao.RESTAPIKEY}")
    private String kakaoAPIKey;

    public KakaoOAuthService(UserRepository userRepository) {this.userRepository = userRepository;}

    public void processKakaoLogin(String code) {
        String token = getAccessToken(code);
        System.out.println(token);
        KakaoUserDTO userInfo = getUserInfo(token);


//        Optional<User> userOpt = userRepository.findByOauthProviderAndOauthId("kakao", userInfo.getId());
//
//        User user = userOpt.orElseGet(() ->
//                userRepository.save(new User(...userInfo...))
//    );
//
//        login(user);
    }

    private KakaoUserDTO getUserInfo(String token) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            Long id = root.path("id").asLong();
            String nickname = root.path("kakao_account").path("profile").path("nickname").asText();
            String email = root.path("kakao_account").path("email").asText(null);

            return new KakaoUserDTO(id, nickname, email);

        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 파싱 실패", e);
        }
    }

    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoAPIKey);
        params.add("redirect_uri", "http://localhost:8080");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map body = response.getBody();
            return (String) body.get("access_token");
        } else {
            throw new RuntimeException("토큰 요청 실패");
        }
    }
}