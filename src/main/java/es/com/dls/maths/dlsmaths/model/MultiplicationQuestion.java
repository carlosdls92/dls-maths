package es.com.dls.maths.dlsmaths.model;

public class MultiplicationQuestion {
    private final int a;
    private final int b;

    public MultiplicationQuestion(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() { return a; }
    public int getB() { return b; }

    public int getResult() {
        return a * b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiplicationQuestion)) return false;
        MultiplicationQuestion that = (MultiplicationQuestion) o;
        return a == that.a && b == that.b;
    }

    @Override
    public int hashCode() {
        return 31 * a + b;
    }
}