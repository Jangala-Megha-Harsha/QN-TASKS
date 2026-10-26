package parking;

public final class ParkingSlot {
    private final String id;
    private final VehicleType type;
    private Vehicle vehicle;

    public ParkingSlot(String id, VehicleType type) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("slot id required");
        this.id = id;
        this.type = type;
    }

    public String id() {
        return id;
    }

    public VehicleType type() {
        return type;
    }

    public boolean isFree() {
        return vehicle == null;
    }

    public Vehicle vehicle() {
        return vehicle;
    }

    void park(Vehicle v) {
        if (!isFree())
            throw new IllegalStateException("slot occupied");
        if (v.type() != type)
            throw new IllegalArgumentException("incompatible slot");
        vehicle = v;
    }

    void release() {
        vehicle = null;
    }

    public String toString() {
        return id + ":" + type + (isFree() ? " FREE" : " OCCUPIED " + vehicle.registration());
    }
}
