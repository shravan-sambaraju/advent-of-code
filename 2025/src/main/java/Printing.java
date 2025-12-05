import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * --- Day 4: Printing Department --- You ride the escalator down to the printing department.
 * They're clearly getting ready for Christmas; they have lots of large rolls of paper everywhere,
 * and there's even a massive printer in the corner (to handle the really big print jobs).
 *
 * <p>Decorating here will be easy: they can make their own decorations. What you really need is a
 * way to get further into the North Pole base while the elevators are offline.
 *
 * <p>"Actually, maybe we can help with that," one of the Elves replies when you ask for help.
 * "We're pretty sure there's a cafeteria on the other side of the back wall. If we could break
 * through the wall, you'd be able to keep moving. It's too bad all of our forklifts are so busy
 * moving those big rolls of paper around."
 *
 * <p>If you can optimize the work the forklifts are doing, maybe they would have time to spare to
 * break through the wall.
 *
 * <p>The rolls of paper (@) are arranged on a large grid; the Elves even have a helpful diagram
 * (your puzzle input) indicating where everything is located.
 *
 * <p>For example:
 *
 * <p>..@@.@@@@. @@@.@.@.@@ @@@@@.@.@@ @.@@@@..@. @@.@@@@.@@ .@@@@@@@.@ .@.@.@.@@@ @.@@@.@@@@
 * .@@@@@@@@. @.@.@@@.@. The forklifts can only access a roll of paper if there are fewer than four
 * rolls of paper in the eight adjacent positions. If you can figure out which rolls of paper the
 * forklifts can access, they'll spend less time looking and more time breaking down the wall to the
 * cafeteria.
 *
 * <p>In this example, there are 13 rolls of paper that can be accessed by a forklift (marked with
 * x):
 *
 * <p>..xx.xx@x. x@@.@.@.@@ @@@@@.x.@@ @.@@@@..@. x@.@@@@.@x .@@@@@@@.@ .@.@.@.@@@ x.@@@.@@@@
 * .@@@@@@@@. x.x.@@@.x. Consider your complete diagram of the paper roll locations. How many rolls
 * of paper can be accessed by a forklift?
 *
 * <p>--- Part Two --- Now, the Elves just need help accessing as much of the paper as they can.
 *
 * <p>Once a roll of paper can be accessed by a forklift, it can be removed. Once a roll of paper is
 * removed, the forklifts might be able to access more rolls of paper, which they might also be able
 * to remove. How many total rolls of paper could the Elves remove if they keep repeating this
 * process?
 *
 * <p>Starting with the same example as above, here is one way you could remove as many rolls of
 * paper as possible, using highlighted @ to indicate that a roll of paper is about to be removed,
 * and using x to indicate that a roll of paper was just removed:
 *
 * <p>Initial state: ..@@.@@@@. @@@.@.@.@@ @@@@@.@.@@ @.@@@@..@. @@.@@@@.@@ .@@@@@@@.@
 * .@.@.@.@@@ @.@@@.@@@@ .@@@@@@@@. @.@.@@@.@.
 *
 * <p>Remove 13 rolls of paper: ..xx.xx@x. x@@.@.@.@@ @@@@@.x.@@ @.@@@@..@. x@.@@@@.@x .@@@@@@@.@
 * .@.@.@.@@@ x.@@@.@@@@ .@@@@@@@@. x.x.@@@.x.
 *
 * <p>Remove 12 rolls of paper: .......x.. .@@.x.x.@x x@@@@...@@ x.@@@@..x. .@.@@@@.x. .x@@@@@@.x
 * .x.@.@.@@@ ..@@@.@@@@ .x@@@@@@@. ....@@@...
 *
 * <p>Remove 7 rolls of paper: .......... .x@.....x. .@@@@...xx ..@@@@.... .x.@@@@... ..@@@@@@..
 * ...@.@.@@x ..@@@.@@@@ ..x@@@@@@. ....@@@...
 *
 * <p>Remove 5 rolls of paper: .......... ..x....... .x@@@..... ..@@@@.... ...@@@@... ..x@@@@@..
 * ...@.@.@@. ..x@@.@@@x ...@@@@@@. ....@@@...
 *
 * <p>Remove 2 rolls of paper: .......... .......... ..x@@..... ..@@@@.... ...@@@@... ...@@@@@..
 * ...@.@.@@. ...@@.@@@. ...@@@@@x. ....@@@...
 *
 * <p>Remove 1 roll of paper: .......... .......... ...@@..... ..x@@@.... ...@@@@... ...@@@@@..
 * ...@.@.@@. ...@@.@@@. ...@@@@@.. ....@@@...
 *
 * <p>Remove 1 roll of paper: .......... .......... ...x@..... ...@@@.... ...@@@@... ...@@@@@..
 * ...@.@.@@. ...@@.@@@. ...@@@@@.. ....@@@...
 *
 * <p>Remove 1 roll of paper: .......... .......... ....x..... ...@@@.... ...@@@@... ...@@@@@..
 * ...@.@.@@. ...@@.@@@. ...@@@@@.. ....@@@...
 *
 * <p>Remove 1 roll of paper: .......... .......... .......... ...x@@.... ...@@@@... ...@@@@@..
 * ...@.@.@@. ...@@.@@@. ...@@@@@.. ....@@@... Stop once no more rolls of paper are accessible by a
 * forklift. In this example, a total of 43 rolls of paper can be removed.
 *
 * <p>Start with your original diagram. How many rolls of paper in total can be removed by the Elves
 * and their forklifts?
 */
