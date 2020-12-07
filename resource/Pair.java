package resource;

// generic pair class

import java.util.Objects;

public class Pair<T, U> {

    public T fst;
    public U snd;

    public Pair(T fst, U snd) {
        this.fst = fst;
        this.snd = snd;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pair<?, ?> && fst.equals(((Pair<?, ?>) obj).fst) && snd.equals(((Pair<?, ?>) obj).snd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fst, snd);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", fst, snd);
    }

}
