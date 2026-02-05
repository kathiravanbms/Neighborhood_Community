package com.garden.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.garden.bean.PlotTaskRow;

public class PlotTaskDAO {

    public int generateRowID(Connection con) throws Exception {
        try (PreparedStatement ps =
                 con.prepareStatement("SELECT plot_task_seq.NEXTVAL FROM dual");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public boolean insertPlotTaskRow(Connection con, PlotTaskRow r) throws Exception {

        String sql = "INSERT INTO PLOT_TASK_TBL VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getRowID());
            ps.setString(2, r.getGardenerID());
            ps.setString(3, r.getServiceType());
            ps.setString(4, r.getPlotNo());
            ps.setString(5, r.getSeasonName());
            ps.setDate(6, r.getAllocationStartDate());
            ps.setDate(7, r.getAllocationEndDate());
            ps.setDate(8, r.getTaskDate());
            ps.setString(9, r.getTaskType());
            ps.setString(10, r.getTaskNotes());
            ps.setString(11, r.getTaskStatus());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean hasOverlappingAllocation(
            Connection con, String plotNo, Date start, Date end) throws Exception {

        String sql =
            "SELECT 1 FROM PLOT_TASK_TBL WHERE PLOT_NO=? " +
            "AND SERVICE_TYPE='PLOT_ALLOCATION' " +
            "AND NOT (ALLOCATION_END_DATE < ? OR ALLOCATION_START_DATE > ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, plotNo);
            ps.setDate(2, start);
            ps.setDate(3, end);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean hasActiveAllocations(
            Connection con, String gardenerID, Date refDate) throws Exception {

        String sql =
            "SELECT 1 FROM PLOT_TASK_TBL WHERE GARDENER_ID=? " +
            "AND SERVICE_TYPE='PLOT_ALLOCATION' AND ALLOCATION_END_DATE>=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, gardenerID);
            ps.setDate(2, refDate);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean hasPendingTasks(Connection con, String gardenerID) throws Exception {

        String sql =
            "SELECT 1 FROM PLOT_TASK_TBL WHERE GARDENER_ID=? " +
            "AND SERVICE_TYPE='MAINTENANCE_TASK' AND TASK_STATUS='PENDING'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, gardenerID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
