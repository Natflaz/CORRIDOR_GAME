package quoridor.control;

import org.mockito.Mockito;
import quoridor.model.Barrier;
import quoridor.model.BarrierType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarrierTransformTests {

    @Test
    public void testGetBarrierType() {
        Barrier barrierMock = Mockito.mock(Barrier.class);
        Mockito.when(barrierMock.getBarrierType()).thenReturn(BarrierType.POT);
        BarrierType expectedType = BarrierType.HORIZONTAL;
        BarrierTransform transform = new BarrierTransform(barrierMock, expectedType);
        BarrierType actualType = transform.getBarrierType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testSetBarrierType() {
        Barrier barrierMock = Mockito.mock(Barrier.class);
        Mockito.when(barrierMock.getBarrierType()).thenReturn(BarrierType.POT);
        BarrierType initialType = BarrierType.HORIZONTAL;
        BarrierType newType = BarrierType.VERTICAL;
        BarrierTransform transform = new BarrierTransform(barrierMock, initialType);
        transform.setBarrierType(newType);
        BarrierType actualType = transform.getBarrierType();
        assertEquals(newType, actualType);
    }

}
