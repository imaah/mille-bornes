package mille_bornes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Bataille;
import mille_bornes.cartes.Botte;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.attaques.LimiteVitesse;
import mille_bornes.cartes.bottes.AsDuVolant;
import mille_bornes.cartes.bottes.Citerne;
import mille_bornes.cartes.bottes.Increvable;
import mille_bornes.cartes.bottes.VehiculePrioritaire;

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

    public EtatJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public int getKm() {
        return km;
    }

    public void ajouteKm(int km) {
        String raison = ditPourquoiPeutPasAvancer();

        if(limiteVitesse && km > MAX_VITESSE_SOUS_LIMITE) {
            throw new IllegalStateException("Vous ne pouvez pas vous déplacer a de plus de " + MAX_VITESSE_SOUS_LIMITE + "km");
        } else if (raison != null) {
            throw new IllegalStateException(raison);
        }

        this.km += km;
    }

    public String ditPourquoiPeutPasAvancer() {
        String message = null;

        if(pileBataille.isEmpty() && !bottes.contains(new VehiculePrioritaire())) {
            message = "Vous n'avez pas encore démarré.";
        }

        if(getBataille() instanceof Attaque) {
            message = "Vous êtes en train de vous faire attaquer !";
        }

        return message;
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

    public void defausseBataille(Jeu jeu) {
        Bataille bataille = pileBataille.pop();
        jeu.defausse(bataille);
    }

    public List<Carte> getMain() {
        return Collections.unmodifiableList(main);
    }

    public void addBotte(Botte botte) {
        bottes.add(botte);
        if(getBataille() != null && getBataille() instanceof Attaque) {
            if(botte.contre((Attaque) getBataille())) {
                pileBataille.pop();
            }
        }
    }

    public void attaque(Jeu jeu, Attaque attaque) {
        for (Carte carte : main) {
            if (carte instanceof Botte) {
                Botte botte = (Botte) carte;

                if (botte.contre(attaque)) {
                    // Coup-fourré
                    System.out.println("Votre adversaire sort un coup-fourré! Votre attaque n'a aucun effet et il récupère la main.");
                    botte.appliqueEffet(jeu, this);
                    jeu.setProchainJoueur(this.joueur);
                    jeu.defausse(attaque);
                    return;
                }
            }
        }

        for (Botte botte : bottes) {
            if (botte.contre(attaque)) throw new IllegalStateException("Ce joueur possède une botte contre cette attaque! Choisissez une autre cible.");
        }

        if(getBataille() instanceof Attaque && !(attaque instanceof LimiteVitesse)) {
            // TODO: 01/05/2021 Message d'erreur : peut pas attaquer car déjà attaque en haut.
            throw new IllegalStateException("");
        }

        if(attaque instanceof LimiteVitesse && limiteVitesse) {
            // TODO: 01/05/2021 Message d'erreur : Attaque avec Limite de vitesse
            throw new IllegalStateException("");
        }

        attaque.appliqueEffet(jeu, this);
    }

    public void prendCarte(Carte carte) {
        main.add(carte);
    }

    public void defausseCarte(Jeu jeu, int i) {
        // TODO: 17/04/2021 Retirer la carte, utiliser la methode jeu.defausser();
        Carte carte = main.get(i);
        main.remove(i);
        jeu.defausse(carte);
    }

    public void joueCarte(Jeu jeu, int i) {
        Carte carte = main.get(i);
        if (carte instanceof Attaque) {
            Joueur cible = joueur.choisitAdversaire(carte);
            joueCarte(jeu, i, cible);
        } else {
            carte.appliqueEffet(jeu, this);
        }

        // Une fois jouée, on retire la carte de la main
        main.remove(i);
    }

    public void joueCarte(Jeu jeu, int i, Joueur joueur) {
        Carte carte = main.get(i);

        if (carte instanceof Attaque) {
            Attaque attaque = (Attaque) carte;
            joueur.attaque(jeu, attaque);
        } else {
            throw new IllegalStateException("La carte n'est pas une attaque, donc ne peut pas être utilisée sur un autre joueur!");
        }
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

        if(getBataille() != null) {
            builder.append(", ")
                    .append(getBataille().nom);
        }

        return builder.toString();
    }
}