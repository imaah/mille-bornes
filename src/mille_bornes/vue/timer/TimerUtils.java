package mille_bornes.vue.timer;

import javafx.concurrent.Task;

/**
 * Permet d'avoir des outils personnalisés pour attendre. Utiles pour les animations
 */
public class TimerUtils {
    /**
     * Permet d'attendre un certain nombre de temps (en millisecondes)
     * @param millis Le nombre de millisecondes à attendre
     * @param action La méthode à exécuter ensuite
     * @return La tâche qui va faire attendre
     */
    public static Task<Void> wait(long millis, Action action) {
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
        return sleeper;
    }

    /**
     * Obligatoire pour exécuter le lambda ensuite
     */
    @FunctionalInterface
    public interface Action {
        void run();
    }
}
