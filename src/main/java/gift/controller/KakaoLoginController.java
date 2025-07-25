package gift.controller;

import gift.service.KakaoOAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginController {
    private final KakaoOAuthService kakaoOAuthService;
    @Value("${kakao.RESTAPIKEY}")
    private String kakaoAPIKey;

    public KakaoLoginController(KakaoOAuthService kakaoOAuthService) {
        this.kakaoOAuthService = kakaoOAuthService;
    }


    @GetMapping("/login/kakao")
    public String page(Model model){
        model.addAttribute("kakaoAPIKey",kakaoAPIKey);
        return "kakao/page";
    }

    @GetMapping("")
    public String handleKakaoRedirect(@RequestParam(required = false) String code) {
        if (code != null) {
            kakaoOAuthService.processKakaoLogin(code);
            return "redirect:/admin/users";
        } else {
            return "redirect:/error";
        }
    }
}