package gift.service.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoUserDTO;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class KakaoApi {
    private static final Logger log = LoggerFactory.getLogger(KakaoApi.class);

    @Value("${kakao.rest-api-key}")
    private String kakaoAPIKey;
    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;


    public String getAccessToken(String code){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoAPIKey);
        params.add("redirect_uri", kakaoRedirectUri);
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

    public KakaoUserDTO getUserInfo(String accessToken){
        log.info("Access Token: {}", accessToken);
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            KakaoUserDTO user = objectMapper.readValue(response.getBody(), KakaoUserDTO.class);
            return user;

        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 파싱 실패", e);
        }
    }
    public void sendToMe(String accessToken, String messageText) throws JSONException {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        log.info("Access Token: {}", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        JSONObject linkObj = new JSONObject();
        linkObj.put("web_url", "about:blank");
        linkObj.put("mobile_web_url", "about:blank");

        JSONObject templateObj = new JSONObject();
        templateObj.put("object_type", "text");
        templateObj.put("text", messageText);
        templateObj.put("link", linkObj);
        templateObj.put("button_title", "확인");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObj.toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            log.info("카카오톡 메시지 전송 완료: {}", response.getBody());
        } catch (Exception e) {
            log.error("카카오톡 메시지 전송 실패", e);
        }
    }
}