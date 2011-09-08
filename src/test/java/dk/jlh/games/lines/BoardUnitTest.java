package dk.jlh.games.lines;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import dk.jlh.games.lines.Board.Space;

public class BoardUnitTest {

    Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board(3);
    }

    @Test
    public void testCalcDistances() {
        Space curSpace = board.getSpace(0, 2);
        curSpace.distanceToDest = 0;
        board.getSpace(0, 1).occupant = 1;
        board.calcDistances(board.getSpace(0, 0), curSpace);
        assertThat(board.getSpace(0, 0).distanceToDest, is(4));
        assertThat(board.getSpace(1, 0).distanceToDest, is(3));
        assertThat(board.getSpace(1, 1).distanceToDest, is(2));
        assertThat(board.getSpace(1, 2).distanceToDest, is(1));
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
        Board board = (new BoardBuilder(9))
                .row(0, " 1 1 1 1         .")
                .row(1, " 1 2 3 4 5 1     .")
                .row(2, "     2 3 4 5 1 2 .")
                .row(3, "   1 2 3 4 5 1    ")
                .row(4, " 1 2 3            ")
                .row(5, " 1 2 3            ")
                .row(6, " 1 2 3            ")
                .row(7, " 1 2 3            ")
                .row(8, " 1 2 3            ")
                .build();
        Space spaceA = board.getSpace(5, 0);
        Space spaceB = board.getSpace(8, 0);
        board.calcDistances(spaceA, spaceB);
        assertThat(spaceA.distanceToDest, is(3));
        assertThat(board.getSpace(4, 0).distanceToDest, is(4));
        assertThat(board.getSpace(5, 1).distanceToDest, is(greaterThan(1000)));
        assertThat(board.getSpace(7, 1).distanceToDest, is(2));
        assertThat(board.getSpace(7, 0).distanceToDest, is(1));
        assertThat(board.getSpace(8, 1).distanceToDest, is(1));
    }
}
