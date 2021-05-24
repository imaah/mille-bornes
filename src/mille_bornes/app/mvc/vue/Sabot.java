package mille_bornes.app.mvc.vue;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import mille_bornes.Jeu;
import mille_bornes.cartes.Carte;

public class Sabot extends HBox {
    public Sabot(Jeu jeu) {
        super(10);
        CarteVue carte1 = new CarteVue(Carte.DEFAULT);
        CarteVue carte2 = new CarteVue(jeu.regardeDefausse());

        getChildren().addAll(carte1, carte2);
        this.setAlignment(Pos.CENTER);
    }
}
