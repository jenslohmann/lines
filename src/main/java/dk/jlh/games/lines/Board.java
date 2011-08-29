package dk.jlh.games.lines;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Data object for the Lines board.
 */
public class Board {
    /** "External" size */
    private final int size;
    private List<Space> freeSet;
    private Random random;
    private Lines controller;

    public class Space {
        private int x;
        private int y;
        private int occupant; // FIXME
        boolean selected;
        int distanceToDest; // Used by shortest path algorithm

        public Space(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setOccupant(int newValue) {
            occupant = newValue;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private Space[] space;
    private int ABOVE;
    private int ABOVE_LEFT;
    private int ABOVE_RIGHT;
    private int LEFT;
    private int RIGHT;
    private int BELOW;
    private int BELOW_LEFT;
    private int BELOW_RIGHT;

    Board() {
        this(9);
    }

    Board(int size) {
        this.size = size;
        space = new Space[1 + (size + 1) * (size + 2)];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                space[1 + x + (y + 1) * (size + 1)] = new Space(x, y);
            }
        }

        ABOVE = -size - 1;
        ABOVE_LEFT = -size - 2;
        ABOVE_RIGHT = -size;
        LEFT = -1;
        RIGHT = 1;
        BELOW = size + 1;
        BELOW_LEFT = size;
        BELOW_RIGHT = size + 2;

        initialize();
    }

    void initialize() {
        random = new Random();
        freeSet = new ArrayList<Space>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Space s = getSpace(x, y);
                s.distanceToDest = Integer.MAX_VALUE;
                freeSet.add(s);
            }
        }
    }

    void setSpace(int x, int y, int newOccupant) {
        Space theSpace = getSpace(x, y);
        if (newOccupant == 0) {
            freeSet.add(theSpace);
        } else {
            freeSet.remove(theSpace);
        }
        theSpace.setOccupant(newOccupant);
    }

    void calcDistances(Space source, Space dest) {
        LinkedList<Space> toDo = new LinkedList<Space>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Space curSpace = getSpace(x, y);
                if (curSpace != null) {
                    if (curSpace == dest) {
                        curSpace.distanceToDest = 0;
                    } else {
                        curSpace.distanceToDest = Integer.MAX_VALUE;
                    }
                }
            }
        }
        dest.distanceToDest = 0;
        calcDistForNeighbours(source, toDo, dest);
        while (!toDo.isEmpty()) {
            calcDistForNeighbours(source, toDo, toDo.removeFirst());
        }
    }

    void calcDistForNeighbours(Space src, LinkedList<Space> toDo, Space curSpace) {
        calcDistForNeighbour(src, toDo, curSpace, getSpace(curSpace.getX() - 1, curSpace.getY()));
        calcDistForNeighbour(src, toDo, curSpace, getSpace(curSpace.getX() + 1, curSpace.getY()));
        calcDistForNeighbour(src, toDo, curSpace, getSpace(curSpace.getX(), curSpace.getY() - 1));
        calcDistForNeighbour(src, toDo, curSpace, getSpace(curSpace.getX(), curSpace.getY() + 1));
    }

    private void calcDistForNeighbour(Space src, LinkedList<Space> toDo, Space curSpace, Space neighbour) {
        if (neighbour != null && (neighbour == src || neighbour.occupant == 0)) {
            if (curSpace.distanceToDest + 1 < neighbour.distanceToDest) {
                neighbour.distanceToDest = curSpace.distanceToDest + 1;
                toDo.add(neighbour);
            }
        }
    }

    public int addPieces(int[] newPieces) {
        int score = 0;
        for (int p : newPieces) {
            int freeSize = freeSet.size();
            if (freeSize > 0) {
                int s = random.nextInt(freeSize);
                Space space = freeSet.remove(s);
                space.setOccupant(p);
                score += removeCreatedLine(space);
            } else {
                controller.setState(GameState.GAME_OVER);
                return 0;
            }
        }
        return score;
    }

    /**
     * Removes a created line (or more?) of same kind of piece.
     * 
     * @param includingSpace
     * The space that has just received a piece.
     * @return The score for the removed pieces.
     */
    int removeCreatedLine(Space includingSpace) {
        if (checkSpaces(includingSpace, LEFT, RIGHT, false) > 4) {
            return 2 * checkSpaces(includingSpace, LEFT, RIGHT, true);
        }
        if (checkSpaces(includingSpace, ABOVE_LEFT, BELOW_RIGHT, false) > 4) {
            return 2 * checkSpaces(includingSpace, ABOVE_LEFT, BELOW_RIGHT, true);
        }
        if (checkSpaces(includingSpace, ABOVE, BELOW, false) > 4) {
            return 2 * checkSpaces(includingSpace, ABOVE, BELOW, true);
        }
        if (checkSpaces(includingSpace, ABOVE_RIGHT, BELOW_LEFT, false) > 4) {
            return 2 * checkSpaces(includingSpace, ABOVE_RIGHT, BELOW_LEFT, true);
        }
        return 0;
    }

    private int checkSpaces(Space includingSpace, int direction1, int direction2, boolean remove) {
        int suite = 1;
        Space s = includingSpace;
        while ((s = space[1 + s.getX() + (s.getY() + 1) * (size + 1) + direction1]) != null
                && s.occupant == includingSpace.occupant) {
            suite++;
            if (remove) {
                freeSpace(s);
            }
        }
        s = includingSpace;
        while ((s = space[1 + s.getX() + (s.getY() + 1) * (size + 1) + direction2]) != null
                && s.occupant == includingSpace.occupant) {
            suite++;
            if (remove) {
                freeSpace(s);
            }
        }
        if (remove) {
            freeSpace(includingSpace);
        }
        return suite;
    }

    Space getSpace(int x, int y) {
        return space[1 + x + (y + 1) * (size + 1)];
    }

    public void setController(Lines controller) {
        this.controller = controller;
    }

    public void freeSpace(Space selectedSpace) {
        selectedSpace.occupant = 0;
        freeSet.add(selectedSpace);
    }
}
