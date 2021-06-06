package mille_bornes.modele.cartes;

import mille_bornes.application.Asset;
import mille_bornes.modele.EtatJoueur;
import mille_bornes.modele.Jeu;

public class Attaque extends Bataille {
    public Attaque(String nom, Asset image) {
        super(nom, Categorie.ATTAQUE, image);
    }

    @Override
    public final boolean contre(Attaque attaque) {
        return false;
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        // On ne peut reçevoir qu'une attaque à la fois
        if (joueur.getBataille() instanceof Attaque) {
            throw new IllegalStateException("Vous ne pouvez pas ajouter d'attaque sur une attaque!");
        }

        // On parcours les bottes du joueurs pour s'assurer qu'il ne possède pas le contre
        for (Botte botte : joueur.getBottes()) {
            if (botte.contre(this)) {
                throw new IllegalStateException("Le joueur a une botte qui bloque cette carte!");
            }
        }

        joueur.setBataille(this);
    }

    @Override
    public String nomColore() {
        return "\u001B[31m" + nom + "\u001B[0m";
    }
}
