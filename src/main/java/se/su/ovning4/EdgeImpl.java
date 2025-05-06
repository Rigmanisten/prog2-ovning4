package se.su.ovning4;

public class EdgeImpl<T> implements Edge<T> {
    private T destination;
    private String name;
    private int weight;


    public EdgeImpl(T destination, String name, int weight){
        if (weight < 0) {
            throw new IllegalArgumentException("weight must be posetive");
        }
        this.destination = destination;
        this.name = name;
        this.weight = weight;
    }

    @Override
    public T getDestination() {
        return destination;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "till " + destination + " med " + name + " tar " + weight;
    }

    @Override
    public void setWeight(int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Must be posetive");
        }
        this.weight = weight;

    }
}
