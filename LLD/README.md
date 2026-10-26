# Extensible Multi-Floor Parking Lot

Java 17+ Maven console application supporting BIKE, CAR, and TRUCK. `ParkingLot` owns floors and active tickets; `ParkingFloor` owns typed slots; domain objects encapsulate state. `FeeStrategy` allows new pricing policies without changing parking logic, and `SlotAllocationStrategy` allows first-fit, nearest, or reservation-aware allocation. `VehicleFactory` centralizes vehicle creation and makes adding a type explicit in one place.

## Patterns and validation

- **Strategy:** fee calculation and slot selection are interfaces injected into `ParkingLot`.
- **Factory:** `VehicleFactory` creates the polymorphic vehicle subtype from `VehicleType`.
- Duplicate registration, duplicate floors/slots, incompatible/no slots, invalid tickets, and repeated exit are rejected with explicit exceptions. `unpark` releases the slot only after fee calculation and removes the active ticket atomically.

## Run

```text
mvn test
mvn package
java -jar target/parking-lot-1.0.0.jar
```

The driver demonstrates setup, factory creation, parking, free-slot viewing, occupancy, fee calculation, duplicate parking, capacity rejection, valid exit, repeated exit, invalid ticket, and active-ticket reporting. JUnit contains eight tests. A dependency-free `ParkingLotTestRunner` executes the same eight behavioral checks when Maven is unavailable. Maven was unavailable in the authoring environment; the source is also compilable with `javac` for a local smoke test.
