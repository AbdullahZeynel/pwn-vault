/**
 * Red Team VulnDB - Client-side JavaScript
 * 
 * İçerik:
 * - Silme onay dialogu
 * - Dosya boyutu kontrolü (10MB limit)
 * - Arama alanında debounce (opsiyonel)
 */

/**
 * Silme işlemi öncesi onay dialogu.
 * Dashboard ve detay sayfasında delete formlarında kullanılır.
 * 
 * @param {HTMLFormElement} form - silme formu
 * @returns {boolean} kullanıcı onaylarsa true
 */
function confirmDelete(form) {
    return confirm('⚠ DİKKAT: Bu vulnerability kaydı kalıcı olarak silinecek.\n\nDevam etmek istiyor musunuz?');
}

/**
 * Dosya yükleme boyut kontrolü.
 * 10MB üzerindeki dosyalar için uyarı gösterir.
 */
document.addEventListener('DOMContentLoaded', function () {

    // Dosya input kontrolü
    var fileInput = document.getElementById('imageFile');
    if (fileInput) {
        fileInput.addEventListener('change', function () {
            var maxSize = 10 * 1024 * 1024; // 10MB in bytes
            if (this.files.length > 0) {
                var file = this.files[0];
                if (file.size > maxSize) {
                    alert('⚠ Dosya boyutu çok büyük!\n\nMaksimum: 10MB\nSeçilen: ' +
                        (file.size / (1024 * 1024)).toFixed(2) + 'MB');
                    this.value = ''; // Input'u temizle
                }
            }
        });
    }

    // Başarı mesajını 5 saniye sonra otomatik gizle
    var alerts = document.querySelectorAll('.alert-success');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () {
                alert.style.display = 'none';
            }, 500);
        }, 5000);
    });
});
