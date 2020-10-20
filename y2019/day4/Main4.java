package y2019.day4;

import java.util.stream.IntStream;

public class Main4 {

    static boolean hasAdjacent(String number) {
        char[] chars = number.toCharArray();
        return chars[0] == chars[1]
                || chars[1] == chars[2]
                || chars[2] == chars[3]
                || chars[3] == chars[4]
                || chars[4] == chars[5];
    }

    static boolean isIncreasing(String number) {
        char[] chars = number.toCharArray();
        return chars[0] <= chars[1]
                && chars[1] <= chars[2]
                && chars[2] <= chars[3]
                && chars[3] <= chars[4]
                && chars[4] <= chars[5];
    }

    static boolean hasGroupOfTwo(String number) {
        char[] chars = number.toCharArray();
        return (chars[0] == chars[1] && chars[1] != chars[2])
                || (chars[0] != chars[1] && chars[1] == chars[2] && chars[2] != chars[3])
                || (chars[1] != chars[2] && chars[2] == chars[3] && chars[3] != chars[4])
                || (chars[2] != chars[3] && chars[3] == chars[4] && chars[4] != chars[5])
                || (chars[3] != chars[4] && chars[4] == chars[5]);
    }

    public static void main(String[] args) {
        int input1 = 134564;
        int input2 = 585159;
        long output = IntStream.rangeClosed(input1, input2)
                .mapToObj(Integer::toString)
                .filter(Main4::hasAdjacent)
                .filter(Main4::isIncreasing)
                .filter(Main4::hasGroupOfTwo)   // comment out for part one
                .count();
        System.out.println(output);
    }

}
