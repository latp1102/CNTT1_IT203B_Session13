package org.example.b5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PatientSer {
    public void themBenhNhan(String name, int age, String bed_id, double amount) {
        Connection connection = null;
        try {
            connection = DBHelper.getConnection();
            connection.setAutoCommit(false);
            String checkSql = "SELECT status FROM beds WHERE bed_id = ?";
            PreparedStatement ps = connection.prepareStatement(checkSql);
            ps.setString(1, bed_id);
            ResultSet rs = ps.executeQuery();
            if(!rs.next() || !"EMPTY".equals(rs.getString("status"))){
                throw new Exception("Giường đã có người");
            }
            String insert = "INSERT INTO patients(name, age, bed_id) VALUES(?, ?,?)";
            PreparedStatement ps1 = connection.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
            ps1.setString(1, name);
            ps1.setInt(2, age);
            ps1.setString(3, bed_id);
            ps1.executeUpdate();

            ResultSet key = ps1.getGeneratedKeys();
            key.next();
            int patientId = key.getInt(1);

            String update = "UPDATE beds SET status = 'OCCUPIED' WHERE bed_id = ?";
            PreparedStatement ps2 = connection.prepareStatement(update);
            ps2.setString(1, bed_id);
            ps2.executeUpdate();

            String payment = "INSERT INTO payments(patient_id, amount) VALUES(?, ?)";
            PreparedStatement ps3 = connection.prepareStatement(payment);
            ps3.setInt(1, patientId);
            ps3.setDouble(2, amount);
            ps3.executeUpdate();
            connection.commit();
            System.out.println("Thêm bệnh nhân thành công");
        } catch (Exception e) {
            try {
                if (connection!= null) {
                    connection.rollback();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            System.out.println("Lỗi: "+e.getMessage());
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
