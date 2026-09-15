package cityrescue;

import javax.naming.InvalidNameException;

import cityrescue.enums.*;
import cityrescue.exceptions.*;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */


/**
 * CityRescueImpl (Starter)
 * the main class which controls all the other classes with key methods and functions 
 * this class contains around 22 methods 
 */
public class CityRescueImpl implements CityRescue {

    // TODO: add fields (map, arrays for stations/units/incidents, counters, tick, etc.)
    private static final int MAX_STATIONS = 20; // as specified in the spec
    private static final int MAX_UNITS = 50;
    private static final int MAX_INCIDENTS = 200;

    private int tick;
    private int stationID_counter;
    private int unitID_counter;
    private int incidentID_counter;

    private CityMap city_map;

    private Station[] stations;
    private int stationCount;

    private Unit[] units;
    private int unitCount;

    private Incident[] incidents;
    private int incidentCount;

    private int obstacleCount;
     /**
     * this class initialises a new , fresh inisitation of the city 
     * it creates all the necessary variables and sets it to zero 
     * it also creates the arrays that will be used for the specific objects
     * @param width and height to make the city map 
     */
    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        if (width > 0 && height > 0) {

            // allocate arrays
            stations = new Station[MAX_STATIONS];
            units = new Unit[MAX_UNITS];
            incidents = new Incident[MAX_INCIDENTS];


            // clear arrays
            for (int i = 0; i < MAX_STATIONS; i++) {
                stations[i] = null;
            }
            for (int i = 0; i < MAX_UNITS; i++) {
                units[i] = null;
            }
            for (int i = 0; i < MAX_INCIDENTS; i++) {
                incidents[i] = null;
            }

            tick = 0;
            city_map = new CityMap(width, height);

            
            stationID_counter = 1;
            unitID_counter = 1;
            incidentID_counter = 1;

            stationCount = 0;
            unitCount = 0;
            incidentCount = 0;
            obstacleCount = 0;

        } else {
            throw new InvalidGridException("width or height is  zero");
        }
    }

    @Override
    public int[] getGridSize() {
        return new int[] { city_map.getWidth(), city_map.getHeight() };
    }
     /**
     * add an obstacle into the map
     * checks if the coordinates are in bound and its valid to be blocked 
     * obstacle count is updated
     * @param x and y for the coordinates to be checked on the map
     */
    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if ((city_map != null && city_map.inBounds(x, y)) == true) {
            if (city_map.isBlocked(x, y) == false) {
                city_map.setBlocked(x, y, true);
                obstacleCount++;
            } else {
                throw new InvalidLocationException("something is already at that coords");
            }
        } else {
            throw new InvalidLocationException("coords out of bounds");
        }
    }
    /**
     * removes an obstacle from the map
     * checks if the coordinates are in bound and its valid to be unblocked
     * obstacle count is updated
     * @param x and y for the coordinates to be checked on the map
     */
    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if ((city_map != null && city_map.inBounds(x, y)) == true) {
            if (city_map.isBlocked(x, y) == true) {
                city_map.setBlocked(x, y, false);
                obstacleCount--;
            } else {
                throw new InvalidLocationException("there is nothing there");
            }
        } else {
            throw new InvalidLocationException("out of bounds");
        }
    }
     /**
     * add an station into the map 
     * checks if the coordinates are in bound and its valid to be added, and name is not null
     * checks if there is a empty space in the max stations array
     * adds it if there is space
     * @param x and y for the coordinates to be checked on the map. Name to be assigned if possible
     * @return id of the station
     */
    @Override
    public int addStation(String name, int x, int y) throws InvalidLocationException {
        int empty_space = -1;

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidLocationException("name is null");
        }

        if (city_map == null || city_map.inBounds(x, y) == false) {
            throw new InvalidLocationException("location or city mall is wrong");
        }

        if (city_map.isBlocked(x, y) == true) {
            throw new InvalidLocationException("something is at that location");
        }

        for (int i = 0; i < MAX_STATIONS; i++) {
            if (stations[i] == null) {
                empty_space = i;
                break;
            }
        }

        if (empty_space == -1) {
            throw new CapacityExceededException("capacity exceeded");
        }

        int id = stationID_counter++;
        int capacity = 10;

        stations[empty_space] = new Station(id, name, x, y, capacity);
        stationCount++;

        return id;
    }
     /**
     * renove an station from the map 
     * checks if the coordinates are in bound and its valid to be removed
     * checks if it exists in the station array
     * removes if found
     * station count is updated
     * @param station id for the id to be checked if it exists in the array
     */

    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        int station_found = -1;
        for (int i = 0; i < MAX_STATIONS; i++) {
            if (stations[i] != null && stations[i].getId() == stationId) {
                station_found = i;
                break;
            }
        }
        if (station_found == -1) {
            throw new IDNotRecognisedException("wrong id");
        }
        if (stations[station_found].getUnitcount() != 0) {
            throw new IllegalStateException("no units in there");
        }
        stations[station_found] = null;
        stationCount--;
    }
    /**
     *  set station capacity  
     * checks if station exists with the station id 
     * checks if max units is below zero 
     * call space restriction when done 
     * @param station id to be checked if it exists, max units to be checked too 
     */
    @Override
    public void setStationCapacity(int stationId, int maxUnits)
            throws IDNotRecognisedException, InvalidCapacityException {

        int station_found = -1;
        for (int i = 0; i < MAX_STATIONS; i++) {
            if (stations[i] != null && stations[i].getId() == stationId) {
                station_found = i;
                break;
            }
        }
        if (station_found == -1) {
            throw new IDNotRecognisedException("station id cannot be found");
        }
        if (maxUnits <= 0) {
            throw new InvalidCapacityException("too many units");
        }
        if (maxUnits < stations[station_found].getUnitcount()) {
            throw new InvalidCapacityException("capacity exceeded");
        }

        stations[station_found].spaceRestriction(maxUnits);
        ;
    }

    /**
     * gets station ids
     * create a new array
     * copy everything into the new array
     * bubble sort it to be in ascending order 
     * @return the id array 
     */

    @Override
    public int[] getStationIds() {
        int[] elements_index = new int[MAX_STATIONS];
        int count = 0;
        for (int i = 0; i < MAX_STATIONS; i++) {
            if (stations[i] != null) {
                elements_index[count++] = i;
            }
        }
        int[] id_copy = new int[count];
        int index;

        for (int j = 0; j < count; j++) {
            index = elements_index[j];
            id_copy[j] = stations[index].getId();
        }

        for (int i = 0; i < id_copy.length - 1; i++) {
            for (int j = 0; j < id_copy.length - 1 - i; j++) {
                if (id_copy[j] > id_copy[j + 1]) {
                    int temp = id_copy[j];
                    id_copy[j] = id_copy[j + 1];
                    id_copy[j + 1] = temp;
                }
            }
        }
        return id_copy;
    }
    /**
     * add an unit 
     * checks if the id given exists in the station array
     * get the coordinates
     * check what unit type is given
     * create that new unit if it doesnt exceed max units
     * @param station id for it be checked if it exists and type for assignment 
     * @return id 
     */
    @Override
    public int addUnit(int stationId, UnitType type)
            throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {

        int station_found = -1;
        for (int i = 0; i < MAX_STATIONS; i++) {
            if (stations[i] != null && stations[i].getId() == stationId) {
                station_found = i;
                break;
            }
        }
        if (station_found == -1) {
            throw new IDNotRecognisedException("station id cannot be found");
        }
        if (type == null) {
            throw new InvalidUnitException("type cannot be null");
        }
        if (stations[station_found].spaceLeft() == false) {
            throw new InvalidUnitException("no space left");
        }
        int ID = unitID_counter++;
        int x = stations[station_found].getX();
        int y = stations[station_found].getY();
        Unit newu;
        if (type == UnitType.AMBULANCE) {
            newu = new Ambulance(ID, stationId, x, y);
        } else if (type == UnitType.FIRE_ENGINE) {
            newu = new FireEngine(ID, stationId, x, y);
        } else if (type == UnitType.POLICE_CAR) {
            newu = new PoliceCar(ID, stationId, x, y);
        } else {
            throw new InvalidUnitException("invalid unit");
        }
        int empty = -1;

        for (int i = 0; i < MAX_UNITS; i++) {
            if (units[i] == null) {
                empty = i;
                break;
            }
        }

        units[empty] = newu;
        stations[station_found].addUnit(ID);
        unitCount++;
        return ID;
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        int unit_found = -1;
        for (int i = 0; i < MAX_UNITS; i++) {
            if (units[i] != null && units[i].getunitId() == unitId) {
                unit_found = i;
                break;
            }
        }
        if (unit_found == -1) {
            throw new IDNotRecognisedException("unit id cannot be found");
        }
        if (units[unit_found].getStatus() == UnitStatus.EN_ROUTE
                || units[unit_found].getStatus() == UnitStatus.AT_SCENE) {
            throw new IllegalStateException("wrong state");
        }
        int stationId = units[unit_found].getStationId();

        int station_found = -1;
        for (int i = 0; i < MAX_STATIONS; i++) {
            if (stations[i] != null && stations[i].getId() == stationId) {
                station_found = i;
                break;
            }
        }
        if (station_found == -1) {
            throw new IDNotRecognisedException("station id cannot be found");
        }

        if (station_found != -1) {
            stations[station_found].find_removeunit(unitId);
        }
        units[unit_found] = null;
        unitCount--;
    }
    /**
     * transfer a unit  
     * checks if the id given exists in the unit array
     * checks if the id given exists in the station array
     *update it 
     * @param unit id for it be checked if it exists and type for assignment and station id 
     * @return id 
     */
    @Override
    public void transferUnit(int unitId, int newStationId)
            throws IDNotRecognisedException, IllegalStateException {

        int unit_found = -1;
        for (int i = 0; i < MAX_UNITS; i++) {
            if (units[i] != null && units[i].getunitId() == unitId) {
                unit_found = i;
                break;
            }
        }
        if (unit_found == -1) {
            throw new IDNotRecognisedException("unit id cannot be found");
        }

        int station_found = -1;
        for (int i = 0; i < MAX_STATIONS; i++) {
            if (stations[i] != null && stations[i].getId() == newStationId) {
                station_found = i;
                break;
            }
        }
        if (station_found == -1) {
            throw new IDNotRecognisedException("station id cnanot be found ");
        }

        if (units[unit_found].getStatus() != UnitStatus.IDLE) {
            throw new IllegalStateException("has to be idle state");
        }

        if (stations[station_found].spaceLeft() == false) {
            throw new IllegalStateException("no space left");
        }

        int oldStationId = units[unit_found].getStationId();

        int oldStation_found = -1;
        for (int i = 0; i < MAX_STATIONS; i++) {
            if (stations[i] != null && stations[i].getId() == oldStationId) {
                oldStation_found = i;
                break;
            }
        }

        if (oldStation_found != -1) {
            stations[oldStation_found].find_removeunit(unitId);
        }

        units[unit_found].updateLocation(stations[station_found].getX(), stations[station_found].getY());
        stations[station_found].addUnit(unitId);
        units[unit_found].changeStationId(newStationId);
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService)
            throws IDNotRecognisedException, IllegalStateException {

        int unit_found = -1;

        
        for (int i = 0; i < MAX_UNITS; i++) {
            if (units[i] != null && units[i].getunitId() == unitId) {
                unit_found = i;
                break;
            }
        }

        if (unit_found == -1) {
            throw new IDNotRecognisedException("unit id cannot be found");
        }

        if (outOfService) {

            
            if (units[unit_found].getStatus() != UnitStatus.IDLE) {
                throw new IllegalStateException("state has to be idle");
            }

            units[unit_found].setStatus(UnitStatus.OUT_OF_SERVICE);

        } else {

           
            if (units[unit_found].getStatus() == UnitStatus.OUT_OF_SERVICE) {
                units[unit_found].setStatus(UnitStatus.IDLE);
            }
        }
    }
    /**
     * gets unit ids
     * create a new array
     * copy everything into the new array
     * bubble sort it to be in ascending order 
     * @return the id array 
     */
    @Override
    public int[] getUnitIds() {
        int[] elements_index = new int[MAX_UNITS];
        int count = 0;

        
        for (int i = 0; i < MAX_UNITS; i++) {
            if (units[i] != null) {
                elements_index[count++] = i;
            }
        }

        
        int[] id_copy = new int[count];
        int index;

        for (int j = 0; j < count; j++) {
            index = elements_index[j];
            id_copy[j] = units[index].getunitId();
        }

       
        for (int i = 0; i < id_copy.length - 1; i++) {
            for (int j = 0; j < id_copy.length - 1 - i; j++) {
                if (id_copy[j] > id_copy[j + 1]) {
                    int temp = id_copy[j];
                    id_copy[j] = id_copy[j + 1];
                    id_copy[j + 1] = temp;
                }
            }
        }

        return id_copy;
    }


    /**
     * gets unit
     * @return view unit
     * @param unit id to see if it exists in the unit array  
     */
    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        int unit_found = -1;
        for (int i = 0; i < MAX_UNITS; i++) {
            if (units[i] != null && units[i].getunitId() == unitId) {
                unit_found = i;
                break;
            }
        }

        if (unit_found == -1) {
            throw new IDNotRecognisedException("unit cannot be found");
        }

        return units[unit_found].viewUnit();
    }
    /**
     * report incident 
     * check severity is between 1 to 5 
     * check type isnt null , city map isnt null and city map is in bounds and not blocked
     * chcek for empty incident space in the array
     *create a new incident
     * @return id of incident
     * @param type of incident , severity of incident, coordinates of the incident   
     */
    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y)
            throws InvalidSeverityException, InvalidLocationException {

        
        if (type == null) {
            throw new InvalidSeverityException("type cannot be null");
        }

        if (severity < 1 || severity > 5) {
            throw new InvalidSeverityException("severity is not between 1 to 5 ");
        }

        if (city_map == null || !city_map.inBounds(x, y) || city_map.isBlocked(x, y)) {
            throw new InvalidLocationException("invalid location given ");
        }

        
        int empty_incident = -1;
        for (int i = 0; i < MAX_INCIDENTS; i++) {
            if (incidents[i] == null) {
                empty_incident = i;
                break;
            }
        }

        if (empty_incident == -1) {
            throw new CapacityExceededException("capacity exceeded");
        }

        int id = incidentID_counter++;

        incidents[empty_incident] = new Incident(id, type, severity, x, y);
        incidentCount++;

        return id;
    }
    /**
     * cancel incident 
     * check if incident exists in the array of incidents
     *if dispatched then release the unit
     * check the unit id is found
     *if not found clear it 
     * @param incident id to check if it exists
     */
    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        int incident_found = -1;

        // find incident
        for (int i = 0; i < MAX_INCIDENTS; i++) {
            if (incidents[i] != null && incidents[i].getId() == incidentId) {
                incident_found = i;
                break;
            }
        }

        if (incident_found == -1) {
            throw new IDNotRecognisedException("incident id cannot be found");
        }

        if (incidents[incident_found].getStatus() != IncidentStatus.REPORTED
                && incidents[incident_found].getStatus() != IncidentStatus.DISPATCHED) {
            throw new IllegalStateException("illegal state");
        }

        // if dispatched, release the unit
        if (incidents[incident_found].getStatus() == IncidentStatus.DISPATCHED) {

            int ver_id = incidents[incident_found].getId();

            int unit_found = -1;
            for (int i = 0; i < MAX_UNITS; i++) {
                if (units[i] != null && units[i].getunitId() == ver_id) {
                    unit_found = i;
                    break;
                }
            }

            if (unit_found != -1) {
                units[unit_found].clearIncident();
                units[unit_found].setStatus(UnitStatus.IDLE); // stays where it is
            }

            incidents[incident_found].clearUnit(); // assigned unit -> -1 inside Incident
        }

        // mark incident cancelled
        incidents[incident_found].setStatus(IncidentStatus.CANCELLED);
    }
    /**
     * escalte the incident 
     * check if the incident can be found in the incidents array
     *validate severity
     * cannot escalarte if resolved or canceleed
     *appky change
     *@param incident id to check if exists and new severity to be assigned
     */
    @Override
    public void escalateIncident(int incidentId, int newSeverity)
            throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {

        int incident_found = -1;

        
        for (int i = 0; i < MAX_INCIDENTS; i++) {
            if (incidents[i] != null && incidents[i].getId() == incidentId) {
                incident_found = i;
                break;
            }
        }

        if (incident_found == -1) {
            throw new IDNotRecognisedException("incident id cannot be found");
        }

        if (newSeverity < 1 || newSeverity > 5) {
            throw new InvalidSeverityException("severity is not between 1 and 5 ");
        }

       
                
        if (incidents[incident_found].getStatus() == IncidentStatus.RESOLVED
                || incidents[incident_found].getStatus() == IncidentStatus.CANCELLED) {

            throw new IllegalStateException("illegal state");
        }

     
        incidents[incident_found].setSeverity(newSeverity);
    }

    @Override
    public int[] getIncidentIds() {
        return sortedIncidentIds();
    }

    /**
     * gets  incident ids
     * create a new array
     * copy everything into the new array
     * bubble sort it to be in ascending order 
     * @return the id array 
     */

    private int[] sortedIncidentIds() {
        int[] elements_index = new int[MAX_INCIDENTS];
        int count = 0;

        for (int i = 0; i < MAX_INCIDENTS; i++) {
            if (incidents[i] != null) {
                elements_index[count++] = i;
            }
        }

        int[] id_copy = new int[count];
        int index;

        for (int j = 0; j < count; j++) {
            index = elements_index[j];
            id_copy[j] = incidents[index].getId();
        }

        for (int i = 0; i < id_copy.length - 1; i++) {
            for (int j = 0; j < id_copy.length - 1 - i; j++) {
                if (id_copy[j] > id_copy[j + 1]) {
                    int temp = id_copy[j];
                    id_copy[j] = id_copy[j + 1];
                    id_copy[j + 1] = temp;
                }
            }
        }

        return id_copy;
    }

   @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        Incident incident = null;

        for(int i = 0; i < MAX_INCIDENTS; i++){
            if(incidents[i] != null && incidents[i].getIncidentId() == incidentId){
                incident = incidents[i];
                break;
            }
        }

        if (incident == null){
            throw new IDNotRecognisedException("id not recognised");
        }

        String unitPart = "-";
        if (incident.getAssignedUnit() != null){
            unitPart = String.valueOf(incident.getAssignedUnit().getunitId());
        }

        return "I#" + incident.getIncidentId() + " TYPE=" + incident.getType() + " SEV=" + incident.getSeverity() + " LOC=(" + incident.getX() + "," + incident.getY() + ")" + " STATUS=" + incident.getStatus() + " UNIT=" + unitPart;
    }

      
    /**
     * assigns available units to REPORTED incidents.
     * incidents are processed in ascending ID order.
     * the best eligible unit is chosen using the required
     * tie-break rules (distance, unitId, stationId).
     */
    @Override
    public void dispatch() {
        int[] incidentIds = getIncidentIds();

        for (int id : incidentIds) {

            Incident incident = null;

            for (int i = 0; i < MAX_INCIDENTS; i++) {
                if (incidents[i] != null &&
                    incidents[i].getIncidentId() == id) {
                    incident = incidents[i];
                    break;
                }
            }

            if (incident == null) continue;
            if (incident.getStatus() != IncidentStatus.REPORTED)
                continue;
            Unit bestUnit = null;
            int bestDistance = Integer.MAX_VALUE;

            for (int j = 0; j < MAX_UNITS; j++){
                Unit unit = units[j];
                if (unit == null) continue; 
                if(unit.getStatus() != UnitStatus.IDLE) continue;
                if (!unit.canHandle(incident.getType())) continue;

                int distance = unit.manhattan_distance(incident.getX(), incident.getY());
                if(bestUnit == null || distance < bestDistance || (distance == bestDistance && unit.getunitId() < bestUnit.getunitId()) || (distance == bestDistance && unit.getunitId() == bestUnit.getunitId() && unit.getStationId() < bestUnit.getStationId())){
                    bestUnit = unit;
                    bestDistance = distance;

                }

            }

            if (bestUnit != null){
                bestUnit.assignIncident(incident.getIncidentId());
                bestUnit.setStatus(UnitStatus.EN_ROUTE);
                incident.setAssignedUnit(bestUnit);
                incident.setStatus(IncidentStatus.DISPATCHED);
            }
        }
    }
     /**
     * advances the simulation by one tick.
     * Order:
     * 1. increment tick
     * 2. mve EN_ROUTE units (ascending unitId)
     * 3. mark arrivals
     * 4. process work at scene
     * 5. resolve completed incidents (ascending incidentId)
     */
    @Override
    public void tick() {
        tick++;

        for(int i = 0; i < MAX_UNITS; i++){
            Unit unit = units[i];
            if (unit == null) continue;
            if(unit.getStatus() == UnitStatus.EN_ROUTE){
                int incidentId = unit.getIncidentid();
                Incident targetIncident = null;
                for (int j = 0; j < MAX_INCIDENTS; j++){
                    if(incidents[j] != null && incidents[j].getIncidentId() == incidentId){
                        targetIncident = incidents[j];
                        break;
                    }
                }
        

                if (targetIncident == null) continue;

                int currentX = unit.getX();
                int currentY = unit.getY();
                int targetX = targetIncident.getX();
                int targetY  = targetIncident.getY();
        
                int[][] directions = {{0, -1},{1,0},{0,1},{-1,0}};
                boolean moved = false;
                int currentDistance = unit.manhattan_distance(targetX, targetY);
        
                for (int[] d: directions){
                    int newX = currentX + d[0];
                    int newY = currentY + d[1];
                    if (city_map.inBounds(newX, newY) && !city_map.isBlocked(newX, newY)){
                        int newDistance = Math.abs(newX - targetX) + Math.abs(newY - targetY);
                        if (newDistance < currentDistance){
                            unit.updateLocation(newX, newY);
                            moved = true;
                            break;
                        }
                    }
                }

                if(!moved){
                    for (int[] d: directions){
                    int newX = currentX + d[0];
                    int newY = currentY + d[1];
                    if (city_map.inBounds(newX, newY) && !city_map.isBlocked(newX, newY)){
                        unit.updateLocation(newX, newY);
                        moved = true;
                        break;
                        }
                    }
                }

                if (unit.getX() == targetX && unit.getY() == targetY){
                    unit.setStatus(UnitStatus.AT_SCENE);
                    unit.start_tick(unit.getTicksToResolve(targetIncident.getSeverity()));
                    targetIncident.setStatus(IncidentStatus.IN_PROGRESS);
                }
            }    
            else if (unit.getStatus() == UnitStatus.AT_SCENE){
                unit.tick_count();
                if(unit.isWorkZero()){
                    int incidentId = unit.getIncidentid();
                    for(int j = 0; j < MAX_INCIDENTS; j++){
                        if(incidents[j] != null && incidents[j].getIncidentId() == incidentId){
                            incidents[j].setStatus(IncidentStatus.RESOLVED);
                            incidents[j].setAssignedUnit(null);
                            break;
                        } 
                    }
                    unit.clearIncident();
                    unit.setStatus(UnitStatus.IDLE);
                }
            }
        }
    }
    /**
     * returns snapshot of the system.
     * includes tick number, counts, all incidents,
     * and all units formatted exactly as specified.
     * @return multi-line status string
     */
    @Override
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("TICK=" ).append(tick).append("\n");

        int[] incidentIds = getIncidentIds();
        sb.append("INCIDENTS");
        for(int id: incidentIds){
            try{
                sb.append(viewIncident(id)).append("\n"); 
            } catch (IDNotRecognisedException e) {}
        }

        sb.append("STATIONS:\n");
        int[] stationIds = getStationIds();
        for (int id : stationIds) {
            sb.append("Station#").append(id).append("\n"); 
        }

        int[] unitIds = getUnitIds();
        sb.append("UNITS");
        for(int id: unitIds){
            try{
                sb.append(viewUnit(id)).append("\n");
            } catch (IDNotRecognisedException e) {}
        }

        return sb.toString().trim();
    }
}
