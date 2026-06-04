import java.util.*;

public class MovieProductionWorkflow {

    static final int WHITE = 0;
    static final int GREY = 1;
    static final int BLACK = 2;

    static Map<String, List<String>> graph = new HashMap<>();
    static Map<String, Integer> color = new HashMap<>();
    static Map<String, String> parent = new HashMap<>();

    static Stack<String> topoOrder = new Stack<>();

    static boolean cycleFound = false;
    static String cycleStart = "";
    static String cycleEnd = "";

    static void addEdge(String from, String to) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        graph.putIfAbsent(to, new ArrayList<>());
    }

    static void dfs(String node) {

        color.put(node, GREY);

        for (String neighbor : graph.get(node)) {

            if (color.get(neighbor) == WHITE) {

                parent.put(neighbor, node);
                dfs(neighbor);

            } else if (color.get(neighbor) == GREY) {

                cycleFound = true;
                cycleStart = neighbor;
                cycleEnd = node;
                return;
            }
        }

        color.put(node, BLACK);
        topoOrder.push(node);
    }

    static void printCycle() {

        List<String> cycle = new ArrayList<>();

        cycle.add(cycleStart);

        String current = cycleEnd;

        while (!current.equals(cycleStart)) {
            cycle.add(current);
            current = parent.get(current);
        }

        cycle.add(cycleStart);

        Collections.reverse(cycle);

        System.out.println("\nCycle Detected!");
        System.out.print("Cycle: ");

        for (int i = 0; i < cycle.size(); i++) {
            System.out.print(cycle.get(i));
            if (i != cycle.size() - 1)
                System.out.print(" -> ");
        }

        System.out.println();
    }

    public static void main(String[] args) {

        addEdge("Script Writing", "Casting");
        addEdge("Casting", "Filming");
        addEdge("Filming", "Editing");
        addEdge("Editing", "Visual Effects");
        addEdge("Visual Effects", "Marketing");

        // Intentional cycle
        addEdge("Marketing", "Casting");

        for (String node : graph.keySet()) {
            color.put(node, WHITE);
            parent.put(node, null);
        }

        for (String node : graph.keySet()) {

            if (color.get(node) == WHITE) {

                dfs(node);

                if (cycleFound)
                    break;
            }
        }

        System.out.println("Movie Production Workflow System");
        System.out.println("--------------------------------");

        if (cycleFound) {

            printCycle();

        } else {

            System.out.println("\nTopological Order:");

            while (!topoOrder.isEmpty()) {
                System.out.print(topoOrder.pop() + " ");
            }

            System.out.println();
        }

        System.out.println("\nTime Complexity:");
        System.out.println("DFS Traversal   : O(V + E)");
        System.out.println("Cycle Detection : O(V + E)");
        System.out.println("Topological Sort: O(V + E)");
    }
}