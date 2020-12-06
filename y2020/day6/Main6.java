package y2020.day6;

import resource.PathReader;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Main6 {

    static Set<Character> countAnyone(String group) {
        StringBuilder input = new StringBuilder(group);
        Set<Character> chars = new HashSet<>();
        while (input.length() > 0) {
            char c = input.charAt(0);
            input.deleteCharAt(0);
            if (c != '\n') {
                chars.add(c);
            }
        }
        return chars;
    }

    static Set<Character> countEveryone(String group) {
        String[] sets = group.split("\n");
        Set<Character> chars = new HashSet<>();
        StringBuilder fst = new StringBuilder(sets[0]);
        String[] rest = Arrays.copyOfRange(sets, 1, sets.length);
        while (fst.length() > 0) {
            char c = fst.charAt(0);
            fst.deleteCharAt(0);
            chars.add(c);
        }
        for (char c : new HashSet<>(chars)) {
            if (!Arrays.stream(rest).allMatch(s -> s.contains(Character.toString(c)))) {
                chars.remove(c);
            }
        }
        return chars;
    }

    static void parseFilepath(String filePath) {
        try {
            String str = PathReader.generate(filePath).lines().collect(Collectors.joining("\n"));
            int sum1 = Arrays.stream(str.split("\n\n"))
                    .map(Main6::countAnyone)
                    .mapToInt(Set::size)
                    .sum();
            System.out.println(sum1);
            int sum2 = Arrays.stream(str.split("\n\n"))
                    .map(Main6::countEveryone)
                    .mapToInt(Set::size)
                    .sum();
            System.out.println(sum2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "y2020/day6/input.in";
        parseFilepath(filePath);
    }

}
