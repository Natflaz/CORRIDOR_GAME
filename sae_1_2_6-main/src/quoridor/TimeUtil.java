package quoridor;

import javafx.concurrent.Task;

/**
 * This class is used to delay the execution of a task.
 */
public class TimeUtil {
    /**
     * Method to delay the execution of a task.
     * @param millis the time to wait in milliseconds
     * @param continuation the task to execute after the delay
     */
    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try { Thread.sleep(millis); }
                catch (InterruptedException ignored) { }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }
}
