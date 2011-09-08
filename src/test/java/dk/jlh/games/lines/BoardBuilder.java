package dk.jlh.games.lines;

/**
 * Board builder to help unit testing.
 */
public class BoardBuilder {
    private Board board;

    public BoardBuilder(int size) {
        board = new Board(size);
    }

    public Board build() {
        return board;
    }

    public BoardBuilder row(int row, String data) {
        for (int i = 0; i < data.length() / 2; i++) {
            int occupant = 0;
            char item = data.charAt(i * 2 + 1);
            switch (item) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                    occupant = item - '1' + 1;
                    break;
                case ' ':
                case '.':
                default:
                    occupant = 0;
                    board.freeSpace(board.getSpace(i, row));
            }
            board.setSpace(i, row, occupant);
        }
        return this;
    }
}
