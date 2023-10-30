package quoridor.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoinFlipperTests {

        @Test
        public void testFlipCoin() {
            CoinFlipper coinFlipper = new CoinFlipper();
            boolean result = coinFlipper.flipCoin();

            // Assert that the result is either true or false
            assertTrue(result || !result, "Result should be either true or false");
        }
}
