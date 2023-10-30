package quoridor.control;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The CoinFlipper class represents a utility class for flipping a coin and getting a random result.
 * It provides a method to flip a coin and obtain a random boolean value.
 *
 */
public class CoinFlipper {

    /**
     * Flips a coin and randomly returns either true or false.
     * The result is determined using ThreadLocalRandom.current().nextBoolean().
     *
     * @return true if the coin flip lands on tails, false if it lands on heads
     */
    public boolean flipCoin() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
