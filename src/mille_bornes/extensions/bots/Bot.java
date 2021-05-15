package mille_bornes.extensions.bots;

import mille_bornes.Joueur;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Botte;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.attaques.LimiteVitesse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class Bot extends Joueur {
    private static final long serialVersionUID = 3357922195600973159L;
    protected transient Random random = new Random();
    protected List<Integer> nCartesRestantes;

    public Bot(String nom) {
        super(nom);
        remplirNCartesRestantes();
    }

    public void remplirNCartesRestantes() {
        this.nCartesRestantes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    }

    public abstract int choisitCarte();

    @Override
    public Joueur choisitAdversaire(Carte carte) {
        if(random == null) random = new Random();
        // Tout les joueurs sont pour l'instant des cibles potentielles
        List<Joueur> ciblesPossibles = new ArrayList<>();
        Joueur cible = this;

        while ((cible = cible.getProchainJoueur()) != this) {
            // On ajoute tout les joueurs qui ne sont pas bloqués
            if (!(cible.getBataille() instanceof Attaque) && cible.getBataille() != null) {
                boolean attaquable = true;
                for (Botte botte : cible.getBottes()) {
                    if (botte.contre((Attaque) carte)) {
                        attaquable = false;
                    }
                }

                if(carte instanceof LimiteVitesse && cible.getLimiteVitesse()) {
                    attaquable = false;
                }

                if (attaquable) {
                    ciblesPossibles.add(cible);
                }
            }
        }

        if (ciblesPossibles.isEmpty()) throw new IllegalStateException();

        // Retour d'un joueur au hasard
        return ciblesPossibles.get(random.nextInt(ciblesPossibles.size()));
    }
}
