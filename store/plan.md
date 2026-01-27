# Shitdows-stXOR - Microsoft Store Package ID Extractor

## 🎯 **Visión general**

**Shitdows-stXOR** es una aplicación Java GUI que extrae **ProductId** y **PackageFamilyName** de apps Microsoft Store usando la **misma API** que usa `apps.microsoft.com` (capturada via HAR analysis). [ppl-ai-file-upload.s3.amazonaws](https://ppl-ai-file-upload.s3.amazonaws.com/web/direct-files/attachments/4447468/338df08b-c6f7-4469-98bd-83a5fe3ddb8a/apps.microsoft.com.har)

**Flujo**:
```
Nombre app ("vantage") → API Microsoft → ProductId + Package → RG-Adguard (.appx)
```

## 🛠️ **Estructura VSCode Java + Maven**

```
Shitdows-stXOR/
├── pom.xml                 ← Dependencias Maven
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java           ← Tu GUI actual
│   │   │   ├── model/
│   │   │   │   └── AppInfo.java    ← DTO app
│   │   │   └── api/
│   │   │       ├── MicrosoftStoreAPI.java  ← Búsqueda
│   │   │       └── RgAdguardAPI.java       ← Descargas
│   │   └── resources/
│   │       └── icon.png
│   └── test/java/          ← Tests (opcional)
└── target/                 ← Build output (Maven)
```

## 📦 **pom.xml** (Dependencias)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.shitdows</groupId>
    <artifactId>stxor</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Jsoup (HTML parsing RG-Adguard) -->
        <dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.18.1</version>
        </dependency>
        
        <!-- Gson (Microsoft Store JSON) -->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.11.0</version>
        </dependency>
        
        <!-- HTTP Client (Java 21 built-in + extras) -->
        <dependency>
            <groupId>org.apache.httpcomponents.client5</groupId>
            <artifactId>httpclient5</artifactId>
            <version>5.3.1</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Maven Shade (fat JAR ejecutable) -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>Main</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

## 🚀 **Implementación paso a paso**

### **1. Crear proyecto Maven (VSCode)**

```
Ctrl+Shift+P → "Java: Create Java Project"
→ Maven → Next → GroupId: com.shitdows → ArtifactId: stxor
```

### **2. src/main/java/model/AppInfo.java**

```java
public class AppInfo {
    public String name, productId, packageFamily, publisher;
    
    public AppInfo(String name, String productId, String packageFamily, String publisher) {
        this.name = name;
        this.productId