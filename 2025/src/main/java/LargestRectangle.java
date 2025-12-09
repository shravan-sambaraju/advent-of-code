/**
 * @author Shravan
 */
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * --- Day 9: Movie Theater --- You slide down the firepole in the corner of the playground and land
 * in the North Pole base movie theater!
 *
 * <p>The movie theater has a big tile floor with an interesting pattern. Elves here are
 * redecorating the theater by switching out some of the square tiles in the big grid they form.
 * Some of the tiles are red; the Elves would like to find the largest rectangle that uses red tiles
 * for two of its opposite corners. They even have a list of where the red tiles are located in the
 * grid (your puzzle input).
 *
 * <p>For example:
 *
 * <p>7,1 11,1 11,7 9,7 9,5 2,5 2,3 7,3 Showing red tiles as # and other tiles as ., the above
 * arrangement of red tiles would look like this:
 *
 * <p>.............. .......#...#.. .............. ..#....#...... .............. ..#......#....
 * .............. .........#.#.. .............. You can choose any two red tiles as the opposite
 * corners of your rectangle; your goal is to find the largest rectangle possible.
 *
 * <p>For example, you could make a rectangle (shown as O) with an area of 24 between 2,5 and 9,7:
 *
 * <p>.............. .......#...#.. .............. ..#....#...... .............. ..OOOOOOOO....
 * ..OOOOOOOO.... ..OOOOOOOO.#.. .............. Or, you could make a rectangle with area 35 between
 * 7,1 and 11,7:
 *
 * <p>.............. .......OOOOO.. .......OOOOO.. ..#....OOOOO.. .......OOOOO.. ..#....OOOOO..
 * .......OOOOO.. .......OOOOO.. .............. You could even make a thin rectangle with an area of
 * only 6 between 7,3 and 2,3:
 *
 * <p>.............. .......#...#.. .............. ..OOOOOO...... .............. ..#......#....
 * .............. .........#.#.. .............. Ultimately, the largest rectangle you can make in
 * this example has area 50. One way to do this is between 2,5 and 11,1:
 *
 * <p>.............. ..OOOOOOOOOO.. ..OOOOOOOOOO.. ..OOOOOOOOOO.. ..OOOOOOOOOO.. ..OOOOOOOOOO..
 * .............. .........#.#.. .............. Using two red tiles as opposite corners, what is the
 * largest area of any rectangle you can make?
 *
 * <p>Your puzzle answer was 4763040296.
 *
 * <p>--- Part Two --- The Elves just remembered: they can only switch out tiles that are red or
 * green. So, your rectangle can only include red or green tiles.
 *
 * <p>In your list, every red tile is connected to the red tile before and after it by a straight
 * line of green tiles. The list wraps, so the first red tile is also connected to the last red
 * tile. Tiles that are adjacent in your list will always be on either the same row or the same
 * column.
 *
 * <p>Using the same example as before, the tiles marked X would be green:
 *
 * <p>.............. .......#XXX#.. .......X...X.. ..#XXXX#...X.. ..X........X.. ..#XXXXXX#.X..
 * .........X.X.. .........#X#.. .............. In addition, all of the tiles inside this loop of
 * red and green tiles are also green. So, in this example, these are the green tiles:
 *
 * <p>.............. .......#XXX#.. .......XXXXX.. ..#XXXX#XXXX.. ..XXXXXXXXXX.. ..#XXXXXX#XX..
 * .........XXX.. .........#X#.. .............. The remaining tiles are never red nor green.
 *
 * <p>The rectangle you choose still must have red tiles in opposite corners, but any other tiles it
 * includes must now be red or green. This significantly limits your options.
 *
 * <p>For example, you could make a rectangle out of red and green tiles with an area of 15 between
 * 7,3 and 11,1:
 *
 * <p>.............. .......OOOOO.. .......OOOOO.. ..#XXXXOOOOO.. ..XXXXXXXXXX.. ..#XXXXXX#XX..
 * .........XXX.. .........#X#.. .............. Or, you could make a thin rectangle with an area of
 * 3 between 9,7 and 9,5:
 *
 * <p>.............. .......#XXX#.. .......XXXXX.. ..#XXXX#XXXX.. ..XXXXXXXXXX.. ..#XXXXXXOXX..
 * .........OXX.. .........OX#.. .............. The largest rectangle you can make in this example
 * using only red and green tiles has area 24. One way to do this is between 9,5 and 2,3:
 *
 * <p>.............. .......#XXX#.. .......XXXXX.. ..OOOOOOOOXX.. ..OOOOOOOOXX.. ..OOOOOOOOXX..
 * .........XXX.. .........#X#.. .............. Using two red tiles as opposite corners, what is the
 * largest area of any rectangle you can make using only red and green tiles?
 */
