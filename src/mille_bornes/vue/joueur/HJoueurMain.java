package mille_bornes.vue.joueur;

import javafx.geometry.Pos;
import mille_bornes.modele.Joueur;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.jeu.CarteVue;

public class HJoueurMain extends JoueurMain {
    /**
     * Permet de créer un joueur horizontal
     *
     * @param milleBornes Le milleBornes ou le faire
     * @param joueur Le joueur à traiter
     * @param survolActif Si le survol des cartes doit être actif
     */
    public HJoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif) {
        this(milleBornes, joueur, survolActif, false);
    }

    /**
     * Permet de créer un joueur horizontal
     *
     * @param milleBornes Le milleBornes ou le faire
     * @param joueur Le joueur à traiter
     * @param survolActif Si le survol des cartes doit être actif
     * @param invert S'il doit être inversé (en haut de l'écran)
     */
    public HJoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif, boolean invert) {
        super(milleBornes, joueur, survolActif);

        setAlignment(Pos.CENTER);

        add(limite, 1, 2);
        add(bataille, 0, 2);

        // S'il est inversé, il faut tourner toutes les cartes
        if (invert) {
            addRow(1, cartes);
            addRow(3, statusLabel);
            for (CarteVue carte : getBottes()) {
                carte.tourner(CarteVue.CarteRotation.DEG_180);
            }
            limite.tourner(CarteVue.CarteRotation.DEG_180);
            bataille.tourner(CarteVue.CarteRotation.DEG_180);
        } else {
            addRow(3, cartes);
            addRow(1, statusLabel);
        }

        // On lui ajoute ses bottes
        for (int i = 0; i < bottes.length; i++) {
            add(bottes[i], 2 + i, 2);
        }
    }
}
