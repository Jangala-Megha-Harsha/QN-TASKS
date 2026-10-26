# LLD Requirements Traceability

| Requirement | Implementation |
|---|---|
| Multiple floors and typed slots | ParkingFloor, ParkingSlot, ParkingLot |
| BIKE/CAR/TRUCK | VehicleType, Bike, Car, Truck |
| Ticket, fees, exit/release | Ticket, FeeStrategy, HourlyFeeStrategy, ParkingLot.unpark |
| Free slots and occupancy | ParkingLot.freeSlots, occupancy |
| Duplicate/invalid/repeated validation | ParkingLot.park/unpark |
| Extensible patterns | VehicleFactory, Strategy interfaces |
| Eight driver operations | Main |
| Eight automated tests | ParkingLotTest |
