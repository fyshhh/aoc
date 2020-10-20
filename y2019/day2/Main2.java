package y2019.day2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Main2 {

    static class IntCode {

        Map<Integer, Integer> program;

        IntCode() {
            program = new HashMap<>();
        }

        IntCode initialiseFromArr(int[] arr) {      // for test cases
            program = IntStream.range(0, arr.length)
                    .boxed()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            i -> arr[i],
                            (a, b) -> a,        // BiFunction implementation unnecessary as there are no collisions
                            HashMap::new));
            return this;
        }

        IntCode initialiseFromPath(String filePath) {   // for input.txt
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                int[] arr = Arrays.stream(reader.readLine().split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                program = IntStream.range(0, arr.length)
                        .boxed()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                i -> arr[i],
                                (a, b) -> a,    // BiFunction implementation unnecessary as there are no collisions
                                HashMap::new));
            } catch (IOException ignored) { }
            return this;
        }

        IntCode rewrite(int index, int value) {
            assert program != null;
            program.put(index, value);
            return this;
        }

        private int iterate(int index) {    // performs one iteration
            int code = program.get(index);
            switch (code) {

            case 1:
                program.put(program.get(index + 3),
                        program.get(program.get(index + 1)) + program.get(program.get(index + 2)));
                return index + 4;

            case 2:
                program.put(program.get(index + 3),
                        program.get(program.get(index + 1)) * program.get(program.get(index + 2)));
                return index + 4;

            case 99:
//                System.out.println("Program completed successfully");
                return -1;

            default:
                System.out.printf("Error encountered at index %d, retrieving value %d.\n", index, code);
                return -2;

            }
        }

        void process() {    // performs iterations until it halts
            int index = 0;
            while (index >= 0) {
                index = iterate(index);
            }
        }

        int get(int index) {
            return program.get(index);
        }

        void display(int index) {
            System.out.println(get(index));
        }

        void print() {
            System.out.println(Arrays.toString(program.values().toArray()));
        }

    }

    public static void main(String[] args) {
        int[] test1 = new int[] {1,9,10,3,2,3,11,0,99,30,40,50};
        String filePath = "y2019/day2/input.txt";
//        IntCode intcode = new IntCode().initialiseFromArr(test1);
//         part one
//        IntCode intcode = new IntCode().initialiseFromPath(filePath);
//        intcode.rewrite(1, 12).rewrite(2, 2);
//        intcode.process();
//        intcode.display(0);
//        part two
        outer:
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                IntCode intcode = new IntCode().initialiseFromPath(filePath).rewrite(1, i).rewrite(2, j);
                intcode.process();
                if (intcode.get(0) == 19690720) {
                    System.out.printf("Value is: %d\n", 100 * i + j);
                    break outer;
                }
            }
        }
    }

}
