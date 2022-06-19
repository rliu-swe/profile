package Utils;

import java.util.Objects;


/**
 * Standard Java Pair utility object.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class Pair<L, R> {
    public final L left;
    public final R right;

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return "Pair{" + left + ", " + right + '}';
    }

    public static <L, R> Pair<L, R> of(L l, R r) {
        return new Pair<>(l, r);
    }

    /**
     * A Symmetric pair is one where <L, R> == <R, L>
     */
    public static class Symmetric<L, R> extends Pair<L, R> {
        private Symmetric(L left, R right) {
            super(left, right);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Symmetric<?, ?> pair = (Symmetric<?, ?>) o;
            return Objects.equals(left, pair.left) && Objects.equals(right, pair.right)
                || Objects.equals(left, pair.right) && Objects.equals(right, pair.left);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right) + Objects.hash(right, left);
        }

        public static <L, R> Symmetric<L, R> of(L l, R r) {
            return new Symmetric<>(l, r);
        }

    }

}
