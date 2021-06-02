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
     * @param from La carte de départ
     * @param to La carte d'arrivée
     * @param duration La durée de la transition
     * @return La transition générée
     */
    public static TranslateTransition getTranslateAnimation(CarteVue from, CarteVue to, Duration duration) {
        TranslateTransition translate = new TranslateTransition(duration, from);

        System.out.println("AAAAAAA " + from.localToScene(from.getX(), from.getY()));
        Point2D positionDepart = from.localToScene(from.getX(), from.getY());
        Point2D positionArrivee = to.localToScene(to.getX(), to.getY());
        double ratio = to.getRatio() / from.getRatio();

        double dW = from.getWidth() * ratio - from.getWidth();
        double dH = from.getHeight() * ratio - from.getHeight();

        double dX = positionDepart.getX() - positionArrivee.getX() - dW / 2;
        double dY = positionDepart.getY() - positionArrivee.getY() - dH / 2;

        translate.setToX(from.getX() - dX);
        translate.setToY(from.getY() - dY);
        translate.setToZ(-2);

        from.setSurvolActif(false);

        return translate;
    }

    /**
     * Permet de changer la taille d'une carte en correspondance avec la destination
     * @param from Carte de départ
     * @param to Carte d'arrivée
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
     * @param from La carte de départ
     * @param to La carte d'arrivée
     * @param duration La durée de la transition
     * @return La transition complète
     */
    public static ParallelTransition getCombinedTransition(CarteVue from, CarteVue to, Duration duration) {
        return new ParallelTransition(from,
                getTranslateAnimation(from, to, duration),
                getScaleAnimation(from, to, duration)
//                getRotateAnimation(from, to, duration)
        );
    }

    public static RotateTransition getRotateAnimation(CarteVue from, CarteVue to, Duration duration) {
        RotateTransition rotate = new RotateTransition(duration, from);

        rotate.setToAngle(to.getRotation().angle);

        return rotate;
    }

}
