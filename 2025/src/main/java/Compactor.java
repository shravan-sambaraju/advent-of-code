import java.io.InputStream;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

/**
 * --- Day 6: Trash Compactor --- After helping the Elves in the kitchen, you were taking a break
 * and helping them re-enact a movie scene when you over-enthusiastically jumped into the garbage
 * chute!
 *
 * <p>A brief fall later, you find yourself in a garbage smasher. Unfortunately, the door's been
 * magnetically sealed.
 *
 * <p>As you try to find a way out, you are approached by a family of cephalopods! They're pretty
 * sure they can get the door open, but it will take some time. While you wait, they're curious if
 * you can help the youngest cephalopod with her math homework.
 *
 * <p>Cephalopod math doesn't look that different from normal math. The math worksheet (your puzzle
 * input) consists of a list of problems; each problem has a group of numbers that need to be either
 * added (+) or multiplied (*) together.
 *
 * <p>However, the problems are arranged a little strangely; they seem to be presented next to each
 * other in a very long horizontal list. For example:
 *
 * <p>123 328 51 64 45 64 387 23 6 98 215 314 + * + Each problem's numbers are arranged vertically;
 * at the bottom of the problem is the symbol for the operation that needs to be performed. Problems
 * are separated by a full column of only spaces. The left/right alignment of numbers within each
 * problem can be ignored.
 *
 * <p>So, this worksheet contains four problems:
 *
 * <p>123 * 45 * 6 = 33210 328 + 64 + 98 = 490 51 * 387 * 215 = 4243455 64 + 23 + 314 = 401 To check
 * their work, cephalopod students are given the grand total of adding together all of the answers
 * to the individual problems. In this worksheet, the grand total is 33210 + 490 + 4243455 + 401 =
 * 4277556.
 *
 * <p>Of course, the actual worksheet is much wider. You'll need to make sure to unroll it
 * completely so that you can read the problems clearly.
 *
 * <p>Solve the problems on the math worksheet. What is the grand total found by adding together all
 * of the answers to the individual problems?
 *
 * <p>Your puzzle answer was 6169101504608.
 *
 * <p>--- Part Two --- The big cephalopods come back to check on how things are going. When they see
 * that your grand total doesn't match the one expected by the worksheet, they realize they forgot
 * to explain how to read cephalopod math.
 *
 * <p>Cephalopod math is written right-to-left in columns. Each number is given in its own column,
 * with the most significant digit at the top and the least significant digit at the bottom.
 * (Problems are still separated with a column consisting only of spaces, and the symbol at the
 * bottom of the problem is still the operator to use.)
 *
 * <p>Here's the example worksheet again:
 *
 * <p>123 328 51 64 45 64 387 23 6 98 215 314 + * + Reading the problems right-to-left one column at
 * a time, the problems are now quite different:
 *
 * <p>The rightmost problem is 4 + 431 + 623 = 1058 The second problem from the right is 175 * 581 *
 * 32 = 3253600 The third problem from the right is 8 + 248 + 369 = 625 Finally, the leftmost
 * problem is 356 * 24 * 1 = 8544 Now, the grand total is 1058 + 3253600 + 625 + 8544 = 3263827.
 *
 * <p>Solve the problems on the math worksheet again. What is the grand total found by adding
 * together all of the answers to the individual problems?
 */
public class Compactor {

  private static long getCompactedSum(String[][] rolls) {
    if (rolls == null || rolls.length == 0) {
      return 0;
    }

    int rows = rolls.length;
    // With the new parsing logic, the array is guaranteed rectangular
    int cols = rolls[0].length;

    long compactedSum = 0;

    // Loop through all columns (problems)
    for (int j = 0; j < cols; j++) {

      String operator = rolls[rows - 1][j];
      boolean isAddition = operator.equals("+");

      // Initialize based on operation
      // If (+), start at 0. If (*), start at 1.
      long colCompaction = isAddition ? 0 : 1;

      // Loop rows upwards (skipping the last row which is the operator)
      for (int i = rows - 2; i >= 0; i--) {
        String cell = rolls[i][j]; // Already trimmed by parser

        if (!cell.isEmpty()) {
          try {
            long val = Long.parseLong(cell);
            if (isAddition) {
              colCompaction += val;
            } else {
              colCompaction *= val;
            }
          } catch (NumberFormatException e) {
            // Handle cases where cell is not a number (just in case)
          }
        }
      }
      compactedSum += colCompaction;
    }

    return compactedSum;
  }

