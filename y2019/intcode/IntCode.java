package y2019.intcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntCode {

    Scanner sc = new Scanner(System.in);
    Map<Integer, Integer> program;

    public IntCode() {
        program = new HashMap<>();
    }

    public IntCode initialiseFromArr(int[] arr) {      // for test cases
        program = IntStream.range(0, arr.length)
                .boxed()
                .collect(Collectors.toMap(
                        Function.identity(),
                        i -> arr[i],
                        (a, b) -> a,        // BiFunction implementation unnecessary as there are no collisions
                        HashMap::new));
        return this;
    }

    public IntCode initialiseFromPath(String filePath) {   // for input.txt
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

    public IntCode rewrite(int index, int value) {
        assert program != null;
        program.put(index, value);
        return this;
    }

    private int getWithMode(int index, int mode) {
        return mode == 0
            ? program.get(index)   // position mode
            : index;               // immediate mode
    }

    private int iterate(int index) {    // performs one iteration
        int code = program.get(index);
        int op = code % 100;
        int modes = code / 100;
        int mode1 = modes % 10;
        int mode2 = (modes / 10) % 10;
        int mode3 = modes / 100;

        assert (mode1 == 0 || mode1 == 1) : String.format("mode1 is %d", mode1);
        assert (mode2 == 0 || mode2 == 1) : String.format("mode2 is %d", mode2);
        assert (mode3 == 0 || mode3 == 1) : String.format("mode3 is %d", mode3);

        switch (op) {

        case 1:
            program.put(getWithMode(index + 3, mode3),
                    program.get(getWithMode(index + 1, mode1))
                            + program.get(getWithMode(index + 2, mode2)));
            return index + 4;

        case 2:
            program.put(getWithMode(index + 3, mode3),
                    program.get(getWithMode(index + 1, mode1))
                            * program.get(getWithMode(index + 2, mode2)));
            return index + 4;

        case 3:
            System.out.print("Input requested: ");
            program.put(getWithMode(index + 1, mode1), sc.nextInt());
            return index + 2;

        case 4:
            System.out.printf("Output printed: %d\n", program.get(getWithMode(index + 1, mode1)));
            return index + 2;

        case 5:
            return program.get(getWithMode(index + 1, mode1)) != 0
                    ? program.get(getWithMode(index + 2, mode2))
                    : index + 3;

        case 6:
            return program.get(getWithMode(index + 1, mode1)) == 0
                    ? program.get(getWithMode(index + 2, mode2))
                    : index + 3;

        case 7:
            program.put(getWithMode(index + 3, mode3),
                    program.get(getWithMode(index + 1, mode1)) < program.get(getWithMode(index + 2, mode2))
                            ? 1
                            : 0);
            return index + 4;

        case 8:
            program.put(getWithMode(index + 3, mode3),
                    program.get(getWithMode(index + 1, mode1)).equals(program.get(getWithMode(index + 2, mode2)))
                            ? 1
                            : 0);
            return index + 4;

        case 99:
            System.out.println("Program completed successfully.");
            return -1;

        default:
            System.out.printf("Error encountered at index %d, retrieving value %d.\n", index, code);
            return -2;

        }
    }

    public void process() {    // performs iterations until it halts
        int index = 0;
        while (index >= 0) {
            index = iterate(index);
        }
    }

    public int get(int index) {
        return program.get(index);
    }

    public void display(int index) {
        System.out.println(get(index));
    }

    public void print() {
        System.out.println(Arrays.toString(program.values().toArray()));
    }

}
