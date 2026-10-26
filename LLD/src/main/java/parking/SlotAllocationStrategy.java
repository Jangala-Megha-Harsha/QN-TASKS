package parking;

import java.util.*;

public interface SlotAllocationStrategy {
    Optional<ParkingSlot> choose(List<ParkingFloor> floors, VehicleType type);
}
