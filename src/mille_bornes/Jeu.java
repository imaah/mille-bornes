package mille_bornes;

import mille_bornes.cartes.Carte;
import mille_bornes.extensions.bots.Bot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Jeu implements Serializable {
    private static final long serialVersionUID = 7915602017457172577L;

    private final List<Joueur> joueurs = new ArrayList<>();
    private Joueur joueurActif;
    private Joueur prochainJoueur;
    private TasDeCartes sabot;
    private TasDeCartes defausse;

    public Jeu(Joueur... joueurs) {
        if (joueurs.length < 2) throw new IllegalStateException("Il faut au minimum 2 joueurs pour faire une partie.");
        if (joueurs.length > 4) throw new IllegalStateException("Le nombre de joueurs ne peut pas excéder 4.");
        ajouteJoueurs(joueurs);
    }

    public void ajouteJoueurs(Joueur... joueurs) throws IllegalStateException {
        // Si le prochain joueur est déjà défini, la partie est déjà démarrée

        if (joueurs.length + this.joueurs.size() > 4)
            throw new IllegalStateException("Le nombre de joueurs ne peut pas excéder 4.");

        if (prochainJoueur != null) {
            throw new IllegalStateException("La partie a déjà commencé !");
        } else {
            this.joueurs.addAll(Arrays.asList(joueurs));
        }
    }

    public void prepareJeu() {
        // Mélange des joueurs
        Collections.shuffle(this.joueurs);

        // Création du sabot et mélange
        this.sabot = new TasDeCartes(true);
        this.sabot.melangeCartes();

        // Création de la défausse
        this.defausse = new TasDeCartes(false);

        // Don de 6 cartes à chaque joueur
        for (int i = 0; i < 6; i++) {
            for (Joueur joueur : this.joueurs) {
                joueur.prendCarte(this.sabot.prend());
            }
        }

        for (int i = 0; i < joueurs.size(); i++) {
            joueurs.get(i).setProchainJoueur(joueurs.get((i + 1) % joueurs.size()));
        }

        // Le premier joueur commence
        prochainJoueur = joueurs.get(0);
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();

        for (Joueur joueur : this.joueurs) {
            resultat.append(joueur.toString()).append("\n");
        }

        resultat.append("Pioche : ")
                .append(getNbCartesSabot() == 0 ? "vide" : getNbCartesSabot())
                .append("\n")
                .append("Défausse : ")
                .append(this.defausse.getNbCartes() == 0 ? "vide" : this.defausse.getNbCartes())
                .append("\n");

        return resultat.toString();
    }

    public boolean joue() {
        boolean carteJouee;

        activeProchainJoueurEtTireCarte();

        if(estPartieFinie()) {
            return true;
        }

        System.out.println("\n");
        System.out.println(this);
        System.out.println("C'est au tour de " + joueurActif.nom);

        // Affichage d'entre-deux-tours
        System.out.println(joueurActif);

        if(!(joueurActif instanceof Bot)) {
            System.out.print("[");
            for (int i = 0; i < joueurActif.getMain().size(); i++) {
                System.out.print((i + 1) + ": " + joueurActif.getMain().get(i));
                if (i < joueurActif.getMain().size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }

        // Tant que la carte n'a pas pu être jouée, on recommence
        do {
            try {
                int nCarte = this.joueurActif.choisitCarte();
                Carte carte = this.joueurActif.getMain().get(Math.abs(nCarte) - 1);

                if (nCarte > 0) {
                    this.joueurActif.joueCarte(this, nCarte - 1);
                } else if (nCarte < 0) {
                    this.joueurActif.defausseCarte(this, -nCarte - 1);
                } else {
                    throw new IllegalStateException("Entrez un numéro de carte entre 1 et 7 inclus (négatif pour défausser)");
                }

                if(joueurActif instanceof Bot) {
                    System.out.println(joueurActif.nom + " à " + (nCarte < 0 ? "défaussé " : "joué ") + carte);
                }

                carteJouee = true;
            } catch (IllegalStateException e) {
                if(!(joueurActif instanceof Bot)) {
                    System.err.println(e.getMessage());
                }
                carteJouee = false;
            } catch (Exception e) {
                e.printStackTrace();
                carteJouee = false;
            }
        } while (!carteJouee);

        // On ne continue que si la partie n'est pas finie
        return estPartieFinie();
    }

    public void activeProchainJoueurEtTireCarte() {
        if (this.joueurActif instanceof Bot) {
            ((Bot) this.joueurActif).remplirNCartesRestantes();
        }

        this.joueurActif = prochainJoueur;

        // Si le joueur n'a pas 7 cartes (à priori impossible), il en pioche autant qu'il faut
        while (joueurActif.getMain().size() < 7) {
            if (!estPartieFinie()) {
                this.joueurActif.prendCarte(pioche());
            } else {
                break;
            }
        }
        prochainJoueur = joueurActif.getProchainJoueur();
    }

    public boolean estPartieFinie() {
        // Si le sabot n'est pas vide mais qu'il ne contient plus aucune borne, la partie est finie
        if (!this.sabot.estVide()) {
            if (!this.sabot.contientBornes()) {
                return true;
            }
        } else {
            return true;
        }

        // Si n'importe quel joueur à dépassé les 1000km
        for (Joueur joueur : this.joueurs) {
            if (joueur.getKm() == 1000) {
                return true;
            }
        }
        return false;
    }

    public void setProchainJoueur(Joueur prochainJoueurActif) {
        this.prochainJoueur = prochainJoueurActif;
    }

    public Joueur getJoueurActif() {
        return this.joueurActif;
    }

    public List<Joueur> getGagnant() {
        if (!estPartieFinie()) {
            return null;
        } else {
            // On récupère le nombre de km maximal actuel
            int maxKm = joueurs.stream().mapToInt(Joueur::getKm).max().orElse(Integer.MAX_VALUE);
            // On renvoit la liste des joueurs ayant le nom de km max (pour les ex-aequo)
            return joueurs.stream().filter(joueur -> joueur.getKm() == maxKm).collect(Collectors.toList());
        }
    }

    public Carte pioche() {
        return this.sabot.prend();
    }

    public void defausse(Carte carte) {
        this.defausse.pose(carte);
    }

    public int getNbCartesSabot() {
        return this.sabot.getNbCartes();
    }

    public Carte regardeDefausse() {
        return this.defausse.regarde();
    }
}
