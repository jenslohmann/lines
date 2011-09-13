package dk.jlh.games.lines;

/**
 * Board builder to help unit testing.
 */
public class BoardBuilder {
    private Board board;
    private Board.Space spaceA, spaceB;

    public BoardBuilder(int size) {
        board = new Board(size);
    }

    public Board build() {
        return board;
    }

    public BoardBuilder row(int row, String data) {
        for (int i = 0; i < data.length() / 2; i++) {
            int occupant;
            int distance;
            char metadataChar = data.charAt(i * 2);
            char item = data.charAt(i * 2 + 1);
            switch (item) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                    occupant = item - '1' + 1;
                    distance = distanceToDest(metadataChar);
                    break;
                case 'a':
                    spaceA = board.getSpace(i, row);
                case 'b':
                    if (item == 'b') {
                        spaceB = board.getSpace(i, row);
                    }
                case ' ':
                case '.':
                default:
                    occupant = 0;
                    board.freeSpace(board.getSpace(i, row));
                    distance = distanceToDest(metadataChar);
            }
            board.setSpace(i, row, occupant);
            board.getSpace(i, row).distanceToDest = distance;
        }
        return this;
    }

    private int distanceToDest(char metadataChar) {
        int distance;
        switch (metadataChar) {
            case '0':
                distance = 0;
                break;
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                distance = metadataChar - '1' + 1;
                break;
            case ' ':
            case '.':
            case '?':
                distance = 999;
                break;
            default:
                distance = 10000;
        }
        return distance;
    }

    public Board.Space getSpaceA() {
        return spaceA;
    }

    public Board.Space getSpaceB() {
        return spaceB;
    }
}
