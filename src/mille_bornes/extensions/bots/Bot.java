package mille_bornes.extensions.bots;

import mille_bornes.Joueur;
import mille_bornes.cartes.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Bot extends Joueur {
    private static final long serialVersionUID = 3357922195600973159L;
    protected List<Integer> nCartesRestantes;

    public Bot(String nom) {
        super(nom);
        remplirNCartesRestantes();
    }

    public void remplirNCartesRestantes() {
        this.nCartesRestantes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    }

    public abstract int choisitCarte();

    public abstract Joueur choisitAdversaire(Carte carte);
}
