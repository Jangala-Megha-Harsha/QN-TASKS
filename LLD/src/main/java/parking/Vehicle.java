package parking;

public abstract class Vehicle {
    private final String registration;
    private final VehicleType type;

    protected Vehicle(String registration, VehicleType type) {
        if (registration == null || registration.isBlank())
            throw new IllegalArgumentException("registration required");
        this.registration = registration;
        this.type = type;
    }

    public String registration() {
        return registration;
    }

    public VehicleType type() {
        return type;
    }
}
