package com.redteam.vulndb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                  AUTH CONTROLLER                              ║
 * ║          Kimlik Doğrulama Endpoint'leri                       ║
 * ╚═══════════════════════════════════════════════════════════════╝
 *
 * ÖNEMLİ NOT:
 * → Login POST işlemi Spring Security tarafından OTOMATİK yönetilir.
 * → SecurityConfig'te .formLogin(form -> form.loginPage("/login")) tanımlandığı için
 *   POST /login isteği Spring Security'nin UsernamePasswordAuthenticationFilter'ına gider.
 * → Bu controller sadece:
 *   1. Login sayfasını GÖSTERMEK (GET /login)
 *   2. Root URL'yi yönlendirmek (GET / → /dashboard)
 *   için vardır.
 */
@Controller
public class AuthController {

    /** Root URL → Dashboard'a yönlendir (auth yoksa Security /login'e yönlendirir) */
    @GetMapping("/")
    public String redirectToDashboard() {
        return "redirect:/dashboard";
    }

    /**
     * Login sayfasını gösterir.
     * Thymeleaf template: login.html
     * 
     * URL parametreleri (Spring Security tarafından eklenir):
     * → ?error=true : Yanlış giriş denemesi
     * → ?logout=true : Başarılı çıkış
     * Bu parametreler template'te th:if="${param.error}" ile kontrol edilir.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
