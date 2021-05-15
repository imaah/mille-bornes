package mille_bornes;

import mille_bornes.cartes.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Bot extends Joueur {
    private Random random = new Random();
    private List<Integer> nCartesRestantes;

    public Bot(String nom) {
        super(nom);
    }

    public void remplirNCartesRestantes() {
        this.nCartesRestantes = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
    }
    
    public int choisitCarte() {
        int n = nCartesRestantes.get(random.nextInt(nCartesRestantes.size()));
        nCartesRestantes = nCartesRestantes.stream().filter(i -> i != n).collect(Collectors.toList());

        return n;
    }

    public Joueur choisitAdversaire(Carte carte) {
        // Tout les joueurs sont pour l'instant des cibles potentielles
        List<Joueur> ciblesPossibles = new ArrayList<>();
        Joueur cible = null;

        while ((cible = getProchainJoueur()) != this) {
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
