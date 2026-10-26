package parking;

public final class VehicleFactory {
    private VehicleFactory() {
    }

    public static Vehicle create(VehicleType type, String registration) {
        return switch (type) {
            case BIKE -> new Bike(registration);
            case CAR -> new Car(registration);
            case TRUCK -> new Truck(registration);
        };
    }
}
