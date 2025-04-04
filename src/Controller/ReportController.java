package Controller;
import Model.*;

import java.util.ArrayList;

public class ReportController {
    private Report report;
    private DatabaseController databaseController;
    private static ReportController instance;

    public ReportController() {
        this.databaseController = DatabaseController.getInstance();
    }
    public static ReportController getInstance() {
        if (instance == null) {
            instance = new ReportController();
        }
        return instance;
    }
    public Report createReport(int reporterId, int userReportedId, int contentId, String description) {
        return new Report(reporterId, contentId, userReportedId, description);
    }
}
