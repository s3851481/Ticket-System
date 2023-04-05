import java.io.Serializable;

public class Ticket implements Serializable {
private String description;
private int severity;
private String creator;
private String assignedTech;
private int status;

public Ticket(String description, int severity, String creator, String assignedTech, int status) {
	super();
	this.description = description;
	this.severity = severity;
	this.creator = creator;
	this.assignedTech = assignedTech;
	this.status = status;
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
}