public class LargestRectangle {

  static class Point {
    long x, y;

    public Point(long x, long y) {
      this.x = x;
      this.y = y;
    }
  }

  private static long getMaxRectangleArea(List<Point> tiles) {
    if (tiles == null || tiles.size() < 2) {
      return 0; // Need at least 2 tiles to form a pair
    }

    long maxArea = 0;

    // Iterate through every unique pair of tiles
    for (int i = 0; i < tiles.size(); i++) {
      for (int j = i + 1; j < tiles.size(); j++) {
        Point p1 = tiles.get(i);
        Point p2 = tiles.get(j);

        // Calculate width and height (Inclusive of the start and end tiles)
        // Example: 7 to 11 is distance 4, but covers 5 tiles (7,8,9,10,11)
        long width = Math.abs(p1.x - p2.x) + 1;
        long height = Math.abs(p1.y - p2.y) + 1;

        long area = width * height;

        if (area > maxArea) {
          maxArea = area;
        }
      }
    }

    return maxArea;
  }

  private static long getMaxRectangleArea2(List<Point> tiles) {
    if (tiles == null || tiles.size() < 2) {
      return 0;
    }

    long maxArea = 0;

    // Iterate through every unique pair of tiles
    for (int i = 0; i < tiles.size(); i++) {
      for (int j = i + 1; j < tiles.size(); j++) {
        Point p1 = tiles.get(i);
        Point p2 = tiles.get(j);

        long width = Math.abs(p1.x - p2.x) + 1;
        long height = Math.abs(p1.y - p2.y) + 1;
        long area = width * height;

        // Optimization: Skip expensive check if this area isn't bigger than what we already found
        if (area <= maxArea) {
          continue;
        }

        // Check if this rectangle is valid (strictly inside the polygon defined by tiles)
        if (isValidRectangle(p1, p2, tiles)) {
          maxArea = area;
        }
      }
    }

    return maxArea;
  }

