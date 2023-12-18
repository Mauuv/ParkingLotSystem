package org.holders;

import org.entities.Vehicle;

public final class VehicleHolder {

    private Vehicle vehicle;
    private final static VehicleHolder INSTANCE = new VehicleHolder();

    private VehicleHolder() {}

    public static VehicleHolder getInstance() {
        return INSTANCE;
    }

    public void setVehicle(Vehicle v) {
        this.vehicle = v;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }
}
