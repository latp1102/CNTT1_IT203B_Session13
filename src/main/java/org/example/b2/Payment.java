package org.example.b2;

import org.example.b1.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Payment {
    public void thanhToanVienPhi(int patientId,int invoiceId, double amount) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);
            String sqlWallet = "UPDATE Patient_Wallet SET balance = balance - ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sqlWallet);
            ps.setDouble(1, amount);
            ps.setInt(2, patientId);
            ps.executeUpdate();

            String sqlInvoice = "UPDATE Invoice SET status = 'PAID' WHERE invoice_id = ?";
            PreparedStatement ps1 = connection.prepareStatement(sqlInvoice);
            ps1.setInt(1, invoiceId);
            ps1.executeUpdate();
            connection.commit();
            System.out.println("Thanh toán viện phí thành công");
        }catch (SQLException e) {
            System.out.println("Lỗi: " +e.getMessage());
            if (connection !=null){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println("Lỗi rollback: " + ex.getMessage());
                }
            }
        }finally {
            if (connection !=null){
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Lỗi đóng kết nối: " + e.getMessage());
                }
            }
        }
    }
}
