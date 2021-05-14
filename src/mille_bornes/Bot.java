package mille_bornes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Bataille;
import mille_bornes.cartes.Botte;
import mille_bornes.cartes.Carte;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Bot extends Joueur {
    public Bot(String nom) {
        super(nom);
    }

    public void prendCarte(TasDeCartes test) {
        System.out.println("Test");
    }
};
