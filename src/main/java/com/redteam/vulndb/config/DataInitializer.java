package com.redteam.vulndb.config;

import com.redteam.vulndb.entity.*;
import com.redteam.vulndb.repository.OperatorRepository;
import com.redteam.vulndb.repository.ProjectRepository;
import com.redteam.vulndb.repository.VulnerabilityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class DataInitializer implements CommandLineRunner {

    private final OperatorRepository operatorRepository;
    private final ProjectRepository projectRepository;
    private final VulnerabilityRepository vulnerabilityRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(OperatorRepository operatorRepository,
                           ProjectRepository projectRepository,
                           VulnerabilityRepository vulnerabilityRepository,
                           PasswordEncoder passwordEncoder) {
        this.operatorRepository = operatorRepository;
        this.projectRepository = projectRepository;
        this.vulnerabilityRepository = vulnerabilityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (operatorRepository.findByUsername("admin").isEmpty()) {
            Operator admin = new Operator();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            operatorRepository.save(admin);
            System.out.println("[+] Admin oluşturuldu: admin / admin123");
        }

        if (projectRepository.count() == 0) {
            createDemoProjects();
            System.out.println("[+] 3 demo proje ve 10 vulnerability oluşturuldu.");
        }
    }

    private void createDemoProjects() {
        // ===== PROJE 1: Web App Pentest =====
        Project p1 = new Project("Banka Web Uygulaması Pentest", 
            "ABC Bankası online bankacılık portalının güvenlik testi. Kapsam: Login, API, ödeme modülleri.");
        p1.setStatus(ProjectStatus.ACTIVE);
        p1 = projectRepository.save(p1);

        addVuln(p1, "SQL Injection on Login Form", "192.168.1.105 (Web Server)", 
            Severity.CRITICAL, "SQL INJECTION", "POST /login", "' OR '1'='1' --", 
            "Response: 302 → /admin", "All user data exposed", new Color(220, 38, 38));

        addVuln(p1, "Reflected XSS in Search Parameter", "webapp.example.com", 
            Severity.MEDIUM, "XSS PAYLOAD", "URL: /search?q=<script>alert(1)</script>", 
            "Response: 200 OK", "Payload reflected in body", "Session hijacking possible", new Color(217, 119, 6));

        addVuln(p1, "Remote Code Execution via File Upload", "192.168.1.200 (App Server)", 
            Severity.CRITICAL, "RCE EXPLOIT", "Uploaded: shell.php.jpg", 
            "Bypass: Content-Type manipulation", "$ whoami → www-data", 
            "$ cat /etc/passwd → SUCCESS", new Color(220, 38, 38));

        addVuln(p1, "Insecure Direct Object Reference (IDOR)", "api.bank.example.com", 
            Severity.HIGH, "IDOR EXPLOIT", "GET /api/accounts/1001", 
            "Changed to /api/accounts/1002", "Other user's data returned", 
            "No authorization check", new Color(234, 88, 12));

        // ===== PROJE 2: İç Ağ Taraması =====
        Project p2 = new Project("İç Ağ Güvenlik Taraması", 
            "Kurumsal iç ağdaki (10.0.0.0/24) sunucuların güvenlik değerlendirmesi.");
        p2.setStatus(ProjectStatus.COMPLETED);
        p2 = projectRepository.save(p2);

        addVuln(p2, "SSH Port 22 Open with Weak Password", "10.0.0.50 (Database Server)", 
            Severity.HIGH, "NMAP SCAN", "Target: 10.0.0.50", "PORT  STATE SERVICE", 
            "22/tcp open ssh OpenSSH 7.4", "Brute-force: admin/admin123", new Color(234, 88, 12));

        addVuln(p2, "Directory Listing Enabled on /uploads", "files.internal.corp", 
            Severity.LOW, "DIRECTORY LISTING", "URL: /uploads/", "Index of /uploads/", 
            "backup_2026.sql  12MB", "credentials.txt  1KB", new Color(5, 150, 105));

        addVuln(p2, "TLS 1.0 Enabled - POODLE Vulnerability", "mail.example.com:443", 
            Severity.MEDIUM, "SSL/TLS SCAN", "Target: mail.example.com:443", 
            "TLSv1.0: ENABLED (INSECURE)", "TLSv1.1: ENABLED (INSECURE)", 
            "Disable TLS < 1.2", new Color(217, 119, 6));

        addVuln(p2, "SMB Signing Not Required", "10.0.0.10 (Domain Controller)", 
            Severity.MEDIUM, "SMB SCAN", "Target: 10.0.0.10:445", 
            "SMB Signing: NOT REQUIRED", "Relay attack possible", 
            "NTLM hash capture risk", new Color(217, 119, 6));

        // ===== PROJE 3: Mobil API Pentest =====
        Project p3 = new Project("Mobil Uygulama API Pentest", 
            "XYZ şirketinin mobil uygulamasının REST API güvenlik analizi.");
        p3.setStatus(ProjectStatus.ACTIVE);
        p3 = projectRepository.save(p3);

        addVuln(p3, "JWT Token Not Validated", "api.mobile.example.com", 
            Severity.CRITICAL, "JWT BYPASS", "Authorization: Bearer <modified>", 
            "alg: none attack successful", "Admin endpoints accessible", 
            "No signature verification", new Color(220, 38, 38));

        addVuln(p3, "Rate Limiting Not Implemented on OTP", "api.mobile.example.com/otp", 
            Severity.HIGH, "BRUTE FORCE", "POST /api/otp/verify", 
            "1000 requests in 60 seconds", "No rate limit detected", 
            "OTP bypass in ~5 minutes", new Color(234, 88, 12));
    }

    private void addVuln(Project project, String title, String target, Severity severity,
                         String imgTitle, String l1, String l2, String l3, String l4, Color color) {
        Vulnerability v = new Vulnerability();
        v.setTitle(title);
        v.setTargetSystem(target);
        v.setSeverity(severity);
        v.setProject(project);
        v.setProofImage(generateProofImage(imgTitle, l1, l2, l3, l4, color));
        v.setImageContentType("image/png");
        vulnerabilityRepository.save(v);
    }

    private byte[] generateProofImage(String title, String l1, String l2, String l3, String l4, Color accent) {
        int w = 640, h = 340;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(15, 23, 42)); g.fillRect(0, 0, w, h);
        g.setColor(new Color(30, 41, 59)); g.fillRect(0, 0, w, 36);
        g.setColor(new Color(239, 68, 68)); g.fillOval(12, 12, 12, 12);
        g.setColor(new Color(234, 179, 8)); g.fillOval(30, 12, 12, 12);
        g.setColor(new Color(34, 197, 94)); g.fillOval(48, 12, 12, 12);
        g.setColor(new Color(148, 163, 184));
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.drawString("root@kali:~# " + title.toLowerCase().replace(" ", "_"), 72, 24);
        g.setFont(new Font("Monospaced", Font.BOLD, 15));
        g.setColor(accent); g.drawString("[*] " + title, 20, 72);
        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g.setColor(new Color(196, 181, 253)); g.drawString("$ " + l1, 20, 110);
        g.setColor(new Color(134, 239, 172)); g.drawString("  " + l2, 20, 145);
        g.setColor(new Color(253, 224, 71)); g.drawString("  " + l3, 20, 180);
        g.setColor(new Color(248, 113, 113)); g.drawString("  [!] " + l4, 20, 215);
        g.setColor(new Color(100, 116, 139));
        g.drawString("─".repeat(50), 20, 260);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.drawString("Scan: " + java.time.LocalDateTime.now().toString().substring(0, 19), 20, 285);
        g.drawString("RedTeam VulnDB - Proof of Concept", 20, 310);
        g.dispose();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) { return new byte[0]; }
    }
}
