package y2019.day13;

import resource.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main13 {

    static class Coord {

        long x;
        long y;

        Coord(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new long[] {x, y});
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Coord && x == ((Coord) obj).x && y == ((Coord) obj).y;
        }

        @Override
        public String toString() {
            return Arrays.toString(new long[] {x, y});
        }

    }

    static class IntCode {      // can't import since this requires some changes to the internal IntCode program

        enum Status {
            INITIALIZED, NEED_INPUT, COMPLETED;
        }

        Scanner sc = new Scanner(System.in);
        boolean isConstructed = false;
        boolean userInput = true;
        boolean printToUser = false;
        List<Long> inputs = new ArrayList<>();
        List<Long> outputs = new ArrayList<>();
        int outputsLength = 0;

        private long index = 0;
        private long relative = 0;
        private Map<Long, Long> program = new HashMap<>();
        private Status status = Status.INITIALIZED;
        Map<Coord, Long> tiles = new HashMap<>();
        long input = 0L;

        public IntCode(boolean userInput) {
            this.userInput = userInput;
        }


        public IntCode initialiseFromPath(String filePath) {   // for input.txt
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                long[] arr = Arrays.stream(reader.readLine().split(","))
                        .mapToLong(Long::parseLong)
                        .toArray();
                program = IntStream.range(0, arr.length)
                        .boxed()
                        .collect(Collectors.toMap(
                                i -> (long) i,
                                i -> arr[i],
                                (a, b) -> a,    // BiFunction implementation unnecessary as there are no collisions
                                HashMap::new));
            } catch (IOException ignored) { }
            return this;
        }

        public long mode(long index, int mode) {
            switch (mode) {

            case 0:     // parameter mode
                return program.getOrDefault(index, 0L);

            case 1:     // immediate mode
                return index;

            case 2:     // relative mode
                return relative + program.getOrDefault(index, 0L);

            default:
                return -1;

            }
        }

        public IntCode iterate() {    // performs iterations until it should stop
            while (true) {
                int code = Math.toIntExact(program.get(index));
                int op = code % 100;
                int modes = code / 100;
                int mode1 = modes % 10;
                int mode2 = (modes / 10) % 10;
                int mode3 = modes / 100;

                assert (mode1 == 0 || mode1 == 1) : String.format("mode1 is %d", mode1);
                assert (mode2 == 0 || mode2 == 1) : String.format("mode2 is %d", mode2);
                assert (mode3 == 0 || mode3 == 1) : String.format("mode3 is %d", mode3);

                switch (op) {

                case 1:         // add
                    program.put(mode(index + 3, mode3),
                            program.getOrDefault(mode(index + 1, mode1), 0L)
                                    + program.getOrDefault(mode(index + 2, mode2), 0L));
                    index += 4;
                    break;

                case 2:         // multiply
                    program.put(mode(index + 3, mode3),
                            program.getOrDefault(mode(index + 1, mode1), 0L)
                                    * program.getOrDefault(mode(index + 2, mode2), 0L));
                    index += 4;
                    break;

                case 3:         // read input
                    if (!isConstructed) {
                        isConstructed = true;
                        IntStream.range(0, outputs.size() / 3)
                                .forEach(i -> tiles.put(new Coord(outputs.get(3 * i), outputs.get(3 * i + 1)),
                                        outputs.get(3 * i + 2)));
                    } else {
                        IntStream.range(outputsLength / 3, outputs.size() / 3)
                                .forEach(i -> {
                                    if (outputs.get(3 * i) == -1L && outputs.get(3 * i + 1) == 0L) {
                                        System.out.printf("Current score: %d\n", outputs.get(3 * i + 2));
                                    } else {
                                        tiles.put(new Coord(outputs.get(3 * i), outputs.get(3 * i + 1)),
                                                outputs.get(3 * i + 2));
                                    }
                                });
                    }
                    outputsLength = outputs.size();
                    if (tiles.entrySet().stream().filter(e -> e.getValue() == 3).findFirst().get().getKey().x ==
                            tiles.entrySet().stream().filter(e -> e.getValue() == 4).findFirst().get().getKey().x) {
                        input = 0L;
                    } else if (tiles.entrySet().stream().filter(e -> e.getValue() == 3).findFirst().get().getKey().x >
                            tiles.entrySet().stream().filter(e -> e.getValue() == 4).findFirst().get().getKey().x) {
                        input = -1L;
                    } else {
                        input = 1L;
                    }
                    program.put(mode(index + 1, mode1), input);
                    index += 2;
                    break;

                case 4:         // write output
                    long output = program.getOrDefault(mode(index + 1, mode1), 0L);
                    if (printToUser) {
                        System.out.printf("Output printed: %d\n", output);
                    }
                    outputs.add(output);
                    index += 2;
                    break;

                case 5:         // jump-if-eq
                    index = program.getOrDefault(mode(index + 1, mode1), 0L) != 0
                            ? program.getOrDefault(mode(index + 2, mode2), 0L)
                            : index + 3;
                    break;

                case 6:         // jump-if-neq
                    index = program.getOrDefault(mode(index + 1, mode1), 0L) == 0
                            ? program.getOrDefault(mode(index + 2, mode2), 0L)
                            : index + 3;
                    break;

                case 7:         // write-if-less
                    program.put(mode(index + 3, mode3),
                            program.getOrDefault(mode(index + 1, mode1), 0L)
                                    < program.getOrDefault(mode(index + 2, mode2), 0L)
                                    ? 1L
                                    : 0L);
                    index += 4;
                    break;

                case 8:         // write-if-eq
                    program.put(mode(index + 3, mode3),
                            program.getOrDefault(mode(index + 1, mode1), 0L)
                                    .equals(program.getOrDefault(mode(index + 2, mode2), 0L))
                                    ? 1L
                                    : 0L);
                    index += 4;
                    break;

                case 9:         // adjust relative
                    relative += program.getOrDefault(mode(index + 1, mode1), 0L);
                    index += 2;
                    break;

                case 99:        // done!
                    System.out.println("Program completed successfully.");
                    index = -1;
                    status = Status.COMPLETED;
                    IntStream.range(0, outputs.size() / 3)
                            .forEach(i -> tiles.put(new Coord(outputs.get(3 * i), outputs.get(3 * i + 1)),
                                    outputs.get(3 * i + 2)));
                    return this;

                default:
                    throw new UnsupportedOperationException(String.format("Unexpected opcode at index %d: %d", index, code));

                }
            }
        }

        public List<Long> getOutputs() {
            return outputs;
        }

        public boolean isCompleted() {
            return status == Status.COMPLETED;
        }

    }

    static class Arcade {

        IntCode intCode;

        Arcade(String filePath) {
            this.intCode = new IntCode(true).initialiseFromPath(filePath);
        }

        void simulate() {
            this.intCode.iterate();
        }

        void findTiles(long type) {
            System.out.println(this.intCode.tiles.values().stream().filter(i -> i == type).count());
        }

        void printScore() {
            System.out.println(this.intCode.getOutputs().get(this.intCode.getOutputs().size() - 1));
        }

    }

    public static void main(String[] args) {
        String filePath = "y2019/day13/input.txt";
        Arcade arcade = new Arcade(filePath);
        arcade.simulate();
//        arcade.findTiles(2);
        arcade.printScore();
    }

}
