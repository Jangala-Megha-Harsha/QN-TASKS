package parking;

import java.time.*;

public final class Main {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot(new HourlyFeeStrategy(10, 20, 30), new FirstFitAllocationStrategy());
        ParkingFloor one = new ParkingFloor(1), two = new ParkingFloor(2);
        one.addSlot(new ParkingSlot("1-B1", VehicleType.BIKE));
        one.addSlot(new ParkingSlot("1-C1", VehicleType.CAR));
        two.addSlot(new ParkingSlot("2-T1", VehicleType.TRUCK));
        two.addSlot(new ParkingSlot("2-C1", VehicleType.CAR));
        lot.addFloor(one);
        lot.addFloor(two);
        Instant start = Instant.parse("2026-01-01T10:00:00Z");
        Ticket bike = lot.park(VehicleFactory.create(VehicleType.BIKE, "BK-1"), start);
        Ticket car = lot.park(new Car("CAR-1"), start);
        System.out.println("Parked " + bike.id() + " and " + car.id());
        System.out.println("Free cars: " + lot.freeSlots(VehicleType.CAR));
        System.out.println("Occupancy:\n" + lot.occupancy());
        try {
            lot.park(new Bike("BK-1"), start);
        } catch (Exception e) {
            System.out.println("Duplicate rejected: " + e.getMessage());
        }
        System.out.println("Bike fee: " + lot.unpark(bike.id(), start.plusSeconds(3660)));
        Ticket truck = lot.park(new Truck("TR-1"), start);
        try {
            lot.park(new Truck("TR-2"), start);
        } catch (Exception e) {
            System.out.println("Capacity result: " + e.getMessage());
        }
        System.out.println("Car fee: " + lot.unpark(car.id(), start.plusSeconds(7200)));
        System.out.println("Truck fee: " + lot.unpark(truck.id(), start.plusSeconds(7200)));
        try {
            lot.unpark(car.id(), start.plusSeconds(7200));
        } catch (Exception e) {
            System.out.println("Repeated exit rejected: " + e.getMessage());
        }
        try {
            lot.unpark("bad-ticket", start);
        } catch (Exception e) {
            System.out.println("Invalid ticket rejected: " + e.getMessage());
        }
        System.out.println("Active tickets: " + lot.activeTickets());
    }
}
