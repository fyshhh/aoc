package y2019.intcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntCode {

    enum Status {
        INITIALIZED, NEED_INPUT, COMPLETED;
    }

    Scanner sc = new Scanner(System.in);
    boolean printIO = true;
    List<Integer> inputs = new ArrayList<>();
    List<Integer> outputs = new ArrayList<>();

    private int index = 0;
    private Map<Integer, Integer> program = new HashMap<>();
    private Status status = Status.INITIALIZED;

    public IntCode() {
    }

    public IntCode(boolean printIO) {
        this.printIO = printIO;
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

    public IntCode addInputs(List<Integer> inputs) {
        this.inputs.addAll(inputs);
        return this;
    }

    public int mode(int index, int mode) {
        return mode == 0 ? program.get(index) : index;
    }

    public IntCode iterate() {    // performs iterations until it should stop
        while (true) {
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
                program.put(mode(index + 3, mode3),
                        program.get(mode(index + 1, mode1))
                                + program.get(mode(index + 2, mode2)));
                index += 4;
                break;

            case 2:
                program.put(mode(index + 3, mode3),
                        program.get(mode(index + 1, mode1))
                                * program.get(mode(index + 2, mode2)));
                index += 4;
                break;

            case 3:
                if (printIO) {
                    System.out.print("Input requested: ");
                    program.put(mode(index + 1, mode1), sc.nextInt());
                } else {
                    if (inputs.size() > 0) {
                        int input = inputs.remove(0);
                        System.out.printf("Input requested: %d\n", input);
                        program.put(mode(index + 1, mode1), input);
                    } else {
                        status = Status.NEED_INPUT;
                        return this;
                    }
                }
                index += 2;
                break;

            case 4:
                if (printIO) {
                    System.out.printf("Output printed: %d\n", program.get(mode(index + 1, mode1)));
                } else {
                    int output = program.get(mode(index + 1, mode1));
                    System.out.printf("Output printed: %d\n", output);
                    outputs.add(output);
                }
                index += 2;
                break;

            case 5:
                index = program.get(mode(index + 1, mode1)) != 0
                        ? program.get(mode(index + 2, mode2))
                        : index + 3;
                break;

            case 6:
                index = program.get(mode(index + 1, mode1)) == 0
                        ? program.get(mode(index + 2, mode2))
                        : index + 3;
                break;

            case 7:
                program.put(mode(index + 3, mode3),
                        program.get(mode(index + 1, mode1))
                                < program.get(mode(index + 2, mode2))
                                ? 1
                                : 0);
                index += 4;
                break;

            case 8:
                program.put(mode(index + 3, mode3),
                        program.get(mode(index + 1, mode1))
                                .equals(program.get(mode(index + 2, mode2)))
                                ? 1
                                : 0);
                index += 4;
                break;

            case 99:
                System.out.println("Program completed successfully.");
                index = -1;
                status = Status.COMPLETED;
                return this;

            default:
                throw new UnsupportedOperationException(String.format("Unexpected opcode at index %d: %d", index, code));

            }
        }
    }

    public int getLast() {
        return outputs.get(outputs.size() - 1);
    }

    public boolean isCompleted() {
        return status == Status.COMPLETED;
    }

    public void print() {
        System.out.println(Arrays.toString(program.values().toArray()));
    }

}
