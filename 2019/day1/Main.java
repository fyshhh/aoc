import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Optional;

class Main {

    static int getFuel(int amount) {
        return Optional.of(Math.floorDiv(amount, 3) - 2).map(i -> i >= 0 ? i : 0).get();
    }

    static int getTotalFuel(int amount) {
        int fuel = amount;
        int total = 0;
        while (fuel > 0) {
            fuel = getFuel(fuel);
            total += fuel;
        }
        return total;
    }

    public static void main(String[] args) {
        String filePath = "./2019/day1/input.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            reader.lines()
                        .map(Integer::parseInt)
                        .map(Main::getFuel)         // comment out to see value for part two
                        .map(Main::getTotalFuel)    // comment out to see value for part one
                        .reduce(Integer::sum)
                        .ifPresent(System.out::println);
        } catch (FileNotFoundException ignored) { }
    }

}