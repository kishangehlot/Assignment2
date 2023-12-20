package Application.Services;

import Application.model.CandidateDetail;
//import Application.model.InterviewDetails;
//import Application.model.WorkLocation;
import Application.model.CandidateDetail;
import Application.model.InterviewDetails;
import Application.model.WorkLocation;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static Application.Repository.Repo.candidateDetailsList;

public class DataBaseConnection {
    public void readDataFromExcel(String excelFileName)  {
        try {

            FileInputStream inputStream = new FileInputStream(excelFileName);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                Date date = row.getCell(0).getDateCellValue();



                String name = String.valueOf(row.getCell(9)).toUpperCase().trim();
                String skill = String.valueOf(row.getCell(5)).toUpperCase().trim();
                double excelTimeValue = row.getCell(6).getNumericCellValue();
                LocalTime time = parseTime(excelTimeValue);
                String teamName = String.valueOf(row.getCell(2)).toUpperCase().trim();
                String panelName = String.valueOf(row.getCell(3)).toUpperCase().trim();
                String interviewRound = String.valueOf(row.getCell(4));
                String preferredLocation = String.valueOf(row.getCell(8)).toUpperCase().trim();
                String workLocation = String.valueOf(row.getCell(7)).toUpperCase().trim();

                WorkLocation workLocation1 = new WorkLocation(preferredLocation, workLocation);
                InterviewDetails interviewDetails = new InterviewDetails(date, time, teamName, panelName, interviewRound);
                CandidateDetail candidateDetails = new CandidateDetail( name, skill, interviewDetails, workLocation1);
                candidateDetailsList.add( candidateDetails);


            }
            workbook.close();
            inputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private LocalTime parseTime(double s) {

        long javaTimeValue = Math.round((s-25569)*86400*1000);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(javaTimeValue), ZoneId.of("Asia/Kolkata"));
        return localDateTime.toLocalTime().minusHours(5).minusMinutes(21).minusSeconds(10);
    }


    private List<InterviewDetails> getAllInterviewDetails() {
        return candidateDetailsList.stream().map(CandidateDetail::getInterviewDetails).collect(Collectors.toList());
    }

    BasicDataSource dataSource = null;
    public void createConnection() {
        try {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

            dataSource.setUrl("jdbc:mysql://localhost:3306/Assignment");
            dataSource.setUsername("root");
            dataSource.setPassword("2208");
            System.out.println("Connection Established");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println();
    }


    public void addRecords() {
        try {
            Statement truncateStatement = dataSource.getConnection().createStatement();
            String truncateQuery = "Truncate table Interview_Status";
            truncateStatement.executeUpdate(truncateQuery);
            candidateDetailsList.parallelStream().forEach(
                    this::insert
            );
            System.out.println("Insertion Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public void topSkillsByTime() {
        System.out.println("Top 3 Skills in Peak Time");
        String sqlQuery = "select InterviewTime,count(*) as InterviewTimeCount from Interview_Status  group by InterviewTime Order by InterviewTimeCount desc limit 1;";
        String time="";
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                time = resultSet.getString("InterviewTime");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Peak Time : "+time);

        String sqlQuery1 = "select skill,count(*) as SkillCount from Interview_Status where InterviewTime = '"+time+"' group by Skill Order by skillcount desc limit 3;";

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sqlQuery1);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String teamName = resultSet.getString("Skill");
                int interviewCount = resultSet.getInt("SkillCount");

                System.out.println("Skill : " + teamName);
                System.out.println("Count: " + interviewCount);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println();
    }

    public void minNumberOfInterviews() {
        System.out.println("Minimum number of interviews , TeamName in October and November");
        String sqlQuery = "select TeamName , count(*) as InterviewCount from Interview_Status where month(InterviewDate) in (10,11)\n" +
                "group by TeamName order by InterviewCount  limit 1;";

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                String teamName = resultSet.getString("TeamName");
                int interviewCount = resultSet.getInt("InterviewCount");

                System.out.println("Team with the mininimum interviews: " + teamName);
                System.out.println("Interview count: " + interviewCount);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println();
    }

    private void insert(CandidateDetail entry) {
        try {
            Connection con;
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            String insertQuery = "INSERT INTO Interview_Status (Name, Skill, InterviewDate, InterviewTime, TeamName, PanelName, InterviewRound, PreferredLocation, WorkLocation) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement;
            insertStatement = con.prepareStatement(insertQuery);

            insertStatement.setString(1, entry.getName());
            insertStatement.setString(2, entry.getSkill());
            insertStatement.setDate(3, new java.sql.Date(entry.getInterviewDetails().getInterviewDate().getTime()));
            insertStatement.setTime(4, Time.valueOf(entry.getInterviewDetails().getInterviewTime()));
            insertStatement.setString(5, entry.getInterviewDetails().getTeamName());
            insertStatement.setString(6, entry.getInterviewDetails().getPanelName());
            insertStatement.setString(7, entry.getInterviewDetails().getInterviewRound());
            insertStatement.setString(8, entry.getWorkLocation().getPreferredLocation());
            insertStatement.setString(9, entry.getWorkLocation().getWorkLocation());
            insertStatement.executeUpdate();

            con.commit();
            con.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

