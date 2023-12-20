package Application;

import Application.Services.DataBaseConnection;

import static Application.Repository.Repo.candidateDetailsList;

public class Main {
    public static void main(String[] args)
    {
        DataBaseConnection db = new DataBaseConnection();
        String excelFileName = "C:\\Users\\kishan.gehlot\\Downloads\\FinalAccolite.xlsx";
        db.readDataFromExcel(excelFileName);
        db.createConnection();
        db.addRecords();
        db.topSkillsByTime();
        db.minNumberOfInterviews();

//        System.out.println(candidateDetailsList);
    }
}