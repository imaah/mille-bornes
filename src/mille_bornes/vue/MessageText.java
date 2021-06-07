package mille_bornes.vue;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Une classe permettant d'afficher un message à l'écran
 */
public class MessageText extends Text {

    private final Queue<Animation> animationQueue = new PriorityQueue<>();
    private boolean playing = false;
    /**
     * Contructeur de MessageText
     */
    public MessageText() {
        super("Lorem Ipsum");
        setVisible(false);
        setFill(Color.WHITE);
        setStyle("-fx-font-size: 30px");
        setTextAlignment(TextAlignment.CENTER);
    }

    /**
     * Affiche un message animé pendant <i>millis</i> ms
     * @param text le texte à afficher
     * @param millis la durée en ms
     */
    public void afficherMessage(String text, double millis, Color color) {
        // On gère le déplacement du message
        Duration duree = Duration.millis(millis);
        TranslateTransition translate = new TranslateTransition(duree, this);
        FadeTransition fade = new FadeTransition(duree.divide(2), this);
        ParallelTransition parallel = new ParallelTransition(fade, translate);

        fade.setFromValue(1.0);
        fade.setToValue(0);
        fade.setDelay(duree.divide(2));

        translate.setFromX(0);
        translate.setFromY(-120);
        translate.setToY(-170);
        setText(text);
        translate.setOnFinished(event ->  {
            setVisible(false);
            Animation animation = animationQueue.poll();
            if(animation != null) {
                animation.play();
            } else {
                playing = false;
            }
        });

        setFill(color);
        setVisible(true);

        if(!playing) {
            parallel.play();
            playing = true;
        }
    }
}
