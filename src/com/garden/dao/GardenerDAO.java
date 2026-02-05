package com.garden.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.garden.bean.Gardener;
import com.garden.util.DBUtil;


public class GardenerDAO {

    public Gardener findGardener(Connection con, String id) throws Exception {

        String sql = "SELECT * FROM GARDENER_TBL WHERE GARDENER_ID=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Gardener g = new Gardener();
                    g.setGardenerID(rs.getString("GARDENER_ID"));
                    g.setFullName(rs.getString("FULL_NAME"));
                    g.setHouseOrApartmentNo(rs.getString("HOUSE_NO"));
                    g.setStreetName(rs.getString("STREET_NAME"));
                    g.setMobile(rs.getString("MOBILE"));
                    g.setEmail(rs.getString("EMAIL"));
                    g.setPreferredCategory(rs.getString("PREFERRED_CATEGORY"));
                    g.setStatus(rs.getString("STATUS"));
                    return g;
                }
            }
        }
        return null;
    }

    public boolean insertGardener(Connection con, Gardener g) throws Exception {

        String sql = "INSERT INTO GARDENER_TBL VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, g.getGardenerID());
            ps.setString(2, g.getFullName());
            ps.setString(3, g.getHouseOrApartmentNo());
            ps.setString(4, g.getStreetName());
            ps.setString(5, g.getMobile());
            ps.setString(6, g.getEmail());
            ps.setString(7, g.getPreferredCategory());
            ps.setString(8, g.getStatus());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteGardener(Connection con, String id) throws Exception {

        String sql = "DELETE FROM GARDENER_TBL WHERE GARDENER_ID=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        }
    }
}
