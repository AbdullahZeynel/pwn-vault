package com.redteam.vulndb.service;

import com.redteam.vulndb.entity.Operator;
import com.redteam.vulndb.repository.OperatorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║            OPERATOR DETAILS SERVICE                          ║
 * ║     Spring Security İçin Kullanıcı Doğrulama Servisi         ║
 * ╚═══════════════════════════════════════════════════════════════╝
 *
 * Bu servis Spring Security'nin UserDetailsService interface'ini implement eder.
 * Spring Security, login sırasında kullanıcıyı doğrulamak için bu servisi kullanır.
 *
 * AUTHENTICATION (Kimlik Doğrulama) AKIŞI:
 * ┌─────────────────────────────────────────────────────────────────┐
 * │ 1. Kullanıcı login formunda username + password girer          │
 * │ 2. POST /login isteği Spring Security'ye ulaşır               │
 * │ 3. DaoAuthenticationProvider bu servisin                       │
 * │    loadUserByUsername() metodunu çağırır                       │
 * │ 4. Veritabanından Operator entity'si çekilir                  │
 * │ 5. Entity → Spring Security UserDetails nesnesine dönüştürülür │
 * │ 6. Spring Security, formdan gelen password'ü BCrypt ile       │
 * │    UserDetails'teki hash'li password ile karşılaştırır        │
 * │ 7a. Eşleşme → Session oluşturulur → /dashboard'a redirect    │
 * │ 7b. Eşleşmeme → /login?error=true'ye redirect                │
 * └─────────────────────────────────────────────────────────────────┘
 *
 * DEPENDENCY INJECTION (Bağımlılık Enjeksiyonu):
 * → Constructor Injection kullanılmıştır (recommended by Spring team).
 * → @Autowired annotasyonu Constructor Injection'da OPTIONAL'dır
 *   (tek constructor varsa Spring otomatik inject eder).
 * → Field Injection (@Autowired private OperatorRepository repo;)
 *   yerine Constructor Injection tercih edilir çünkü:
 *   1. Test edilebilirlik: Mock repository kolayca verilebilir
 *   2. Immutability: final alanlar kullanılabilir
 *   3. Açıklık: Bağımlılıklar constructor'da görünür
 *
 * @Service annotasyonu:
 * → Bu sınıfı Spring Bean olarak işaretler.
 * → @Component'in özelleşmiş halidir (service layer'ı ifade eder).
 * → Spring Container tarafından otomatik oluşturulur ve yönetilir.
 */
@Service
public class OperatorDetailsService implements UserDetailsService {

    private final OperatorRepository operatorRepository;

    /**
     * Constructor Injection - Spring, OperatorRepository bean'ini
     * otomatik olarak bu constructor'a inject eder.
     */
    public OperatorDetailsService(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    /**
     * Spring Security tarafından login sırasında otomatik çağrılır.
     *
     * @param username login formundan gelen kullanıcı adı
     * @return UserDetails - Spring Security'nin anlayacağı kullanıcı objesi
     * @throws UsernameNotFoundException kullanıcı veritabanında bulunamazsa
     *
     * User.builder() ile oluşturulan UserDetails nesnesi şu bilgileri içerir:
     * → username: Kimlik doğrulama için kullanıcı adı
     * → password: BCrypt hash'li şifre (karşılaştırma için)
     * → roles("OPERATOR"): Kullanıcının rolü (yetkilendirme için)
     *
     * NOT: Buradaki "OPERATOR" rolü, Spring Security tarafından otomatik olarak
     * "ROLE_OPERATOR" prefix'ine dönüştürülür. SecurityConfig'te
     * hasRole("OPERATOR") şeklinde kontrol edilir.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Veritabanında kullanıcıyı ara
        Operator operator = operatorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Operator bulunamadı: " + username));

        // 2. Operator entity'sini Spring Security UserDetails'e dönüştür
        return User.builder()
                .username(operator.getUsername())
                .password(operator.getPassword())  // BCrypt hash'li şifre
                .roles("OPERATOR")                  // Varsayılan rol
                .build();
    }
}
