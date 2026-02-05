package com.garden.service;

import java.sql.Connection;
import java.sql.Date;

import com.garden.bean.Gardener;
import com.garden.bean.PlotTaskRow;
import com.garden.dao.GardenerDAO;
import com.garden.dao.PlotTaskDAO;
import com.garden.util.ActiveAllocationsOrPendingTasksException;
import com.garden.util.DBUtil;
import com.garden.util.PlotAllocationConflictException;
import com.garden.util.ValidationException;

public class GardenService {

    private GardenerDAO gardenerDAO = new GardenerDAO();
    private PlotTaskDAO plotDAO = new PlotTaskDAO();

    public boolean registerNewGardener(Gardener g) throws Exception {

        if (g == null || g.getGardenerID() == null || g.getFullName() == null)
            throw new ValidationException("Invalid gardener data");

        try (Connection con = DBUtil.getDBConnection()) {

            if (gardenerDAO.findGardener(con, g.getGardenerID()) != null)
                throw new ValidationException("Gardener already exists");

            g.setStatus("ACTIVE");
            return gardenerDAO.insertGardener(con, g);
        }
    }

    public boolean allocatePlotToGardener(
            String gardenerID, String plotNo, String season,
            Date start, Date end) throws Exception {

        Connection con = DBUtil.getDBConnection();

        try {
            con.setAutoCommit(false);

            if (plotDAO.hasOverlappingAllocation(con, plotNo, start, end))
                throw new PlotAllocationConflictException();

            PlotTaskRow r = new PlotTaskRow();
            r.setRowID(plotDAO.generateRowID(con));
            r.setGardenerID(gardenerID);
            r.setServiceType("PLOT_ALLOCATION");
            r.setPlotNo(plotNo);
            r.setSeasonName(season);
            r.setAllocationStartDate(start);
            r.setAllocationEndDate(end);
            r.setTaskStatus("PENDING");

            plotDAO.insertPlotTaskRow(con, r);

            con.commit();
            return true;

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }

    public boolean removeGardener(String gardenerID, Date refDate)
            throws Exception {

        Connection con = DBUtil.getDBConnection();

        try {
            if (plotDAO.hasActiveAllocations(con, gardenerID, refDate) ||
                plotDAO.hasPendingTasks(con, gardenerID))
                throw new ActiveAllocationsOrPendingTasksException();

            return gardenerDAO.deleteGardener(con, gardenerID);

        } finally {
            con.close();
        }
    }
}
