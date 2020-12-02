package y2020.day2;

import resource.PathReader;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main2 {

    static boolean countValid(String input) {
        Pattern pattern = Pattern.compile("(?<min>.+)-(?<max>.+) (?<char>.+): (?<password>.+)");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        char toMatch = matcher.group("char").charAt(0);
        long count = matcher.group("password").chars()
                .filter(c -> c == toMatch)
                .count();
        return count >= Integer.parseInt(matcher.group("min"))
                && count <= Integer.parseInt(matcher.group("max"));
    }

    static boolean positionValid(String input) {
        Pattern pattern = Pattern.compile("(?<min>.+)-(?<max>.+) (?<char>.+): (?<password>.+)");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        char toMatch = matcher.group("char").charAt(0);
        return matcher.group("password").charAt(Integer.parseInt(matcher.group("min")) - 1) == toMatch
                ^ matcher.group("password").charAt(Integer.parseInt(matcher.group("max")) - 1) == toMatch;
    }

    static void execute(String filePath) {
        try {
            long count1 = PathReader.generate(filePath).lines()
                    .map(Main2::countValid)
                    .filter(b -> b)
                    .count();
            long count2 = PathReader.generate(filePath).lines()
                    .map(Main2::positionValid)
                    .filter(b -> b)
                    .count();
            System.out.println(count1);
            System.out.println(count2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "y2020/day2/input.txt";
        execute(filePath);
    }

}
