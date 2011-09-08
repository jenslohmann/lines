package dk.jlh.games.lines;

import org.hamcrest.Description;

/**
 * Compares / matches Lines boards.
 */
public class BoardMatcher extends org.hamcrest.BaseMatcher<Board> {
    private Board expected;

    BoardMatcher(Board expected) {

        this.expected = expected;
    }

    public static BoardMatcher matches(Board expected) {
     return new BoardMatcher(expected);
    }

    @Override
    public boolean matches(Object o) {
        if (!(o instanceof Board)) {
            return false;
        }
        Board board = (Board) o;
        for(int x = 0; x < board.size; x++) {
            for(int y = 0; y < board.size; y++) {
                 return board.getSpace(x, y).occupant == expected.getSpace(x, y).occupant;
            }
        }
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void describeTo(Description description) {
        // FIXME
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
