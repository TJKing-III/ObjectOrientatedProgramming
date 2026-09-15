package cityrescue;

import cityrescue.enums.*;
    /**
     * Unit class handles all the units
     * it has 3 subclasses fire engine, polica car and ambulance
     * has protected types so variables cna be passed down to the subclasses  
     */
public abstract class Unit {

    protected final int unitId;
    protected final UnitType type; //polic ambulance or firetruck
    protected int StationId; //station unit is dispatched from 
    protected UnitStatus status; //all of these are protected because they will be inherited to the subclasses 
    protected int GiveincidentId; //id of the incident that the unit will go to
    protected int workleft; //work left of the incident 
    protected int x;
    protected int y;

    public Unit(int unitId, UnitType type, int StationId, int X_assign, int Y_assign) {
        this.unitId = unitId;
        this.type = type;
        this.StationId = StationId;
        this.x = X_assign;
        this.y = Y_assign;
        this.status = UnitStatus.IDLE; //put them all to default values poossible 
        this.GiveincidentId = -1;
        this.workleft = 0;
    }

    public int getunitId() {
        return unitId;
    }
    public UnitType getunitType() {
        return type;
    }
    public int getX() { //x coordinate where the unit is on the map
        return x;
    }
    public int getStationId() {
        return StationId;
    }

    public void changeStationId(int newStationId) { //
        this.StationId = newStationId;
    }


    public int getY() {//y coordinate where the unit is on the map
        return y;
    }

    public void updateLocation(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
    public void setStatus(UnitStatus status) {//making sure workleft doesnt go below zero 
        this.status = status;
        if (status != UnitStatus.AT_SCENE) {
            workleft = 0;
        }
    }

    public UnitStatus getStatus() {
        return status; 
    }



    public int getIncidentid() {
        return GiveincidentId;
    }

    public void assignIncident(int incidentId) {
        this.GiveincidentId = incidentId;
    }

    public void clearIncident() {//after the incident has been done and work left is zsero
        this.GiveincidentId = -1; 
        this.workleft = 0;
    }

    public abstract boolean canHandle(IncidentType type); //polymorphism as the spec asked 
    public abstract int getTicksToResolve(int severity);


    public int getWorkleft() {
        return workleft;
    }

    public void start_tick(int ticks) {
        this.workleft = ticks; //one tick for one cycle for the work to be done
    }

    public void tick_count() {
        if (workleft != 0) {
            workleft = workleft - 1; //
            }
    }

    public boolean isWorkZero() { //to check if work is zero
        if( workleft == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public int manhattan_distance(int EndX, int EndY) { //manhatoon method to see the distance of the unit from the incideint
        int finalX = x - EndX; //given two coordinates and 2 coordinates of the current location , do (X2 -X1) + (Y2-Y1)
        if (finalX < 0) {
            finalX = -finalX; //we dont want them to be negative in case it messes up with the addition
        }

        int finalY = y - EndY;
        if (finalY < 0) {
            finalY = -finalY;
        }
        int Z =  finalX + finalY;
        

        return Z;
    }

    public String viewUnit() { //output format exactly like spec told me

        String incidentstring;
        if (GiveincidentId == -1) {
            incidentstring = "-"; //make sure the negative is accounted for 
        } else {
            incidentstring = String.valueOf(GiveincidentId);
        }

        String coords = "LOC=(" + x + "," + y + ")";
        if (status != UnitStatus.AT_SCENE) {//only difference is that work left is included if its at sccene
            return "U#" + unitId
                    + " TYPE=" + type
                    + " HOME=" + StationId
                    + " " + coords
                    + " STATUS=" + status
                    + " INCIDENT=" + incidentstring;
        }
        return "U#" + unitId //work left is NOT included if its NOT at the scene
                + " TYPE=" + type
                + " HOME=" + StationId
                + " " + coords
                + " STATUS=" + status
                + " INCIDENT=" + incidentstring
                + " WORK=" + workleft;
    }
}






