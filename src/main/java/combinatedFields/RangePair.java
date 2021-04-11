package combinatedFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RangePair {
    private int value1;
    private int value2;

    public RangePair(int value1, int value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }
    public int getMax() {
        return Math.max(value1,value2);
    }
    public int getMin() {
        return Math.min(value1,value2);
    }
    public List<Integer> getAsList() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(value1);
        integerList.add(value2);
        return integerList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangePair rangePair = (RangePair) o;
        return value1 == rangePair.value1 && value2 == rangePair.value2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value1, value2);
    }
}
