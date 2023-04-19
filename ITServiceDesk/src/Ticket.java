import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.time.temporal.ChronoField;

public class Ticket implements Serializable {
    private static final long serialVersionUID = 8563315072368557911L;
    private String description;
    private int severity;
    private String creator;
    private String assignedTech;
    private int status;
    private LocalDate created;
    private LocalDate closed;
    private long timeCounter;

    public Ticket(String description, int severity, String creator, String assignedTech, int status, LocalDate created, LocalDate closed, long timeCounter) {
        super();
        this.description = description;
        this.severity = severity;
        this.creator = creator;
        this.assignedTech = assignedTech;
        this.status = status;
        this.created = created;
        this.closed = closed;
        this.timeCounter = timeCounter;
    }

    public long getTimeCounter() {
        return timeCounter;
    }

    public void setTimeCounter(long timeCounter) {
        this.timeCounter = timeCounter;
    }

    public LocalDate getCreated() {
        return created;
    }

    public String getFormattedCreated() {
        if (this.created == null) {
            return "";
        } else {
            DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
            DateTimeFormatter formatter = builder.appendPattern("dd/MM/yyyy").toFormatter();
            return formatter.format(this.getCreated());
        }
    }

    public LocalDate getClosed() {
        return closed;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public void setClosed(LocalDate closed) {
        this.closed = closed;
    }

    public String getDescription() {
        return description;
    }

    public int getSeverity() {
        return severity;
    }

    public String getCreator() {
        return creator;
    }

    public String getAssignedTech() {
        return assignedTech;
    }

    public int getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setAssignedTech(String assignedTech) {
        this.assignedTech = assignedTech;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String severityString = "error";
        if (severity == 1) {
            severityString = "Low";
        } else if (severity == 2) {
            severityString = "medium";
        } else if (severity == 3) {
            severityString = "High";
        }
        String statusString = "error";
        if (status == 1) {
            statusString = "Open";
        } else if (status == 2) {
            statusString = "closed & resolved";
        } else if (status == 3) {
            statusString = "closed & unresolved";
        }

        return "Description : " + description + "\n    Created : " + getFormattedCreated()
                + "\n    Severity : " + severityString + "\n    Status : " + statusString
                + "\n    Assigned to : " + assignedTech;
    }
}
