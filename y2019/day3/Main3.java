package y2019.day3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main3 {

    static class Node {

        int x;
        int y;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object object) {  // instance checking not required since we only use Nodes as map keys
            return this.x == ((Node) object).x && this.y == ((Node) object).y;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new int[] {x, y});
        }

        @Override
        public String toString() {  // for testing
            return String.format("[%d, %d]", x, y);
        }

    }

    static class Grid {

        int currSteps;
        Node currNode;
        Map<Node, Integer> line1 = new HashMap<>();
        Map<Node, Integer> line2 = new HashMap<>();

        void parseMove(String input, Map<Node, Integer> grid) {
            char direction = input.charAt(0);
            int steps = Integer.parseInt(input.substring(1));
            switch (direction) {

            case 'U':
                for (int i = 0; i < steps; i++) {
                    grid.putIfAbsent(new Node(++currNode.x, currNode.y), ++currSteps);
                }
                break;

            case 'D':
                for (int i = 0; i < steps; i++) {
                    grid.putIfAbsent(new Node(--currNode.x, currNode.y), ++currSteps);
                }
                break;

            case 'L':
                for (int i = 0; i < steps; i++) {
                    grid.putIfAbsent(new Node(currNode.x, --currNode.y), ++currSteps);
                }
                break;

            case 'R':
                for (int i = 0; i < steps; i++) {
                    grid.putIfAbsent(new Node(currNode.x, ++currNode.y), ++currSteps);
                }
                break;

            default:
                System.out.println("something went wrong");
                break;

            }
        }

        void parseFirstPath(List<String> inputs) {
            currSteps = 0;
            currNode = new Node(0, 0);
            inputs.forEach(input -> parseMove(input, line1));
        }

        void parseSecondPath(List<String> inputs) {
            currSteps = 0;
            currNode = new Node(0, 0);
            inputs.forEach(input -> parseMove(input, line2));
        }

        void execute(String filePath) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                List<String> firstPath = Arrays.asList(reader.readLine().split(","));
                List<String> secondPath = Arrays.asList(reader.readLine().split(","));
                parseFirstPath(firstPath);
                parseSecondPath(secondPath);
            } catch (IOException ignored) { }
        }

        Map<Node, Integer> intersection() {
            Map<Node, Integer> intersection = new HashMap<>();
            line1.keySet().stream()
                    .filter(k -> line2.containsKey(k))
                    .forEach(k -> intersection.put(k, line1.get(k) + line2.get(k)));
            return intersection;
        }

        int closest() {
            return intersection().keySet().stream()
                    .map(n -> Math.abs(n.x) + Math.abs(n.y))
                    .min(Comparator.comparing(Integer::valueOf))
                    .orElse(-1);
        }

        int shortest() {
            return intersection().entrySet().stream()
                    .map(Map.Entry::getValue)
                    .min(Comparator.comparing(Integer::valueOf))
                    .orElse(-1);
        }

    }

    public static void main(String[] args) {
        String filePath = "y2019/day3/input.txt";
        Grid grid = new Grid();
        grid.execute(filePath);
        System.out.println(grid.closest());     // part one
        System.out.println(grid.shortest());    // part two
    }

}
