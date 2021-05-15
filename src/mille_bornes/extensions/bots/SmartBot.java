package mille_bornes.extensions.bots;

import mille_bornes.Joueur;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Borne;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.Parade;
import mille_bornes.cartes.bottes.VehiculePrioritaire;
import mille_bornes.cartes.parades.FeuVert;

import java.util.Random;

public class SmartBot extends Bot {
    private static final long serialVersionUID = -2895798750732205816L;
    private final Random random = new Random();

    public SmartBot(String nom) {
        super(nom);
    }

    @Override
    public int choisitCarte() {
        int carteAJouer;
        if (getBataille() == null) { // n'a pas demarré

            if ((carteAJouer = trouveCarteDeType(VehiculePrioritaire.class)) != -1
                || (carteAJouer = trouveCarteDeType(FeuVert.class)) != -1) { // Si le joueur a de quoi demarrer alors on l'utilise.
                return carteAJouer;
            }

//            if((carteAJouer = trouveCarteDeType(Attaque.class)) != -1) { // Si le joueur n'a pas de quoi démarrer alors il attaque.
//                return carteAJouer;
//            }
        }

        if (getBataille() instanceof Attaque) {
            Attaque attaque = (Attaque) getBataille();

            if ((carteAJouer = trouveContre(attaque)) != -1) {
                return carteAJouer;
            }
        }

        return -random.nextInt(7); // si rien n'est bon alors on defausse une carte aléatoire.
    }

    @Override
    public Joueur choisitAdversaire(Carte carte) {
        return null;
    }

    private int trouveCarteDeType(Class<? extends Carte> carteClass) {
        for (int i = 0; i < getMain().size(); i++) {
            Carte carte = getMain().get(i);

            if (carteClass.isAssignableFrom(carte.getClass())) {
                return i;
            }
        }

        return -1;
    }

    private int trouveMinBorne() {
        Borne borne = null;
        int index = -1;

        for (int i = 0; i < getMain().size(); i++) {
            Carte carte = getMain().get(i);

            if (carte instanceof Borne) {
                if (borne == null || borne.km < ((Borne) carte).km) {
                    borne = (Borne) carte;
                    index = i;
                }
            }
        }

        return index;
    }

    private int trouveMaxBorne(int max) {
        Borne borne = null;
        int index = -1;

        for (int i = 0; i < getMain().size(); i++) {
            Carte carte = getMain().get(i);

            if (carte instanceof Borne) {
                if (borne == null || (max >= borne.km && borne.km > ((Borne) carte).km)) {
                    borne = (Borne) carte;
                    index = i;
                }
            }
        }

        return index;
    }

    private int trouveContre(Attaque attaque) {
        for (int i = 0; i < getMain().size(); i++) {
            Carte carte = getMain().get(i);

            if (carte instanceof Parade) {
                if (((Parade) carte).contre(attaque)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
