package mille_bornes.cartes.attaques;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;

public class LimiteVitesse extends Attaque {
    private static final long serialVersionUID = -6417586415503867598L;

    public LimiteVitesse() {
        super("Limite de vitesse");
    }

    @Override
    public boolean estContreeParFinDeLimite() {
        return true;
    }

    @Override
    public boolean estContreeParVehiculePrioritaire() {
        return true;
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        // Un joueur ne peut avoir qu'une limite de vitesse au maximum
        if(joueur.getLimiteVitesse()) {
            throw new IllegalStateException("Le joueur a déjà une limite de vitesse!");
        }

        // S'il n'en a pas, on lui applique
        joueur.setLimiteVitesse(true);
    }
}
