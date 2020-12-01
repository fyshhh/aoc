package y2020.day1;

import resource.Pair;
import resource.PathReader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Main1 {

    static Set<Integer> getInputs(String filePath) {
        Set<Integer> ret = new HashSet<>();
        try {
            ret = PathReader.generate(filePath).lines()
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }

    static int findPair(Set<Integer> set, int val) {
        return set.stream()
                .filter(i -> set.contains(val - i))
                .findFirst()
                .orElse(-1);
    }

    static void findEntryPair(String filePath) {
        Set<Integer> inputs = getInputs(filePath);
        int output = findPair(inputs, 2020);
        System.out.println(output * (2020 - output));
    }

    static void findEntryTriplet(String filePath) {
        Set<Integer> inputs = getInputs(filePath);
        for (int i : inputs) {
            Set<Integer> copy = new HashSet<>(inputs);
            copy.remove(i);
            int j = findPair(copy, 2020 - i);
            if (j >= 0) {
                System.out.println(i * j * (2020 - i - j));
                break;
            } else {
                continue;
            }
        }
    }

    public static void main(String[] args) {
        String filePath = "y2020/day1/input.txt";
        findEntryPair(filePath);    // part one
        findEntryTriplet(filePath); // part two
    }

}
