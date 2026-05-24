# 🦐 Hệ Thống Quản Lý Tôm Giống (Shrimp Seed Management System)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36.svg)](https://maven.apache.org/)

## 📖 Tổng Quan (Overview)
**Shrimp Seed Management System** là một hệ thống phần mềm quản lý vận hành dành cho các trại giống/doanh nghiệp kinh doanh tôm giống. Hệ thống cung cấp các giải pháp toàn diện từ việc quản lý danh mục (tôm, thuộc tính, khách hàng, nhà cung cấp) cho đến theo dõi toàn bộ chuỗi cung ứng: nhập hàng theo lô, xuất bán, kiểm soát kho hàng theo thời gian thực và hệ thống báo cáo thống kê chuyên sâu.

Dự án được xây dựng dựa trên kiến trúc **RESTful API** với **Spring Boot**, thiết kế các luồng dữ liệu minh bạch, dễ dàng tích hợp với các ứng dụng Frontend (React/Vue/Angular) và Mobile.

---

## ✨ Tính Năng Nổi Bật (Key Features)

### 1. Quản lý Danh mục (Catalog Management)
*   **Tôm & Thuộc tính (Shrimp & Attributes):** Quản lý chi tiết từng giống tôm và thuộc tính sinh học/chất lượng của chúng (`ShrimpAttribute`).
*   **Đối tác (Partners):** Quản lý thông tin chi tiết Khách hàng (`Customer`) và Nhà cung cấp (`Supplier`).

### 2. Quản lý Chuỗi Cung Ứng (Supply Chain)
*   **Nhập Hàng (Import):** Quản lý hóa đơn nhập, tự động tạo mới `Batch` (Lô hàng) khi nhập, liên kết với nhà cung cấp.
*   **Xuất Hàng (Export):** Trừ trực tiếp số lượng tồn kho chuyên sâu theo Lô (`Batch`) và Loại tôm (`ShrimpAttribute`). Hỗ trợ quản lý tôm hao hụt, khách trả lại (Return).
*   **Quản Lý Lô Hàng (Batch Management):** Theo dõi vòng đời của một lô hàng, trạng thái nuôi cấy/phân phối.

### 3. Kiểm Soát Tồn Kho (Inventory Management)
*   **Cập nhật Real-time:** Tự động điều chỉnh số lượng tồn kho (Stock Quantity) khi có giao dịch Nhập/Xuất/Xóa/Sửa.
*   **Truy xuất khả dụng (Available Stock):** API cung cấp số lượng hàng thực tế còn lại trong kho theo từng chi tiết lô.

### 4. Báo Cáo & Thống Kê (Dashboard & Statistics)
*   Thống kê chỉ số KPI tổng quan (Doanh thu, tỉ lệ hao hụt, chi phí,...).
*   Biểu đồ doanh thu theo thời gian, theo dõi xu hướng kinh doanh.
*   Bảng xếp hạng: Sản phẩm bán chạy nhất, Khách hàng thân thiết, Nhà cung cấp lớn nhất.
*   Hệ thống cảnh báo cảnh báo tự động: Tồn kho thấp (Low stock warnings), Công nợ / Thanh toán.

### 5. Bảo mật & Phân quyền (Security & Authentication)
*   Tích hợp **Spring Security** & **JWT (JSON Web Token)** để bảo mật các endpoints.
*   Mã hóa mật khẩu, phân quyền truy cập an toàn.

---

## 🛠 Công Nghệ Sử Dụng (Technology Stack)

*   **Backend Framework:** Spring Boot (Java)
*   **Database:** SQL Server (sử dụng Hibernate/JPA để giao tiếp ORM)
*   **Data Mapping:** MapStruct (giúp map DTO và Entity gọn gàng, Type-safe)
*   **Code Reduction:** Lombok
*   **Build Tool:** Maven
*   **Validation:** Spring Boot Starter Validation
*   **Security:** Spring Security + JWT

---

## 📂 Tiêu Chuẩn & Cấu Trúc Dự Án (Project Structure)

Dự án tuân theo mô hình **Controller - Service - Repository** chuẩn mực:

```text
src/main/java/com/example/quanlytom/
 ├── config/         # Cấu hình hệ thống (Security, CORS, MapStruct...)
 ├── controller/     # Các REST API Endpoints (Routing & Request/Response handling)
 ├── dto/            # Data Transfer Objects (Request/Response models)
 ├── entity/         # Cấu trúc CSDL (JPA Entities)
 ├── enums/          # Tập hợp các hằng số, Enum trạng thái
 ├── exception/      # Global Exception Handler (Bắt lỗi và trả form JSON thống nhất)
 ├── mapper/         # Các Interface MapStruct (chuyển đổi Entity <-> DTO)
 ├── repository/     # Giao tiếp với CSDL (Spring Data JPA)
 ├── security/       # Cấu hình JWT, Filters, Authentication Services
 ├── service/        # Business Logic (Các nghiệp vụ xử lý chính)
 └── specification/  # JPA Specification hỗ trợ query động (Filter, Search)
```

---

## 🚀 Hướng Dẫn Cài Đặt (Getting Started)

### 1. Yêu cầu hệ thống (Prerequisites)
*   JDK 17 trở lên
*   Maven 3.8+
*   SQL Server (để setup database)

### 2. Thiết lập Database
1. Mở SQL Server Management Studio (SSMS) và thực thi script khởi tạo (nếu có trong `DBver3.sql`).
2. Mở file `src/main/resources/application.properties`.
3. Thay đổi cấu hình Data Source cho phù hợp với máy cá nhân:
    ```properties
    spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=quanlytom;encrypt=true;trustServerCertificate=true
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

### 3. Build và Chạy Ứng Dụng
Sử dụng Maven wrapper có sẵn trong dự án:

```bash
# Build hệ thống
./mvnw clean install -DskipTests

# Chạy hệ thống
./mvnw spring-boot:run
```
Ứng dụng sẽ mặc định khởi chạy tại: `http://localhost:8080/`
---
**Tác giả / Nhóm phát triển:** [Công Hiếu]

