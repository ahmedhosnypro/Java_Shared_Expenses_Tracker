package splitter;

import java.util.Date;

public record Payment(Person from, Person to, int amount, Date date)  implements Comparable<Payment>{
    // setup to be ordered by date when added to a TreeSet
    public int compareTo(Payment other) {
        return date.compareTo(other.date);
    }
}
