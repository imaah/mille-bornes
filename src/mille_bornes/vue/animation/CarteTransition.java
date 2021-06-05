package mille_bornes.vue.animation;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import mille_bornes.vue.jeu.CarteVue;

public class CarteTransition {
    /**
     * Permet de changer la position d'une carte en correspondance avec la destination
     *
     * @param from     La carte de départ
     * @param to       La carte d'arrivée
     * @return La transition générée
     */
    public static TranslateTransition getTranslateAnimation(CarteVue from, CarteVue to) {
        // On transforme les deux cartes en tant que Point2D pour pouvoir travailler plus facilement
        Point2D positionDepart = from.localToScene(from.getX(), from.getY());
        Point2D positionArrivee = to.localToScene(to.getX(), to.getY());
        // On calcule un ratio en fonction des tailles des deux cartes (pour les changements)
        double ratio = to.getRatio() / from.getRatio();

        // Déplacements horizontaux et verticaux + angle
        double dW = from.getWidth() * ratio - from.getWidth();
        double dH = from.getHeight() * ratio - from.getHeight();

        double angle = Math.abs(from.getRotation().angle - to.getRotation().angle);

        // Offsets
        double dX;
        double dY;
        if (ratio != 1) { // Si la carte doit tourner et qu'elle doit changer de taille
            dX = positionDepart.getX() - positionArrivee.getX() - dW / 2;
            dY = positionDepart.getY() - positionArrivee.getY() - dH / 2;
        } else { // Si elle ne doit pas changer de taille...
            if (angle == 0 || angle == 180) { // ...et qu'elle ne doit pas tourner
                dX = positionDepart.getX() - positionArrivee.getX();
                dY = positionDepart.getY() - positionArrivee.getY();
            } else { // ...et qu'elle doit tourner
                // On décale de 13. Ce décalage est artisanal pour pallier à la rotation de la carte
                dX = positionDepart.getX() - positionArrivee.getX() - 13;
                dY = positionDepart.getY() - positionArrivee.getY() + 13;
            }
        }

        // La durée de la transition est proportionnelle à la distance à parcourir
        // Le minimum sert à ne pas avoir de transition trop rapide
        int minimum = 250;
        Duration duration = Duration.millis(minimum + Math.sqrt(dX*dX + dY*dY));
        TranslateTransition translate = new TranslateTransition(duration, from);

        translate.setToX(from.getX() - dX);
        translate.setToY(from.getY() - dY);
        translate.setToZ(-2);

        from.setSurvolActif(false);

        return translate;
    }

    /**
     * Permet de changer la taille d'une carte en correspondance avec la destination
     *
     * @param from     Carte de départ
     * @param to       Carte d'arrivée
     * @param duration Durée de la transformation
     * @return Une ScaleTransition
     */
    public static ScaleTransition getScaleAnimation(CarteVue from, CarteVue to, Duration duration) {
        ScaleTransition scale = new ScaleTransition(duration, from);
        double ratio = to.getRatio() / from.getRatio();

        scale.setToX(ratio);
        scale.setToY(ratio);
        return scale;
    }

    /**
     * Combine les deux transitions précédentes
     *
     * @param from     La carte de départ
     * @param to       La carte d'arrivée
     * @return La transition complète
     */
    public static ParallelTransition getCombinedTransition(CarteVue from, CarteVue to) {
        TranslateTransition transition = getTranslateAnimation(from, to);

        from.setTranslateZ(-3);

        // L'animation complète
        return new ParallelTransition(from,
                transition,
                getRotateAnimation(from, to, transition.getDuration()),
                getScaleAnimation(from, to, transition.getDuration())
        );
    }

    /**
     * Permet de créer l'animation de rotation de la carte
     *
     * @param from     La carte de départ
     * @param to       La carte d'arrivée
     * @param duration La durée de la transition
     * @return La transition complète
     */
    public static RotateTransition getRotateAnimation(CarteVue from, CarteVue to, Duration duration) {
        RotateTransition rotate = new RotateTransition(duration, from);
        rotate.setToAngle(to.getRotation().angle);
        return rotate;
    }

    /**
     * Permet d'avoir l'animation des bornes quand elles sont jouées
     *
     * @param borne    La carte à animer
     * @param duration La durée de l'animation
     * @return L'animation complète
     */
    public static ScaleTransition getBorneAnimation(CarteVue borne, Duration duration) {
        ScaleTransition scale = new ScaleTransition(duration, borne);
        scale.setToX(0);
        scale.setToY(0);
        return scale;
    }
}
