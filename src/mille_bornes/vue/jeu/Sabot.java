package mille_bornes.vue.jeu;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.cartes.Carte;
import mille_bornes.modele.cartes.DefaultCarte;
import mille_bornes.vue.Updatable;

public class Sabot extends GridPane implements Updatable {
    private final CarteVue pioche = new CarteVue(DefaultCarte.DEFAULT, false);
    private final CarteVue defausse = new CarteVue(null, false);
    private final Label piocheLabel = new Label("Pioche : 0");
    private final Jeu jeu;

    public Sabot(Jeu jeu) {
        this.jeu = jeu;
        update();
        defausse.setAfficherSiNull(true);
        setGridLinesVisible(true);
        Label piocheHeader = new Label("Pioche");
        Label defausseHeader = new Label("Défausse");

        addRow(0, piocheHeader, defausseHeader);
        addRow(1, pioche, defausse);
        addRow(2, piocheLabel);
        piocheLabel.setTextAlignment(TextAlignment.CENTER);
        this.setAlignment(Pos.CENTER);
    }

    @Override
    public void update() {
        Carte carteDefausse = jeu.regardeDefausse();

        if (carteDefausse != null) {
            defausse.changeCarte(carteDefausse);
        }

        piocheLabel.setText(jeu.getNbCartesSabot() + " Cartes");
    }
}
