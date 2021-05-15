package mille_bornes;

import mille_bornes.cartes.Carte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Jeu {
    private final List<Joueur> joueurs = new ArrayList<>();
    private Joueur joueurActif;
    private Joueur prochainJoueur;
    private TasDeCartes sabot;
    private TasDeCartes defausse;

    public Jeu() {}

    public Jeu(Joueur... joueurs) {
        ajouteJoueurs(joueurs);
    }

    public void ajouteJoueurs(Joueur... joueurs) throws IllegalStateException {
        // Si le prochain joueur est déjà défini, la partie est déjà démarrée
        if (prochainJoueur != null) {
            throw new IllegalStateException("La partie a déjà commencée!");
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

        for(int i = 0; i < joueurs.size(); i++) {
            joueurs.get(i).setProchainJoueur(joueurs.get((i + 1) % joueurs.size()));
        }

        // Le premier joueur commence (on prévoit que le tour va changer)
        this.joueurActif = this.joueurs.get(joueurs.size() - 1);
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();

        // Ajout de tout les joueurs au résultats
        for (Joueur joueur : this.joueurs) {
            resultat.append(joueur.toString());
        }

        resultat.append("Pioche : ")
                .append(this.sabot.getNbCartes())
                .append(" cartes;")
                .append("Défausse : ")
                .append(this.defausse.getNbCartes() == 0 ? "vide" : this.defausse.getNbCartes());

        return resultat.toString();
    }

    public boolean joue() {
        boolean carteJouee;

        activeProchainJoueurEtTireCarte();

        // Affichage du jeu du joueur
        System.out.println(joueurActif);
        System.out.println(joueurActif.getMain());

        // Tant que la carte n'a pas pu être jouée, on recommence
        do {
            carteJouee = false;

            try {
                int nCarte = this.joueurActif.choisitCarte();
                if(nCarte > 0) {
                    this.joueurActif.joueCarte(this, nCarte - 1);
                } else if(nCarte < 0) {
                    this.joueurActif.defausseCarte(this, -nCarte - 1);
                } else {
                    throw new IllegalStateException("Entrez un numéro de carte entre 1 et 7 inclus (négatif pour défausser)");
                }

                carteJouee = true;
            } catch (IllegalStateException e) {
                System.err.println(e.getMessage());
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

        this.joueurActif = joueurActif.getProchainJoueur();

        while (joueurActif.getMain().size() < 7) {
            if (!estPartieFinie()) {
                this.joueurActif.prendCarte(sabot.prend());
            }
        }
    }

    public boolean estPartieFinie() {
        // Si le sabot n'est pas vide mais qu'il ne contient plus aucune borne, la partie est finie
        if (!this.sabot.estVide()) {
            if (!this.sabot.contientBornes()) {
                return true;
            }
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
        if (this.sabot.getNbCartes() != 0) {
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