  private static long getCompactedSum2(List<String> sheets) {
    if (sheets == null || sheets.isEmpty()) {
      return 0;
    }

    // Find max width
    int maxWidth = 0;
    for (String line : sheets) {
      maxWidth = Math.max(maxWidth, line.length());
    }

    // Pad all lines to same width
    String[] paddedLines = new String[sheets.size()];
    for (int i = 0; i < sheets.size(); i++) {
      paddedLines[i] = String.format("%-" + maxWidth + "s", sheets.get(i));
    }

    long grandTotal = 0;
    int col = maxWidth - 1; // Start from rightmost column

    while (col >= 0) {
      // Skip empty columns (separators)
      boolean isEmpty = true;
      for (String line : paddedLines) {
        if (line.charAt(col) != ' ') {
          isEmpty = false;
          break;
        }
      }

      if (isEmpty) {
        col--;
        continue;
      }

      // Collect all columns for this problem (going left until separator)
      List<Integer> problemCols = new ArrayList<>();
      while (col >= 0) {
        isEmpty = true;
        for (String line : paddedLines) {
          if (line.charAt(col) != ' ') {
            isEmpty = false;
            break;
          }
        }
        if (isEmpty) break;

        problemCols.add(col);
        col--;
      }

      if (problemCols.isEmpty()) continue;

      // Reverse to get left-to-right order within the problem
      Collections.reverse(problemCols);

      // Get operator from last row
      char operator = ' ';
      for (int c : problemCols) {
        char ch = paddedLines[paddedLines.length - 1].charAt(c);
        if (ch == '+' || ch == '*') {
          operator = ch;
          break;
        }
      }

      boolean isAddition = (operator == '+');
      long problemResult = isAddition ? 0 : 1;

      // Each column represents ONE complete number
      for (int c : problemCols) {
        StringBuilder numBuilder = new StringBuilder();

        // Read top to bottom (skip last row which has operator)
        for (int row = 0; row < paddedLines.length - 1; row++) {
          char ch = paddedLines[row].charAt(c);
          if (ch != ' ') {
            numBuilder.append(ch);
          }
        }

        if (numBuilder.length() > 0) {
          try {
            long val = Long.parseLong(numBuilder.toString());
            if (isAddition) {
              problemResult += val;
            } else {
              problemResult *= val;
            }
          } catch (NumberFormatException e) {
            // Skip invalid numbers
          }
        }
      }

      grandTotal += problemResult;
    }

    return grandTotal;
  }

  public static void main(String[] args) {
    List<String> sheets = new ArrayList<>();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    // Ensure the file exists in your resources folder
    InputStream is = loader.getResourceAsStream("compactor.txt");

    Scanner scanner = new Scanner(is);
    int maxLineLength = 0;
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      // We keep empty lines to maintain vertical spacing if necessary,
      // though usually only the last line (operators) defines the width.
      sheets.add(line);
      if (line.length() > maxLineLength) {
        maxLineLength = line.length();
      }
    }

    // --- NEW PARSING LOGIC: Detect Vertical Separators ---

    // 1. Identify valid column ranges (non-space columns)
    List<int[]> columnRanges = new ArrayList<>();
    int start = -1;

    for (int c = 0; c < maxLineLength; c++) {
      boolean isColumnEmpty = true;
      for (String line : sheets) {
        if (c < line.length() && line.charAt(c) != ' ') {
          isColumnEmpty = false;
          break;
        }
      }

      // If we find a character and aren't in a block, start one
      if (!isColumnEmpty && start == -1) {
        start = c;
      }
      // If we hit a separator (empty col) and were in a block, close it
      else if (isColumnEmpty && start != -1) {
        columnRanges.add(new int[] {start, c});
        start = -1;
      }
    }
    // Close the final block if the line didn't end with spaces
    if (start != -1) {
      columnRanges.add(new int[] {start, maxLineLength});
    }

    // 2. Extract data into rectangular grid
    String[][] nums = new String[sheets.size()][columnRanges.size()];

    for (int i = 0; i < sheets.size(); i++) {
      String line = sheets.get(i);
      for (int j = 0; j < columnRanges.size(); j++) {
        int[] range = columnRanges.get(j);
        int s = range[0];
        int e = Math.min(range[1], line.length());

        if (s < line.length()) {
          nums[i][j] = line.substring(s, e).trim();
        } else {
          nums[i][j] = "";
        }
      }
    }

    long rolls = getCompactedSum(nums);
    System.out.println(rolls);
    long rolls2 = getCompactedSum2(sheets);
    System.out.println(rolls2);
  }
}
