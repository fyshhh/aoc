package resource;

// this class is used to read inputs from files.

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class PathReader {

    public static BufferedReader generate(String filePath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filePath));
    }

}
