package org.example.b1;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Hopital {
    private static final String URL = "jdbc:mysql://localhost:3306/b1";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    public void capPhatThuoc(int medicineId, int patientId) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            String sqlUpdateInventory = "UPDATE Medicine_Inventory SET quantity = quantity - 1 WHERE medicine_id = ?";
            PreparedStatement ps1 = conn.prepareStatement(sqlUpdateInventory);
            ps1.setInt(1, medicineId);
            ps1.executeUpdate();
            String sqlInsertHistory = "INSERT INTO Prescription_History (patient_id, medicine_id, date) VALUES (?, ?, NOW())";
            PreparedStatement ps2 = conn.prepareStatement(sqlInsertHistory);
            ps2.setInt(1, patientId);
            ps2.setInt(2, medicineId);
            ps2.executeUpdate();
            conn.commit();
            System.out.println("Cấp phát thuốc thành công");
        } catch (Exception e) {
            System.out.println("Có lỗi xảy ra: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Rollback dữ liệu thành công");
                }
            } catch (Exception rollbackEx) {
                System.out.println("Lỗi khi rollback: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

}
