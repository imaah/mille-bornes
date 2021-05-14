package mille_bornes;

import com.google.gson.annotations.Expose;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Bataille;
import mille_bornes.cartes.Botte;
import mille_bornes.cartes.Carte;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Joueur {
    @Expose
    public final String nom;
    @Expose
    private final EtatJoueur etat;
    @Expose(serialize = false, deserialize = false)
    private final Scanner input;
    @Expose(serialize = false, deserialize = false)
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
        boolean correcte = false;
        int valeur = 0;

        do {
            try {
                String line = input.nextLine();

                valeur = Integer.parseInt(line);
                if ((-7 <= valeur) && (valeur <= 7) && valeur != 0) {
                    correcte = true;
                }
            } catch (NoSuchElementException e) {
                System.err.println("La valeur que vous avez entrée n'est pas correcte. Choisissez une carte avec son numéro (-7 <= numéro < 0 pour la défausse, 0 < numéro <= 7 pour jouer)");
                correcte = false;
            } catch (NumberFormatException e) {
                System.err.println("Veuillez entrer un entier valide !");
                correcte = false;
            }
        } while (!correcte);

        return valeur;
    }

    public Joueur choisitAdversaire(Carte carte) {
        Joueur cible = null;
        boolean estValide = false;

        while (!estValide) {
            String nomDuJoueur = input.nextLine().trim();

            if (nomDuJoueur.equalsIgnoreCase("annuler")) throw new IllegalStateException();

            cible = getProchainJoueur();

            while (!cible.nom.equalsIgnoreCase(nomDuJoueur) && !cible.equals(this)) {
                cible = cible.getProchainJoueur();
            }

            // TODO: 01/05/2021 Check attaques
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
