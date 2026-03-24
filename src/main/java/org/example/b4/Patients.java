package org.example.b4;

import java.sql.*;
import java.util.*;

public class Patients {
    private String maBN;
    private String tenBN;
    private List<DichVu> dsDichVu;

    public Patients() {
        this.dsDichVu = new ArrayList<>();
    }

    public String getMaBN() {
        return maBN;
    }

    public void setMaBN(String maBN) {
        this.maBN = maBN;
    }

    public String getTenBN() {
        return tenBN;
    }

    public void setTenBN(String tenBN) {
        this.tenBN = tenBN;
    }

    public List<DichVu> getDsDichVu() {
        return dsDichVu;
    }

    public void setDsDichVu(List<DichVu> dsDichVu) {
        this.dsDichVu = dsDichVu;
    }
    public List<Patients> getPatientsList(Connection connection) {
        Map<String, Patients> map = new LinkedHashMap<>();

        String sql = """
            SELECT 
                bn.maBN, 
                bn.tenBN, 
                dv.id AS dv_id, 
                dv.tenDichVu AS tenDV
            FROM patient bn
            LEFT JOIN DichVuSuDung dv 
                ON bn.maBN = dv.maBN
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String maBN = rs.getString("maBN");
                Patients bn = map.get(maBN);
                if (bn == null) {
                    bn = new Patients();
                    bn.setMaBN(maBN);
                    bn.setTenBN(rs.getString("tenBN"));
                    map.put(maBN, bn);
                }
                int dvId = rs.getInt("dv_id");
                if (!rs.wasNull()) {
                    DichVu dv = new DichVu();
                    dv.setId(dvId);
                    dv.setTenDichVu(rs.getString("tenDV"));
                    bn.getDsDichVu().add(dv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(map.values());
    }
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/b4", "root", "1234");
            Patients p = new Patients();
            List<Patients> list = p.getPatientsList(conn);
            for (Patients bn : list) {
                System.out.println("Bệnh nhân: " + bn.getTenBN());
                for (DichVu dv : bn.getDsDichVu()) {
                    System.out.println("   - " + dv.getTenDichVu());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}