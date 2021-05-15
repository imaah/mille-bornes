package mille_bornes;

import mille_bornes.cartes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Bot extends Joueur {
    public Bot(String nom) {
        super(nom);
    }

    public int choisitCarte() {
        // 3 chances sur 10 de défausser, sinon on essaye de jouer une carte au hasard
        if (Math.random() >= 0.3f) {
            return (int) Math.random() * 7 + 1;
        } else {
            return - (int) Math.random() * 7 + 1;
        }
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
        return ciblesPossibles.get((int) Math.random() * ciblesPossibles.size() + 1);
    }
};
