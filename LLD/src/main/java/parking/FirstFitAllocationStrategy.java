package parking;

import java.util.*;

public final class FirstFitAllocationStrategy implements SlotAllocationStrategy {
    public Optional<ParkingSlot> choose(List<ParkingFloor> floors, VehicleType type) {
        return floors.stream().flatMap(f -> f.slots().stream()).filter(s -> s.isFree() && s.type() == type).findFirst();
    }
}
