package dk.jlh.games.lines;

import org.junit.Test;

import static dk.jlh.games.lines.BoardMatcher.doesMatch;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the BoardMatcher minimally.
 */
public class BoardMatcherTest {
    @Test
    public void testBoardMatcher1() throws Exception {
        BoardBuilder boardBuilder = new BoardBuilder(2);
        Board b1 = boardBuilder
                .row(0, ".1.b")
                .row(1, ".a..")
                .build();
        Board b2 = new BoardBuilder(2)
                .row(0, ".10.")
                .row(1, "2.1.")
                .build();
        b1.calcDistances(boardBuilder.getSpaceA(), boardBuilder.getSpaceB());
        assertThat(b2, doesMatch(b1));
    }

    @Test
    public void testBoardMatcher2() throws Exception {
        BoardBuilder boardBuilder = new BoardBuilder(2);
        Board b1 = boardBuilder
                .row(0, ".1.a")
                .row(1, ".b..")
                .build();
        Board b2 = new BoardBuilder(2)
                .row(0, ".12.")
                .row(1, "0.1.")
                .build();
        b1.calcDistances(boardBuilder.getSpaceA(), boardBuilder.getSpaceB());
        assertThat(b2, doesMatch(b1));
    }
}
