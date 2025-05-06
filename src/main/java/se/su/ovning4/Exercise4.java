package se.su.ovning4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;
import java.util.SortedMap;

public class Exercise4 {
    private Graph<Node> graph = new ListGraph<>();

    public void loadLocationGraph(String fileName){

    }

    public SortedMap<Integer, SortedSet<Record>> getAlsoLiked(Record item) {
       return null;
    }

    public int getPopularity(Record item) {
       return -1;
    }

    public SortedMap<Integer, Set<Record>> getTop5() {
       return null;
    }

    public void loadRecommendationGraph(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                Person person = new Person(parts[0]);
                Record record = new Record(parts[1], parts[2]);

                graph.add(person);
                graph.add(record);
                graph.connect(person, record, "rekommendation", 0);  // eller rätt vikt
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
