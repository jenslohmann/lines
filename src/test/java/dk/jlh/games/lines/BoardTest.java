package dk.jlh.games.lines;

import dk.jlh.games.lines.Board.Space;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static dk.jlh.games.lines.BoardMatcher.doesMatch;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BoardTest {

    Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board(3);
    }

    @Test
    public void testCalcDistances() {
        BoardBuilder boardBuilder = new BoardBuilder(3);
        Board board = boardBuilder
                .row(0, " a . .")
                .row(1, " 1 . .")
                .row(2, " b . .").build();
        board.calcDistances(boardBuilder.getSpaceA(), boardBuilder.getSpaceB());

        Board expectedBoard = (new BoardBuilder(3))
                .row(0, "4.3 4 ")
                .row(1, " 12 3 ")
                .row(2, "0 1 2 ").build();
        assertThat(board, doesMatch(expectedBoard));
    }

    @Test
    public void testCalcDistForNeighbours1() {
        LinkedList<Board.Space> toDo = new LinkedList<Board.Space>();
        Space curSpace = board.getSpace(1, 1);
        curSpace.distanceToDest = 0;
        board.calcDistForNeighbours(board.getSpace(0, 1), toDo, curSpace);
        assertThat(board.getSpace(0, 1).distanceToDest, is(1));
    }

    @Test
    public void testSetOccupant() {
        board.getSpace(1, 1).setOccupant(1);
        board.getSpace(1, 2).setOccupant(1);
    }

    @Test
    public void testCalcDistForNeighbours2() throws Exception {
        BoardBuilder boardBuilder = new BoardBuilder(9);
        Board board = boardBuilder
                .row(0, " 1 1 1 1   a     b")
                .row(1, " 1 2 3 4 5 1     .")
                .row(2, "     2 3 4 5 1 2 .")
                .row(3, "   1 2 3 4 5 1    ")
                .row(4, " 1 2 3            ")
                .row(5, " 1 2 3            ")
                .row(6, " 1 2 3            ")
                .row(7, " 1 2 3            ")
                .row(8, " 1 2 3            ")
                .build();
        Space spaceA = boardBuilder.getSpaceA();
        Space spaceB = boardBuilder.getSpaceB();

        board.calcDistances(spaceA, spaceB);

        Board wantedBoard = (new BoardBuilder(9))
                .row(0, "?1?1?1?1403.2.1.0.")
                .row(1, " 1 2 3?4?5?13.2.1.")
                .row(2, "? ? ?2 3 4 5?1 22.")
                .row(3, "? ?1 2 3 4 5 14 3 ")
                .row(4, " 1 2 3      6 5 4 ")
                .row(5, " 1 2 3        6 5 ")
                .row(6, " 1 2 3            ")
                .row(7, " 1 2 3            ")
                .row(8, " 1 2 3            ")
                .build();

        System.out.println(board);
        System.out.println(wantedBoard);

        assertThat(board, doesMatch(wantedBoard));
    }
}
