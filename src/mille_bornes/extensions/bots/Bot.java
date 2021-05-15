package mille_bornes.extensions.bots;

import mille_bornes.Joueur;
import mille_bornes.Main;
import mille_bornes.cartes.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Bot extends Joueur {
    private static final long serialVersionUID = 3357922195600973159L;
    protected List<Integer> nCartesRestantes;
    protected transient final Random random = new Random();

    public Bot(String nom) {
        super(nom);
        remplirNCartesRestantes();
    }

    public void remplirNCartesRestantes() {
        this.nCartesRestantes = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7));
    }

    public abstract int choisitCarte();

    @Override
    public Joueur choisitAdversaire(Carte carte) {
        // Tout les joueurs sont pour l'instant des cibles potentielles
        List<Joueur> ciblesPossibles = new ArrayList<>();
        Joueur cible = this;

        while ((cible = cible.getProchainJoueur()) != this) {
            // On ajoute tout les joueurs qui ne sont pas bloqués
            if (! (cible.getBataille() instanceof Attaque) && cible.getBataille() != null) {
                boolean attaquable = true;
                for(Botte botte : cible.getBottes()) {
                    if(botte.contre((Attaque) carte)) {
                        attaquable = false;
                    }
                }

                if(attaquable) {
                    ciblesPossibles.add(cible);
                }
            }
        }

        if (ciblesPossibles.isEmpty()) throw new IllegalStateException();

        // Retour d'un joueur au hasard
        return ciblesPossibles.get(random.nextInt(ciblesPossibles.size()));
    }
}
