package y2020.day4;

import resource.PathReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class Main4 {

    static class Passport {

    }

    static void process(String filePath) {
        try {
            BufferedReader br = PathReader.generate(filePath);
            StringBuilder temp = new StringBuilder();
            String line = "";
            int count1 = 0;
            int count2 = 0;
            while (line != null) {
                line = br.readLine();
                if (line == null || line.isEmpty()) {
                    String str = temp.toString();
                    outer:
                    if (str.contains("ecl") && str.contains("pid") && str.contains("eyr") && str.contains("hcl")
                        && str.contains("byr") && str.contains("iyr") && str.contains("hgt")) {
                        count1++;
                        String[] args = str.trim().split(" ");
                        for (String arg : args) {
                            String fieldType = arg.trim().substring(0, 3);
                            String field = arg.trim().substring(4);
                            switch (fieldType) {

                            case "byr":
                                try {
                                    int byr = Integer.parseInt(field);
                                    if (byr < 1920 || byr > 2002) {
                                        break outer;
                                    }
                                } catch (NumberFormatException e) {
                                    break outer;
                                }
                                break;

                            case "iyr":
                                try {
                                    int iyr = Integer.parseInt(field);
                                    if (iyr < 2010 || iyr > 2020) {
                                        break outer;
                                    }
                                } catch (NumberFormatException e) {
                                    break outer;
                                }
                                break;

                            case "eyr":
                                try {
                                    int eyr = Integer.parseInt(field);
                                    if (eyr < 2020 || eyr > 2030) {
                                        break outer;
                                    }
                                } catch (NumberFormatException e) {
                                    break outer;
                                }
                                break;

                            case "hgt":
                                try {
                                    int hgt = Integer.parseInt(field.substring(0, field.length() - 2));
                                    String type = field.substring(field.length() - 2);
                                    if (!((type.equals("cm") && hgt >= 150 && hgt <= 193)
                                            || (type.equals("in") && hgt >= 59 && hgt <= 76))) {
                                        break outer;
                                    }
                                } catch (NumberFormatException e) {
                                    break outer;
                                }
                                break;

                            case "hcl":
                                String hcl = field.substring(1);
                                if (field.charAt(0) == '#' && hcl.length() == 6) {
                                    boolean b = hcl.chars()
                                            .mapToObj(c -> (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f'))
                                            .reduce(true, (b1, b2) -> b1 && b2);
                                    if (!b) {
                                        break outer;
                                    }
                                } else {
                                    break outer;
                                }
                                break;

                            case "ecl":
                                if (!(field.equals("amb") || field.equals("blu") || field.equals("brn")
                                        || field.equals("gry") || field.equals("grn") || field.equals("hzl")
                                        || field.equals("oth"))) {
                                    break outer;
                                }
                                break;

                            case "pid":
                                try {
                                    if (field.length() != 9) {
                                        break outer;
                                    }
                                    Integer.parseInt(field);
                                } catch (NumberFormatException e) {
                                    break outer;
                                }
                                break;

                            default:
                                break;
                            }
                        }
                        count2++;
                    }
                    temp = new StringBuilder();
                } else {
                    temp.append(" ").append(line);
                }
            }
            System.out.println(count1);
            System.out.println(count2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String filePath = "y2020/day4/input.in";
        process(filePath);
    }

}
