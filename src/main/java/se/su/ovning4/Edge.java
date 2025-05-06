package se.su.ovning4;

public interface Edge<T> {

    int getWeight();

    void setWeight(int weight);

    T getDestination();

    String getName();
}
