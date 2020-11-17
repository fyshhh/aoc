package y2019.day12;

import resource.PathReader;
import resource.Pair;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main12 {

    static class Triplet {

        int x;
        int y;
        int z;

        Triplet(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object object) {
            Triplet obj = (Triplet) object;
            return this.x == obj.x && this.y == obj.y && this.z == obj.z;
        }

        @Override
        public String toString() {
            return String.format("<x=%3d, y=%3d, z=%3d>", x, y, z);
        }

    }

    static class Moons {

        Map<Integer, Pair<Triplet, Triplet>> init;
        Map<Integer, Pair<Triplet, Triplet>> curr;

        public Moons() {
            init = new HashMap<>();
            curr = new HashMap<>();
        }

        Moons initialise(String filePath) {
            try {
                List<String> list = PathReader.generate(filePath).lines().collect(Collectors.toList());
                Pattern format = Pattern.compile("<x=(?<x>\\S+), y=(?<y>\\S+), z=(?<z>\\S+)>");
                for (int i = 0; i < 4; i++) {
                    Matcher matcher = format.matcher(list.get(i));
                    matcher.matches();
                    init.put(i, new Pair<>(
                            new Triplet(Integer.parseInt(matcher.group("x")),
                                    Integer.parseInt(matcher.group("y")),
                                    Integer.parseInt(matcher.group("z"))),
                            new Triplet(0, 0, 0)));
                    curr.put(i, new Pair<>(
                            new Triplet(Integer.parseInt(matcher.group("x")),
                                    Integer.parseInt(matcher.group("y")),
                                    Integer.parseInt(matcher.group("z"))),
                            new Triplet(0, 0, 0)));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return this;
        }

        void iterate() {
            for (int i = 0; i < 4; i++) {
                for (int j = i + 1; j < 4; j++) {
                    var pos1 = curr.get(i);
                    var pos2 = curr.get(j);
                    curr.put(i, new Pair<>(pos1.fst, new Triplet(
                            pos1.snd.x + Integer.compare(pos2.fst.x, pos1.fst.x),
                            pos1.snd.y + Integer.compare(pos2.fst.y, pos1.fst.y),
                            pos1.snd.z + Integer.compare(pos2.fst.z, pos1.fst.z))));
                    curr.put(j, new Pair<>(pos2.fst, new Triplet(
                            pos2.snd.x + Integer.compare(pos1.fst.x, pos2.fst.x),
                            pos2.snd.y + Integer.compare(pos1.fst.y, pos2.fst.y),
                            pos2.snd.z + Integer.compare(pos1.fst.z, pos2.fst.z))));
                }
            }
            for (int i = 0; i < 4; i++) {
                var pos = curr.get(i);
                curr.put(i, new Pair<>(
                        new Triplet(pos.fst.x + pos.snd.x, pos.fst.y + pos.snd.y, pos.fst.z + pos.snd.z),
                        pos.snd));
            }
        }

        Moons simulate(int x) {
            for (int i = 0; i < x; i++) {
                iterate();
            }
            return this;
        }

        int energy() {
            return curr.values().stream()
                    .mapToInt(p -> (Math.abs(p.fst.x) + Math.abs(p.fst.y) + Math.abs(p.fst.z))
                            * (Math.abs(p.snd.x) + Math.abs(p.snd.y) + Math.abs(p.snd.z)))
                    .reduce(Integer::sum)
                    .getAsInt();
        }

        long simulateExh() {
            int index = 0;
            Optional<Integer> x = Optional.empty();
            Optional<Integer> y = Optional.empty();
            Optional<Integer> z = Optional.empty();
            while (true) {
                iterate();
                index++;
                if (x.isEmpty() && init.get(0).fst.x == curr.get(0).fst.x &&
                        init.get(1).fst.x == curr.get(1).fst.x &&
                        init.get(2).fst.x == curr.get(2).fst.x &&
                        init.get(3).fst.x == curr.get(3).fst.x) {
                    x = Optional.of(index + 1);                     // gotta add 1... not sure why
                }
                if (y.isEmpty() && init.get(0).fst.y == curr.get(0).fst.y &&
                        init.get(1).fst.y == curr.get(1).fst.y &&
                        init.get(2).fst.y == curr.get(2).fst.y &&
                        init.get(3).fst.y == curr.get(3).fst.y) {
                    y = Optional.of(index + 1);
                }
                if (z.isEmpty() && init.get(0).fst.z == curr.get(0).fst.z &&
                        init.get(1).fst.z == curr.get(1).fst.z &&
                        init.get(2).fst.z == curr.get(2).fst.z &&
                        init.get(3).fst.z == curr.get(3).fst.z) {
                    z = Optional.of(index + 1);
                }
                if (x.isPresent() && y.isPresent() && z.isPresent()) {  // lcm... somehow
                    int xValue = x.get();
                    int yValue = y.get();
                    int zValue = z.get();
                    return LongStream.iterate(xValue, i -> i + xValue)
                            .filter(i -> i % yValue == 0)
                            .filter(i -> i % zValue == 0)
                            .findFirst()
                            .getAsLong();
                }
            }
        }

        @Override
        public String toString() {
            return curr.values()
                    .stream()
                    .map(p -> String.format("pos=%s, vel=%s", p.fst, p.snd))
                    .reduce((x, y) -> String.format("%s\n%s", x, y))
                    .orElseThrow();
        }

    }

    public static void main(String[] args) {
        Moons moons = new Moons().initialise("y2019/day12/input.txt");
        System.out.println(moons.simulateExh());        // still takes about a minute
    }

}
