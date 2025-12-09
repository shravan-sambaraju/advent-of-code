/**
 * @author Shravan
 */
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * --- Day 8: Playground --- Equipped with a new understanding of teleporter maintenance, you
 * confidently step onto the repaired teleporter pad.
 *
 * <p>You rematerialize on an unfamiliar teleporter pad and find yourself in a vast underground
 * space which contains a giant playground!
 *
 * <p>Across the playground, a group of Elves are working on setting up an ambitious Christmas
 * decoration project. Through careful rigging, they have suspended a large number of small
 * electrical junction boxes.
 *
 * <p>Their plan is to connect the junction boxes with long strings of lights. Most of the junction
 * boxes don't provide electricity; however, when two junction boxes are connected by a string of
 * lights, electricity can pass between those two junction boxes.
 *
 * <p>The Elves are trying to figure out which junction boxes to connect so that electricity can
 * reach every junction box. They even have a list of all of the junction boxes' positions in 3D
 * space (your puzzle input).
 *
 * <p>For example:
 *
 * <p>162,817,812 57,618,57 906,360,560 592,479,940 352,342,300 466,668,158 542,29,236 431,825,988
 * 739,650,466 52,470,668 216,146,977 819,987,18 117,168,530 805,96,715 346,949,466 970,615,88
 * 941,993,340 862,61,35 984,92,344 425,690,689 This list describes the position of 20 junction
 * boxes, one per line. Each position is given as X,Y,Z coordinates. So, the first junction box in
 * the list is at X=162, Y=817, Z=812.
 *
 * <p>To save on string lights, the Elves would like to focus on connecting pairs of junction boxes
 * that are as close together as possible according to straight-line distance. In this example, the
 * two junction boxes which are closest together are 162,817,812 and 425,690,689.
 *
 * <p>By connecting these two junction boxes together, because electricity can flow between them,
 * they become part of the same circuit. After connecting them, there is a single circuit which
 * contains two junction boxes, and the remaining 18 junction boxes remain in their own individual
 * circuits.
 *
 * <p>Now, the two junction boxes which are closest together but aren't already directly connected
 * are 162,817,812 and 431,825,988. After connecting them, since 162,817,812 is already connected to
 * another junction box, there is now a single circuit which contains three junction boxes and an
 * additional 17 circuits which contain one junction box each.
 *
 * <p>The next two junction boxes to connect are 906,360,560 and 805,96,715. After connecting them,
 * there is a circuit containing 3 junction boxes, a circuit containing 2 junction boxes, and 15
 * circuits which contain one junction box each.
 *
 * <p>The next two junction boxes are 431,825,988 and 425,690,689. Because these two junction boxes
 * were already in the same circuit, nothing happens!
 *
 * <p>This process continues for a while, and the Elves are concerned that they don't have enough
 * extension cables for all these circuits. They would like to know how big the circuits will be.
 *
 * <p>After making the ten shortest connections, there are 11 circuits: one circuit which contains 5
 * junction boxes, one circuit which contains 4 junction boxes, two circuits which contain 2
 * junction boxes each, and seven circuits which each contain a single junction box. Multiplying
 * together the sizes of the three largest circuits (5, 4, and one of the circuits of size 2)
 * produces 40.
 *
 * <p>Your list contains many junction boxes; connect together the 1000 pairs of junction boxes
 * which are closest together. Afterward, what do you get if you multiply together the sizes of the
 * three largest circuits?
 *
 * <p>Your puzzle answer was 81536.
 *
 * <p>--- Part Two --- The Elves were right; they definitely don't have enough extension cables.
 * You'll need to keep connecting junction boxes together until they're all in one large circuit.
 *
 * <p>Continuing the above example, the first connection which causes all of the junction boxes to
 * form a single circuit is between the junction boxes at 216,146,977 and 117,168,530. The Elves
 * need to know how far those junction boxes are from the wall so they can pick the right extension
 * cable; multiplying the X coordinates of those two junction boxes (216 and 117) produces 25272.
 *
 * <p>Continue connecting the closest unconnected pairs of junction boxes together until they're all
 * in the same circuit. What do you get if you multiply together the X coordinates of the last two
 * junction boxes you need to connect?
 *
 * <p>Your puzzle answer was 7017750530.
 */
public class JunctionBoxManager {

  // Helper class to represent a point in 3D space
  static class Box {
    int id;
    long x, y, z;

    public Box(int id, long x, long y, long z) {
      this.id = id;
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }

  // Helper class to represent a potential connection (Edge)
  static class Connection implements Comparable<Connection> {
    int boxA;
    int boxB;
    long distanceSq; // We use squared distance to avoid expensive Math.sqrt()

    public Connection(int boxA, int boxB, long distanceSq) {
      this.boxA = boxA;
      this.boxB = boxB;
      this.distanceSq = distanceSq;
    }

    @Override
    public int compareTo(Connection other) {
      return Long.compare(this.distanceSq, other.distanceSq);
    }
  }

  // Union-Find (Disjoint Set Union) Data Structure
  static class UnionFind {
    private int[] parent;
    private int[] size;

