package parking;

import java.time.*;

public final class Ticket {
    private final String id;
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    private final Instant entryTime;
    private Instant exitTime;
    private Long fee;

    Ticket(String id, Vehicle vehicle, ParkingSlot slot, Instant entryTime) {
        this.id = id;
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = entryTime;
    }

    public String id() {
        return id;
    }

    public Vehicle vehicle() {
        return vehicle;
    }

    public ParkingSlot slot() {
        return slot;
    }

    public Instant entryTime() {
        return entryTime;
    }

    public Instant exitTime() {
        return exitTime;
    }

    public boolean closed() {
        return exitTime != null;
    }

    void close(Instant time) {
        if (closed())
            throw new IllegalStateException("ticket already closed");
        exitTime = time;
    }

    public long durationHours(Instant at) {
        return Math.max(1, (Duration.between(entryTime, at).toMinutes() + 59) / 60);
    }
}
