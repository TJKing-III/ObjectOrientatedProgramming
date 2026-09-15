package cityrescue;

import cityrescue.enums.*;
public class Station {
    private final int stationId; //id of station 
    private final String name;
    private final int x;
    private final int y;
    private int capacity; //max number of units possible 
    private int[] unitIds; //ids that are inside this station 
    private int total_units; //total number of units in the station

    public Station(int stationId, String name, int x, int y, int capacity) {

        this.stationId = stationId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
        unitIds = new int[capacity]; //create an empty array for the ids to be stored in
        total_units = 0;
    }

    public String getName() {
        return name;
    }
    
    public int getId() {
        return stationId;
    }


    public int getX() {
        return x;
    }

     public int getUnitcount() {
        return total_units;
    }
    public int[] getUnitids() {
        return unitIds;
    }

    public int getY() {
        return y;
    }

    public int getCapacity() {
        return capacity;
    }


    public void spaceRestriction(int maxunits) { //to make an array have a set amount of elements

        
        int[] newUnitIds = new int[maxunits]; // make the new array with new restriction

       
        for (int i = 0; i < total_units; i++) { //copy all the ids to the new srray
            newUnitIds[i] = unitIds[i];
        }

        //make them equal agian
        unitIds = newUnitIds;
        capacity = maxunits;
    }

    
    public boolean spaceLeft() { //to check if there is any space left in the station 

        if (total_units < capacity){
            return true; 
        }
        else{
            return false;
        }
        
    }
    public void addUnit(int unitId) { // to add units to the station 
        //add it to the next avaiable space 
        unitIds[total_units] = unitId;
        total_units++; //incremebt
        sorting();//call sorting function 
    }

    //call bubble sort 
    public void sorting() {
        bubblesortunit();
    }

    public void removeunit(int position){//to remove the a unit from the station
        for (int i = position; i < total_units - 1; i++) {
            unitIds[i] = unitIds[i + 1]; //shift them all 
        }
        total_units--; //units decreased

    }

    public void find_removeunit(int unitId) { //to find the element to remove a unit form the station 
        int position = -1;

        for (int i = 0; i < total_units; i++) { //to find the unit that needs to be removed 
            if (unitIds[i] == unitId) {
                position = i; //postion found 
                break; 
            }
        }
        if (position == -1) { //not found? the return notihng
            return;
        }
        removeunit(position);//call the method that actually removes it 
    }
    public int[] returnUnitIds() {//return all the ids 
        sorting();

        int[] copy = new int[total_units];
        for (int i = 0; i < total_units; i++) {
            copy[i] = unitIds[i]; //send back a copy not the original so it doesn't get altered
        }
        return copy;
    }

    private void bubblesortunit() {//normal bubble sort to arrange them in a order
        for (int i = 0; i < total_units - 1; i++) {
            for (int j = 0; j < total_units - 1 - i; j++) {
                if (unitIds[j] > unitIds[j + 1]) {
                    int temp = unitIds[j];
                    unitIds[j] = unitIds[j + 1];
                    unitIds[j + 1] = temp;
                }
            }
        }
    }
}






