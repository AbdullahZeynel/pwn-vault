package com.redteam.vulndb.config;

import com.redteam.vulndb.entity.Operator;
import com.redteam.vulndb.entity.Severity;
import com.redteam.vulndb.entity.Vulnerability;
import com.redteam.vulndb.repository.OperatorRepository;
import com.redteam.vulndb.repository.VulnerabilityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * DataInitializer - Uygulama başlangıcında demo verileri oluşturur.
 * CommandLineRunner: Uygulama ayağa kalktıktan sonra otomatik çalışır.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final OperatorRepository operatorRepository;
    private final VulnerabilityRepository vulnerabilityRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(OperatorRepository operatorRepository,
                           VulnerabilityRepository vulnerabilityRepository,
                           PasswordEncoder passwordEncoder) {
        this.operatorRepository = operatorRepository;
        this.vulnerabilityRepository = vulnerabilityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Admin kullanıcı oluştur
        if (operatorRepository.findByUsername("admin").isEmpty()) {
            Operator admin = new Operator();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            operatorRepository.save(admin);
            System.out.println("[+] Admin kullanıcı oluşturuldu: admin / admin123");
        }

        // Demo vulnerability kayıtları (sadece tablo boşsa oluştur)
        if (vulnerabilityRepository.count() == 0) {
            createDemoVulnerabilities();
            System.out.println("[+] 6 adet demo vulnerability kaydı oluşturuldu.");
        }
    }

    private void createDemoVulnerabilities() {
        // 1. SQL Injection - CRITICAL
        Vulnerability v1 = new Vulnerability();
        v1.setTitle("SQL Injection on Login Form");
        v1.setTargetSystem("192.168.1.105 (Web Server)");
        v1.setSeverity(Severity.CRITICAL);
        v1.setProofImage(generateProofImage("SQL INJECTION", "POST /login", "' OR '1'='1' --", "Response: 302 Redirect → /admin", "All user data exposed", new Color(220, 38, 38)));
        v1.setImageContentType("image/png");
        vulnerabilityRepository.save(v1);

        // 2. Open SSH Port - HIGH
        Vulnerability v2 = new Vulnerability();
        v2.setTitle("SSH Port 22 Open with Weak Password");
        v2.setTargetSystem("10.0.0.50 (Database Server)");
        v2.setSeverity(Severity.HIGH);
        v2.setProofImage(generateProofImage("NMAP SCAN RESULT", "Target: 10.0.0.50", "PORT   STATE SERVICE", "22/tcp open  ssh  OpenSSH 7.4", "Brute-force: admin/admin123", new Color(234, 88, 12)));
        v2.setImageContentType("image/png");
        vulnerabilityRepository.save(v2);

        // 3. XSS - MEDIUM
        Vulnerability v3 = new Vulnerability();
        v3.setTitle("Reflected XSS in Search Parameter");
        v3.setTargetSystem("webapp.example.com");
        v3.setSeverity(Severity.MEDIUM);
        v3.setProofImage(generateProofImage("XSS PAYLOAD", "URL: /search?q=<script>alert(1)</script>", "Response: 200 OK", "Payload reflected in response body", "Session hijacking possible", new Color(217, 119, 6)));
        v3.setImageContentType("image/png");
        vulnerabilityRepository.save(v3);

        // 4. Directory Listing - LOW
        Vulnerability v4 = new Vulnerability();
        v4.setTitle("Directory Listing Enabled on /uploads");
        v4.setTargetSystem("files.internal.corp");
        v4.setSeverity(Severity.LOW);
        v4.setProofImage(generateProofImage("DIRECTORY LISTING", "URL: /uploads/", "Index of /uploads/", "backup_2026.sql  12MB", "credentials.txt  1KB", new Color(5, 150, 105)));
        v4.setImageContentType("image/png");
        vulnerabilityRepository.save(v4);

        // 5. RCE - CRITICAL
        Vulnerability v5 = new Vulnerability();
        v5.setTitle("Remote Code Execution via File Upload");
        v5.setTargetSystem("192.168.1.200 (App Server)");
        v5.setSeverity(Severity.CRITICAL);
        v5.setProofImage(generateProofImage("RCE EXPLOIT", "Uploaded: shell.php.jpg", "Bypass: Content-Type manipulation", "$ whoami → www-data", "$ cat /etc/passwd → SUCCESS", new Color(220, 38, 38)));
        v5.setImageContentType("image/png");
        vulnerabilityRepository.save(v5);

        // 6. Outdated TLS - MEDIUM
        Vulnerability v6 = new Vulnerability();
        v6.setTitle("TLS 1.0 Enabled - POODLE Vulnerability");
        v6.setTargetSystem("mail.example.com:443");
        v6.setSeverity(Severity.MEDIUM);
        v6.setProofImage(generateProofImage("SSL/TLS SCAN", "Target: mail.example.com:443", "TLSv1.0: ENABLED (INSECURE)", "TLSv1.1: ENABLED (INSECURE)", "Recommendation: Disable TLS < 1.2", new Color(217, 119, 6)));
        v6.setImageContentType("image/png");
        vulnerabilityRepository.save(v6);
    }

    /**
     * Programatik olarak demo proof image oluşturur.
     * Gerçek bir terminal/scan çıktısını simüle eden basit bir PNG üretir.
     * Bu sayede demo kayıtlarında BLOB görsel gösterimi test edilebilir.
     */
    private byte[] generateProofImage(String title, String line1, String line2,
                                       String line3, String line4, Color accentColor) {
        int width = 640;
        int height = 340;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Arka plan - koyu terminal
        g.setColor(new Color(15, 23, 42));
        g.fillRect(0, 0, width, height);

        // Üst başlık çubuğu
        g.setColor(new Color(30, 41, 59));
        g.fillRect(0, 0, width, 36);

        // Terminal noktaları
        g.setColor(new Color(239, 68, 68)); g.fillOval(12, 12, 12, 12);
        g.setColor(new Color(234, 179, 8)); g.fillOval(30, 12, 12, 12);
        g.setColor(new Color(34, 197, 94)); g.fillOval(48, 12, 12, 12);

        // Başlık
        g.setColor(new Color(148, 163, 184));
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.drawString("root@kali:~# " + title.toLowerCase().replace(" ", "_"), 72, 24);

        // İçerik satırları
        g.setFont(new Font("Monospaced", Font.BOLD, 15));
        g.setColor(accentColor);
        g.drawString("╔══ " + title + " ══╗", 20, 72);

        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g.setColor(new Color(196, 181, 253));
        g.drawString("$ " + line1, 20, 110);

        g.setColor(new Color(134, 239, 172));
        g.drawString("  " + line2, 20, 145);

        g.setColor(new Color(253, 224, 71));
        g.drawString("  " + line3, 20, 180);

        g.setColor(new Color(248, 113, 113));
        g.drawString("  [!] " + line4, 20, 215);

        // Alt bilgi
        g.setColor(new Color(100, 116, 139));
        g.drawString("─".repeat(50), 20, 260);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.drawString("Scan completed at " + java.time.LocalDateTime.now().toString().substring(0, 19), 20, 285);
        g.drawString("RedTeam VulnDB - Proof of Concept", 20, 310);

        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
