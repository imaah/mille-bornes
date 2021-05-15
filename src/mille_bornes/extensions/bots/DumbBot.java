package mille_bornes.extensions.bots;

import mille_bornes.Joueur;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Carte;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DumbBot extends Bot {
    private static final long serialVersionUID = 5572469609476004597L;
    protected transient final Random random = new Random();

    public DumbBot(String nom) {
        super(nom);
    }

    @Override
    public int choisitCarte() {
        // Si il reste des cartes non testées
        if (nCartesRestantes.size() != 0) {
            int n = nCartesRestantes.get(random.nextInt(nCartesRestantes.size()));
            nCartesRestantes.remove(Integer.valueOf(n));
            return n;
        } else {
            return -(random.nextInt(7) + 1);
        }
    }

    @Override
    public Joueur choisitAdversaire(Carte carte) {
        // Tout les joueurs sont pour l'instant des cibles potentielles
        List<Joueur> ciblesPossibles = new ArrayList<>();
        Joueur cible = this;

        while ((cible = cible.getProchainJoueur()) != this) {
            // On ajoute tout les joueurs qui ne sont pas bloqués
            if (! (cible.getBataille() instanceof Attaque) && cible.getBataille() != null) {
                ciblesPossibles.add(cible);
            }
        }

        if (ciblesPossibles.isEmpty()) throw new IllegalStateException();

        // Retour d'un joueur au hasard
        return ciblesPossibles.get(random.nextInt(ciblesPossibles.size()));
    }
}
