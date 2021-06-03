package mille_bornes.vue.timer;

import javafx.concurrent.Task;

public class TimerUtils {
    /**
     * Permet d'attendre un certain nombre de temps (en millisecondes)
     *  @param millis Le nombre de millisecondes à attendre
     * @param action La méthode à exécuter ensuite
     */
    public static void wait(long millis, Action action) {
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException ignored /* parce qu'on s'en branle. */) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> action.run());
        new Thread(sleeper).start();
    }

    /**
     * Obligatoire pour exécuter le lambda ensuite
     */
    @FunctionalInterface
    public interface Action {
        void run();
    }
}
