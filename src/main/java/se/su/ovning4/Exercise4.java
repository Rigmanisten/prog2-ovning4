package se.su.ovning4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Exercise4 {
    private Graph<Node> graph = new ListGraph<>();

    public void loadLocationGraph(String fileName){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            if ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                for (int i = 0; i < parts.length; i += 3) {
                    String name = parts[i];
                    double x = Double.parseDouble(parts[i + 1]);
                    double y = Double.parseDouble(parts[i + 2]);
                    Location location = new Location(name, x, y);
                    graph.add(location);
                }
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String from = parts[0];
                String to = parts[1];
                String label = parts[2];
                int weight = Integer.parseInt(parts[3]);

                Location fromNode = null;
                Location toNode = null;
                for (Node node : graph.getNodes()) {
                    if (node instanceof Location loc) {
                        if (loc.getName().equals(from)) fromNode = loc;
                        if (loc.getName().equals(to)) toNode = loc;
                    }
                }

                if (fromNode != null && toNode != null) {
                    graph.connect(fromNode, toNode, label, weight);
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.err.printf("%s not found%n", fileName);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public SortedMap<Integer, SortedSet<Record>> getAlsoLiked(Record item) {
        SortedMap<Integer, SortedSet<Record>> alsoLikedMap = new TreeMap<>(Comparator.reverseOrder());

        var edges = graph.getEdgesFrom(item);
        for (Edge<Node> edge : edges) {

            Node person = edge.getDestination();

            var personEdges = graph.getEdgesFrom(person);
            for (Edge<Node> personEdge : personEdges) {
                Record record = (Record) personEdge.getDestination();
                int popularity = getPopularity(record);

                SortedSet<Record> recordSet = alsoLikedMap.get(popularity);
                if (recordSet == null) {
                    recordSet = new TreeSet<>(Comparator.comparing(Record::toString));
                    alsoLikedMap.put(popularity, recordSet);
                }
                recordSet.add(record);
            }
        }

        return alsoLikedMap;
    }


    public int getPopularity(Record item) {
        int popularity = graph.getEdgesFrom(item).size();
        return popularity;
    }

    public SortedMap<Integer, Set<Record>> getTop5() {
        SortedMap<Integer, Set<Record>> popularityMap = new TreeMap<>(Collections.reverseOrder());

        for (Node node : graph.getNodes()) {
            if (node instanceof Record record) {
                int popularity = getPopularity(record);
                popularityMap.computeIfAbsent(popularity, k -> new HashSet<>()).add(record);
            }
        }

        SortedMap<Integer, Set<Record>> top5 = new TreeMap<>(Collections.reverseOrder());
        int count = 0;
        for (Map.Entry<Integer, Set<Record>> entry : popularityMap.entrySet()) {
            if (count >= 5) break;
            top5.put(entry.getKey(), entry.getValue());
            count++;
        }

        return top5;
    }

    public void loadRecommendationGraph(String fileName) {
        Map<String, Person> personMap = new HashMap<>();
        Map<String, Record> recordMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String personName = parts[0];
                String recordName = parts[1];
                String artist = parts[2];

                Person person = personMap.computeIfAbsent(personName, Person::new);
                Record record = recordMap.computeIfAbsent(recordName + "|" + artist, k -> new Record(recordName, artist));

                graph.add(person);
                graph.add(record);

                if (graph.getEdgeBetween(person, record) == null) {
                    graph.connect(person, record, "recommendation", 0);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.printf("%s not found%n", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}