    public UnionFind(int n) {
      parent = new int[n];
      size = new int[n];
      for (int i = 0; i < n; i++) {
        parent[i] = i; // Everyone is their own parent initially
        size[i] = 1; // Everyone is a circuit of size 1 initially
      }
    }

    public int find(int i) {
      if (parent[i] == i) {
        return i;
      }
      // Path compression: point directly to the root
      parent[i] = find(parent[i]);
      return parent[i];
    }

    public void union(int i, int j) {
      int rootA = find(i);
      int rootB = find(j);

      if (rootA != rootB) {
        // Merge smaller set into larger set
        if (size[rootA] < size[rootB]) {
          int temp = rootA;
          rootA = rootB;
          rootB = temp;
        }
        parent[rootB] = rootA;
        size[rootA] += size[rootB];
      }
    }

    // Returns the size of the circuit that 'i' belongs to
    public int getSize(int i) {
      return size[find(i)];
    }

    // Helper to check if i is a representative root of a circuit
    public boolean isRoot(int i) {
      return parent[i] == i;
    }
  }

  public static long solveCircuits(List<Box> inputLines) {

    // 1. Generate ALL possible pairs and calculate distances
    List<Connection> allConnections = new ArrayList<>();
    for (int i = 0; i < inputLines.size(); i++) {
      for (int j = i + 1; j < inputLines.size(); j++) {
        Box a = inputLines.get(i);
        Box b = inputLines.get(j);

        // Calculate Euclidean distance squared
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        long dz = a.z - b.z;
        long distSq = (dx * dx) + (dy * dy) + (dz * dz);

        allConnections.add(new Connection(a.id, b.id, distSq));
      }
    }

    // 2. Sort connections by shortest distance
    Collections.sort(allConnections);

    // 3. Connect the closest 1000 pairs using Union-Find
    int numBoxes = inputLines.size();
    UnionFind uf = new UnionFind(numBoxes);

    int limit = Math.min(1000, allConnections.size());

    for (int i = 0; i < limit; i++) {
      Connection c = allConnections.get(i);
      uf.union(c.boxA, c.boxB);
    }

    // 4. Gather sizes of all unique circuits
    List<Integer> circuitSizes = new ArrayList<>();
    for (int i = 0; i < numBoxes; i++) {
      // Only count the size if 'i' is the root of a set (avoid duplicates)
      if (uf.isRoot(i)) {
        circuitSizes.add(uf.getSize(i));
      }
    }

    // 6. Sort sizes descending and multiply top 3
    Collections.sort(circuitSizes, Collections.reverseOrder());

    long result = 1;
    // Take up to 3 largest, or fewer if fewer exist
    int topCount = Math.min(3, circuitSizes.size());

    for (int i = 0; i < topCount; i++) {
      result *= circuitSizes.get(i);
    }

    return result;
  }

  // Part 2: Connect until one single circuit remains
  public static long SolveCircuits2(List<Box> boxes) {
    if (boxes == null || boxes.size() < 2) {
      return 0;
    }

    // 1. Generate ALL possible pairs (same logic as Part 1)
    List<Connection> allConnections = new ArrayList<>();
    for (int i = 0; i < boxes.size(); i++) {
      for (int j = i + 1; j < boxes.size(); j++) {
        Box a = boxes.get(i);
        Box b = boxes.get(j);

        long dx = a.x - b.x;
        long dy = a.y - b.y;
        long dz = a.z - b.z;
        long distSq = (dx * dx) + (dy * dy) + (dz * dz);

        allConnections.add(new Connection(a.id, b.id, distSq));
      }
    }

    // 2. Sort connections by shortest distance
    Collections.sort(allConnections);

    // 3. Connect pairs until only 1 component remains
    int numBoxes = boxes.size();
    UnionFind uf = new UnionFind(numBoxes);
    int disconnectedComponents = numBoxes;

    for (Connection c : allConnections) {
      int rootA = uf.find(c.boxA);
      int rootB = uf.find(c.boxB);

      // If they are in different sets, connect them
      if (rootA != rootB) {
        uf.union(c.boxA, c.boxB);
        disconnectedComponents--;

        // If we have reached exactly 1 component, the graph is fully connected
        if (disconnectedComponents == 1) {
          Box boxA = boxes.get(c.boxA);
          Box boxB = boxes.get(c.boxB);
          // Return the product of their X coordinates
          return boxA.x * boxB.x;
        }
      }
    }

    return 0; // Should not happen if the graph is connectable
  }

  public static void main(String[] args) {
    List<Box> boxesList = new ArrayList<>();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    // Ensure you have a file named 'junctions.txt' or similar in resources
    // Or replace this with standard Scanner(System.in) for testing
    InputStream is = loader.getResourceAsStream("junctions.txt");

    Scanner scanner = new Scanner(is);
    int i = 0;
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] arr = line.split(",");
      boxesList.add(
          new Box(i++, Long.parseLong(arr[0]), Long.parseLong(arr[1]), Long.parseLong(arr[2])));
    }

    long answer = solveCircuits(boxesList);
    System.out.println("Product of 3 largest circuits: " + answer);
    long answer2 = SolveCircuits2(boxesList);
    System.out.println(answer2);
  }
}
