package se;

import com.sun.deploy.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class KeywordUrlSourcePositions implements Serializable {
    String source;
    HashSet<Integer> positions;

    public KeywordUrlSourcePositions(String source) {
        this.source = source;
        this.positions = new HashSet<>();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && this.source.equals(((KeywordUrlSourcePositions)obj).source);
    }

    @Override
    public String toString() {
        ArrayList<String> ret = new ArrayList<>();
        for (Integer v : positions) {
            ret.add(v.toString());
        }
        return "{" + "source=" + source + ", positions=[" + StringUtils.join(ret, ", ") + "]}";
    }

    public void add(int value) {
        this.positions.add(value);
    }

    public void addAll(Collection<? extends Integer> values) {
        this.positions.addAll(values);
    }
}
