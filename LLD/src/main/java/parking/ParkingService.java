package parking;

import java.time.*;

public final class ParkingService {
    private final ParkingLot lot;

    public ParkingService(ParkingLot lot) {
        this.lot = lot;
    }

    public Ticket enter(Vehicle v) {
        return lot.park(v, Instant.now());
    }

    public long exit(String ticket) {
        return lot.unpark(ticket, Instant.now());
    }

    public void show() {
        System.out.println(lot.occupancy());
    }
}
