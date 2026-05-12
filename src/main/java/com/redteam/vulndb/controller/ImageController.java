package com.redteam.vulndb.controller;

import com.redteam.vulndb.entity.Vulnerability;
import com.redteam.vulndb.service.VulnerabilityService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                  IMAGE CONTROLLER                            ║
 * ║     BLOB Olarak Kaydedilen Görselleri HTTP ile Sunar          ║
 * ╚═══════════════════════════════════════════════════════════════╝
 *
 * Bu controller'ın TEK GÖREVİ:
 * Veritabanındaki byte[] (BLOB) verisini, uygun Content-Type header'ı
 * ile HTTP response olarak tarayıcıya döndürmektir.
 *
 * BLOB GÖRSEL RENDER AKIŞI:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ Thymeleaf: <img th:src="@{'/image/' + ${vuln.id}}" />     │
 * │    ↓                                                       │
 * │ Browser: GET /image/5                                      │
 * │    ↓                                                       │
 * │ ImageController.getImage(5)                                │
 * │    ↓                                                       │
 * │ Service → Repository → SELECT proof_image FROM vulns       │
 * │    ↓                                                       │
 * │ byte[] + "image/png" → ResponseEntity                      │
 * │    ↓                                                       │
 * │ HTTP Response: Content-Type: image/png + binary body       │
 * │    ↓                                                       │
 * │ Browser, görseli <img> tag'ında render eder                │
 * └─────────────────────────────────────────────────────────────┘
 *
 * NEDEN AYRI BİR CONTROLLER?
 * → Görseller ayrı HTTP request olarak yüklenir (<img src="...">)
 * → Base64 encoding yerine doğrudan binary stream tercih edilir
 *   (Base64 veri boyutunu %33 artırır, performans düşer)
 * → ResponseEntity ile Content-Type, Cache-Control gibi header'lar
 *   hassas şekilde kontrol edilebilir
 */
@Controller
public class ImageController {

    private final VulnerabilityService vulnerabilityService;

    public ImageController(VulnerabilityService vulnerabilityService) {
        this.vulnerabilityService = vulnerabilityService;
    }

    /**
     * BLOB proof image'ı HTTP response olarak döndürür.
     *
     * @PathVariable: URL'deki {id} değerini parametre olarak alır
     *   Örnek: GET /image/5 → id = 5
     *
     * ResponseEntity<byte[]>: HTTP response'u tam kontrol eder
     *   → body: byte[] (görsel verisi)
     *   → headers: Content-Type (image/png, image/jpeg vb.)
     *   → status: 200 OK veya 404 NOT FOUND
     */
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return vulnerabilityService.findById(id)
                .filter(vuln -> vuln.getProofImage() != null)  // Görsel var mı kontrol
                .map(vuln -> {
                    HttpHeaders headers = new HttpHeaders();
                    // MIME type belirle (tarayıcının görseli doğru yorumlaması için)
                    String contentType = vuln.getImageContentType() != null
                            ? vuln.getImageContentType()
                            : "image/png";
                    headers.setContentType(MediaType.parseMediaType(contentType));
                    return new ResponseEntity<>(vuln.getProofImage(), headers, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));  // Görsel yoksa 404
    }
}
