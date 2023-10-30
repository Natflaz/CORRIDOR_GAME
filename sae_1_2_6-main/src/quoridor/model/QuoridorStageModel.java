package quoridor.model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

/**
 * A Model for the Quoridor game
 * @see GameStageModel
 */
public class QuoridorStageModel extends GameStageModel { // TODO: make use of Pawn (pion) and barrier

    public final static int STATE_SELECT = 1;
    public final static int STATE_BARRIER = 2;

    public final static int STATE_DEST = 3;
    public final static int BLACK = 0;
    public final static int WHITE = 1;
    private QuoridorBoard board;
    private QuoridorBarrierPot blackBarrierPot;
    private QuoridorBarrierPot whiteBarrierPot;
    private Pawn blackPawn;
    private Pawn whitePawn;
    private Barrier[] blackBarrier;
    private Barrier[] whiteBarrier;
    private TextElement playerName;

    public QuoridorStageModel(String name, Model model) {
        super(name, model);
        state = 1;
        setupCallbacks();
    }

    public QuoridorBoard getBoard() { return board; }

    public void setBoard(QuoridorBoard board) {
        this.board = board;
        addGrid(board);
    }

    public QuoridorBarrierPot getBlackBarrierPot() {
        return blackBarrierPot;
    }

    public void setBlackBarrierPot(QuoridorBarrierPot blackPot) {
        this.blackBarrierPot = blackPot;
        addGrid(blackPot);
    }

    public QuoridorBarrierPot getWhiteBarrierPot() {
        return whiteBarrierPot;
    }

    public void setWhiteBarrierPot(QuoridorBarrierPot whitePot) {
        this.whiteBarrierPot = whitePot;
        addGrid(whitePot);
    }

    public Pawn getBlackPawn() {
        return blackPawn;
    }

    public void setBlackPawn(Pawn blackPawn) {
        this.blackPawn = blackPawn;
        addElement(blackPawn);
    }

    public Pawn getWhitePawn() {
        return whitePawn;
    }


    public void setWhitePawn(Pawn whitePawn) {
        this.whitePawn = whitePawn;
        addElement(whitePawn);
    }

    /**
     * returns all the black barriers.
     * formerly called  getBlackBarrier()
     * @return an array of all the black barriers (10 in standard rules)
     */
    public Barrier[] getBlackBarrierArray() {
        return blackBarrier;
    }
    /**
     * sets all the black barriers.
     * formerly called  setBlackBarrier(Barrier[] blackBarrier)
     * @param blackBarrier an array of all the black barriers (10 in standard rules)
     */
    public void setBlackBarrierArray(Barrier[] blackBarrier) {
        this.blackBarrier = blackBarrier;
        for (int i = 0 ; i < blackBarrier.length ; i++) {
            addElement(blackBarrier[i]);
        }
    }

    /**
     * returns all the white barriers.
     * formerly called  getWhiteBarrier()
     * @return an array of all the white barriers (10 in standard rules)
     */
    public Barrier[] getWhiteBarrierArray() {
        return whiteBarrier;
    }

    /**
     * sets all the white barriers.
     * formerly called  setWhiteBarrier(Barrier[] whiteBarrier)
     * @param whiteBarrier an array of all the white barriers (10 in standard rules)
     */
    public void setWhiteBarrierArray(Barrier[] whiteBarrier) {
        this.whiteBarrier = whiteBarrier;
        for (int i = 0 ; i < whiteBarrier.length ; i++) {
            addElement(whiteBarrier[i]);
        }
    }

    /**
     * If the pot is empty
     * @return true if the pot is empty, false otherwise
     */
    public boolean isPotEmpty(){
        QuoridorBarrierPot pot = model.getIdPlayer() == BLACK ? getBlackBarrierPot() : getWhiteBarrierPot();
        return pot.isEmpty();
    }

    private void setupCallbacks() {
        onSelectionChange(() -> {
            if (selected.size() == 0) {
                board.resetReachableCells(false);
                return;
            }
            if (selected.get(0) instanceof Pawn) {
                Pawn p = (Pawn) selected.get(0);
                board.resetReachableCells(false);
                board.setValidCells(p);
            } else if (selected.get(0) instanceof Barrier) {
                Barrier b = (Barrier) selected.get(0);
                board.resetReachableCells(false);
                board.setValidCells(b);
            }
        });
        /*onPutInGrid((element, gridDest, rowDest, colDest) -> { // TODO: Need
            if (gridDest != board) return;
            if (element instanceof Pawn) {
                Pawn p = (Pawn) element;
                if (p.getColor() == 0) { // black playing

                } else { //white playing

                }
            } else if (element instanceof Barrier) {
                Barrier b = (Barrier) element;
                if (b.getNumber() < 1) {
                    System.out.println("Don't have enougth barrier, retry.");
                } else {
                    if (b.getColor() == 0) { // black playing
                        blackBarrierToPlay--;
                    } else { //white playing
                        whiteBarrierToPlay--;
                    }
                }
            }
        });*/
    }

    public TextElement getPlayerName() {
        return playerName;
    }
    public void setPlayerName(TextElement playerName) {
        this.playerName = playerName;
        addElement(playerName);
    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new QuoridorStageFactory(this);
    }

    public Barrier getBlackBarrierToPlay() {
        return blackBarrierPot.getBarrier();
    }

    public Barrier getWhiteBarrierToPlay() {
        return whiteBarrierPot.getBarrier();
    }
}
