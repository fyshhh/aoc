package y2020.day3;

import resource.Coord;
import resource.PathReader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main3 {

    static class Biome {

        int height;
        int width;
        Map<Coord, Integer> map = new HashMap<>();

        static Biome generate(String filePath) {
            Biome b = new Biome();
            try {
                List<String> lines = PathReader.generate(filePath).lines()
                        .collect(Collectors.toList());
                b.height = lines.size();
                b.width = lines.get(0).length();
                for (int i = 0; i < b.height; i++) {
                    String line = lines.get(i);
                    for (int j = 0; j < b.width; j++) {
                        b.map.put(new Coord(i, j), line.charAt(j) == '.' ? 0 : 1);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return b;
        }

        int get(Coord c) {
            return map.get(new Coord(c.fst, c.snd % width));
        }

        int countTrees(int x, int y) {
            return Stream.iterate(new Coord(0, 0),
                    c -> c.fst < height,
                    c -> new Coord(c.fst + x, c.snd + y))
                    .mapToInt(this::get)
                    .sum();
        }

        long multiplied() {
            return (long) countTrees(1, 1) * countTrees(1, 3) * countTrees(1, 5)
                    * countTrees(1, 7) * countTrees(2, 1);
        }

    }

    public static void main(String[] args) {
        String filePath = "y2020/day3/input.in";
        Biome b = Biome.generate(filePath);
        System.out.println(b.countTrees(1, 3));
        System.out.println(b.multiplied());
    }

}
