package org.example.b3;

import java.sql.*;

public class HosptitalSer {
    private static final String URL = "jdbc:mysql://localhost:3306/b3";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    public void xuatVienVaThanhToan(int maBenhNhan, double tienVienPhi) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false);
            String sqlCheck = "SELECT balance FROM patient_wallet WHERE id = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setInt(1, maBenhNhan);
            ResultSet rs = psCheck.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Không tìm thấy bệnh nhân!");
            }
            double balance = rs.getDouble("balance");
            if (balance < tienVienPhi) {
                throw new SQLException("Không đủ tiền thanh toán!");
            }
            String sqlWallet = "UPDATE patient_wallet SET balance = balance - ? WHERE id = ?";
            PreparedStatement ps1 = conn.prepareStatement(sqlWallet);
            ps1.setDouble(1, tienVienPhi);
            ps1.setInt(2, maBenhNhan);

            int row1 = ps1.executeUpdate();
            if (row1 == 0) {
                throw new SQLException("Không cập nhật được ví!");
            }
            String sqlBed = "UPDATE bed SET status = 'AVAILABLE' WHERE patient_id = ?";
            PreparedStatement ps2 = conn.prepareStatement(sqlBed);
            ps2.setInt(1, maBenhNhan);

            int row2 = ps2.executeUpdate();
            if (row2 == 0) {
                throw new SQLException("Không tìm thấy giường!");
            }
            String sqlPatient = "UPDATE patient SET status = 'DISCHARGED' WHERE id = ?";
            PreparedStatement ps3 = conn.prepareStatement(sqlPatient);
            ps3.setInt(1, maBenhNhan);

            int row3 = ps3.executeUpdate();
            if (row3 == 0) {
                throw new SQLException("Không cập nhật được bệnh nhân!");
            }
            conn.commit();
            System.out.println("Xuất viện và thanh toán thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Đã rollback giao dịch!");
                } catch (SQLException ex) {
                    System.out.println("Lỗi rollback: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Lỗi đóng kết nối: " + e.getMessage());
                }
            }
        }
    }
    public static void main(String[] args) {
        HosptitalSer service = new HosptitalSer();
        service.xuatVienVaThanhToan(1, 200);
    }
}