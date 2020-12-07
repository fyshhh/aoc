package y2020.day7;

import resource.Pair;
import resource.PathReader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main7 {

    static class Bag {

        String name;
        Set<Bag> parents = new HashSet<>();
        Set<Pair<Bag, Integer>> children = new HashSet<>();

        Bag(String name) {
            this.name = name;
        }

        void addParent(Bag b) {
            parents.add(b);
        }

        void addChild(Pair<Bag, Integer> p) {
            children.add(p);
        }

        @Override
        public String toString() {
            return String.format("%s", children.stream()
                    .map(p -> p.snd + " " + p.fst.name)
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("nothing"));
        }

    }

    static class Processor {

        Map<String, Bag> codex = new HashMap<>();

        void parseLine(String line) {
            Pattern pattern = Pattern.compile("(?<parent>.+) contain (?<children>.+).");
            Matcher matcher = pattern.matcher(line);
            matcher.matches();
            String parentStr = matcher.group("parent");
            parentStr = parentStr.substring(0, parentStr.length() - 4).trim();
            Bag parentBag = codex.computeIfAbsent(parentStr, Bag::new);
            String[] childrenStr = matcher.group("children").split(", ");
            for (String s : childrenStr) {
                String childStr = s.substring(0, s.length() - 4).trim();
                Pattern childPattern = Pattern.compile("(?<amt>[0-9]+) (?<child>.+)");
                Matcher childMatcher = childPattern.matcher(childStr);
                if (childMatcher.matches()) {
                    int amt = Integer.parseInt(childMatcher.group("amt"));
                    Bag childBag = codex.computeIfAbsent(childMatcher.group("child"), Bag::new);
                    parentBag.addChild(new Pair<>(childBag, amt));
                    childBag.addParent(parentBag);
                }
            }
        }

        static Processor parseFilepath(String filePath) {
            Processor p = new Processor();
            try {
                PathReader.generate(filePath).lines()
                        .forEach(p::parseLine);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return p;
        }

        void countParents(String input) {
            Set<String> parents = new HashSet<>();
            var queue = new LinkedList<String>();
            queue.add(input);
            while (!queue.isEmpty()) {
                String str = queue.pop();
                Bag bag = codex.get(str);
                bag.parents.stream()
                        .map(b -> b.name)
                        .peek(parents::add)
                        .forEach(queue::add);
            }
            System.out.println(parents.size());
        }

        void countChildren(String input) {
            int count = 0;
            var queue = new LinkedList<Pair<String, Integer>>();
            queue.add(new Pair<>(input, 1));
            while (!queue.isEmpty()) {
                var pair = queue.pop();
                Bag bag = codex.get(pair.fst);
                int amount = bag.children.stream()
                        .map(p -> new Pair<>(p.fst, p.snd * pair.snd))
                        .peek(p -> queue.add(new Pair<>(p.fst.name, p.snd)))
                        .mapToInt(p -> p.snd)
                        .sum();
                count += amount;
            }
            System.out.println(count);
        }


        @Override
        public String toString() {
            return codex.entrySet().stream()
                    .map(e -> e.getKey() + " holds " + e.getValue())
                    .reduce((s1, s2) -> s1 + "\n" + s2)
                    .orElse("empty");
        }

    }

    public static void main(String[] args) {
        String filepath = "y2020/day7/input.in";
        Processor p = Processor.parseFilepath(filepath);
        p.countParents("shiny gold");
        p.countChildren("shiny gold");
    }

}
