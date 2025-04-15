# IoT Sensors Data Streaming API

A Spring Boot-based REST API to ingest, store, and aggregate data from simulated IoT sensors (e.g., temperature, humidity).

---

## ✅ Tech Stack

- **Java 17**
- **Spring Boot 3.4.1**
- **Spring Data JPA (H2 in-memory DB)**
- **MapStruct** for DTO ↔ Entity mapping
- **Spring Security (Basic Auth)**
- **Spring Scheduler** for simulation
- **OpenAPI / Swagger UI**
- **JUnit, Mockito, MockMvc** for testing

---

## 🚀 How to Run the Application

### 1. Clone the Repository
```bash
git clone https://github.com/SreeniAmili/iot-data-streaming.git
cd iot-data-streaming
```

### 2. Build with Maven
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

OR

```bash
java -jar target/iot-data-streaming-0.0.1-SNAPSHOT.jar
```

---

## 🔑 Authentication

| Username | Password |
|----------|----------|
| `admin`  | `password` |

---

## 📂 API Endpoints

### ▶️ POST: Ingest Sensor Data

**URL**: `/api/v1/sensors/data`  
**Method**: `POST`

**Sample Request**:
```json
{
  "deviceId": "thermostat-1",
  "metric": "temperature",
  "metricValue": 23.5,
  "dataTimestamp": "2025-04-10T10:00:00",
  "deviceType": "thermostat"
}
```

**Response**: `201 Created`

---

### 📊 GET: Aggregated Metrics

**URL**: `/api/v1/sensors/data/stats`  
**Method**: `GET`  
**Query Parameters**:
- `deviceId=thermostat-1`
- `metric=temperature`

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Aggregation fetched successfully",
  "data": {
    "metric": "temperature",
    "min": 22.0,
    "max": 30.0,
    "avg": 25.5,
    "median": 25.0
  }
}
```

**Response (204 No Content)**: If no data found

---

### 📊 GET: Aggregated Metrics by Time Range

**URL**: `/api/v1/sensors/data/stats/range`  
**Method**: `GET`  
**Query Parameters**:
- `deviceId=thermostat-1`
- `metric=temperature`
- `from=2025-04-10T08:00:00`
- `to=2025-04-10T10:00:00`

**Response**:
Same structure as above. Returns `204 No Content` if no data in range.

---

## 📘 Swagger UI & DB Console

- **Swagger UI**:  
  [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

- **H2 Console**:  
  [http://localhost:8081/h2-console](http://localhost:8081/h2-console)  
  JDBC URL: `jdbc:h2:mem:testdb`

---

## 🧪 Simulator

Simulates IoT device data every 2 seconds.

### Configure in `application.yml`
```yaml
simulator:
  enabled: true
  fixed-rate-ms: 2000
  max-records: 100
  allow-spikes: true
```

Disable it by setting:
```yaml
simulator:
  enabled: false
```

---

## 🛡️ Error Handling

- Validation and business errors return structured `ErrorResponse`.
- Missing path returns 404 with friendly message.
- Empty query result → `204 No Content`
- Invalid input → `400 Bad Request`

---

## 📁 Profiles

Supports multiple Spring profiles.

### Default (no profile):
- Uses H2 DB
- In-memory simulation

### Run with `dev` profile:
```bash
-Dspring.profiles.active=dev
```

---

## ✅ Acceptance Criteria Covered

- [x] Sensor Data ingestion via REST
- [x] Aggregation metrics: min, max, avg, median
- [x] Filter by time range
- [x] H2 database
- [x] Spring Scheduler-based simulator
- [x] Basic Auth + Swagger UI
- [x] Good error handling & validation
- [x] Clean architecture (controller/service/repo)

---


**Author**: Sreenivasulu A  
