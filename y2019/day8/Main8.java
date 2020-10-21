package y2019.day8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main8 {

    static class Layer {

        int height;
        int width;
        Map<Integer, Map<Integer, Integer>> pixels;

        Layer(int height, int width) {
            this.height = height;
            this.width = width;
            this.pixels = new HashMap<>();
        }

        Layer parse(String input) {
            StringBuilder builder = new StringBuilder(input);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    pixels.computeIfAbsent(i, k -> new HashMap<>())
                            .put(j, Character.getNumericValue(builder.charAt(0)));
                    builder.deleteCharAt(0);
                }
            }
            return this;
        }

        int numberOf(int value) {
            return (int) pixels.values().stream()
                    .flatMap(map -> map.values().stream())
                    .filter(i -> i == value)
                    .count();
        }

        @Override
        public String toString() {
            return pixels.toString();
        }

    }

    static class Image {

        int height;
        int width;
        List<Layer> layers;

        Image parse(int height, int width, String input) {
            this.height = height;
            this.width = width;
            int pixels = height * width;
            layers = IntStream.range(0, input.length() / pixels)
                    .mapToObj(i -> input.substring(i * pixels, (i + 1) * pixels))
                    .map(str -> new Layer(height, width).parse(str))
                    .collect(Collectors.toList());
            return this;
        }

        Layer fewestZeroes() {
            Layer toReturn = null;
            int zeroes = Integer.MAX_VALUE;
            for (Layer layer : layers) {
                if (zeroes > layer.numberOf(0)) {
                    toReturn = layer;
                    zeroes = layer.numberOf(0);
                }
            }
            return toReturn;
        }

        String formImage() {
            Map<Integer, Map<Integer, Integer>> output = new HashMap<>();
            for (Layer layer : layers) {
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j ++) {
                        int finalJ = j;
                        if (Optional.ofNullable(output.get(i)).map(map -> map.get(finalJ)).isEmpty()) {
                            int pixel = layer.pixels.get(i).get(j);
                            if (pixel != 2) {
                                output.computeIfAbsent(i, k -> new HashMap<>()).put(j, pixel);
                            }
                        }
                    }
                }
            }
            return output.values().stream()
                    .map(map -> map.values().stream()
                            .map(i -> Integer.toString(i))
                            .reduce("", String::concat) + "\n")
                    .reduce("", String::concat);
        }

    }

    public static void main(String[] args) {
        try {
            String filePath = "y2019/day8/input.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String input = reader.readLine();
            Image image = new Image().parse(25, 6, input);
            System.out.println(image.fewestZeroes().numberOf(1) * image.fewestZeroes().numberOf(2));
            System.out.println(image.formImage());

        } catch (IOException ignored) { }
    }


}
