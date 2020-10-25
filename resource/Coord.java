package resource;

// this class is used exclusively for storing some form of coordinates

import java.util.Arrays;

public class Coord {

    public final int fst;
    public final int snd;

    public Coord(int fst, int snd) {
        this.fst = fst;
        this.snd = snd;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] {fst, snd});
    }

    @Override
    public boolean equals(Object object) {      // used for hashing only, which explains the... weird implementation
        return ((Coord) object).fst == this.fst &&
                ((Coord) object).snd == this.snd;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", fst, snd);
    }

}
