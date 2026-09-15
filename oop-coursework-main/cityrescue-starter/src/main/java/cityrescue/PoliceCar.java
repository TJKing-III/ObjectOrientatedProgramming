
package cityrescue;

import cityrescue.enums.*;
public class PoliceCar extends Unit {

    public PoliceCar(int unitId, int stationId, int x, int y) {
        super(unitId, UnitType.POLICE_CAR, stationId, x, y);
    }

    @Override
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.CRIME;
    }

    @Override
    public int getTicksToResolve(int severity) {
        return 3;
    }
}

