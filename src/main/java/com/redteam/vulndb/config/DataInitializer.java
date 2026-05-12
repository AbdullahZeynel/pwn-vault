package com.redteam.vulndb.config;

import com.redteam.vulndb.entity.Operator;
import com.redteam.vulndb.repository.OperatorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                DATA INITIALIZER                              ║
 * ║   Uygulama Başlangıcında Varsayılan Verileri Oluşturur       ║
 * ╚═══════════════════════════════════════════════════════════════╝
 *
 * CommandLineRunner interface'i:
 * → Spring Boot uygulaması tamamen ayağa kalktıktan sonra
 *   run() metodu OTOMATİK olarak çağrılır.
 * → Veritabanı seed (tohum) verisi oluşturmak için idealdir.
 * → Uygulama her başladığında çalışır, bu yüzden
 *   "zaten varsa oluşturma" kontrolü yapılmalıdır.
 *
 * Varsayılan kullanıcı: admin / admin123
 * Şifre BCrypt ile hash'lenerek kaydedilir.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final OperatorRepository operatorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(OperatorRepository operatorRepository,
                           PasswordEncoder passwordEncoder) {
        this.operatorRepository = operatorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // İdempotent: Admin zaten varsa tekrar oluşturma
        if (operatorRepository.findByUsername("admin").isEmpty()) {
            Operator admin = new Operator();
            admin.setUsername("admin");
            // BCrypt hash: "admin123" → "$2a$10$..." (her seferinde farklı hash)
            admin.setPassword(passwordEncoder.encode("admin123"));

            operatorRepository.save(admin);
            System.out.println("═══════════════════════════════════════════");
            System.out.println("  [+] Varsayılan admin kullanıcı oluşturuldu");
            System.out.println("  [+] Username: admin");
            System.out.println("  [+] Password: admin123");
            System.out.println("═══════════════════════════════════════════");
        }
    }
}
