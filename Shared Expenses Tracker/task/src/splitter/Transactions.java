package splitter;

import java.time.LocalDate;
import java.util.*;

class Transactions {

    public enum BalanceType {
        OPEN, CLOSE
    }

    List<Payment> payments;

    public Transactions() {
        this.payments = new ArrayList<>();
    }

    public void addTransaction(LocalDate date, Person from, Person to, int amount) {
        payments.add(new Payment(date, from, to, amount));
    }

    public Set<String> getBalance(LocalDate date, BalanceType type) {
        Map<Pair<Person>, Integer> relations = new HashMap<>();
        LocalDate endDate = date;

        if (type == BalanceType.CLOSE) {
            endDate = endDate.plusDays(1);
        } else {
            endDate = date.withDayOfMonth(1);
        }

        processTransactions(endDate, relations);
        return getBalance(relations);
    }

    private static Set<String> getBalance(Map<Pair<Person>, Integer> relations) {
        Set<String> balance = new TreeSet<>();
        for (Map.Entry<Pair<Person>, Integer> p2p : relations.entrySet()) {
            if (p2p.getValue() > 0) {
                balance.add(String.format("%s owes %s %d", p2p.getKey().getVal1().name(), p2p.getKey().getVal2().name(), p2p.getValue()));
            } else if (p2p.getValue() < 0) {
                balance.add(String.format("%s owes %s %d", p2p.getKey().getVal2().name(), p2p.getKey().getVal1().name(), -p2p.getValue()));
            }
        }
        return balance;
    }

    private void processTransactions(LocalDate endDate, Map<Pair<Person>, Integer> relations) {
        for (Payment payment : payments) {
            if (!payment.date().isBefore(endDate)) {
                continue;
            }
            int amount = payment.amount();
            Pair<Person> pair = new Pair<>(payment.from(), payment.to());
            boolean foundPair = false;
            if (!relations.containsKey(pair)) {
                Pair<Person> reversePair = new Pair<>(payment.to(), payment.from());
                if (relations.containsKey(reversePair)) {
                    pair = reversePair;
                    amount = -amount;
                    foundPair = true;
                }
            } else {
                foundPair = true;
            }
            if (foundPair) {
                relations.put(pair, relations.get(pair) + amount);
            } else {
                relations.put(pair, amount);
            }

        }
    }
}