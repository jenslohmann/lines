package dk.jlh.games.lines;

import org.hamcrest.Description;

/**
 * Compares / doesMatch Lines boards.
 */
public class BoardMatcher extends org.hamcrest.BaseMatcher<Board> {
    private Board expected;
    private int errorX;
    private int errorY;
    private Board.Space spaceExpected;
    private Board.Space spaceActual;

    BoardMatcher(Board expected) {

        this.expected = expected;
    }

    public static BoardMatcher doesMatch(Board expected) {
        return new BoardMatcher(expected);
    }

    @Override
    public boolean matches(Object o) {
        if (!(o instanceof Board)) {
            return false;
        }
        Board actual = (Board) o;
        for (int y = 0; y < actual.size; y++) {
            for (int x = 0; x < actual.size; x++) {
                spaceExpected = expected.getSpace(x, y);
                if (spaceExpected.distanceToDest == 999) {
                    continue;
                }
                spaceActual = actual.getSpace(x, y);
                if (!(spaceExpected.occupant == spaceActual.occupant
                        && (spaceExpected.distanceToDest == spaceActual.distanceToDest
                        || (spaceExpected.distanceToDest > 100 && spaceActual.distanceToDest > 100)))) {
                    errorX = x;
                    errorY = y;
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Error in (" + errorX + "," + errorY + "), "
                + (char) ('1' + spaceActual.occupant - 1) + ","
                + (char) ('1' + spaceExpected.occupant - 1) + ", distances:"
                + spaceActual.distanceToDest + "," + spaceExpected.distanceToDest + ".");
    }
}
