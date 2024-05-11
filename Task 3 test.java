import java.util.*;
import java.io.*;

public class Analysis {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Analysis <path-to-file>");
            System.exit(1);
        }

        String filePath = args[0];
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> followerCounts = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line, graph, followerCounts);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            return;
        } catch (IOException e) {
            System.out.println("An I/O error occurred: " + e.getMessage());
            return;
        }

        // Print the analysis results
        printAnalysis(graph, followerCounts);
    }

    private static void processLine(String line, Map<String, Set<String>> graph, Map<String, Integer> followerCounts) {
        String[] parts = line.split("\\s+");
        if (parts.length <= 1) {
            graph.putIfAbsent(parts[0], new HashSet<>());
            return;
        }

        String source = parts[0];
        graph.putIfAbsent(source, new HashSet<>());
        for (int i = 1; i < parts.length; i++) {
            String target = parts[i];
            graph.get(source).add(target);
            graph.putIfAbsent(target, new HashSet<>());
            followerCounts.putIfAbsent(target, 0);
            followerCounts.put(target, followerCounts.get(target) + 1);
        }
    }

    private static void printAnalysis(Map<String, Set<String>> graph, Map<String, Integer> followerCounts) {
        int V = graph.size();
        int E = graph.values().stream().mapToInt(Set::size).sum();
        double density = V > 1 ? (double) E / (V * (V - 1)) : 0;
        System.out.println("Density of the graph is: " + density);

        String mostFollowed = findMostFollowed(followerCounts);
        System.out.println("Person with the most followers: " + mostFollowed);

        String mostFollowing = findMostFollowing(graph);
        System.out.println("Person following the most people: " + mostFollowing);
    }

    private static String findMostFollowed(Map<String, Integer> followerCounts) {
        return followerCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue(Comparator.naturalOrder()))
            .map(Map.Entry::getKey)
            .orElse("No data");
    }

    private static String findMostFollowing(Map<String, Set<String>> graph) {
        return graph.entrySet().stream()
            .max(Comparator.comparingInt(entry -> entry.getValue().size()))
            .map(Map.Entry::getKey)
            .orElse("No data");
    }
}
