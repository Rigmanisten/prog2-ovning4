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
         Graph <Location> locationGraph = new ListGraph<>();
         if ((line = reader.readLine()) != null){
            line = reader.readLine();
            String[] parts = line.split(";");
           
            for (int i = 0; i < parts.length; i += 3) {
               String name = parts[i];
               double xKoordinat = Double.parseDouble(parts[i+1]);
               double yKoordinat = Double.parseDouble(parts[i+2]);
               locationGraph.add(new Location(name, xKoordinat, yKoordinat));
            }
         }
         while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            String fromName = parts[0];
            String toName = parts[1];
            String vehicle = parts[2];
            int weight = Integer.parseInt(parts[3]);

            Location from = null;
            Location to = null;

            for(Location Node : locationGraph.getNodes()){
               if(Node.getName().equals(fromName)){from = Node;}
               if(Node.getName().equals(toName)){to = Node;}
            }
            graph.connect(from, to, vehicle ,weight);
            }

         reader.close();
		} catch (FileNotFoundException e) {
			System.out.printf("%s not found",fileName);
		} catch (IOException e){
			e.printStackTrace();
		}
    }

    public SortedMap<Integer, SortedSet<Record>> getAlsoLiked(Record item) {
        
        Map<Record, Integer> countingMap = new HashMap<>();
        
        for(Edge<Node> toPerson: graph.getEdgesFrom(item)){
            Node p = toPerson.getDestination();
            for(Edge<Node> fromPerson: graph.getEdgesFrom(p)){
                Record record = (Record) fromPerson.getDestination();
                if(!record.equals(item)){
                    countingMap.put(record, countingMap.getOrDefault(record, 0)+1);
                }
            }
        }
        SortedMap<Integer, SortedSet<Record>> alosLiked = new TreeMap<>(Collections.reverseOrder()); 
        for(Map.Entry< Record,Integer> entry : countingMap.entrySet()){
            int count = entry.getValue();
            alosLiked.computeIfAbsent(count, k -> 
            new TreeSet<>(Comparator.comparing(Record::toString))).add(entry.getKey());
        }
        
        
        return alosLiked;

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
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(";");
                Person person = new Person(parts[0]);
                Record record = new Record(parts[1], parts[2]);

                graph.add(person);
                graph.add(record);
                graph.connect(person, record, "recommendation", 0);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.printf("%s not found",fileName);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
