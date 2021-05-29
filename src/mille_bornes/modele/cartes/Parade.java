package mille_bornes.modele.cartes;

import mille_bornes.modele.EtatJoueur;
import mille_bornes.modele.Jeu;

public abstract class Parade extends Bataille {
    private static final long serialVersionUID = -1307910562516914625L;

    public Parade(String nom, String imagePath) {
        super("\u001B[92m" + nom + "\u001B[0m", Categorie.PARADE, imagePath);
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        Bataille bataille = joueur.getBataille();

        // Si le joueur a bien une attaque
        if (bataille instanceof Attaque) {
            if (contre((Attaque) bataille)) {
                joueur.defausseBataille(jeu);
            } else {
                // Sinon on ne peut pas la parer
                throw new IllegalStateException("Cette carte ne peut pas être jouée maintenant");
            }
        } else {
            // On a rien à parer
            throw new IllegalStateException("Cette carte ne peut pas être jouée maintenant");
        }
        joueur.setBataille(this);
    }
}
