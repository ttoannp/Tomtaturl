package db;


import dao.UserDAO;
import model.User;

public class test {

    public static void main(String[] args) {
        System.out.println("Đang tiến hành kiểm tra kết nối CSDL và DAO...");

        UserDAO userDAO = new UserDAO();
        String testUsername = "admin";

        try {
            System.out.println("Đang thử lấy thông tin user: " + testUsername);
            User user = userDAO.getUserByUsername(testUsername);

            if (user != null) {
                System.out.println("\n========================================================");
                System.out.println("✅ KẾT NỐI VÀ TRUY VẤN THÀNH CÔNG!");
                System.out.println("========================================================");
                System.out.println("Đã tìm thấy người dùng:");
                System.out.println("  - ID: " + user.getId());
                System.out.println("  - Username: " + user.getUsername());
                System.out.println("  - Email: " + user.getEmail());
                System.out.println("  - Password : " + user.getPassword());
                System.out.println("  - Ngày tạo: " + user.getCreatedAt());
            } else {
                System.out.println("\n========================================================");
                System.out.println("❌ KẾT NỐI THÀNH CÔNG, NHƯNG KHÔNG TÌM THẤY USER 'admin'.");
                System.out.println("========================================================");
                System.out.println("Vui lòng kiểm tra lại dữ liệu trong bảng `users`.");
            }

        } catch (Exception e) {
            System.err.println("\n========================================================");
            System.err.println("🔥 LỖI: KHÔNG THỂ KẾT NỐI HOẶC TRUY VẤN CSDL.");
            System.err.println("========================================================");
            System.err.println("Vui lòng kiểm tra các thông tin sau:");
            System.err.println("  1. File `DBConnection.java`: URL, username, password đã đúng chưa?");
            System.err.println("  2. MySQL Server đã được khởi động chưa?");
            System.err.println("  3. Thư viện MySQL Connector/J đã được thêm vào `pom.xml` chưa?");
            System.err.println("  4. Database 'webscraper' và bảng 'users' đã được tạo chưa?");
            System.err.println("\nChi tiết lỗi:");
            e.printStackTrace();
        }
    }
}