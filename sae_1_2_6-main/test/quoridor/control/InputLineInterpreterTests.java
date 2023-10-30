package quoridor.control;

import boardifier.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import quoridor.model.Pawn;
import quoridor.model.QuoridorBoard;
import quoridor.model.QuoridorStageModel;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class InputLineInterpreterTests {
    private static InputLineInterpreter inputLineInterpreter;
    private static Model model;
    private static QuoridorStageModel quoridorStageModel;
    private static MovesVerifier movesVerifier;
    private static Pawn pawn;
    private static QuoridorBoard board;

    @BeforeAll
    static void Setup() {
        model = mock(Model.class);
        movesVerifier = mock(MovesVerifier.class);
        quoridorStageModel = mock(QuoridorStageModel.class);
        pawn = mock(Pawn.class);
        board = mock(QuoridorBoard.class);
        inputLineInterpreter = new InputLineInterpreter(model, movesVerifier);

    }

    @Test
    public void playerSouthWestWhenNoJump() {
        when(model.getIdPlayer()).thenReturn(1);
        when(quoridorStageModel.getWhitePawn()).thenReturn(pawn);
        when(quoridorStageModel.getBoard()).thenReturn(board);
        try {
            inputLineInterpreter.interpret("psw");
            Assertions.fail();
        } catch (Exception e) {}


    }
}
