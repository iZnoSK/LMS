# 📚 Simple Library Management System

Jednoduchá webová aplikácia vytvorená pomocou Spring Boot, ktorá umožňuje spravovať knihy a ich kópie.

---

## 🔧 Využité technológie

- Java 24
- Spring Boot
- Spring Data JPA
- H2 In-Memory Database
- Swagger / OpenAPI (springdoc-openapi)
- Maven
- JUnit (unit testy)
- Lombok

---

## 🚀 Ako spustiť aplikáciu
### 🛠️ Požiadavky

* Java 17+
* Git
* (Maven nie je potrebný – projekt používa **Maven Wrapper**)
* Odporúčané IDE: IntelliJ IDEA Ultimate 

---

### 📅 Klonovanie repozitára

```bash
git clone https://github.com/iZnoSK/LMS.git
cd LMS
```

---

### ▶️ Spustenie aplikácie

#### 🖥️ A) cez terminál

**Windows:**

```bash
mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

---

#### 💡 B) cez IntelliJ IDEA

##### 1. Otvor projekt:
 - Spusti IntelliJ IDEA.
 - Klikni na File > Open a vyber priečinok s projektom (ten, kde sa nachádza pom.xml).
##### 2. Nechaj IntelliJ načítať závislosti:
- Po otvorení IntelliJ automaticky načíta Maven dependencies.
- Ak nie, otvor pom.xml a klikni na Load Maven Changes.
##### 3. Spusti aplikáciu:
 - Otvor súbor Main.java.
 - Klikni na ikonu ▶️ vedľa metódy main alebo použij klávesovú skratku Shift + F10.

---

### 🌐 Prístup k aplikácii

Po spustení bude aplikácia bežať na:

```
http://localhost:8080
```

Ak máš zapnutý Swagger/OpenAPI, dokumentáciu API je možné nájsť tu:

```
http://localhost:8080/swagger-ui/index.html
```

---

