# Red Team Vulnerability Database - Sunum Kopya Kağıdı

Bu doküman, proje sunumunuz sırasında hocanızın sorabileceği teknik sorulara hızlıca cevap verebilmeniz ve ilgili kod bloklarını anında bulabilmeniz için hazırlanmıştır.

## 🗂️ Proje Yapısı ve Dosyaların Görevleri

Proje, modern Java web uygulamalarında endüstri standardı olan **MVC (Model-View-Controller)** mimarisine uygun olarak geliştirilmiştir.

### 1. Entities (Veritabanı Tabloları / Modeller)
**Yol:** `src/main/java/com/redteam/vulndb/entity/`
*   `Vulnerability.java`: Zafiyetlerin (Bulguların) tutulduğu ana tablodur. (Örn: Zafiyet adı, açıklaması, CVSS skoru).
*   `Project.java`: Sızma testi yapılan projelerin/hedeflerin tutulduğu tablodur.
*   `Operator.java`: Sistemi kullanan siber güvenlik uzmanlarının (kullanıcıların) bilgilerini tutar.
*   `Severity.java`: Zafiyetin kritiklik seviyesini (Düşük, Orta, Yüksek, Kritik) belirleyen enum yapısıdır.
*   `ProjectStatus.java`: Projenin durumunu (Aktif, Tamamlandı vb.) belirleyen enum yapısıdır.

### 2. Repositories (Veritabanı İşlemleri)
**Yol:** `src/main/java/com/redteam/vulndb/repository/`
*   `VulnerabilityRepository.java`, `ProjectRepository.java`, `OperatorRepository.java`
*   **Görevleri:** Spring Data JPA kullanarak veritabanına kayıt ekleme, silme, güncelleme ve listeleme (CRUD) işlemlerini SQL yazmadan yapmamızı sağlayan arayüzlerdir (Interface).

### 3. Services (İş Mantığı)
**Yol:** `src/main/java/com/redteam/vulndb/service/`
*   `VulnerabilityService.java`, `ProjectService.java`
*   **Görevleri:** Controller'dan gelen verilerin veritabanına yazılmadan önce işlendiği, kontrollerin yapıldığı katmandır (Örn: Veri doğrulama, hesaplamalar).
*   `OperatorDetailsService.java`: Kullanıcı giriş (Login) işlemleri için Spring Security altyapısına kullanıcının veritabanından nasıl çekileceğini söyler.

### 4. Controllers (Web İsteklerini Karşılayan Katman)
**Yol:** `src/main/java/com/redteam/vulndb/controller/`
*   `VulnerabilityController.java`: Zafiyet ekleme, silme, düzenleme ve listeleme sayfalarının adreslerini (`@GetMapping`, `@PostMapping`) yönetir.
*   `ProjectController.java`: Projelerle ilgili sayfa yönlendirmelerini ve veri işlemlerini yapar.
*   `AuthController.java`: Kayıt olma (Register) ve Giriş yapma (Login) işlemlerini karşılar.
*   `ImageController.java`: Zafiyetlere ait ekran görüntülerini (PoC) sisteme yükleme ve gösterme işlemlerini yönetir.

### 5. Frontend (Önyüz ve Tasarım)
**Yol:** `src/main/resources/templates/` (HTML Dosyaları)
*   **Thymeleaf** motoru kullanılmıştır. Saf HTML içine Java'dan gelen verileri (`th:text`, `th:each` gibi etiketlerle) basmamızı sağlar.
*   `dashboard.html`: Ana zafiyet listesinin olduğu panel.
*   `projects.html`: Projelerin listelendiği sayfa.
*   `vulnerability-form.html`: Yeni zafiyet ekleme formu.
*   `vulnerability-detail.html`: Zafiyetin detaylarının ve resimlerinin gösterildiği sayfa.
*   `login.html` ve `register.html`: Giriş ve kayıt sayfaları.

---

## 🎯 Hoca Soru Sorarsa Kodu Nerede Göstermelisiniz?

Sunum sırasında hocanızın sorabileceği muhtemel sorular ve açmanız gereken dosyalar:

> [!TIP]
> **Soru:** *"Veritabanı bağlantısını ve ayarlarını nerede yaptınız?"*
> **Cevap:** `src/main/resources/application.properties` dosyasını açın. Burada veritabanı URL'i, kullanıcı adı, şifresi ve Hibernate ayarları yer alır.

> [!TIP]
> **Soru:** *"Veritabanı tablolarını kodda nasıl oluşturdunuz?"*
> **Cevap:** `entity` klasöründeki `Vulnerability.java` veya `Project.java` dosyasını açın. Sınıfın başındaki `@Entity` ve `@Table` anotasyonlarını, değişkenlerin başındaki `@Id`, `@Column` anotasyonlarını gösterin.

> [!TIP]
> **Soru:** *"Kullanıcı girişini (Login/Auth) nasıl güvenli hale getirdiniz?"*
> **Cevap:** `service` klasöründeki `OperatorDetailsService.java` dosyasını gösterin. Spring Security'nin `UserDetailsService` arayüzünü implement ederek veritabanından kullanıcıyı nasıl çektiğinizi anlatın.

> [!TIP]
> **Soru:** *"Yeni bir zafiyet eklendiğinde arka planda akış nasıl gerçekleşiyor?"*
> **Cevap:** Sırasıyla şu akışı gösterin:
> 1. `templates/vulnerability-form.html` (Formun gönderildiği yer).
> 2. `controller/VulnerabilityController.java` (Formun ulaştığı `@PostMapping` metodu).
> 3. `repository/VulnerabilityRepository.java` (`.save()` metodu ile veritabanına kaydın yapıldığı arayüz).

> [!TIP]
> **Soru:** *"Proje ve Zafiyet tabloları arasındaki bağlantıyı (Relation) nasıl kurdunuz?"*
> **Cevap:** `entity/Vulnerability.java` dosyasını açın. İçerisinde `Project` değişkeninin üzerinde bulunan `@ManyToOne` (veya `@OneToMany`) anotasyonunu göstererek "Bir zafiyet bir projeye aittir" ilişkisini kurduğunuzu söyleyin.

> [!TIP]
> **Soru:** *"Ekran görüntülerini (Resimleri) sisteme nasıl yüklüyorsunuz?"*
> **Cevap:** `controller/ImageController.java` dosyasını açın. Dosyayı sunucu klasörlerine mi yoksa veritabanına mı (byte array olarak) kaydettiğinizi buradaki kod üzerinden anlatın.

> [!TIP]
> **Soru:** *"Java'daki verileri HTML sayfasına nasıl aktarıyorsunuz?"*
> **Cevap:** Örnek olarak `controller/VulnerabilityController.java` içinde `model.addAttribute("vulnerabilities", list)` yazan kısmı gösterin. Ardından `templates/dashboard.html` dosyasını açıp HTML içindeki `th:each="vuln : ${vulnerabilities}"` döngüsünü göstererek bağlantıyı açıklayın.

> [!TIP]
> **Soru:** *"Hiç SQL yazmadan veritabanından veriyi nasıl çekiyorsunuz?"*
> **Cevap:** `repository/VulnerabilityRepository.java` dosyasını açın. Bunun bir `JpaRepository`'den miras (extends) aldığını ve Spring Data JPA'nın `findAll()`, `findById()` gibi metotları bizim yerimize otomatik olarak SQL'e çevirdiğini belirtin. Eğer dosya içinde özel metodlar (örneğin `findByProjectId`) varsa, JPA'nın isimlendirme standartlarından yola çıkarak SQL ürettiğini açıklayın.
