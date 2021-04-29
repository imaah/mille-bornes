package mille_bornes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Bataille;
import mille_bornes.cartes.Botte;
import mille_bornes.cartes.Carte;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Joueur {
    public final String nom;
    private final EtatJoueur etat;
    private final Scanner input;
    private Joueur prochainJoueur;

    public Joueur(String nom) {
        this.nom = nom;
        this.etat = new EtatJoueur(this);
        this.input = new Scanner(System.in);
    }

    public Joueur getProchainJoueur() {
        return prochainJoueur;
    }

    public void setProchainJoueur(Joueur joueur) {
        prochainJoueur = joueur;
    }

    public List<Carte> getMain() {
        return etat.getMain();
    }

    public int getKm() {
        return etat.getKm();
    }

    public boolean getLimiteVitesse() {
        return etat.getLimiteVitesse();
    }

    public int choisitCarte() {
        boolean correcte;
        int valeur = 0;

        do {
            try {
                valeur = input.nextInt();
                correcte = true;
            } catch (NoSuchElementException e) {
                // TODO: 17/04/2021 Afficher le bon message
                System.out.println("Err");
                correcte = false;
            }
        } while (!correcte);

        return valeur;
    }

    public Joueur choisitAdversaire(Carte carte) {
        Joueur cible = null;
        boolean estValide = false;;

        while(!estValide) {
            String nomDuJoueur = input.nextLine();

            if(nomDuJoueur.equalsIgnoreCase("annuler")) throw new IllegalStateException();

            cible = getProchainJoueur();

            while (!cible.nom.equalsIgnoreCase(nomDuJoueur) && !cible.equals(this)) {
                cible = cible.getProchainJoueur();
            }

            estValide = !cible.equals(this);
        }

        return cible;
    }

    public void prendCarte(Carte carte) {
        etat.prendCarte(carte);
    }

    public void joueCarte(Jeu jeu, int i) {
        etat.joueCarte(jeu, i);
    }

    public void joueCarte(Jeu jeu, int i, Joueur joueur) {
        etat.joueCarte(jeu, i, joueur);
    }

    public void defausseCarte(Jeu jeu, int i) {
        etat.defausseCarte(jeu, i);
    }

    public void attaque(Jeu jeu, Attaque attaque) {
        etat.attaque(jeu, attaque);
    }

    public Bataille getBataille() {
        return etat.getBataille();
    }

    public String ditPourquoiPeutPasAvancer() {
        return etat.ditPourquoiPeutPasAvancer();
    }

    public String toString() {
        return nom + " : " + etat.toString();
    }

    public List<Botte> getBottes() {
        return etat.getBottes();
    }
};
