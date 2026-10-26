package parking;

import java.util.*;

public final class ParkingFloor {
    private final int number;
    private final List<ParkingSlot> slots = new ArrayList<>();

    public ParkingFloor(int number) {
        if (number < 1)
            throw new IllegalArgumentException("floor number");
        this.number = number;
    }

    public int number() {
        return number;
    }

    public void addSlot(ParkingSlot slot) {
        if (slots.stream().anyMatch(s -> s.id().equals(slot.id())))
            throw new IllegalArgumentException("duplicate slot");
        slots.add(slot);
    }

    public List<ParkingSlot> slots() {
        return Collections.unmodifiableList(slots);
    }
}
