# OOP_N02_NGUYEN_THANH_PHONG_NHOM
*MSV : 24100259*
**Hotel Room Manager** là một ứng dụng quản lý phòng khách sạn được phát triển bằng Java. 
Ứng dụng hỗ trợ quản lý thông tin phòng, tình trạng phòng, và các dịch vụ liên quan. 
Dự án này sử dụng JDK 21 và triển khai trên nền tảng web với giao diện người dùng (UI) được thiết kế bằng Javaswing.

**Giới Thiệu** 
Hotel Room Manager là một công cụ quản lý phòng khách sạn với các tính năng như:
1) Quản lý thông tin phòng (số phòng, loại phòng, tình trạng phòng).
2) Đặt phòng và hủy phòng.
3) Xem tình trạng phòng hiện tại (còn trống, đã đặt, đang sử dụng).
4) Giao diện người dùng đơn giản và dễ sử dụng, được thiết kế bằng javaswing

**Structure** 
'hotel-room-manager/
│
├── src/
│   ├── com/
│   │   ├── hotelroommanager/
│   │   │   ├── model/         # Các lớp mô hình dữ liệu như Room, Reservation
│   │   │   ├── service/       # Logic nghiệp vụ, xử lý dữ liệu
│   │   │   ├── ui/            # Các lớp liên quan tới giao diện Swing
│   │   │   └── Main.java      # Lớp chạy chính của ứng dụng
│
├── bin/                       # Thư mục chứa file .class sau khi biên dịch
└── README.md                  # Tệp này
'

**Hướng Dẫn Sử Dụng**
Khi chạy ứng dụng, cửa sổ chính sẽ hiện ra danh sách các phòng.
Bạn có thể sử dụng các nút trên giao diện để:
1) Thêm phòng mới.
2) Chỉnh sửa thông tin phòng.
3) Đặt phòng hoặc hủy đặt phòng.
4) Xem chi tiết từng phòng và tình trạng sử dụng.
Giao diện Swing hỗ trợ tương tác trực tiếp với người dùng, các form nhập liệu và bảng thông tin rõ ràng.
