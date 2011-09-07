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
        for (int i = 0; i < data.length(); i++) {
            int occupant = 0;
            switch (data.charAt(i)) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                    occupant = data.charAt(i) - '1';
                    break;
                case ' ':
                default:
                    occupant = 0;
            }
            board.setSpace(i, row, occupant);
        }
        return this;
    }
}
