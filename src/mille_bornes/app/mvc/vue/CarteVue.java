package mille_bornes.app.mvc.vue;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import mille_bornes.cartes.Carte;

public class CarteVue extends Rectangle {

    public CarteVue(Carte carte) {
        int size = 140;

        Image image;

        if(carte != null) {
            image = new Image(carte.getImagePath());
        } else {
            image = new Image(getClass().getResource("/assets/cartes/Vide.jpg").toString());
        }
        ImagePattern pattern = new ImagePattern(image);

        setWidth(size * (image.getWidth() / image.getHeight()));
        setHeight(size);
        setArcHeight(10);
        setArcWidth(10);
        setFill(pattern);
    }
}
