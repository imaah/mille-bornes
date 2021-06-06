package mille_bornes.vue.joueur;

import mille_bornes.modele.Joueur;
import mille_bornes.vue.MilleBornes;
import mille_bornes.vue.jeu.CarteVue;

/**
 * Permet d'afficher la barre d'un joueur à la verticale.
 * @see JoueurMain
 */
public class VJoueurMain extends JoueurMain {
    /**
     * Permet de créer un joueur vertical
     *
     * @param milleBornes Le milleBornes ou le faire
     * @param joueur Le joueur à traiter
     * @param survolActif Si le survol des cartes doit être actif
     */
    public VJoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif) {
        this(milleBornes, joueur, survolActif, false);
    }

    /**
     * Permet de créer un joueur vertical
     *
     * @param milleBornes Le milleBornes ou le faire
     * @param joueur Le joueur à traiter
     * @param survolActif Si le survol des cartes doit être actif
     * @param invert S'il doit être inversé (à droite au lieu de gauche)
     */
    public VJoueurMain(MilleBornes milleBornes, Joueur joueur, boolean survolActif, boolean invert) {
        super(milleBornes, joueur, survolActif);

        for (CarteVue carte : cartes) {
            carte.tourner(CarteVue.CarteRotation.DEG_90);
        }
        for (CarteVue botte : bottes) {
            botte.tourner(CarteVue.CarteRotation.DEG_90);
        }
        limite.tourner(CarteVue.CarteRotation.DEG_90);
        bataille.tourner(CarteVue.CarteRotation.DEG_90);

        add(limite, 2, 1);
        add(bataille, 2, 0);

        // S'il est inversé, il faut tourner toutes les cartes
        if (invert) {
            addColumn(3, cartes);
            addColumn(1, statusLabel);
            for (CarteVue carte : getBottes()) {
                carte.tourner(CarteVue.CarteRotation.DEG_270);
            }
            limite.tourner(CarteVue.CarteRotation.DEG_270);
            bataille.tourner(CarteVue.CarteRotation.DEG_270);
        } else {
            addColumn(1, cartes);
            addColumn(3, statusLabel);
        }

        // On lui ajoute ses bottes
        for (int i = 0; i < bottes.length; i++) {
            add(bottes[i], 2, 2 + i);
        }

    }
}
