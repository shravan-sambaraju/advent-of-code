import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * --- Day 7: Laboratories --- You thank the cephalopods for the help and exit the trash compactor,
 * finding yourself in the familiar halls of a North Pole research wing.
 *
 * <p>Based on the large sign that says "teleporter hub", they seem to be researching teleportation;
 * you can't help but try it for yourself and step onto the large yellow teleporter pad.
 *
 * <p>Suddenly, you find yourself in an unfamiliar room! The room has no doors; the only way out is
 * the teleporter. Unfortunately, the teleporter seems to be leaking magic smoke.
 *
 * <p>Since this is a teleporter lab, there are lots of spare parts, manuals, and diagnostic
 * equipment lying around. After connecting one of the diagnostic tools, it helpfully displays error
 * code 0H-N0, which apparently means that there's an issue with one of the tachyon manifolds.
 *
 * <p>You quickly locate a diagram of the tachyon manifold (your puzzle input). A tachyon beam
 * enters the manifold at the location marked S; tachyon beams always move downward. Tachyon beams
 * pass freely through empty space (.). However, if a tachyon beam encounters a splitter (^), the
 * beam is stopped; instead, a new tachyon beam continues from the immediate left and from the
 * immediate right of the splitter.
 *
 * <p>For example:
 *
 * <p>.......S....... ............... .......^....... ............... ......^.^......
 * ............... .....^.^.^..... ............... ....^.^...^.... ............... ...^.^...^.^...
 * ............... ..^...^.....^.. ............... .^.^.^.^.^...^. ............... In this example,
 * the incoming tachyon beam (|) extends downward from S until it reaches the first splitter:
 *
 * <p>.......S....... .......|....... .......^....... ............... ......^.^......
 * ............... .....^.^.^..... ............... ....^.^...^.... ............... ...^.^...^.^...
 * ............... ..^...^.....^.. ............... .^.^.^.^.^...^. ............... At that point,
 * the original beam stops, and two new beams are emitted from the splitter:
 *
 * <p>.......S....... .......|....... ......|^|...... ............... ......^.^......
 * ............... .....^.^.^..... ............... ....^.^...^.... ............... ...^.^...^.^...
 * ............... ..^...^.....^.. ............... .^.^.^.^.^...^. ............... Those beams
 * continue downward until they reach more splitters:
 *
 * <p>.......S....... .......|....... ......|^|...... ......|.|...... ......^.^......
 * ............... .....^.^.^..... ............... ....^.^...^.... ............... ...^.^...^.^...
 * ............... ..^...^.....^.. ............... .^.^.^.^.^...^. ............... At this point,
 * the two splitters create a total of only three tachyon beams, since they are both dumping
 * tachyons into the same place between them:
 *
 * <p>.......S....... .......|....... ......|^|...... ......|.|...... .....|^|^|.....
 * ............... .....^.^.^..... ............... ....^.^...^.... ............... ...^.^...^.^...
 * ............... ..^...^.....^.. ............... .^.^.^.^.^...^. ............... This process
 * continues until all of the tachyon beams reach a splitter or exit the manifold:
 *
 * <p>.......S....... .......|....... ......|^|...... ......|.|...... .....|^|^|.....
 * .....|.|.|..... ....|^|^|^|.... ....|.|.|.|.... ...|^|^|||^|... ...|.|.|||.|... ..|^|^|||^|^|..
 * ..|.|.|||.|.|.. .|^|||^||.||^|. .|.|||.||.||.|. |^|^|^|^|^|||^| |.|.|.|.|.|||.| To repair the
 * teleporter, you first need to understand the beam-splitting properties of the tachyon manifold.
 * In this example, a tachyon beam is split a total of 21 times.
 *
 * <p>Analyze your manifold diagram. How many times will the beam be split?
 *
 * <p>Your puzzle answer was 1553.
 *
 * <p>--- Part Two --- With your analysis of the manifold complete, you begin fixing the teleporter.
 * However, as you open the side of the teleporter to replace the broken manifold, you are surprised
 * to discover that it isn't a classical tachyon manifold - it's a quantum tachyon manifold.
 *
 * <p>With a quantum tachyon manifold, only a single tachyon particle is sent through the manifold.
 * A tachyon particle takes both the left and right path of each splitter encountered.
 *
 * <p>Since this is impossible, the manual recommends the many-worlds interpretation of quantum
 * tachyon splitting: each time a particle reaches a splitter, it's actually time itself which
 * splits. In one timeline, the particle went left, and in the other timeline, the particle went
 * right.
 *
 * <p>To fix the manifold, what you really need to know is the number of timelines active after a
 * single particle completes all of its possible journeys through the manifold.
 *
 * <p>In the above example, there are many timelines. For instance, there's the timeline where the
 * particle always went left:
 *
 * <p>.......S....... .......|....... ......|^....... ......|........ .....|^.^......
 * .....|......... ....|^.^.^..... ....|.......... ...|^.^...^.... ...|........... ..|^.^...^.^...
 * ..|............ .|^...^.....^.. .|............. |^.^.^.^.^...^. |.............. Or, there's the
 * timeline where the particle alternated going left and right at each splitter:
 *
 * <p>.......S....... .......|....... ......|^....... ......|........ ......^|^......
 * .......|....... .....^|^.^..... ......|........ ....^.^|..^.... .......|....... ...^.^.|.^.^...
 * .......|....... ..^...^|....^.. .......|....... .^.^.^|^.^...^. ......|........ Or, there's the
 * timeline where the particle ends up at the same point as the alternating timeline, but takes a
 * totally different path to get there:
 *
 * <p>.......S....... .......|....... ......|^....... ......|........ .....|^.^......
 * .....|......... ....|^.^.^..... ....|.......... ....^|^...^.... .....|......... ...^.^|..^.^...
 * ......|........ ..^..|^.....^.. .....|......... .^.^.^|^.^...^. ......|........ In this example,
 * in total, the particle ends up on 40 different timelines.
 *
 * <p>Apply the many-worlds interpretation of quantum tachyon splitting to your manifold diagram. In
 * total, how many different timelines would a single tachyon particle end up on?
 */
