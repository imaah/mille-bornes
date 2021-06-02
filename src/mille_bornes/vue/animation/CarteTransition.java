package mille_bornes.vue.animation;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import mille_bornes.vue.jeu.CarteVue;

public class CarteTransition {
    public static TranslateTransition getTranslateAnimation(CarteVue from, CarteVue to, Duration duration) {
        TranslateTransition translate = new TranslateTransition(duration, from);

        from.setViewOrder(Double.MAX_VALUE);
//        to.getParent().toBack();
        from.toFront();

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

        return translate;
    }

    public static ScaleTransition getScaleAnimation(CarteVue from, CarteVue to, Duration duration) {
        ScaleTransition scale = new ScaleTransition(duration, from);
        double ratio = to.getRatio() / from.getRatio();

        scale.setToX(ratio);
        scale.setToY(ratio);
        return scale;
    }

    public static ParallelTransition getCombinedTransition(CarteVue from, CarteVue to, Duration duration) {
        return new ParallelTransition(from,
                getTranslateAnimation(from, to, duration),
                getScaleAnimation(from, to, duration)
        );
    }

}
