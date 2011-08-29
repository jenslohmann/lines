package dk.jlh.games.lines;

enum GameState {
    /** Accepts user input. */
    READY,
    /** Does not accept user input - is occupied. */
    BUSY,
    /** Accepts restricted input (such as "New game"). Game is over. */
    GAME_OVER,
}
