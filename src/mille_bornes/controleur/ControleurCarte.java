package mille_bornes.controleur;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.jeu.CarteVue;

public class ControleurCarte {
    private final CarteVue carte;
    private final Controleur controleur;

    public ControleurCarte(CarteVue carte, MilleBornes milleBornes) {
        this.carte = carte;
        this.controleur = new Controleur(milleBornes);
        this.carte.setOnMouseEntered(this::onEnter);
        this.carte.setOnMouseExited(this::onExit);
        this.carte.setOnMouseClicked(this::onClick);
    }

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

    protected void onEnter(MouseEvent event) {
        if (carte.estSurvolActif()) {
            carte.setEffect(new DropShadow(3d, Color.BLACK));
        }
    }

    private void onClick(MouseEvent event) {
        if (carte.estSurvolActif()) {
            if (event.getButton() == MouseButton.PRIMARY) {
                controleur.joueCarte(this.carte.getCarte());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                controleur.defausseCarte(this.carte.getCarte());
            }
        }

    }
}
