package Application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkLocation {
    private String preferredLocation;
    private String workLocation;

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public WorkLocation(String preferredLocation, String workLocation) {
        this.preferredLocation = preferredLocation;
        this.workLocation = workLocation;
    }
}
