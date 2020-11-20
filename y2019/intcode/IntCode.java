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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntCode {

    enum Status {
        INITIALIZED, NEED_INPUT, COMPLETED;
    }

    Scanner sc = new Scanner(System.in);
    boolean userInput = true;
    boolean printToUser = false;
    List<Long> inputs = new ArrayList<>();
    List<Long> outputs = new ArrayList<>();

    private long index = 0;
    private long relative = 0;
    private Map<Long, Long> program = new HashMap<>();
    private Status status = Status.INITIALIZED;

    public IntCode() {
    }

    public IntCode(boolean userInput) {
        this.userInput = userInput;
    }

    public IntCode initialiseFromArr(long[] arr) {      // for test cases
        program = IntStream.range(0, arr.length)
                .boxed()
                .collect(Collectors.toMap(
                        i -> (long) i,
                        i -> arr[i],
                        (a, b) -> a,        // BiFunction implementation unnecessary as there are no collisions
                        HashMap::new));
        return this;
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

    public IntCode setPrintToUser(boolean b) {
        this.printToUser = b;
        return this;
    }

    public IntCode addInputs(List<Integer> inputs) {
        inputs.forEach(i -> this.inputs.add((long) i));
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
                if (userInput) {
                    System.out.print("Input requested: ");
                    program.put(mode(index + 1, mode1), sc.nextLong());
                } else {
                    if (inputs.size() > 0) {
                        long input = inputs.remove(0);
                        if (printToUser) {
                            System.out.printf("Input requested: %d\n", input);
                        }
                        program.put(mode(index + 1, mode1), input);
                    } else {
                        status = Status.NEED_INPUT;
                        return this;
                    }
                }
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
                return this;

            default:
                throw new UnsupportedOperationException(String.format("Unexpected opcode at index %d: %d", index, code));

            }
        }
    }

    public List<Long> getOutputs() {
        return outputs;
    }

    public long getLast() {
        return outputs.get(outputs.size() - 1);
    }

    public boolean isCompleted() {
        return status == Status.COMPLETED;
    }

    public void print() {
        System.out.println(Arrays.toString(program.values().toArray()));
    }

}
