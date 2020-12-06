package y2019.day15;

import resource.Coord;
import resource.Pair;
import y2019.intcode.IntCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main15 {

    static class Bot {

        Set<Coord> cells = new HashSet<>();

        IntCode intCode;

        Bot(String filePath) {
            intCode = new IntCode(false).setPrintToUser(false).initialiseFromPath(filePath);
        }

        static Coord nextCoord(Coord c, int i) {
            switch (i) {
            case 1:
                return new Coord(c.fst, c.snd + 1);
            case 2:
                return new Coord(c.fst, c.snd - 1);
            case 3:
                return new Coord(c.fst - 1, c.snd);
            default:
                return new Coord(c.fst + 1, c.snd);
            }
        }

        static List<Integer> nextSteps(int i) {
            var list = new ArrayList<>(List.of(1, 2, 3, 4));
            switch (i) {
            case 1:
                list.removeIf(x -> x == 2);
                list.add(2);
                return list;
            case 2:
                list.removeIf(x -> x == 1);
                list.add(1);
                return list;
            case 4:
                list.removeIf(x -> x == 3);
                list.add(3);
                return list;
            default:
                return list;
            }
        }

        Set<Coord> adjacentCoords(Set<Coord> set) {
            return set.stream()
                    .flatMap(c -> IntStream.rangeClosed(1, 4)
                            .mapToObj(i -> nextCoord(c, i)))
                    .filter(c -> cells.contains(c))
                    .collect(Collectors.toSet());
        }

        void execute() {
            Coord curr = new Coord(0, 0);
            Coord oxygen = null;
            var steps = new LinkedList<Pair<Coord, List<Integer>>>();
            steps.add(new Pair<>(curr, List.of(1, 2, 3, 4)));
            cells.add(curr);
            while (!steps.isEmpty()) {
                var last = steps.getLast();
                if (last.snd.isEmpty()) {
                    var temp = new LinkedList<>(steps);
                    temp.removeLast();
                    steps = temp;
                } else {
                    var copy = new ArrayList<>(last.snd);
                    int step = copy.remove(0);
                    intCode.addInputs(List.of(step));
                    intCode.iterate();
                    int nextCell = (int) intCode.getLast();
                    Coord nextCoord = nextCoord(last.fst, step);
                    var temp = new LinkedList<>(steps);
                    if (steps.stream().map(p -> p.fst).anyMatch(c -> c.equals(nextCoord)) || nextCell == 0) {
                        temp.removeLast();
                        temp.add(new Pair<>(last.fst, copy));
                    } else {
                        if (nextCell == 2) {
                            System.out.println("found it!");
                            System.out.println(steps.size());
                            oxygen = nextCoord;
                        }
                        temp.removeLast();
                        temp.add(new Pair<>(last.fst, copy));
                        temp.add(new Pair<>(nextCoord, nextSteps(step)));
                        cells.add(nextCoord);
                    }
                    steps = temp;
                }
            }
            int count = -1;
            Set<Coord> next = Set.of(oxygen);
            Set<Coord> copy = new HashSet<>(cells);
            while (!copy.isEmpty()) {
                copy.removeAll(next);
                next = adjacentCoords(next);
                count++;
            }
            System.out.println(count);
        }

    }

    public static void main(String[] args) {
        String filePath = "y2019/day15/input.txt";
        Bot bot = new Bot(filePath);
        bot.execute();
    }

}
