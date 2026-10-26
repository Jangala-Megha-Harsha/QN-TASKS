package parking;

import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public final class ParkingLot {
    private final List<ParkingFloor> floors = new ArrayList<>();
    private final Map<String, Ticket> active = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong();
    private final FeeStrategy fees;
    private final SlotAllocationStrategy allocation;

    public ParkingLot(FeeStrategy fees, SlotAllocationStrategy allocation) {
        this.fees = Objects.requireNonNull(fees);
        this.allocation = Objects.requireNonNull(allocation);
    }

    public void addFloor(ParkingFloor floor) {
        if (floors.stream().anyMatch(f -> f.number() == floor.number()))
            throw new IllegalArgumentException("duplicate floor");
        floors.add(floor);
    }

    public synchronized Ticket park(Vehicle v, Instant now) {
        if (active.values().stream().anyMatch(t -> t.vehicle().registration().equals(v.registration())))
            throw new IllegalStateException("vehicle already parked");
        ParkingSlot slot = allocation.choose(floors, v.type())
                .orElseThrow(() -> new IllegalStateException("no available slot"));
        slot.park(v);
        Ticket t = new Ticket("T-" + sequence.incrementAndGet(), v, slot, now);
        active.put(t.id(), t);
        return t;
    }

    public synchronized long unpark(String ticketId, Instant now) {
        Ticket t = active.remove(ticketId);
        if (t == null)
            throw new IllegalArgumentException("invalid or repeated ticket");
        long fee = fees.calculate(t, now);
        t.close(now);
        t.slot().release();
        return fee;
    }

    public List<ParkingSlot> freeSlots(VehicleType type) {
        return floors.stream().flatMap(f -> f.slots().stream()).filter(s -> s.type() == type && s.isFree()).toList();
    }

    public String occupancy() {
        return floors.stream().map(f -> "Floor " + f.number() + " " + f.slots())
                .reduce((a, b) -> a + System.lineSeparator() + b).orElse("No floors");
    }

    public int activeTickets() {
        return active.size();
    }
}
