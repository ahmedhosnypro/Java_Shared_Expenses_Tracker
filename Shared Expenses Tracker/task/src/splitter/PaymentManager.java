package splitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PaymentManager {
    private PaymentManager() {
    }

    private static final Set<Payment> payments = new TreeSet<>();

    public static void addPayment(Payment payment) {
        payments.add(payment);
    }

    public static void balance(Date date, boolean close) {
        List<Payment> tmpPayments;
        if (close) {
            tmpPayments = payments.stream().filter(p -> p.date().compareTo(date) <= 0).toList();
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int referenceYear = cal.get(Calendar.YEAR);   // Extract the year from the reference date
            int referenceMonth = cal.get(Calendar.MONTH); // Extract the month from the reference date
            tmpPayments = payments.stream().filter(payment -> {
                cal.setTime(payment.date());
                int dateYear = cal.get(Calendar.YEAR);   // Extract the year from the current date
                int dateMonth = cal.get(Calendar.MONTH); // Extract the month from the current date
                return dateYear < referenceYear || (dateYear == referenceYear && dateMonth < referenceMonth);
            }).toList();
        }

        Map<Person, Map<Person, Integer>> fromToAmount = new ConcurrentHashMap<>();
        synchronized (tmpPayments) {
            for (var p : tmpPayments) {
                if (fromToAmount.containsKey(p.from())) {
                    var fromPerson = fromToAmount.get(p.from());
                    if (fromPerson.containsKey(p.to())) {
                        fromPerson.put(p.to(), fromPerson.get(p.to()) + p.amount());
                    } else {
                        fromPerson.put(p.to(), p.amount());
                    }
                } else {
                    var toPerson = new ConcurrentHashMap<>(Map.of(p.to(), p.amount()));
                    fromToAmount.put(p.from(), toPerson);
                }
            }
        }
        balance(fromToAmount);
    }

    private static void balance(Map<Person, Map<Person, Integer>> fromToAmount) {
        Set<Set<Person>> calculated = new HashSet<>();
        Set<String> repayments = new TreeSet<>();
        for (var entry : fromToAmount.entrySet()) {
            var fromPerson = entry.getKey();
            var value = entry.getValue();
            int repay;
            int borrow = 0;
            for (var toEntry : value.entrySet()) {
                var toPerson = toEntry.getKey();
                var amount = toEntry.getValue();
                if (!calculated.contains(Set.of(fromPerson, toPerson))) {
                    repay = amount;
                    if (fromToAmount.containsKey(toPerson) && fromToAmount.get(toPerson).containsKey(fromPerson)) {
                        borrow = fromToAmount.get(toPerson).get(fromPerson);
                    }
                    if (repay < borrow) {
                        repayments.add(fromPerson.name + " owes " + toPerson.name + " " + (borrow - repay));
                    } else if (repay > borrow) {
                        repayments.add(toPerson.name + " owes " + fromPerson.name + " " + (repay - borrow));
                    }
                }
                calculated.add(Set.of(fromPerson, toPerson));
            }
        }
        if (repayments.isEmpty()) {
            System.out.println("No repayments");
        } else {
            repayments.forEach(System.out::println);
        }
    }
}
