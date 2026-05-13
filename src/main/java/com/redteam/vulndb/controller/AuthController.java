package com.redteam.vulndb.controller;

import com.redteam.vulndb.entity.Operator;
import com.redteam.vulndb.repository.OperatorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    private final OperatorRepository operatorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(OperatorRepository operatorRepository, PasswordEncoder passwordEncoder) {
        this.operatorRepository = operatorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Root URL → Dashboard'a yönlendir (auth yoksa Security /login'e yönlendirir) */
    @GetMapping("/")
    public String redirectToProjects() {
        return "redirect:/projects";
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

    /** Kayıt sayfasını gösterir */
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    /** Kayıt işlemini gerçekleştirir */
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, Model model) {
        // Kullanıcı adı daha önce alınmış mı kontrol et
        if (operatorRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Bu kullanıcı adı zaten alınmış!");
            return "register";
        }

        // Yeni operatör oluştur ve şifresini hashleyerek kaydet
        Operator operator = new Operator();
        operator.setUsername(username);
        operator.setPassword(passwordEncoder.encode(password));
        operatorRepository.save(operator);

        // Başarılı kayıttan sonra login sayfasına yönlendir
        return "redirect:/login?registered=true";
    }
}
