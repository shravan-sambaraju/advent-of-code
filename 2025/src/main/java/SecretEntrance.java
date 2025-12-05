import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * --- Day 1: Secret Entrance --- The Elves have good news and bad news.
 *
 * <p>The good news is that they've discovered project management! This has given them the tools
 * they need to prevent their usual Christmas emergency. For example, they now know that the North
 * Pole decorations need to be finished soon so that other critical tasks can start on time.
 *
 * <p>The bad news is that they've realized they have a different emergency: according to their
 * resource planning, none of them have any time left to decorate the North Pole!
 *
 * <p>To save Christmas, the Elves need you to finish decorating the North Pole by December 12th.
 *
 * <p>Collect stars by solving puzzles. Two puzzles will be made available on each day; the second
 * puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 *
 * <p>You arrive at the secret entrance to the North Pole base ready to start decorating.
 * Unfortunately, the password seems to have been changed, so you can't get in. A document taped to
 * the wall helpfully explains:
 *
 * <p>"Due to new security protocols, the password is locked in the safe below. Please see the
 * attached document for the new combination."
 *
 * <p>The safe has a dial with only an arrow on it; around the dial are the numbers 0 through 99 in
 * order. As you turn the dial, it makes a small click noise as it reaches each number.
 *
 * <p>The attached document (your puzzle input) contains a sequence of rotations, one per line,
 * which tell you how to open the safe. A rotation starts with an L or R which indicates whether the
 * rotation should be to the left (toward lower numbers) or to the right (toward higher numbers).
 * Then, the rotation has a distance value which indicates how many clicks the dial should be
 * rotated in that direction.
 *
 * <p>So, if the dial were pointing at 11, a rotation of R8 would cause the dial to point at 19.
 * After that, a rotation of L19 would cause it to point at 0.
 *
 * <p>Because the dial is a circle, turning the dial left from 0 one click makes it point at 99.
 * Similarly, turning the dial right from 99 one click makes it point at 0.
 *
 * <p>So, if the dial were pointing at 5, a rotation of L10 would cause it to point at 95. After
 * that, a rotation of R5 could cause it to point at 0.
 *
 * <p>The dial starts by pointing at 50.
 *
 * <p>You could follow the instructions, but your recent required official North Pole secret
 * entrance security training seminar taught you that the safe is actually a decoy. The actual
 * password is the number of times the dial is left pointing at 0 after any rotation in the
 * sequence.
 *
 * <p>For example, suppose the attached document contained the following rotations:
 *
 * <p>L68 L30 R48 L5 R60 L55 L1 L99 R14 L82 Following these rotations would cause the dial to move
 * as follows:
 *
 * <p>The dial starts by pointing at 50. The dial is rotated L68 to point at 82. The dial is rotated
 * L30 to point at 52. The dial is rotated R48 to point at 0. The dial is rotated L5 to point at 95.
 * The dial is rotated R60 to point at 55. The dial is rotated L55 to point at 0. The dial is
 * rotated L1 to point at 99. The dial is rotated L99 to point at 0. The dial is rotated R14 to
 * point at 14. The dial is rotated L82 to point at 32. Because the dial points at 0 a total of
 * three times during this process, the password in this example is 3.
 *
 * <p>Analyze the rotations in your attached document. What's the actual password to open the door?
 *
 * <p>Your puzzle answer was 997.
 *
 * <p>--- Part Two --- You're sure that's the right password, but the door won't open. You knock,
 * but nobody answers. You build a snowman while you think.
 *
 * <p>As you're rolling the snowballs for your snowman, you find another security document that must
 * have fallen into the snow:
 *
 * <p>"Due to newer security protocols, please use password method 0x434C49434B until further
 * notice."
 *
 * <p>You remember from the training seminar that "method 0x434C49434B" means you're actually
 * supposed to count the number of times any click causes the dial to point at 0, regardless of
 * whether it happens during a rotation or at the end of one.
 *
 * <p>Following the same rotations as in the above example, the dial points at zero a few extra
 * times during its rotations:
 *
 * <p>The dial starts by pointing at 50. The dial is rotated L68 to point at 82; during this
 * rotation, it points at 0 once. The dial is rotated L30 to point at 52. The dial is rotated R48 to
 * point at 0. The dial is rotated L5 to point at 95. The dial is rotated R60 to point at 55; during
 * this rotation, it points at 0 once. The dial is rotated L55 to point at 0. The dial is rotated L1
 * to point at 99. The dial is rotated L99 to point at 0. The dial is rotated R14 to point at 14.
 * The dial is rotated L82 to point at 32; during this rotation, it points at 0 once. In this
 * example, the dial points at 0 three times at the end of a rotation, plus three more times during
 * a rotation. So, in this example, the new password would be 6.
 *
 * <p>Be careful: if the dial were pointing at 50, a single rotation like R1000 would cause the dial
 * to point at 0 ten times before returning back to 50!
 *
 * <p>Using password method 0x434C49434B, what is the password to open the door?
 */
public class SecretEntrance {
  private static int buildPassword(List<Integer> rotations) {
    if (rotations == null || rotations.isEmpty()) {
      return 0;
    }
    int dial = 50;
    int password = 0;
    for (int rotation : rotations) {
      if (rotation >= 0) {
        dial = (dial + rotation) % 100;
      } else {
        dial = (dial + rotation) % 100;
        if (dial < 0) {
          dial += 100;
        }
      }
      if (dial == 0) {
        password++;
      }
    }
    return password;
  }

  private static int buildPassword2(List<Integer> rotations) {
    if (rotations == null || rotations.isEmpty()) {
      return 0;
    }
    int dial = 50; // starts at 50
    int password = 0;

    for (int rotation : rotations) {
      if (rotation > 0) {
        // moving right (Positive)
        // Integer division works here because we are counting multiples of 100
        // in the positive direction starting from a number < 100.
        int crosses = (dial + rotation) / 100;
        password += crosses;

        dial = (dial + rotation) % 100;
      } else if (rotation < 0) {
        // moving left (Negative)
        // We calculate the number of 100-boundaries crossed between the start and end point.
        // We use Math.floorDiv because standard division rounds towards zero, which is incorrect
        // for negative numbers (e.g., -1/100 = 0, but floor(-1/100) = -1).

        // startInterval: which "100-block" are we in currently? (dial-1 because hitting 0 counts)
        int startInterval = Math.floorDiv(dial - 1, 100);

        // endInterval: which "100-block" do we end up in?
        int endInterval = Math.floorDiv(dial + rotation - 1, 100);

        password += (startInterval - endInterval);

        // Update dial position
        dial = (dial + rotation) % 100;
        if (dial < 0) dial += 100;
      }
      // rotation == 0: nothing moves
    }

    return password;
  }

  public static void main(String[] args) throws FileNotFoundException {
    List<Integer> documents = new ArrayList<>();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream is = loader.getResourceAsStream("secretentrance.txt");
    Scanner scanner = new Scanner(is);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      char direction = line.charAt(0);
      int rotations = Integer.parseInt(line.substring(1));
      if (direction == 'L') {
        rotations = -rotations;
      }
      documents.add(rotations);
    }

    int password = buildPassword(documents);
    int password2 = buildPassword2(documents);
    System.out.println(password);
    System.out.println(password2);
  }
}
