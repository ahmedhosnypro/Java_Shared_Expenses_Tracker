package splitter;

import java.time.LocalDate;

public record Payment(LocalDate date, Person from, Person to, int amount) {
}
