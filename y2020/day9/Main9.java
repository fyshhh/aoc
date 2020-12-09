package y2020.day9;

import resource.PathReader;

import java.io.FileNotFoundException;
import java.util.stream.IntStream;

public class Main9 {

    static class Decoder {

        long[] code = new long[]{1000};

        static Decoder parseFilepath(String filepath) {
            Decoder d = new Decoder();
            try {
                d.code = PathReader.generate(filepath).lines()
                        .mapToLong(Long::parseLong)
                        .toArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return d;
        }

        long findInvalid() {
            for (int i = 25; i < code.length; i++) {
                boolean isInvalid = true;
                for (int j = i - 25; j < i; j++) {
                    for (int k = j; k < i; k++) {
                        if (code[i] == code[j] + code[k]) {
                            isInvalid = false;
                        }
                    }
                }
                if (isInvalid) {
                    System.out.println(code[i]);
                    return code[i];
                }
            }
            return -1;
        }

        void findSet() {
            int min = 0;
            int max = 0;
            long sum = 0;
            long amt = findInvalid();
            while (true) {
                if (sum == amt) {
                    long small = IntStream.rangeClosed(min, max)
                            .mapToLong(i -> code[i])
                            .min()
                            .getAsLong();
                    long big = IntStream.rangeClosed(min, max)
                            .mapToLong(i -> code[i])
                            .max()
                            .getAsLong();
                    System.out.println(small + big);
                    return;
                } else if (sum < amt) {
                    sum += code[max];
                    max += 1;
                } else {
                    sum -= code[min];
                    min += 1;
                }
            }
        }

    }

    public static void main(String[] args) {
        String filepath = "y2020/day9/input.in";
        Decoder decoder = Decoder.parseFilepath(filepath);
        decoder.findSet();
    }

}
