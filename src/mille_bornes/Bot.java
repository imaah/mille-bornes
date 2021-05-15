package mille_bornes;

import mille_bornes.cartes.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Bot extends Joueur {
    private Random random = new Random();
    private ArrayList<Integer> nCartesRestantes = new ArrayList<Integer>();

    public Bot(String nom) {
        super(nom);
        remplirNCartesRestantes();
    }

    // Exécuté avant chaque tour, pour s'assurer qu'il puisse jouer n'importe quelle carte
    public void remplirNCartesRestantes() {
        this.nCartesRestantes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    }

    public int choisitCarte() {
        // Si il reste des cartes non testées
        int taille = nCartesRestantes.size();

        if (taille > 0) {
            int n = nCartesRestantes.get(random.nextInt(taille));
            nCartesRestantes.remove(Integer.valueOf(n));

            return n;
        // Sinon on défausse une carte au hasard
        } else {
            return -1;
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
};
