package com.redteam.vulndb.entity;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    SEVERITY ENUM                             ║
 * ║        Güvenlik Açığı Şiddet Seviyesi Tanımlaması            ║
 * ╚═══════════════════════════════════════════════════════════════╝
 *
 * CVSS (Common Vulnerability Scoring System) standartlarına uygun
 * 4 farklı şiddet seviyesi tanımlanmıştır.
 *
 * NEDEN ENUM KULLANIYORUZ?
 * → String yerine Enum kullanmak type-safety sağlar.
 * → Veritabanında "yanlışlıkla" geçersiz bir değer girilmesini engeller.
 * → Vulnerability entity'de @Enumerated(EnumType.STRING) ile
 *   veritabanına "LOW", "MEDIUM", "HIGH", "CRITICAL" string olarak kaydedilir.
 *   (EnumType.ORDINAL kullanılsaydı 0, 1, 2, 3 sayıları kaydedilirdi
 *    ki bu okunabilirliği düşürür.)
 *
 * CVSS SEVİYELERİ:
 *   LOW      → 0.1 - 3.9  → Düşük risk, sınırlı etki
 *   MEDIUM   → 4.0 - 6.9  → Orta risk, belirli koşullarda istismar edilebilir
 *   HIGH     → 7.0 - 8.9  → Yüksek risk, ciddi etki
 *   CRITICAL → 9.0 - 10.0 → Kritik risk, uzaktan kod çalıştırma vb.
 */
public enum Severity {
    LOW,       // Düşük risk - CVSS: 0.1-3.9
    MEDIUM,    // Orta risk  - CVSS: 4.0-6.9
    HIGH,      // Yüksek risk - CVSS: 7.0-8.9
    CRITICAL   // Kritik risk - CVSS: 9.0-10.0
}
