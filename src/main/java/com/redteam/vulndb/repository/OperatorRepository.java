package com.redteam.vulndb.repository;

import com.redteam.vulndb.entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                 OPERATOR REPOSITORY                          ║
 * ║          Operator Entity İçin Veri Erişim Katmanı            ║
 * ╚═══════════════════════════════════════════════════════════════╝
 *
 * REPOSITORY PATTERN NEDİR?
 * → Veritabanı erişim mantığını iş mantığından (Service) ayırır.
 * → SOLID prensiplerinden Single Responsibility'ye uyar.
 * → Controller veya Service doğrudan SQL yazmaz, Repository aracılığıyla erişir.
 *
 * SPRING DATA JPA NASIL ÇALIŞIR?
 * → Bu bir interface'dir, sınıf DEĞİLDİR.
 * → Spring Data JPA, çalışma zamanında (runtime) bu interface'in
 *   implementasyonunu OTOMATIK olarak proxy ile üretir.
 * → Yani biz hiçbir metot gövdesi yazmıyoruz, Spring bizim için yazıyor!
 *
 * JpaRepository<Operator, Long> parametreleri:
 * → Operator: Entity tipi (hangi tablo için repository)
 * → Long: Primary Key'in Java tipi (id alanının tipi)
 *
 * JpaRepository'DEN MIRAS ALINAN HAZIR METODLAR:
 * ┌─────────────────────────────┬─────────────────────────────────┐
 * │ Metot                       │ SQL Karşılığı                   │
 * ├─────────────────────────────┼─────────────────────────────────┤
 * │ findAll()                   │ SELECT * FROM operators          │
 * │ findById(Long id)           │ SELECT * FROM operators WHERE id=?│
 * │ save(Operator entity)       │ INSERT INTO / UPDATE operators   │
 * │ deleteById(Long id)         │ DELETE FROM operators WHERE id=? │
 * │ count()                     │ SELECT COUNT(*) FROM operators   │
 * │ existsById(Long id)         │ SELECT 1 FROM operators WHERE id=?│
 * └─────────────────────────────┴─────────────────────────────────┘
 *
 * @Repository annotasyonu:
 * → Bu interface'i Spring Bean olarak işaretler.
 * → Spring Data JPA zaten otomatik algılar ama açıkça belirtmek best practice'tir.
 * → Ayrıca DataAccessException çevrimini (translation) aktifleştirir.
 */
@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {

    /**
     * Kullanıcı adına göre operator arar.
     *
     * DERIVED QUERY METHOD (Türetilmiş Sorgu Metodu):
     * → Spring Data JPA, metot adını parse ederek otomatik SQL üretir.
     * → "findBy" + "Username" → WHERE username = ?
     *
     * Üretilen SQL:
     *   SELECT * FROM operators WHERE username = :username
     *
     * Optional<Operator> neden kullanılıyor?
     * → Kullanıcı bulunamayabilir → null dönmek yerine Optional.empty() döner.
     * → NullPointerException riskini ortadan kaldırır.
     * → Java 8+ best practice: null yerine Optional kullan.
     *
     * Bu metot nerede kullanılıyor?
     * → OperatorDetailsService.loadUserByUsername() → Spring Security login
     * → DataInitializer.run() → Varsayılan admin kontrolü
     *
     * @param username aranacak kullanıcı adı
     * @return Optional<Operator> - bulunursa Present, bulunamazsa Empty
     */
    Optional<Operator> findByUsername(String username);
}
