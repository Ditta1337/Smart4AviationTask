package org.example.util;

public record Tuple<T, U>(T first, U second) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return first.equals(tuple.first) && second.equals(tuple.second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}

