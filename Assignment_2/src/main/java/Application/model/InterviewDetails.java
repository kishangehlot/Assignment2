package Application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@ToString
public class InterviewDetails {
    private Date interviewDate;
    private LocalTime interviewTime;
    private String teamName;
    private String panelName;
    private String interviewRound;

    public Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(Date interviewDate) {
        this.interviewDate = interviewDate;
    }

    public LocalTime getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(LocalTime interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }

    public String getInterviewRound() {
        return interviewRound;
    }

    public void setInterviewRound(String interviewRound) {
        this.interviewRound = interviewRound;
    }

    public InterviewDetails(Date interviewDate, LocalTime interviewTime, String teamName, String panelName, String interviewRound) {
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
        this.teamName = teamName;
        this.panelName = panelName;
        this.interviewRound = interviewRound;
    }
}
