package com.redteam.vulndb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                   OPERATOR ENTITY                            ║
 * ║       Sisteme Giriş Yapan Kullanıcıları Temsil Eder          ║
 * ╚═══════════════════════════════════════════════════════════════╝
 *
 * Bu sınıf JPA Entity'sidir ve veritabanındaki "operators" tablosuna
 * karşılık gelir (ORM - Object Relational Mapping).
 *
 * KULLANILAN ANNOTASYONLAR:
 *
 * @Entity → Bu sınıfın bir JPA entity'si olduğunu belirtir.
 *           Hibernate bu sınıfı veritabanı tablosuna map eder.
 *
 * @Table(name = "operators") → Tablo adını açıkça belirtir.
 *           Belirtilmezse sınıf adı ("Operator") kullanılır.
 *
 * @Id → Primary Key alanını işaretler. Her entity'de zorunludur.
 *
 * @GeneratedValue(strategy = GenerationType.IDENTITY)
 *   → ID değeri veritabanı tarafından otomatik oluşturulur.
 *   → PostgreSQL'de SERIAL/BIGSERIAL, MySQL'de AUTO_INCREMENT kullanılır.
 *   → Diğer stratejiler: TABLE, SEQUENCE, AUTO
 *
 * @Column(unique = true, nullable = false)
 *   → Veritabanı seviyesinde kısıtlama ekler.
 *   → unique: Aynı username'den iki kayıt olamaz (UNIQUE CONSTRAINT)
 *   → nullable: NULL değer kabul etmez (NOT NULL CONSTRAINT)
 *
 * @NotBlank → Bean Validation (JSR 380) annotasyonu.
 *   → Hem NULL kontrolü hem de boş string ("") kontrolü yapar.
 *   → Controller'da @Valid ile birlikte kullanılınca otomatik çalışır.
 *   → @NotNull'dan farkı: " " (sadece boşluk) değerini de reddeder.
 *
 * GÜVENLİK NOTU:
 * → password alanı düz metin (plain text) olarak SAKLANMAZ.
 * → DataInitializer sınıfında BCryptPasswordEncoder ile hash'lenir.
 * → Örnek: "admin123" → "$2a$10$Xz8kL..." şeklinde saklanır.
 * → BCrypt tek yönlü hash'tir, geri çözülemez.
 */
@Entity
@Table(name = "operators")
public class Operator {

    /**
     * Primary Key - Otomatik artan benzersiz tanımlayıcı.
     * PostgreSQL'de BIGSERIAL tipine karşılık gelir.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Kullanıcı adı - Sisteme giriş için kullanılır.
     * UNIQUE constraint ile aynı username'den iki kayıt oluşturulamaz.
     * Spring Security, login sırasında bu alanı kullanarak kullanıcıyı arar.
     */
    @NotBlank(message = "Kullanıcı adı boş bırakılamaz")
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Şifre - BCrypt algoritmasıyla hash'lenmiş olarak saklanır.
     * ASLA düz metin olarak kaydedilmez!
     * BCrypt hash örneği: $2a$10$N9qo8uLOickgx2ZMRZoMye...
     */
    @NotBlank(message = "Şifre boş bırakılamaz")
    @Column(nullable = false)
    private String password;

    // ==================== CONSTRUCTORS ====================
    // JPA, entity nesnelerini oluştururken no-arg constructor gerektirir.
    // Bu zorunlu bir JPA kuralıdır (JPA Specification 2.1, Section 2.1).

    /** Parametresiz constructor - JPA için zorunlu */
    public Operator() {
    }

    /** Parametreli constructor - Programatik kullanım için */
    public Operator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ==================== GETTERS & SETTERS ====================
    // JPA ve Thymeleaf, verilere erişmek için getter/setter metodlarını kullanır.
    // Bu JavaBean standardının bir gereğidir.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
