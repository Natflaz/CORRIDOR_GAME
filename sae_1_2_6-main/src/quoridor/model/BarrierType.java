package quoridor.model;

/**
 * there exists three barrier types :
 * pot (for when the barrier is not on the board, it is stored in the barrier pot)
 * horizontal (for when the barrier is a horizontal barrier, placed on the board)
 * vertical (for when the barrier is a vertical barrier, placed on the board)
 * preview_horizontal (for when the barrier is a horizontal barrier, and the user is placing it on the board)
 * preview_vertical (for when the barrier is a vertical barrier, and the user is placing it on the board)
 */
public enum BarrierType {
    POT, HORIZONTAL, VERTICAL, PREVIEW_HORIZONTAL, PREVIEW_VERTICAL
}

