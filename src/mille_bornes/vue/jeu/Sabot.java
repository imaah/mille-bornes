package mille_bornes.vue.jeu;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.cartes.Carte;
import mille_bornes.modele.cartes.DefaultCarte;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.Updatable;

public class Sabot extends GridPane implements Updatable {
    private final CarteVue defausse;
    private final Label piocheLabel = new Label("Pioche : 0");
    private Jeu jeu;

    public Sabot(Jeu jeu, MilleBornes milleBornes) {
        this.jeu = jeu;
        update();
        defausse = new CarteVue(DefaultCarte.VIDE, milleBornes, false);
        defausse.setAfficherSiNull(false);

        toBack();

        Label piocheHeader = new Label("Pioche");
        Label defausseHeader = new Label("Défausse");

        setVgap(5);
        setHgap(5);

        addRow(0, piocheHeader, defausseHeader);
        CarteVue pioche = new CarteVue(DefaultCarte.DEFAULT, milleBornes, false);
        addRow(1, pioche, defausse);
        addRow(2, piocheLabel);
        piocheLabel.setTextAlignment(TextAlignment.CENTER);
        this.setAlignment(Pos.CENTER);
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
        update();
    }

    @Override
    public void update() {
        if (jeu == null) return;
        Carte carteDefausse = jeu.regardeDefausse();

        if (carteDefausse != null) {
            defausse.changeCarte(carteDefausse);
        }

        piocheLabel.setText(jeu.getNbCartesSabot() + " Cartes");
    }

    public CarteVue getDefausse() {
        return defausse;
    }
}
