package y2019.day14;

import resource.Pair;
import resource.PathReader;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


public class Main14 {

    static class Chem {

        int tier;
        int value;
        String name;
        Set<Pair<Integer, Chem>> input;

        Chem(String name) {
            this.tier = Integer.MIN_VALUE;
            this.value = -1;
            this.name = name;
            this.input = new HashSet<>();
        }

        Chem(int value, String name) {
            this.tier = Integer.MIN_VALUE;
            this.value = value;
            this.name = name;
            this.input = new HashSet<>();
        }

        void setTier(int tier) {
            this.tier = tier;
        }

        void setValue(int value) {
            this.value = value;
        }

        void addChild(int value, Chem chem) {
            this.input.add(new Pair<>(value, chem));
        }

        @Override
        public String toString() {
            return String.format("%d, %s", this.tier,
                    this.input.stream()
                            .map(p -> String.format("%d %s", p.fst, p.snd.name))
                            .reduce((x, y) -> x + ", " + y)
                            .orElse("NIL"));
        }

    }

    static class Factory {

        Map<String, Chem> codex = new HashMap<>();
        Map<String, Long> amount = new HashMap<>();

        static Pair<Integer, String> parse(String str) {
            String[] inputs = str.split(" ");
            return new Pair<>(Integer.parseInt(inputs[0]), inputs[1]);
        }

        void parseLine(String str) {
            Pattern pattern = Pattern.compile("(?<in>.+) => (?<out>.+)");
            Matcher matcher = pattern.matcher(str);
            matcher.matches();
            String[] inputString = matcher.group("in").split(", ");
            Pair<Integer, String> outputPair = parse(matcher.group("out"));
            Chem output;
            if (codex.containsKey(outputPair.snd)) {
                output = codex.get(outputPair.snd);
                output.setValue(outputPair.fst);
            } else {
                output = new Chem(outputPair.fst, outputPair.snd);
                codex.put(outputPair.snd, output);
            }
            for (String string : inputString) {
                Chem input;
                Pair<Integer, String> inputPair = parse(string);
                if (codex.containsKey(inputPair.snd)) {
                    input = codex.get(inputPair.snd);
                } else {
                    input = new Chem(inputPair.snd);
                    codex.put(inputPair.snd, input);
                }
                output.addChild(inputPair.fst, input);
            }
        }

        void updateTiers() {
            var queue = new PriorityQueue<Pair<Integer, Chem>>(Comparator.comparingInt(p -> p.snd.tier));
            queue.add(new Pair<>(0, codex.get("FUEL")));
            while (!queue.isEmpty()) {
                var pair = queue.poll();
                int tier = pair.fst;
                pair.snd.setTier(Integer.max(tier, pair.snd.tier));
                pair.snd.input.stream()
                        .map(p -> p.snd)
                        .forEach(c -> queue.add(new Pair<>(tier + 1, c)));
            }
        }

        static Factory parseList(String filePath) {
            Factory factory = new Factory();
            try {
                PathReader.generate(filePath).lines()
                        .forEach(factory::parseLine);
                factory.updateTiers();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return factory;
        }

        Pair<Long, Long> calculateOre(long fuel) {
//            var amount = new HashMap<String, Long>();
            amount = new HashMap<>();
            var queue = new PriorityQueue<String>(Comparator.comparingInt(s -> codex.get(s).tier));
            amount.put("FUEL", fuel);
            queue.add("FUEL");
            while (!queue.isEmpty()) {
                String name = queue.poll();
                if (!name.equals("ORE")) {
                    Chem chem = codex.get(name);
                    long mul = -Math.floorDiv(-amount.get(name), chem.value);
                    for (var p : chem.input) {
                        String input = p.snd.name;
                        amount.merge(input, mul * p.fst, Long::sum);
                        if (!queue.contains(input)) {
                            queue.add(input);
                        }
                    }
                }
            }
//            System.out.println(amount.get("ORE").intValue());
            return new Pair<>(amount.get("ORE"), amount.get("FUEL"));
        }

        void maximizeEfficiency() {
            var p = calculateOre(1);
            long ore = 1000000000000L;
            long minFuel = Math.floorDiv(ore, p.fst) * p.snd;
            long maxFuel = minFuel;
            // determine upper bound
            while (calculateOre(maxFuel).fst < ore) {
                maxFuel = (long) (maxFuel * 1.1);
            }
            long fuel = (minFuel + maxFuel) / 2;
            // binary search algorithm
            while (true) {
                var pair = calculateOre(fuel);
                System.out.println(pair);
                if (pair.fst > ore) {
                    maxFuel = fuel;
                    fuel = (fuel + minFuel) / 2;
                } else {
                    var next = calculateOre(pair.snd + 1);
                    if (next.fst > ore) {
                        System.out.println(next.snd - 1);
                        break;
                    } else {
                        minFuel = fuel;
                        fuel = (fuel + maxFuel + 1) / 2;
                    }
                }
            }
        }

        @Override
        public String toString() {
            return this.codex.entrySet().stream()
                    .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
                    .reduce((x, y) -> x + "\n" + y)
                    .get();
        }

    }

    public static void main(String[] args) {
        Factory factory = Factory.parseList("y2019/day14/input.txt");
//        factory.calculateOre();
        factory.maximizeEfficiency();
    }

}
