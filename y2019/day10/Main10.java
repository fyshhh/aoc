package y2019.day10;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import resource.Coord;
import resource.Pair;
import resource.PathReader;

public class Main10 {

    static class Field {

        Map<Coord, Set<Coord>> field = new HashMap<>();

        Field initialiseFromPath(String filePath) {
            try {
                return initialiseFromList(PathReader.generate(filePath).lines().collect(Collectors.toList()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        Field initialiseFromList(List<String> inputs) {
            for (int i = 0; i < inputs.size(); i++) {
                StringBuilder builder = new StringBuilder(inputs.get(i));
                int size = builder.length();
                for (int j = 0; j < size; j++) {
                    if (builder.charAt(0) == '#') {
                        Coord c = new Coord(j, i);
                        field.put(c, new HashSet<>());
                    }
                    builder.deleteCharAt(0);
                }
            }
            return this;
        }

        boolean canSee(Coord spot1, Coord spot2) {
            if (spot1.fst == spot2.fst) {
                int min = Integer.min(spot1.snd, spot2.snd);
                int max = Integer.max(spot1.snd, spot2.snd);
                for (int i = min + 1; i < max; i ++) {
                    if (field.containsKey(new Coord(spot1.fst, i))) {
                        return false;
                    }
                }
            } else if (spot1.snd == spot2.snd) {
                int min = Integer.min(spot1.fst, spot2.fst);
                int max = Integer.max(spot1.fst, spot2.fst);
                for (int i = min + 1; i < max; i ++) {
                    if (field.containsKey(new Coord(i, spot1.snd))) {
                        return false;
                    }
                }
            } else {
                double diffY = spot2.fst - spot1.fst;
                double diffX = spot2.snd - spot1.snd;
                if (spot1.fst < spot2.fst) {
                    for (int i = 1; i < diffY; i++) {
                        double factor = diffX / diffY * i;
                        if (factor % 1 == 0 &&
                                field.containsKey(new Coord(spot1.fst + i, spot1.snd + (int) factor))) {
                            return false;
                        }
                    }
                } else {
                    for (int i = -1; i > diffY; i--) {
                        double factor = diffX / diffY * i;
                        if (factor % 1 == 0 &&
                                field.containsKey(new Coord(spot1.fst + i, spot1.snd + (int) factor))) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        Field iterate() {
            for (Coord c1 : field.keySet()) {
                for (Coord c2 : field.keySet()) {
                    if (canSee(c1, c2) && !field.get(c1).contains(c2) && !c1.equals(c2)) {
                        field.merge(c1, Set.of(c2), (s1, s2) -> { s1.addAll(s2); return s1; });
                        field.merge(c2, Set.of(c1), (s1, s2) -> { s1.addAll(s2); return s1; });
                    }
                }
            }
            return this;
        }

        void highest() {
            int highest =  field.values().stream()
                    .mapToInt(Set::size)
                    .max()
                    .orElse(-1);
            System.out.println(highest);
        }

        Coord stationLocation() {
            return field.entrySet().stream()
                    .max(Comparator.comparingInt(e -> e.getValue().size()))
                    .map(Map.Entry::getKey)
                    .orElseThrow();
        }

        Pair<Double, Double> displacement(Coord centre, Coord other) {
            double angle = (Math.atan2(other.snd - centre.snd, other.fst - centre.fst) + 5 * Math.PI / 2)
                    % (2 * Math.PI);
            double distance = Math.hypot(other.snd - centre.snd, other.fst - centre.fst);
            return new Pair<>(angle, distance);
        }

        List<Coord> vaporise() {
            Coord centre = stationLocation();
            var order = new ArrayList<Coord>();
            var asteroids = new HashMap<Double, Set<Pair<Double, Coord>>>();
            var queue = new LinkedList<Pair<Double, PriorityQueue<Pair<Double, Coord>>>>();
            field.keySet().stream()
                    .filter(c -> !c.equals(centre))
                    .map(c -> new Pair<>(c, displacement(centre, c)))
                    .peek(p -> asteroids.putIfAbsent(p.snd.fst, new HashSet<>()))
                    .forEach(p -> asteroids.merge(p.snd.fst,
                            Set.of(new Pair<>(p.snd.snd, p.fst)),
                            (s1, s2) -> { s1.addAll(s2); return s1; }));
            asteroids.entrySet().stream()
                    .map(e -> {
                        var innerQueue = new PriorityQueue<Pair<Double, Coord>>(Comparator.comparingDouble(p -> p.fst));
                        innerQueue.addAll(e.getValue());
                        return new Pair<>(e.getKey(), innerQueue);
                    })
                    .forEach(queue::add);
            queue.sort(Comparator.comparingDouble(p -> p.fst));
            while (!queue.isEmpty()) {
                var pair = queue.poll();
                var innerQueue = pair.snd;
                var asteroid = innerQueue.poll().snd;
                order.add(asteroid);
                if (!innerQueue.isEmpty()) {
                    queue.addLast(pair);
                }
            }
            return order;
        }

        void print() {
            field.forEach((key, value) -> System.out.printf("%s sees %d: %s\n", key, value.size(), value));
        }

    }

    public static void main(String[] args) {
        String filePath = "y2019/day10/input.txt";
        Field field = new Field().initialiseFromPath(filePath);
        field.iterate().stationLocation();
        System.out.println(field.vaporise().get(199));
    }

}
