package parking;

import java.time.*;

public final class HourlyFeeStrategy implements FeeStrategy {
    private final long bike, car, truck;

    public HourlyFeeStrategy(long bike, long car, long truck) {
        this.bike = bike;
        this.car = car;
        this.truck = truck;
    }

    public long calculate(Ticket t, Instant exit) {
        long rate = switch (t.vehicle().type()) {
            case BIKE -> bike;
            case CAR -> car;
            case TRUCK -> truck;
        };
        return rate * t.durationHours(exit);
    }
}
