# 🛡️ Red Team VulnDB

**Red Team Vulnerability & Exploit Database** — Siber güvenlik uzmanlarının penetrasyon testleri sırasında keşfettikleri güvenlik açıklarını, kanıt ekran görüntüleriyle birlikte kayıt altına aldığı web portalı.

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📸 Ekran Görüntüleri

| Login | Dashboard |
|-------|-----------|
| Kurumsal giriş ekranı | Vulnerability listesi + arama |

| Detay | Dark Mode |
|-------|-----------|
| BLOB görsel render | Koyu tema desteği |

---

## ✨ Özellikler

- 🔐 **Kimlik Doğrulama** — Spring Security ile form-based login + BCrypt şifreleme
- 📋 **CRUD Operasyonları** — Vulnerability ekleme, listeleme, güncelleme, silme
- 🖼️ **BLOB Görsel Depolama** — Kanıt görselleri veritabanında `byte[]` olarak saklanır (`@Lob` yerine `columnDefinition = "BYTEA"`)
- 🔍 **Akıllı Arama** — Başlık ve hedef sistem alanlarında eşzamanlı case-insensitive arama (JPQL `@Query`)
- 🌙 **Dark Mode** — LocalStorage ile kalıcı tema tercihi
- 📱 **Responsive Tasarım** — Mobil uyumlu kurumsal UI
- 🐳 **Docker Desteği** — Tek komutla kurulum (`docker compose up`)
- 🎯 **Demo Verileri** — Uygulama başlangıcında 6 gerçekçi vulnerability kaydı otomatik oluşturulur

---

## 🏗️ Mimari

```
Katmanlı Mimari (Layered Architecture)
┌──────────────────────────────────────┐
│         Presentation Layer           │
│   Thymeleaf + CSS + JavaScript       │
├──────────────────────────────────────┤
│          Controller Layer            │
│  AuthController, VulnController,     │
│  ImageController                     │
├──────────────────────────────────────┤
│           Service Layer              │
│  VulnerabilityService,               │
│  OperatorDetailsService              │
├──────────────────────────────────────┤
│          Repository Layer            │
│  OperatorRepository,                 │
│  VulnerabilityRepository             │
├──────────────────────────────────────┤
│           Entity Layer               │
│  Operator, Vulnerability, Severity   │
├──────────────────────────────────────┤
│           PostgreSQL                 │
└──────────────────────────────────────┘
```

---

## 🛠️ Teknoloji Stack

| Katman | Teknoloji |
|--------|-----------|
| Backend | Java 17, Spring Boot 3.2.5 |
| Security | Spring Security, BCrypt |
| ORM | Spring Data JPA (Hibernate 6) |
| Database | PostgreSQL 16 |
| Frontend | Thymeleaf, HTML5, CSS3, Vanilla JS |
| Build | Maven |
| Deploy | Docker, Docker Compose |

---

## 🚀 Kurulum

### Yöntem 1: Docker ile (Önerilen) ⭐

