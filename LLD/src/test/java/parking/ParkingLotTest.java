package parking;

import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import org.junit.jupiter.api.*;

class ParkingLotTest {
    ParkingLot lot;
    Instant t = Instant.parse("2026-01-01T10:00:00Z");

    @BeforeEach
    void setup() {
        lot = new ParkingLot(new HourlyFeeStrategy(10, 20, 30), new FirstFitAllocationStrategy());
        ParkingFloor f = new ParkingFloor(1);
        f.addSlot(new ParkingSlot("B", VehicleType.BIKE));
        f.addSlot(new ParkingSlot("C", VehicleType.CAR));
        lot.addFloor(f);
    }

    @Test
    void parksAndAllocates() {
        assertEquals("B", lot.park(new Bike("b"), t).slot().id());
    }

    @Test
    void duplicateRejected() {
        lot.park(new Bike("b"), t);
        assertThrows(IllegalStateException.class, () -> lot.park(new Bike("b"), t));
    }

    @Test
    void incompatibleCapacityRejected() {
        lot.park(new Bike("b"), t);
        assertThrows(IllegalStateException.class, () -> lot.park(new Bike("c"), t));
    }

    @Test
    void feeAndRelease() {
        String id = lot.park(new Car("c"), t).id();
        assertEquals(40, lot.unpark(id, t.plusSeconds(3601)));
        assertEquals(1, lot.freeSlots(VehicleType.CAR).size());
    }

    @Test
    void invalidTicket() {
        assertThrows(IllegalArgumentException.class, () -> lot.unpark("bad", t));
    }

    @Test
    void repeatedExit() {
        String id = lot.park(new Car("c"), t).id();
        lot.unpark(id, t);
        assertThrows(IllegalArgumentException.class, () -> lot.unpark(id, t));
    }

    @Test
    void multipleFloors() {
        ParkingFloor f = new ParkingFloor(2);
        f.addSlot(new ParkingSlot("T", VehicleType.TRUCK));
        lot.addFloor(f);
        assertEquals("T", lot.park(new Truck("t"), t).slot().id());
    }

    @Test
    void occupancyAndFreeView() {
        lot.park(new Car("c"), t);
        assertEquals(0, lot.freeSlots(VehicleType.CAR).size());
        assertTrue(lot.occupancy().contains("OCCUPIED"));
    }
}
