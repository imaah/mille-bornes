package mille_bornes.vue.animation;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Stack;

/**
 * Une classe permettant d'afficher un message à l'écran
 */
public class MessageText extends Text {

    /**
     * Liste des prochaines animations à afficher
     */
    private final Stack<MessageProperties> animationQueue = new Stack<>();
    /**
     * Si l'animation est en train d'être jouée.
     */
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
     *
     * @param text   le texte à afficher
     * @param millis la durée en ms
     */
    public void afficherMessage(final String text, double millis, final Color couleur) {
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

        MessageProperties properties = this.new MessageProperties(text, couleur, parallel);

        translate.setOnFinished(event -> {
            setVisible(false);
            if (animationQueue.isEmpty()) {
                playing = false;
            } else {
                MessageProperties animation;

                // Pour éviter qu'une animation se répète 2 fois d'affilé.
                do {
                    if(animationQueue.isEmpty()) {
                        animation = null;
                        break;
                    }
                    animation = animationQueue.pop();
                } while (animation.equals(properties));

                if (animation == null) {
                    playing = false;
                } else {
                    animation.play();
                }
            }
        });

        setFill(couleur);
        setVisible(true);

        if (!animationQueue.isEmpty() || playing) {
            animationQueue.add(properties);
        } else {
            parallel.play();
            playing = true;
        }
    }

    /**
     * Classe permettant de stocker les propriétés des messages à afficher.
     */
    private class MessageProperties {
        final String texte;
        final Color couleur;
        final Animation animation;

        /**
         * Constructeur de la classe
         * @param texte le texte à afficher
         * @param couleur la couleur du texte
         * @param animation l'animation que doit faire le texte
         */
        public MessageProperties(String texte, Color couleur, Animation animation) {
            this.texte = texte;
            this.couleur = couleur;
            this.animation = animation;
        }
        /**
         * Lance l'animation
         */
        private void play() {
            MessageText.this.setFill(couleur);
            MessageText.this.setText(texte);
            MessageText.this.setVisible(true);
            MessageText.this.playing = true;
            animation.playFromStart();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MessageProperties)) return false;
            MessageProperties that = (MessageProperties) o;
            return Objects.equals(texte, that.texte) && Objects.equals(couleur, that.couleur);
        }
    }
}
