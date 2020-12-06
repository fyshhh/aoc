package y2020.day5;

import resource.Pair;
import resource.PathReader;

import java.io.FileNotFoundException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main5 {

    static Pair<Integer, Integer> parse(String input) {
        StringBuilder ver = new StringBuilder(input.substring(0, 7));
        StringBuilder hor = new StringBuilder(input.substring(7));
        int minV = 0;
        int maxV = 127;
        while (ver.length() > 0) {
            char c = ver.charAt(0);
            ver.deleteCharAt(0);
            if (c == 'F') {
                maxV = (minV + maxV - 1) / 2;
            } else {
                minV = (minV + maxV + 1) / 2;
            }
        }
        int minH = 0;
        int maxH = 7;
        while (hor.length() > 0) {
            char c = hor.charAt(0);
            hor.deleteCharAt(0);
            if (c == 'L') {
                maxH = (minH + maxH - 1) / 2;
            } else {
                minH = (minH + maxH + 1) / 2;
            }
        }
        return new Pair<>(minV, minH);
    }

    static void parseFilepath(String filePath) {
        try {
            var possibleSeats = IntStream.rangeClosed(0, 127)
                    .boxed()
                    .flatMap(i -> IntStream.rangeClosed(0, 7)
                                .mapToObj(j -> new Pair<>(i, j)))
                    .collect(Collectors.toList());
            var actualSeats = PathReader.generate(filePath).lines()
                    .map(Main5::parse)
                    .peek(possibleSeats::remove)
                    .collect(Collectors.toList());
            actualSeats.stream()
                    .mapToInt(p -> p.fst * 8 + p.snd)
                    .max()
                    .ifPresent(System.out::println);
            possibleSeats.removeIf(p -> p.fst == 0 || p.fst == 127);
            possibleSeats.removeIf(p -> !actualSeats.contains(new Pair<>(p.fst + 1, p.snd)) ||
                    !actualSeats.contains(new Pair<>(p.fst - 1, p.snd)));
            System.out.println(possibleSeats.get(0).fst * 8 + possibleSeats.get(0).snd);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "y2020/day5/input.in";
        parseFilepath(filePath);
    }

}
