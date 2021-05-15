package mille_bornes;

import mille_bornes.cartes.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Bot extends Joueur {
    private static final long serialVersionUID = 3357922195600973159L;
    private transient final Random random = new Random();
    private List<Integer> nCartesRestantes;

    public Bot(String nom) {
        super(nom);
        remplirNCartesRestantes();
    }

    public void remplirNCartesRestantes() {
        this.nCartesRestantes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    }

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
