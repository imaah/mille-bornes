package mille_bornes.controleur;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.jeu.CarteVue;

public class ControleurCarte {
    /** La carte à afficher */
    private final CarteVue carte;
    /** Le controleur qu'on va utiliser */
    private final Controleur controleur;


    /**
     * Permet d'instancier l'affichage d'une carte dans un mille bornes précis
     *
     * @param carte La carte à afficher
     * @param milleBornes Le milleBornes dans lequel créer l'affichage
     */
    public ControleurCarte(CarteVue carte, MilleBornes milleBornes) {
        this.carte = carte;
        this.controleur = new Controleur(milleBornes);
        this.carte.setOnMouseEntered(this::onEnter);
        this.carte.setOnMouseExited(this::onExit);
        this.carte.setOnMouseClicked(this::onClick);
    }

    /**
     * Permet de gérer quand la souris quitte le survol de la carte
     *
     * @param event L'event en question
     */
    protected void onExit(MouseEvent event) {
        carte.setEffect(null);
        if (carte.estGrisee()) {
            ColorAdjust adjust = new ColorAdjust();
            adjust.setSaturation(-.9);

            if (carte.getEffect() != null) {
                adjust.setInput(adjust);
            }
            carte.setEffect(adjust);
        }
    }

    /**
     * Permet de gérer quand la souris passe sur la carte
     *
     * @param event L'event en question
     */
    protected void onEnter(MouseEvent event) {
        if (carte.estSurvolActif()) {
            carte.setEffect(new DropShadow(3d, Color.BLACK));
        }
    }

    /**
     * Permet de cliquer sur une carte
     *
     * @param event L'event en question
     */
    private void onClick(MouseEvent event) {
        System.out.printf("carte : %f %f%n", carte.getLayoutX(), carte.getLayoutY());
        if (carte.estSurvolActif()) { // Si on peut la jouer
            if (event.getButton() == MouseButton.PRIMARY) { // Clique droit = jouer
                controleur.joueCarte(this.carte);
            } else if (event.getButton() == MouseButton.SECONDARY) { // Clique gauche = défausser
                controleur.defausseCarte(this.carte);
            }
        }
    }
}
