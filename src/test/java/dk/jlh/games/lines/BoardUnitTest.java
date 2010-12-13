package dk.jlh.games.lines;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import dk.jlh.games.lines.Board.Space;

public class BoardUnitTest {
	
	Board b;

	@Before
	public void setUp() throws Exception {
		b = new Board(3);
	}

	@Test
	public void testCalcDistances() {
		Space curSpace = b.getSpace(0,2);
		curSpace.distanceToDest = 0;
		b.getSpace(0,1).occupant = 1;
		b.calcDistances(b.getSpace(0,0), curSpace);
		assertEquals(4, b.getSpace(0,0).distanceToDest);
		assertEquals(3, b.getSpace(1,0).distanceToDest);
		assertEquals(2, b.getSpace(1,1).distanceToDest);
		assertEquals(1, b.getSpace(1,2).distanceToDest);
	}

	@Test
	public void testCalcDistForNeighbours1() {
		LinkedList<Board.Space> toDo = new LinkedList<Board.Space>();
		Space curSpace = b.getSpace(1,1);
		curSpace.distanceToDest = 0;
		b.calcDistForNeighbours(b.getSpace(0, 1), toDo, curSpace);
		assertEquals(1, b.getSpace(0,1).distanceToDest);
	}
	
	@Test
	public void testSetOccupant() {
	    b.getSpace(1,1).setOccupant(1);
        b.getSpace(1,2).setOccupant(1);
	}
}
