package cityrescue;

import javax.naming.InvalidNameException;

import cityrescue.enums.*;
import cityrescue.exceptions.*;
/**
* ahandles all elements related to incidents 
* returns and sets severity and other elemts 
*/
public class Incident{
    private int incidentId;
    private IncidentType type;
    private int severity;
    private int x;
    private int y;
    private IncidentStatus status;
    private Unit assignedUnit;

    public Incident(int incidentId, IncidentType type, int severity, int x, int y){
        this.incidentId = incidentId;
        this.type = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.status = IncidentStatus.REPORTED;
        this.assignedUnit = null;
    }

    public int getIncidentId(){
        return incidentId;
    }

    public IncidentType getType(){
        return type;
    }

    public int getSeverity(){
        return severity;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    public int getAssignedUnitId(){
        return incidentId; 
    }

    public IncidentStatus getStatus(){
        return status;
    }
    public int getId(){
        return incidentId;
    }

    public Unit getAssignedUnit() {
        return assignedUnit;
    }

    public void setSeverity(int severity){
        this.severity = severity;
    }

    public void setStatus(IncidentStatus status){
        this.status = status;
    }

    public void setAssignedUnit(Unit unit){
        this.assignedUnit = unit;
    }

    public void clearUnit(){
        this.assignedUnit = null;
    }

}



