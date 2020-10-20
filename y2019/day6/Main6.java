package y2019.day6;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main6 {

    static class Orbit {

        Map<String, String> map = new HashMap<>();

        void parse(String input) {
            String[] planets = input.split("\\)");
            map.put(planets[1], planets[0]);    // p0)p1
        }

        Orbit initialise(String filePath) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                reader.lines().forEach(this::parse);
            } catch (FileNotFoundException ignored) { }
            return this;
        }

        int orbits(String planet) {
            int orbits = 0;
            String currPlanet = planet;
            while (currPlanet != null) {
                currPlanet = map.get(currPlanet);
                orbits++;
            }
            return orbits;
        }

        int totalOrbits() {
            return map.values()
                    .stream()
                    .mapToInt(this::orbits)
                    .sum();
        }

        List<String> pathToCOM(String planet) {
            String currPlanet = planet;
            List<String> path = new ArrayList<>();
            while (currPlanet != null) {
                currPlanet = map.get(currPlanet);
                path.add(currPlanet);
            }
            return path;
        }

        int pathTo(String planet1, String planet2) {
            List<String> path1 = pathToCOM(planet1);
            List<String> path2 = pathToCOM(planet2);
            for (String planet : path1) {
                if (path2.contains(planet)) {
                    return path1.indexOf(planet) + path2.indexOf(planet);
                }
            }
            return -1;
        }

    }

    public static void main(String[] args) {
        String filePath = "y2019/day6/input.txt";
        Orbit orbit = new Orbit().initialise(filePath);
//        System.out.println(orbit.totalOrbits());
        System.out.println(orbit.pathTo("YOU", "SAN"));
    }

}
