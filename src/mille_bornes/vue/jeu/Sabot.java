package mille_bornes.vue.jeu;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.cartes.Carte;
import mille_bornes.modele.cartes.DefaultCarte;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.Updatable;

import java.util.Objects;

public class Sabot extends GridPane implements Updatable {
    /* La carte sur le haut de la defausse */
    private final CarteVue defausse;
    /* Le label sous le tas de picoche */
    private final Label piocheLabel = new Label("Pioche : 0");
    /* Le jeu en cours */
    private Jeu jeu;


    /**
     * Permet d'initialiser le sabot avec le jeu et la partie en cours
     *
     * @param jeu         Le jeu en cours
     * @param milleBornes L'instance de mille bornes à utiliser
     */
    public Sabot(Jeu jeu, MilleBornes milleBornes) {
        this.jeu = jeu;
        update();
        defausse = new CarteVue(DefaultCarte.VIDE, milleBornes, -1, false);
        defausse.setAfficherSiNull(false);

        Label piocheHeader = new Label("Pioche");
        Label defausseHeader = new Label("Défausse");

        piocheHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 17px");
        defausseHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 17px");

        centrer(piocheLabel, piocheHeader, defausseHeader);

        setVgap(5);
        setHgap(5);

        // On ajoute nos nodes à l'interface et on personnalise
        addRow(0, piocheHeader, defausseHeader);
        CarteVue pioche = new CarteVue(DefaultCarte.DEFAULT, milleBornes, -1, false);
        addRow(1, pioche, defausse);
        addRow(2, piocheLabel);
        piocheLabel.setTextAlignment(TextAlignment.CENTER);
        this.setAlignment(Pos.CENTER);
        update();
    }

    private void centrer(Node... nodes) {
        for (Node node : nodes) {
            GridPane.setValignment(node, VPos.CENTER);
            GridPane.setHalignment(node, HPos.CENTER);
        }
    }

    /**
     * Permet de set un jeu en particulier, même s'il est commencé
     *
     * @param jeu Le jeu à définir
     */
    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
        update();
    }

    /**
     * Permet de rafraichir la fenêtre en retirant puis ajoutant la même carte dans la defausse. Actualise le nombre de
     * cartes restantes dans la pioche
     */
    @Override
    public void update() {
        if (jeu == null) return;
        Carte carteDefausse = jeu.regardeDefausse();

        defausse.changeCarte(Objects.requireNonNullElse(carteDefausse, DefaultCarte.VIDE));

        piocheLabel.setText(jeu.getNbCartesSabot() + " Cartes");
    }

    /**
     * Retourne la défausse
     *
     * @return La défausse
     */
    public CarteVue getDefausse() {
        return defausse;
    }
}
