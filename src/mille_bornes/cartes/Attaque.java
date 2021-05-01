package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public class Attaque extends Bataille {
    public Attaque(String nom) {
        super(nom, Categorie.ATTAQUE);
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        if(joueur.getBataille() instanceof Attaque) {
            throw new IllegalStateException("Vous ne pouvez pas ajouter d'attaque sur une attaque!");
        }

        for(Botte botte : joueur.getBottes()) {
            if(botte.contre(this)) {
                throw new IllegalStateException("Le joueur a une botte qui bloque cette carte!");
            }
        }

        joueur.setBataille(this);
    }

    @Override
    public final boolean contre(Attaque attaque) {
        return false;
    }
}
