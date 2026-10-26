package parking;

import java.time.*;

public final class ParkingLotTestRunner {
    private static int passed;

    private static void check(boolean value, String name) {
        if (!value)
            throw new AssertionError(name);
        passed++;
    }

    private static ParkingLot lot() {
        ParkingLot lot = new ParkingLot(new HourlyFeeStrategy(10, 20, 30), new FirstFitAllocationStrategy());
        ParkingFloor floor = new ParkingFloor(1);
        floor.addSlot(new ParkingSlot("B", VehicleType.BIKE));
        floor.addSlot(new ParkingSlot("C", VehicleType.CAR));
        lot.addFloor(floor);
        return lot;
    }

    public static void main(String[] args) {
        Instant t = Instant.parse("2026-01-01T10:00:00Z");
        ParkingLot a = lot();
        check(a.park(new Bike("b"), t).slot().id().equals("B"), "allocation");
        ParkingLot b = lot();
        b.park(new Bike("b"), t);
        try {
            b.park(new Bike("b"), t);
            throw new AssertionError();
        } catch (IllegalStateException expected) {
            passed++;
        }
        ParkingLot c = lot();
        c.park(new Bike("b"), t);
        try {
            c.park(new Bike("c"), t);
            throw new AssertionError();
        } catch (IllegalStateException expected) {
            passed++;
        }
        ParkingLot d = lot();
        String id = d.park(new Car("c"), t).id();
        check(d.unpark(id, t.plusSeconds(3601)) == 20, "fee");
        try {
            d.unpark("bad", t);
            throw new AssertionError();
        } catch (IllegalArgumentException expected) {
            passed++;
        }
        ParkingLot e = lot();
        String eid = e.park(new Car("c"), t).id();
        e.unpark(eid, t);
        try {
            e.unpark(eid, t);
            throw new AssertionError();
        } catch (IllegalArgumentException expected) {
            passed++;
        }
        ParkingLot f = lot();
        ParkingFloor second = new ParkingFloor(2);
        second.addSlot(new ParkingSlot("T", VehicleType.TRUCK));
        f.addFloor(second);
        check(f.park(new Truck("t"), t).slot().id().equals("T"), "multiple floors");
        ParkingLot g = lot();
        g.park(new Car("c"), t);
        check(g.freeSlots(VehicleType.CAR).isEmpty() && g.occupancy().contains("OCCUPIED"), "views");
        System.out.println("LLD smoke tests passed: " + passed);
    }
}
