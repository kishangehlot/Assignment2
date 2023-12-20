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
public class CandidateDetail {
        private String Name;
        private String Skill;
        private InterviewDetails interviewDetails;
        private WorkLocation workLocation;



        public String getName() {
                return Name;
        }

        public void setName(String name) {
                Name = name;
        }

        public String getSkill() {
                return Skill;
        }

        public void setSkill(String skill) {
                Skill = skill;
        }

        public InterviewDetails getInterviewDetails() {
                return interviewDetails;
        }

        public void setInterviewDetails(InterviewDetails interviewDetails) {
                this.interviewDetails = interviewDetails;
        }

        public WorkLocation getWorkLocation() {
                return workLocation;
        }

        public void setWorkLocation(WorkLocation workLocation) {
                this.workLocation = workLocation;
        }

        public CandidateDetail( String name, String skill, InterviewDetails interviewDetails, WorkLocation workLocation) {
                Name = name;
                Skill = skill;
                this.interviewDetails = interviewDetails;
                this.workLocation = workLocation;
        }
}

