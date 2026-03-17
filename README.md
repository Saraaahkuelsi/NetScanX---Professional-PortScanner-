# NetScanX 
### A Smart Port Scanner

> Professional port scanner with CVE vulnerability detection, OS fingerprinting, firewall detection and Spring Boot web interface.




##  Features

-  **TCP/UDP Port Scanning** — Multi-threaded scanning with configurable port ranges
-  **CVE Detection** — Real-time vulnerability detection using NVD API
-  **OS Fingerprinting** — Operating system identification via banner, ports, HTTP headers and TTL
-  **Firewall Detection** — Distinguish between CLOSED and FILTERED ports
-  **Subnet Scanner** — Scan entire networks using CIDR notation (e.g. 192.168.1.0/24)
-  **Real-time Alerts** — Instant notifications for CRITICAL and HIGH vulnerabilities
-  **Scan History** — Save and review all past scans
-  **HTML & JSON Reports** — Export scan results in multiple formats
-  **Stealth Mode** — Random delays to avoid detection
   **Web Interface** — Clean and professional Spring Boot web UI



## 🛠️ Technologies

| Technology | Usage |
|-----------|-------|
| Java 21 | Core language |
| Spring Boot 3.2 | Web framework |
| Thymeleaf | HTML templating |
| Jackson | JSON serialization |
| Maven | Dependency management |
| NVD API | CVE data source |
| ExecutorService | Multi-threading |



##  Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+

### Installation
```bash
# Clone the repository
git clone https://github.com/TonNom/NetScanX.git

# Navigate to project directory
cd NetScanX

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Access the application
```
http://localhost:8080
```

---

##  Project Structure
```
com.portscanner/
├── core/         → Scan engine, policies and main entry points
├── model/        → Data models (PortResult, ScanReport, ScanTarget)
├── scanner/      → TCP/UDP scanners and service detection
├── security/     → CVE fetcher, vulnerability checker, audit logger
├── report/       → HTML and JSON report generators
├── util/         → Network utilities, OS fingerprinter
└── web/          → Spring Boot controllers and services
```

---

##  Usage

### Single Host Scan
```
Host     : 192.168.1.1
Port range: 1 - 1024
Mode     : Host
```

### Subnet Scan
```
CIDR     : 192.168.1.0/24
Port range: 1 - 1024
Mode     : Subnet
```

---

##  Legal Disclaimer

> This tool is intended for **educational purposes** and **authorized security testing only**.
> Scanning systems without explicit permission is illegal.
> The author is not responsible for any misuse of this tool.



## 👩‍💻 Author

**SARA ACHAHBAR** —  Cybersecurity Engineering Student 

---

## 📜 License

This project is licensed under the MIT License.


