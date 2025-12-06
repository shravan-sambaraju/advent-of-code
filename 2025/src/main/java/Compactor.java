import java.io.InputStream;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

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
