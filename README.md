*HyperLogLog (HLL) - Cardinality Estimation Project*
Bu proje, büyük veri analitiğinde kullanılan olasılıksal veri yapılarından biri olan HyperLogLog (HLL) algoritmasının Java ile sıfırdan gerçeklenmesini içermektedir. Projenin temel amacı, bellekten tasarruf ederek çok büyük veri setlerindeki benzersiz (unique) öğe sayısını düşük hata payı ile tahmin etmektir.

🚀 Proje Bileşenleri
Algoritma, ödev tanımında belirtilen aşağıdaki temel bileşenleri içermektedir:
Hasing (Karıştırma): Veriler, uniform dağılım sağlayan bir hash fonksiyonu ile 64-bit değerlere dönüştürülür.
Bucketing (Kovalama): Hash değerinin ilk $b$ biti kullanılarak veri $m = 2^b$ adet kovaya dağıtılır.
Register Yapısı: Her kova (register), o kovaya düşen hash değerlerinin binary temsilindeki en uzun ardışık sıfır serisini saklar.
Harmonik Ortalama: Tahmin hesaplanırken aritmetik ortalama yerine, uç değerlere (outliers) karşı daha dirençli olan harmonik ortalama formülü kullanılır.
Düzeltme Faktörleri: Küçük veri setleri için Linear Counting düzeltmesi ve kova sayısına göre değişen alpha_m bias düzeltmesi uygulanmıştır.
Merge (Birleştirme): İki farklı HLL yapısının veri kaybı olmadan Math.max mantığıyla birleştirilebilmesi özelliği eklenmiştir.

📊 Teorik Analiz ve Hata Sınırları
HLL algoritmasında tahmin hassasiyeti doğrudan kova sayısına ($m$) bağlıdır. 
Kova sayısı arttıkça bellek kullanımı artar ancak tahmin hatası azalır.
Standart hata formülü: 1.04/(KÖK M)
b (Bit)   m(Kova Sayısı)  Tahmini Hata Payı   (%)Bellek Kullanımı
10       1024                %3.25                   -1 KB
12       4096                %1.62                   -4 KB
14       16384               %0.81                   -16 KB

🛠️ Kurulum ve Çalıştırma
Bilgisayarınızda Java JDK'nın (8 veya üzeri) yüklü olduğundan emin olun.
Main.java dosyasını indirin.
Terminal veya komut satırından derleyin:
Bash
javac Main.java
Çalıştırın:
Bash
java Main

🤖 Agentic Kodlama Yaklaşımı
Bu proje geliştirilirken yapay zeka modelleriyle (Gemini) iş birliği yapılmış, kodun modüler yapısı, hata yönetimi ve matematiksel doğruluğu adım adım kontrol edilerek "Agentic Kodlama" prensiplerine uygun olarak tasarlanmıştır.

