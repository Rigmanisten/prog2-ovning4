package se.su.ovning4;


import java.util.Set;
import java.util.SortedSet;
import java.util.SortedMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Exercise4 {
    private Graph<Node> graph = new ListGraph<>();

    public void loadLocationGraph(String fileName){
      
      try{
         BufferedReader reader = new BufferedReader(new FileReader(fileName));
         String line;
         while ((line = reader.readLine()) != null) {
            
            String[] parts = line.split(";");
            String name = parts[0];
            double xKoordinat = Double.parseDouble(parts[1]);
            double yKoordinat = Double.parseDouble(parts[2]);
            new Location(name, xKoordinat, yKoordinat);
         }


         reader.close();
		} catch (FileNotFoundException e) {
			System.out.printf("%s not found",fileName);
		} catch (IOException e){
			e.printStackTrace();
		}
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
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(";");
                Person person = new Person(parts[0]);
                Record record = new Record(parts[1], parts[2]);

                graph.add(person);
                graph.add(record);
                graph.connect(person, record, "rekomendation", 1);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.printf("%s not found",fileName);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
