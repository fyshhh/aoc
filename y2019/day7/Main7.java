package y2019.day7;

import y2019.intcode.IntCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main7 {

    static class Permutation<T> {       // generates permutations of a given list... if it's useful I'll package it out

        static class Construct<T> {

            private final List<T> list;

            @SafeVarargs
            public Construct(T... args) {
                this.list = List.of(args);
            }

            public Construct(List<T> list) {
                this.list = new ArrayList<>(list);
            }

            public Construct<T> splice(Construct<T> c) {
                List<T> copy = new ArrayList<>(this.list);
                copy.addAll(c.list);
                return new Construct<>(copy);
            }

            public boolean isUnique() {
                return this.list.stream()
                        .distinct()
                        .count() == this.list.size();
            }

            public List<T> getList() {
                return new ArrayList<>(list);
            }

        }

        public static <T> Set<List<T>> of(List<T> inputs) {
            Set<Construct<T>> base = new HashSet<>();
            inputs.stream()
                    .map((Function<T, Construct<T>>) Construct::new)
                    .forEach(base::add);
            Set<Construct<T>> set = new HashSet<>(base);
            for (int i = 0; i < inputs.size() - 1; i++) {
                set = set.stream()
                        .flatMap(c -> base.stream()
                                .map(c::splice)).collect(Collectors.toSet());
            }
            return set.stream()
                    .filter(Construct::isUnique)
                    .map(Construct::getList)
                    .collect(Collectors.toSet());
        }

    }

    static class AmplifierLoop {

        static class Amplifier {

            String filePath;
            IntCode intCode;

            Amplifier(String filePath) {
                this.filePath = filePath;
                intCode = new IntCode(false).initialiseFromPath(filePath);
            }

            Amplifier addInputs(List<Integer> list) {
                intCode.addInputs(list);
                return this;
            }

            long process() {
                intCode.iterate();
                return intCode.getLast();
            }

            public Amplifier clone() {
                return new Amplifier(filePath);
            }

        }

        List<Amplifier> amps;

        AmplifierLoop(String filePath) {
            amps = new LinkedList<>();
            IntStream.range(0, 5)
                    .forEach(i -> amps.add(new Amplifier(filePath)));
        }

        List<Amplifier> mapInputs (List<Integer> inputs) {
            return IntStream.range(0, 5)
                    .mapToObj(i -> amps.get(i).clone().addInputs(inputs.subList(i, i + 1)))
                    .collect(Collectors.toList());
        }

        int process(List<Integer> inputs) {     // processes one loop
            return mapInputs(inputs).stream()
                    .reduce(0, (out, amp) -> Math.toIntExact(amp.addInputs(List.of(out)).process()), (out, amp) -> out);
        }

        int highest(List<Integer> inputs) {
            return Permutation.of(inputs)
                    .stream()
                    .mapToInt(this::process)
                    .max()
                    .orElse(-1);
        }

        int processExh(List<Integer> inputs) {      // processes exhaustively
            var queue = new LinkedList<>(mapInputs(inputs));
            int output = 0;
            while (!queue.isEmpty()) {
                Amplifier amp = queue.poll().addInputs(List.of(output));
                output = (int) amp.process();
                if (!amp.intCode.isCompleted()) {
                    queue.add(amp);
                }
            }
            return output;
        }

        int highestExh(List<Integer> inputs) {
            return Permutation.of(inputs)
                    .stream()
                    .mapToInt(this::processExh)
                    .max()
                    .orElse(-1);
        }

    }

    public static void main(String[] args) {
        String filePath = "y2019/day7/input.txt";
        AmplifierLoop loop = new AmplifierLoop(filePath);
        System.out.println(loop.highest(List.of(0, 1, 2, 3, 4)));       // part one
        System.out.println(loop.highestExh(List.of(5, 6, 7, 8, 9)));    // part two
    }

}
