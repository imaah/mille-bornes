package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public class Attaque extends Bataille {
    private static final long serialVersionUID = -8149538162471657341L;

    public Attaque(String nom, String imagePath) {
        super("\u001B[31m" + nom + "\u001B[0m", Categorie.ATTAQUE, imagePath);
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        // On ne peut reçevoir qu'une attaque à la fois
        if(joueur.getBataille() instanceof Attaque) {
            throw new IllegalStateException("Vous ne pouvez pas ajouter d'attaque sur une attaque!");
        }

        // On parcours les bottes du joueurs pour s'assurer qu'il ne possède pas le contre
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
