package org.example.b5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static PatientSer ser = new PatientSer();
    static int choice;
    public static void main(String[] args) {
        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Xem giường trống");
            System.out.println("2. Tiếp nhận bệnh nhân");
            System.out.println("3. Thoát chương trình");
            System.out.print("Lựa chọn của bạn: ");
            choice = getInt();
            switch (choice) {
                case 1:
                    xemGiuongTrong();
                    break;
                case 2:
                    themBenhNhan();
                    break;
                case 3:
                    System.out.println("Thoát chương trình");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ");
            }
        } while (choice != 3);
    }
    private static void xemGiuongTrong() {
        try (Connection conn = DBHelper.getConnection()) {
            String sql = "SELECT * FROM beds WHERE status = 'EMPTY'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("Giường: " + rs.getString("bed_id"));
            }
            if (!hasData) {
                System.out.println("Không có giường trống!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
    private static void themBenhNhan() {

        System.out.print("Tên: ");
        String name = sc.nextLine();

        System.out.print("Tuổi: ");
        int age = getInt();

        System.out.print("Mã giường: ");
        String bedId = sc.nextLine();

        System.out.print("Tiền tạm ứng: ");
        double money = getDouble();

        ser.themBenhNhan(name, age, bedId, money);
    }
    private static int getInt() {
        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine());
                return value;
            } catch (Exception e) {
                System.out.print("Nhập số hợp lệ: ");
            }
        }
    }
    private static double getDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(sc.nextLine());
                return value;
            } catch (Exception e) {
                System.out.print("Nhập số tiền hợp lệ: ");
            }
        }
    }
}