Sadece [Docker](https://docs.docker.com/get-docker/) ve [Docker Compose](https://docs.docker.com/compose/install/) gerekir.

```bash
# 1. Repoyu klonla
git clone https://github.com/KULLANICI_ADI/redteam-vulndb.git
cd redteam-vulndb

# 2. Tek komutla başlat (PostgreSQL + Uygulama)
docker compose up --build

# 3. Tarayıcıda aç
# http://localhost:8080
# Giriş: admin / admin123
```

Durdurmak için:
```bash
docker compose down          # Durdur
docker compose down -v       # Durdur + veritabanı verilerini sil
```

### Yöntem 2: Manuel Kurulum

#### Gereksinimler
- Java 17+ (`java -version`)
- Maven 3.8+ (`mvn -version`)
- PostgreSQL 14+ (`psql --version`)

#### Adımlar

```bash
# 1. Repoyu klonla
git clone https://github.com/KULLANICI_ADI/redteam-vulndb.git
cd redteam-vulndb

# 2. PostgreSQL veritabanı oluştur
sudo systemctl start postgresql
sudo -u postgres psql -c "CREATE DATABASE vulndb;"
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'postgres';"

# 3. Uygulamayı başlat
mvn spring-boot:run

# 4. Tarayıcıda aç
# http://localhost:8080
# Giriş: admin / admin123
```

> **Not:** Farklı veritabanı bilgileri kullanmak istiyorsanız `src/main/resources/application.properties` dosyasını düzenleyin.

---

## 📁 Proje Yapısı

```
redteam-vulndb/
├── pom.xml                                    # Maven bağımlılıkları
├── Dockerfile                                 # Multi-stage Docker build
├── docker-compose.yml                         # PostgreSQL + App orchestration
├── .gitignore
├── README.md
└── src/main/
    ├── java/com/redteam/vulndb/
    │   ├── VulnDbApplication.java             # @SpringBootApplication entry point
    │   ├── entity/
    │   │   ├── Severity.java                  # Enum (LOW, MEDIUM, HIGH, CRITICAL)
    │   │   ├── Operator.java                  # Kullanıcı entity
    │   │   └── Vulnerability.java             # Vulnerability + BLOB proof image
    │   ├── repository/
    │   │   ├── OperatorRepository.java        # findByUsername (Derived Query)
    │   │   └── VulnerabilityRepository.java   # searchByKeyword (@Query JPQL)
    │   ├── service/
    │   │   ├── OperatorDetailsService.java    # Spring Security UserDetailsService
    │   │   └── VulnerabilityService.java      # CRUD + MultipartFile→byte[]
    │   ├── controller/
    │   │   ├── AuthController.java            # Login page
    │   │   ├── VulnerabilityController.java   # 7 CRUD endpoints
    │   │   └── ImageController.java           # BLOB → HTTP Response
    │   └── config/
    │       ├── SecurityConfig.java            # Filter chain + BCrypt
    │       └── DataInitializer.java           # Seed data (admin + 6 demo vulns)
    └── resources/
        ├── application.properties             # DB + JPA + Multipart config
        ├── templates/
        │   ├── login.html                     # Giriş ekranı
        │   ├── dashboard.html                 # Ana sayfa + tablo + arama
        │   ├── vulnerability-form.html        # Ekleme/düzenleme formu
        │   └── vulnerability-detail.html      # Detay + BLOB render
        └── static/
            ├── css/terminal.css               # Kurumsal tema + dark mode
            └── js/app.js                      # Theme toggle + validation
```

---

## 🔑 Varsayılan Kullanıcı

| Kullanıcı Adı | Şifre | Rol |
|---------------|-------|-----|
| `admin` | `admin123` | OPERATOR |

> Uygulama ilk başlatıldığında `DataInitializer` tarafından otomatik oluşturulur. Şifre BCrypt ile hash'lenerek veritabanına kaydedilir.

---

## 📊 API Endpoint'leri

| Method | URL | Açıklama |
|--------|-----|----------|
| GET | `/login` | Login sayfası |
| GET | `/dashboard` | Vulnerability listesi |
| GET | `/dashboard?search=keyword` | Arama (title + targetSystem) |
| GET | `/vulnerability/new` | Yeni kayıt formu |
| POST | `/vulnerability/save` | Kaydet (multipart/form-data) |
| GET | `/vulnerability/{id}` | Detay sayfası |
| GET | `/vulnerability/{id}/edit` | Düzenleme formu |
| POST | `/vulnerability/{id}/update` | Güncelle |
| POST | `/vulnerability/{id}/delete` | Sil |
| GET | `/image/{id}` | BLOB görseli serve et |

---

## 🔒 Güvenlik

- **BCrypt** ile şifre hash'leme (tek yönlü, salt'lı)
- **Spring Security Filter Chain** ile URL bazlı erişim kontrolü
- **CSRF koruması** aktif (Thymeleaf otomatik token)
- **Session-based authentication**
- POST metodu ile silme (GET ile silme güvenlik riski)

---

## 📝 Lisans

Bu proje üniversite ödevi kapsamında geliştirilmiştir.
