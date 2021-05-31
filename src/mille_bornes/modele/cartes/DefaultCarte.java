package mille_bornes.modele.cartes;

import mille_bornes.modele.EtatJoueur;
import mille_bornes.modele.Jeu;

public class DefaultCarte extends Carte {
    public static final Carte DEFAULT = new DefaultCarte("DEFAUT", Categorie.DEFAULT, "images/cartes/Null.jpg");
    public static final Carte VIDE = new DefaultCarte("DEFAUT", Categorie.DEFAULT, "images/cartes/Vide.jpg");

    private DefaultCarte(String nom, Categorie categorie, String imagePath) {
        super(nom, categorie, imagePath);
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException {
        throw new IllegalStateException("Vous ne pouvez pas jouer une carte de Categorie DEFAULT!");
    }

    @Override
    public String nomColore() {
        return nom;
    }
}