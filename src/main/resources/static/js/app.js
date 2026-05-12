/**
 * Red Team VulnDB - Client-side JavaScript
 * Dark mode toggle, silme onayı, dosya boyutu kontrolü
 */

// ==================== DARK MODE ====================

/** Tema tercihini localStorage'dan yükle */
function loadTheme() {
    var saved = localStorage.getItem('vulndb-theme');
    if (saved === 'dark') {
        document.documentElement.setAttribute('data-theme', 'dark');
        updateToggleIcon('dark');
    }
}

/** Tema değiştir (light ↔ dark) */
function toggleTheme() {
    var current = document.documentElement.getAttribute('data-theme');
    var next = current === 'dark' ? 'light' : 'dark';

    if (next === 'dark') {
        document.documentElement.setAttribute('data-theme', 'dark');
    } else {
        document.documentElement.removeAttribute('data-theme');
    }

    localStorage.setItem('vulndb-theme', next);
    updateToggleIcon(next);
}

/** Toggle butonundaki ikonu güncelle */
function updateToggleIcon(theme) {
    var btn = document.querySelector('.theme-toggle');
    if (btn) {
        btn.textContent = theme === 'dark' ? '☀️' : '🌙';
    }
}

// Sayfa yüklendiğinde temayı uygula
loadTheme();

// ==================== DELETE CONFIRMATION ====================

function confirmDelete(form) {
    return confirm('Bu vulnerability kaydı kalıcı olarak silinecek.\nDevam etmek istiyor musunuz?');
}

// ==================== FILE SIZE VALIDATION ====================

document.addEventListener('DOMContentLoaded', function () {
    var fileInput = document.getElementById('imageFile');
    if (fileInput) {
        fileInput.addEventListener('change', function () {
            var maxSize = 10 * 1024 * 1024; // 10MB
            if (this.files.length > 0 && this.files[0].size > maxSize) {
                alert('Dosya boyutu çok büyük!\nMaksimum: 10MB\nSeçilen: ' +
                    (this.files[0].size / (1024 * 1024)).toFixed(2) + 'MB');
                this.value = '';
            }
        });
    }

    // Başarı mesajını 5 saniye sonra gizle
    var alerts = document.querySelectorAll('.alert-success');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () { alert.style.display = 'none'; }, 500);
        }, 5000);
    });
});