  // Helper to check if the rectangle defined by two corners is fully inside the polygon
  private static boolean isValidRectangle(Point p1, Point p2, List<Point> polygon) {
    long minX = Math.min(p1.x, p2.x);
    long maxX = Math.max(p1.x, p2.x);
    long minY = Math.min(p1.y, p2.y);
    long maxY = Math.max(p1.y, p2.y);

    // 1. Check all 4 corners.
    // p1 and p2 are definitely on the boundary (they are red tiles), so they are valid.
    // We need to check the other two virtual corners.
    if (!isPointInPolygon(minX, maxY, polygon)) return false; // Top-Left
    if (!isPointInPolygon(maxX, minY, polygon)) return false; // Bottom-Right

    // 2. Check the center point.
    // This handles cases where corners are on the boundary but the rectangle spans "empty space"
    // (like a U-shape).
    double centerX = (minX + maxX) / 2.0;
    double centerY = (minY + maxY) / 2.0;
    if (!isPointInPolygon(centerX, centerY, polygon)) return false;

    // 3. Check if any polygon edge cuts THROUGH the rectangle.
    // This handles complex shapes where the polygon might zig-zag through the rectangle.
    int n = polygon.size();
    for (int i = 0; i < n; i++) {
      Point a = polygon.get(i);
      Point b = polygon.get((i + 1) % n); // Wrap around to first point

      // Check intersection based on edge orientation
      if (a.x == b.x) { // Vertical Edge
        // Does this vertical line pass strictly between minX and maxX?
        if (a.x > minX && a.x < maxX) {
          // Does the Y-segment of the edge overlap with the Y-range of the rectangle?
          long edgeMinY = Math.min(a.y, b.y);
          long edgeMaxY = Math.max(a.y, b.y);
          // Check for overlap strictly inside the rectangle height
          if (Math.max(minY, edgeMinY) < Math.min(maxY, edgeMaxY)) {
            return false; // Intersection found inside rectangle
          }
        }
      } else { // Horizontal Edge
        // Does this horizontal line pass strictly between minY and maxY?
        if (a.y > minY && a.y < maxY) {
          // Does the X-segment of the edge overlap with the X-range of the rectangle?
          long edgeMinX = Math.min(a.x, b.x);
          long edgeMaxX = Math.max(a.x, b.x);
          // Check for overlap strictly inside the rectangle width
          if (Math.max(minX, edgeMinX) < Math.min(maxX, edgeMaxX)) {
            return false; // Intersection found inside rectangle
          }
        }
      }
    }

    return true;
  }

  // Ray Casting algorithm to check if a point is inside or on the boundary of the polygon
  // Accepts double coordinates to handle center-point checks
  private static boolean isPointInPolygon(double px, double py, List<Point> polygon) {
    boolean inside = false;
    int n = polygon.size();
    for (int i = 0, j = n - 1; i < n; j = i++) {
      Point vi = polygon.get(i);
      Point vj = polygon.get(j);

      // First, check if point is exactly on the segment (Boundary is valid/Green)
      if (isOnSegment(px, py, vi, vj)) {
        return true;
      }

      // Ray casting: Check intersections with horizontal ray to the right
      if (((vi.y > py) != (vj.y > py))
          && (px < (vj.x - vi.x) * (py - vi.y) / (double) (vj.y - vi.y) + vi.x)) {
        inside = !inside;
      }
    }
    return inside;
  }

  // Helper to check if point (px, py) lies on the segment between a and b
  private static boolean isOnSegment(double px, double py, Point a, Point b) {
    // Allow a small epsilon for double comparison if needed,
    // but since input is discrete grid, direct comparison with logic is usually fine.
    // For 'long' coords, we check bounding box and cross product (which is 0 for collinear).

    // Since edges are rectilinear (horizontal/vertical), math is simpler:
    if (a.x == b.x) { // Vertical segment
      return Math.abs(px - a.x) < 0.0001 && py >= Math.min(a.y, b.y) && py <= Math.max(a.y, b.y);
    } else if (a.y == b.y) { // Horizontal segment
      return Math.abs(py - a.y) < 0.0001 && px >= Math.min(a.x, b.x) && px <= Math.max(a.x, b.x);
    }
    return false; // Should not happen given problem constraints (rectilinear)
  }

  public static void main(String[] args) {
    List<Point> tiles = new ArrayList<>();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    // Ensure you have a file named 'tiles.txt' in resources or change logic to read specific input
    InputStream is = loader.getResourceAsStream("tiles.txt");

    Scanner scanner = new Scanner(is);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] parts = line.split(",");
      long x = Long.parseLong(parts[0].trim());
      long y = Long.parseLong(parts[1].trim());
      tiles.add(new Point(x, y));
    }

    long result = getMaxRectangleArea(tiles);
    System.out.println("Largest Rectangle Area: " + result);
    long result2 = getMaxRectangleArea2(tiles);
    System.out.println("Largest Rectangle Area: " + result2);
  }
}
