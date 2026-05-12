package com.redteam.vulndb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║          RED TEAM VULNERABILITY & EXPLOIT DATABASE           ║
 * ║                  Ana Uygulama Sınıfı                         ║
 * ╚═══════════════════════════════════════════════════════════════╝
 *
 * Bu sınıf Spring Boot uygulamasının giriş noktasıdır (entry point).
 *
 * @SpringBootApplication annotasyonu 3 annotasyonu bir arada içerir:
 *   1. @Configuration    → Bu sınıf bir konfigürasyon sınıfıdır
 *   2. @EnableAutoConfiguration → Spring Boot'un otomatik konfigürasyonunu aktifleştirir
 *                                  (JPA, Security, Thymeleaf vb. otomatik ayarlanır)
 *   3. @ComponentScan    → com.redteam.vulndb paketi ve alt paketlerindeki
 *                          @Component, @Service, @Repository, @Controller
 *                          annotasyonlu sınıfları otomatik bulup Spring Container'a kaydeder
 *
 * UYGULAMA BAŞLATMA AKIŞI:
 *   1. main() metodu çalışır
 *   2. SpringApplication.run() çağrılır
 *   3. Spring IoC Container oluşturulur
 *   4. Component Scan ile tüm bean'ler bulunur ve register edilir
 *   5. Auto Configuration çalışır (DataSource, JPA, Security vb.)
 *   6. Embedded Tomcat sunucusu başlatılır (port: 8080)
 *   7. CommandLineRunner (DataInitializer) çalışarak varsayılan admin oluşturur
 *   8. Uygulama hazır → http://localhost:8080
 */
@SpringBootApplication
public class VulnDbApplication {

    public static void main(String[] args) {
        // Spring Boot uygulamasını başlat
        // Bu çağrı ApplicationContext'i oluşturur ve embedded Tomcat'i ayağa kaldırır
        SpringApplication.run(VulnDbApplication.class, args);
    }
}
