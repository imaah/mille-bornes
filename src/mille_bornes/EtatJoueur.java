package mille_bornes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Bataille;
import mille_bornes.cartes.Botte;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.bottes.AsDuVolant;
import mille_bornes.cartes.bottes.Citerne;
import mille_bornes.cartes.bottes.Increvable;
import mille_bornes.cartes.bottes.VehiculePrioritaire;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class EtatJoueur {
    public static final int MAX_VITESSE_SOUS_LIMITE = 50;

    private final Joueur joueur;
    private final Stack<Bataille> pileBataille = new Stack<>();
    private final List<Carte> main = new LinkedList<>();
    private final List<Botte> bottes = new LinkedList<>();
    private int km = 0;
    private boolean limiteVitesse;

    public EtatJoueur(@NotNull Joueur joueur) {
        this.joueur = joueur;
    }

    public int getKm() {
        return km;
    }

    public void ajouteKm(int km) {
        this.km += km;
    }

    public String ditPourquoiPeutPasAvancer() {
        return "";
        // TODO: 17/04/2021 direPourquoiPeutPasAvancer()
    }

    public boolean getLimiteVitesse() {
        return limiteVitesse;
    }

    public void setLimiteVitesse(boolean limiteVitesse) {
        this.limiteVitesse = limiteVitesse;
    }

    public Bataille getBataille() {
        if (pileBataille.isEmpty()) return null;
        return pileBataille.peek();
    }

    public void setBataille(Bataille bataille) {
        pileBataille.push(bataille);
    }

    public void defausseBataille(@NotNull Jeu jeu) {
        Bataille bataille = pileBataille.pop();
        jeu.defausse(bataille);
        // TODO: 17/04/2021 Defausser la carte "bataille" avec la méthode jeu.defausse(bataille);
    }

    public List<Carte> getMain() {
        return Collections.unmodifiableList(main);
    }

    public void addBotte(@NotNull Botte botte) {
        bottes.add(botte);
    }

    public void attaque(@NotNull Jeu jeu, @NotNull Attaque attaque) {
        for (Carte carte : main) {
            if (carte instanceof Botte) {
                Botte botte = (Botte) carte;

                if (botte.contre(attaque)) {
                    // Coup-fourré
                    // TODO: 17/04/2021 Meilleur message lors du coup-fourré
                    System.out.println("Coup-fourré!");
                    addBotte(botte);
                    return;
                }
            }
        }

        for (Botte botte : bottes) {
            // TODO: 17/04/2021 Ajouter un message pour l'erreur.
            if (botte.contre(attaque)) throw new IllegalStateException("");
        }

        setBataille(attaque);
    }

    public void prendCarte(Carte carte) {
        main.add(carte);
    }

    public void defausseCarte(@NotNull Jeu jeu, @Range(from = 0, to = 6) int i) {
        // TODO: 17/04/2021 Retirer la carte, utiliser la methode jeu.defausser();
        Carte carte = main.get(i);
        jeu.defausse(carte);
    }

    public void joueCarte(@NotNull Jeu jeu, @Range(from = 0, to = 6) int i) {
        Carte carte = main.get(i);
        if (carte instanceof Attaque) throw new IllegalStateException("Vous ne pouvez pas vous attaquer vous-même!");

        carte.appliqueEffet(jeu, this);
    }

    public void joueCarte(@NotNull Jeu jeu, @Range(from = 0, to = 6) int i, @NotNull Joueur joueur) {
        Carte carte = main.get(i);

        if (carte instanceof Attaque) {
            Attaque attaque = (Attaque) carte;
            joueur.attaque(jeu, attaque);
        }

        throw new IllegalStateException("La carte n'est pas une attaque, donc ne peut pas être utilisée sur un autre joueur!");
    }

    public List<Botte> getBottes() {
        return Collections.unmodifiableList(bottes);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(km)
                .append("km ");

        if (getLimiteVitesse()) {
            builder.append("(50) ");
        }

        builder.append('[')
                .append(bottes.contains(new AsDuVolant()) ? 'A' : '.')
                .append(bottes.contains(new Citerne()) ? 'C' : '.')
                .append(bottes.contains(new Increvable()) ? 'I' : '.')
                .append(bottes.contains(new VehiculePrioritaire()) ? 'V' : '.')
                .append(']');

        builder.append(", ")
                .append(getBataille().nom);

        return builder.toString();
    }
}