public class TachyonBeam {

  private static int getBeamSplits(List<String> beamGrid) {
    if (beamGrid == null || beamGrid.isEmpty()) {
      return 0;
    }

    int rows = beamGrid.size();
    int width = beamGrid.get(0).length();
    boolean startFound = false;
    int strtRow = -1;
    HashSet<Integer> currentBeams = new HashSet<>();
    for (int r = 0; r < rows; r++) {
      int sIdx = beamGrid.get(r).indexOf('S');
      if (sIdx != -1) {

        currentBeams.add(sIdx);
        startFound = true;
        strtRow = r;
        break;
      }
    }
    if (!startFound) {
      return 0;
    }
    int splitsCount = 0;

    for (int row = strtRow; row < rows; row++) {
      String currRow = beamGrid.get(row);
      HashSet<Integer> nextRowBeams = new HashSet<>();
      for (int col : currentBeams) {
        if (col < 0 || col >= width) {
          continue;
        }
        char ch = currRow.charAt(col);
        if (ch == '^') {
          splitsCount++;
          nextRowBeams.add(col - 1);
          nextRowBeams.add(col + 1);
        } else {
          nextRowBeams.add(col);
        }
      }
      currentBeams = nextRowBeams;
      if (currentBeams.isEmpty()) {
        break;
      }
    }

    return splitsCount;
  }

  private static long getBeamSplits2(List<String> beamGrid) {
    if (beamGrid == null || beamGrid.isEmpty()) {
      return 0;
    }
    int rows = beamGrid.size();
    int width = 0;
    for (String line : beamGrid) {
      width = Math.max(width, line.length());
    }
    int strtRow = -1;
    long[] currentTimelines = new long[width];
    boolean startFound = false;
    for (int r = 0; r < rows; r++) {
      String currRow = beamGrid.get(r);
      int sIdx = currRow.indexOf('S');
      if (sIdx != -1) {
        currentTimelines[sIdx] = 1;
        startFound = true;
        strtRow = r;
        break;
      }
    }
    if (!startFound) {
      return 0;
    }

    long completedTimelines = 0;
    for (int row = strtRow + 1; row < rows; row++) {
      String currRow = beamGrid.get(row);
      int rowLen = currRow.length();
      boolean activeRow = false;
      long[] nextTimelines = new long[width];
      for (int col = 0; col < width; col++) {
        if (currentTimelines[col] == 0) {
          continue;
        }
        long count = currentTimelines[col];

        if (col >= rowLen) {
          completedTimelines += count;
          continue;
        }
        char ch = currRow.charAt(col);
        if (ch == '^') {
          if (col - 1 > 0) {
            nextTimelines[col - 1] += count;
            activeRow = true;
          } else {
            completedTimelines += count;
          }
          if (col + 1 < width) {
            nextTimelines[col + 1] += count;
            activeRow = true;
          } else {
            completedTimelines += count;
          }
        } else {
          nextTimelines[col] += count;
          activeRow = true;
        }
      }
      currentTimelines = nextTimelines;
      if (!activeRow) {
        break;
      }
    }
    for (long count : currentTimelines) {
      completedTimelines += count;
    }

    return completedTimelines;
  }

  public static void main(String[] args) {
    List<String> beamGrid = new ArrayList<>();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    // Ensure the file exists in your resources folder
    InputStream is = loader.getResourceAsStream("tachyonbeam.txt");

    Scanner scanner = new Scanner(is);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      beamGrid.add(line);
    }
    int splits = getBeamSplits(beamGrid);
    System.out.println(splits);
    long splits2 = getBeamSplits2(beamGrid);
    System.out.println(splits2);
  }
}
