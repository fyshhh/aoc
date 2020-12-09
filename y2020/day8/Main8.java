package y2020.day8;

import resource.Pair;
import resource.PathReader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main8 {

    static class Console {

        int accu = 0;
        int curr = 0;
        Map<Integer, Pair<String, Integer>> instr = new HashMap<>();

        static Console parseFilepath(String filepath) {
            Console console = new Console();
            try {
                List<String> list = PathReader.generate(filepath).lines().collect(Collectors.toList());
                IntStream.range(0, list.size())
                        .forEach(i -> console.instr.put(i, new Pair<>(list.get(i), 0)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return console;
        }

        void parse(String input) {
            String[] instr = input.split(" ");
            switch (instr[0]) {
            case "jmp":
                curr += Integer.parseInt(instr[1]);
                break;

            case "acc":
                accu += Integer.parseInt(instr[1]);

            case "nop":
                curr += 1;
                break;
            }
        }

        void iterate() {
            while (true) {
                var p = instr.get(curr);
                if (p.snd == 1) {
                    System.out.println(accu);
                    break;
                } else {
                    instr.compute(curr, (k, v) -> new Pair<>(v.fst, v.snd + 1));
                    parse(p.fst);
                }
            }
        }

        void fixedIterate() {
            outer:
            for (int i : instr.keySet()) {
                curr = 0;
                accu = 0;
                var copy = new HashMap<Integer, Pair<String, Integer>>();
                instr.forEach((k, v) -> copy.put(k, new Pair<>(v.fst, 0)));
                var pair = copy.get(i);
                if (pair.fst.startsWith("acc")) {
                    continue;
                } else if (pair.fst.startsWith("nop")) {
                    copy.put(i, new Pair<>("jmp" + pair.fst.substring(3), 0));
                } else {
                    copy.put(i, new Pair<>("nop" + pair.fst.substring(3), 0));
                }
                while (true) {
                    if (curr == copy.size()) {
                        System.out.println(accu);
                        break outer;
                    }
                    var p = copy.get(curr);
                    if (p.snd == 1) {
                        break;
                    } else {
                        copy.compute(curr, (k, v) -> new Pair<>(v.fst, v.snd + 1));
                        parse(p.fst);
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        String filepath = "y2020/day8/input.in";
        Console c = Console.parseFilepath(filepath);
        c.iterate();
        c.fixedIterate();
    }

}
