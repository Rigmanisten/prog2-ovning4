package se.su.ovning4;

import java.util.*;

public class ListGraph<T> implements Graph<T> {

    //Vi valde att använda List före Set främst för att ha mindre begränsningar och vara mer expanderbar.
    //Om vi skulle behöva introducera multi-graph eller använde oss av någon ordning i framtiden så är List mer lämplig.
    //Set skulle vara marginellt mer effektiv men den skulle samtidigt vara mer strikt att implementera.

    private final Map<T, List<Edge<T>>> adjacencyList;

    public ListGraph() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public void add(T node) {
        if (!adjacencyList.containsKey(node)) {
            adjacencyList.put(node, new ArrayList<>());  //addIFAbssent
        }
    }

    @Override
    public void connect(T node1, T node2, String name, int weight) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            throw new NoSuchElementException();
        }
        if(weight < 0){
            throw new IllegalArgumentException("weight must be positive");
        }
        if(getEdgeBetween(node1, node2) != null){
            throw new IllegalStateException("Edge already exists between " + node1 + " and " + node2);
        }
        Edge<T> edge1 = new EdgeImpl<>(node2, name, weight);
        Edge<T> edge2 = new EdgeImpl<>(node1, name, weight);

        adjacencyList.get(node1).add(edge1);
        adjacencyList.get(node2).add(edge2);
    }

    @Override
    public void setConnectionWeight(T node1, T node2, int weight) {
        if(!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2) || (getEdgeBetween(node1, node2) == null)) {
            throw new NoSuchElementException("Node or edge not exists between " + node1 + " and " + node2);
        }
        if(weight < 0){throw new IllegalArgumentException("weight must be positive");}
        getEdgeBetween(node1, node2).setWeight(weight);
        getEdgeBetween(node2, node1).setWeight(weight);
    }

    @Override
    public Set<T> getNodes() {
        return new HashSet<>(adjacencyList.keySet());
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {
        if(!adjacencyList.containsKey(node)){throw new NoSuchElementException();}
        List<Edge<T>> result = new ArrayList<>(adjacencyList.get(node));
        return Collections.unmodifiableCollection(result);
    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {throw new NoSuchElementException();}
        for(Edge<T> edge : adjacencyList.get(node1)){
            if(edge.getDestination().equals(node2)){
                return edge;
            }
        }
        return null;
    }

    @Override
    public void disconnect(T node1, T node2) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {throw new NoSuchElementException();}
        if (getEdgeBetween(node1, node2)== null){
            throw new IllegalStateException("Edge dose not exists between " + node1 + " and " + node2);
        }
        Edge<T> edge1 = getEdgeBetween(node1, node2);
        Edge<T> edge2 = getEdgeBetween(node2, node1);

        adjacencyList.get(node1).remove(edge1);
        adjacencyList.get(node2).remove(edge2);
    }

    @Override
    public void remove(T node) {
        if(!adjacencyList.containsKey(node)){throw new NoSuchElementException();}

        List<Edge<T>> edgesCopy = new ArrayList<>(adjacencyList.get(node));
        for(Edge<T> edge : edgesCopy){  // kan vara ett problem med att vi manipulerar edges medans vi loopar edges
            disconnect(node, edge.getDestination());
        }

        adjacencyList.remove(node);
    }

    @Override
    public boolean pathExists(T from, T to) {
        List<Edge<T>> result = getPath(from, to);
        return result != null;
    }

    private boolean Searcher(T current, T goal, Set<T> nodesVisited, Stack<T> path) {
        nodesVisited.add(current);
        if (current.equals(goal)) {
            return true;
        }

        for (Edge<T> edge : adjacencyList.get(current)) {

            T neighbor = edge.getDestination();
            if (!nodesVisited.contains(neighbor)) {
                path.push(neighbor);
                boolean found = Searcher(neighbor, goal, nodesVisited, path);

                if (found) {
                    return true;
                } else {
                    path.pop();
                }
            }
        }
        return false;
    }

    @Override
    public List<Edge<T>> getPath(T from, T to) {
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            return null;
        }

        Set<T> nodesVisited = new HashSet<>();
        Stack<T> path = new Stack<>();
        path.push(from);

        boolean found = Searcher(from, to, nodesVisited, path);
        if (!found) return null;

        List<Edge<T>> edges = new ArrayList<>();
        T prev = path.get(0);
        for (int i = 1; i < path.size(); i++) {
            T next = path.get(i);
            Edge<T> edge = getEdgeBetween(prev, next);
            if (edge != null) {
                edges.add(edge);
            }
            prev = next;
        }

        return edges;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(T node : adjacencyList.keySet()){
            sb.append(node.toString()).append(", ").append("\n");
            for(Edge<T> edge: getEdgesFrom(node)) {
                sb.append(edge.toString()).append(", ");
            }
        }
        return sb.toString();
    }
}