public class Printing {

  private static int forkLiftRolls(char[][] rolls) {
    if (rolls == null || rolls.length == 0) {
      return 0;
    }
    int totalAccesible = 0;
    int rows = rolls.length;
    int cols = rolls[0].length;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (rolls[row][col] == '@') {
          if (countValid(rolls, row, col, rows, cols)) {
            totalAccesible++;
          }
        }
      }
    }
    return totalAccesible;
  }

  private static int forkLiftRolls2(char[][] rolls) {
    if (rolls == null || rolls.length == 0) {
      return 0;
    }
    int totalAccesible = 0;
    int rows = rolls.length;
    int cols = rolls[0].length;
    boolean proceedIteration = false;
    ArrayList<int[]> toBeRemoved = new ArrayList<>();
    do {

      proceedIteration = false;
      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          if (rolls[row][col] == '@') {
            if (countValid(rolls, row, col, rows, cols)) {
              toBeRemoved.add(new int[] {row, col});
            }
          }
        }
      }
      if (!toBeRemoved.isEmpty()) {
        totalAccesible += toBeRemoved.size();
        proceedIteration = true;
        for (int[] roll : toBeRemoved) {
          rolls[roll[0]][roll[1]] = '.';
        }
        toBeRemoved.clear();
      }

    } while (proceedIteration);

    return totalAccesible;
  }

  private static boolean countValid(char[][] rolls, int row, int col, int rows, int cols) {
    int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
    int count = 0;
    for (int[] dir : dirs) {
      int r = row + dir[0];
      int c = col + dir[1];
      if (r >= 0 && r < rows && c >= 0 && c < cols && rolls[r][c] == '@') {
        count++;
      }
    }
    return count < 4;
  }

  public static void main(String[] args) {
    List<String> rollsList = new ArrayList<>();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream is = loader.getResourceAsStream("printing.txt");
    Scanner scanner = new Scanner(is);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      rollsList.add(line);
    }
    char[][] paperRolls = new char[rollsList.size()][];
    for (int i = 0; i < rollsList.size(); i++) {
      paperRolls[i] = rollsList.get(i).toCharArray();
    }
    long rolls = forkLiftRolls(paperRolls);
    System.out.println(rolls);
    long rolls2 = forkLiftRolls2(paperRolls);
    System.out.println(rolls2);
  }
}
