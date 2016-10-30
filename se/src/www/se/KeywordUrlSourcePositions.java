package se;

import java.util.Collection;
import java.util.HashSet;

public class KeywordUrlSourcePositions {
    String source;
    HashSet<Integer> positions;

    public KeywordUrlSourcePositions(String source) {
        this.source = source;
        this.positions = new HashSet<>();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && this.source.equals((KeywordUrlSourcePositions)obj);
    }

    public void add(int value) {
        this.positions.add(value);
    }

    public void addAll(Collection<? extends Integer> values) {
        this.positions.addAll(values);
    }
}
