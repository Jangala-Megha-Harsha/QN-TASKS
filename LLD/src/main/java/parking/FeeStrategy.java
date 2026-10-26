package parking;

import java.time.*;

public interface FeeStrategy {
    long calculate(Ticket ticket, Instant exitTime);
}
