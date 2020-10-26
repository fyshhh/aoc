package y2019.day11;

import resource.Coord;
import resource.PathReader;
import y2019.intcode.IntCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.UnaryOperator;

public class Main11 {

    static class Painter {

        IntCode intCode;
        int currDirection = 0;
        Coord currCoord = new Coord(0, 0);
        Map<Coord, Integer> panel = new HashMap<>();

        static Map<Integer, UnaryOperator<Coord>> funcs = Map.of(
                0, c -> new Coord(c.fst + 1, c.snd),
                1, c -> new Coord(c.fst, c.snd + 1),
                2, c -> new Coord(c.fst - 1, c.snd),
                3, c -> new Coord(c.fst, c.snd - 1));

        Painter(String filePath) {
            intCode = new IntCode(false).initialiseFromPath(filePath);
        }

        Painter paint(int start) {
            Queue<Integer> queue = new PriorityQueue<>();
            queue.add(start);
            while (!queue.isEmpty() && !intCode.isCompleted()) {
                int input = queue.poll();
                intCode.addInputs(List.of(input));
                intCode.iterate();
                List<Long> outputs = intCode.getOutputs();
                panel.put(currCoord, Math.toIntExact(outputs.get(outputs.size() - 2)));
                currDirection = outputs.get(outputs.size() - 1) == 0
                        ? (currDirection + 3) % 4
                        : (currDirection + 1) % 4;
                currCoord = funcs.get(currDirection).apply(currCoord);
                queue.add(panel.getOrDefault(currCoord, 0));
            }
            return this;
        }

        String toImage() {
            int minX = panel.keySet().stream()
                    .mapToInt(c -> c.fst)
                    .min()
                    .getAsInt();
            int maxX = panel.keySet().stream()
                    .mapToInt(c -> c.fst)
                    .max()
                    .getAsInt();
            int minY = panel.keySet().stream()
                    .mapToInt(c -> c.snd)
                    .min()
                    .getAsInt();
            int maxY = panel.keySet().stream()
                    .mapToInt(c -> c.snd)
                    .max()
                    .getAsInt();
            StringBuilder image = new StringBuilder();
            for (int i = maxX; i >= minX; i--) {
                for (int j = minY; j <= maxY; j++) {
                    String pixel = panel.getOrDefault(new Coord(i, j), 0) == 0 ? " " : "X";
                    image.append(pixel);
                }
                image.append("\n");
            }
            return image.toString();
        }

    }

    public static void main(String[] args) {
        String filePath = "y2019/day11/input.txt";
        Painter painter = new Painter(filePath);
        painter.paint(1);
        System.out.println(painter.panel.toString());
        System.out.println(painter.toImage());
    }

}