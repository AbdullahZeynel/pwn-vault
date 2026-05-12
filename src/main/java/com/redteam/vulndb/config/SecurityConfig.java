package com.redteam.vulndb.config;

import com.redteam.vulndb.service.OperatorDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║               SPRING SECURITY CONFIG                         ║
 * ║        Kimlik Doğrulama ve Yetkilendirme Kuralları           ║
 * ╚═══════════════════════════════════════════════════════════════╝
 *
 * Bu sınıf Spring Security'nin tüm güvenlik kurallarını tanımlar:
 * → Hangi URL'ler herkese açık (login, css, js)
 * → Hangi URL'ler kimlik doğrulama gerektirir
 * → Login/logout mekanizması
 * → Şifre hash'leme algoritması (BCrypt)
 *
 * SPRING SECURITY FILTER CHAIN AKIŞI:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ HTTP Request                                                 │
 * │    ↓                                                         │
 * │ SecurityFilterChain (bu sınıfta tanımlı)                    │
 * │    ↓                                                         │
 * │ URL /login, /css/**, /js/** mı? → EVET → permitAll (geç)   │
 * │    ↓ HAYIR                                                   │
 * │ Kullanıcı authenticated mı? → HAYIR → /login'e redirect    │
 * │    ↓ EVET                                                    │
 * │ İsteği Controller'a ilet                                     │
 * └──────────────────────────────────────────────────────────────┘
 *
 * @Configuration: Bu sınıfın Spring konfigürasyon sınıfı olduğunu belirtir.
 *                 İçindeki @Bean metodları Spring Container'a bean kaydeder.
 *
 * @EnableWebSecurity: Spring Security'nin web güvenlik özelliklerini aktifleştirir.
 *                     SecurityFilterChain'i devreye sokar.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OperatorDetailsService operatorDetailsService;

    public SecurityConfig(OperatorDetailsService operatorDetailsService) {
        this.operatorDetailsService = operatorDetailsService;
    }

    /**
     * BCrypt Password Encoder Bean.
     * 
     * BCrypt neden kullanıyoruz?
     * → Tek yönlü hash: Hash'ten şifreye geri dönülemez
     * → Salt: Her hash'e rastgele salt eklenir → aynı şifre farklı hash üretir
     * → Adaptive: Bilgisayarlar hızlandıkça strength artırılabilir
     * → Brute-force'a karşı dayanıklı (kasıtlı olarak yavaştır)
     * 
     * Örnek: "admin123" → "$2a$10$Xz8kL..." (her çağrıda farklı hash!)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication Provider - Kullanıcı doğrulama mekanizması.
     * 
     * DaoAuthenticationProvider:
     * → "Dao" = Data Access Object
     * → UserDetailsService üzerinden veritabanından kullanıcı arar
     * → PasswordEncoder ile şifre karşılaştırması yapar
     * → İkisini birleştiren köprü bileşendir
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(operatorDetailsService);  // Kullanıcıyı nereden bul
        provider.setPasswordEncoder(passwordEncoder());           // Şifreyi nasıl karşılaştır
        return provider;
    }

    /**
     * Security Filter Chain - HTTP güvenlik kuralları.
     * 
     * Bu metot Spring Security'nin davranışını tamamen tanımlar.
     * Lambda DSL (Spring Security 6+) ile konfigüre edilmiştir.
     * 
     * CSRF (Cross-Site Request Forgery) koruması:
     * → Varsayılan olarak AKTİF'tir (kapatmıyoruz)
     * → Thymeleaf, form'lara otomatik olarak CSRF token ekler
     * → th:action="@{/url}" kullandığımızda hidden input olarak eklenir
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Authentication Provider'ı tanıt
            .authenticationProvider(authenticationProvider())
            
            // URL bazlı erişim kuralları
            .authorizeHttpRequests(auth -> auth
                // Bu URL'ler herkese açık (login olmadan erişilebilir)
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                // Geri kalan TÜM URL'ler authentication gerektirir
                .anyRequest().authenticated()
            )
            
            // Form tabanlı login konfigürasyonu
            .formLogin(form -> form
                .loginPage("/login")                    // Özel login sayfası URL'si
                .defaultSuccessUrl("/dashboard", true)  // Başarılı giriş → dashboard
                .failureUrl("/login?error=true")        // Başarısız giriş → hata mesajı
                .permitAll()
            )
            
            // Logout konfigürasyonu
            .logout(logout -> logout
                .logoutUrl("/logout")                       // Logout URL'si (POST)
                .logoutSuccessUrl("/login?logout=true")     // Logout sonrası yönlendirme
                .permitAll()
            );

        return http.build();
    }
}
