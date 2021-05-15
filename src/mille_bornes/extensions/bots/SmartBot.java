package mille_bornes.extensions.bots;

import mille_bornes.EtatJoueur;
import mille_bornes.cartes.*;
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

            if ((carteAJouer = trouveCarteDeType(Attaque.class)) != -1) { // Si le joueur n'a pas de quoi démarrer alors il attaque.
                return carteAJouer;
            }

            return defausseCarte();
        }

        if (getBataille() instanceof Attaque) {
            Attaque attaque = (Attaque) getBataille();

            if ((carteAJouer = trouveContre(attaque)) != -1) {
                return carteAJouer;
            }

            if ((carteAJouer = trouveCarteDeType(Attaque.class)) != -1) {
                return carteAJouer;
            }

            return defausseCarte();
        }

        // S'il n'y a rien avancer ...
        if (getLimiteVitesse()) {
            if ((carteAJouer = trouveMaxBorne(EtatJoueur.MAX_VITESSE_SOUS_LIMITE)) != -1) {
                return carteAJouer;
            }
        } else {
            if ((carteAJouer = trouveMaxBorne(200)) != -1) {
                return carteAJouer;
            }
        }

        // si on peut pas avancer alors attaquer
        if ((carteAJouer = trouveCarteDeType(Attaque.class)) != -1) {
            return carteAJouer;
        }

        // si on peut rien faire alors on defausse une carte
        return defausseCarte();
    }

    private int defausseCarte() {
        int carteADefausser;

        if ((carteADefausser = trouveMinBorne(75, false)) != -1) {
            return -carteADefausser;
        }

        if ((carteADefausser = trouveCarteDeType(Attaque.class, false)) != -1) {
            return -carteADefausser;
        }

        Carte carte;

        do {
            carteADefausser = random.nextInt(getMain().size());
            carte = getMain().get(carteADefausser);
        } while (carte instanceof Botte);

        return -carteADefausser;
    }

    private int trouveCarteDeType(Class<? extends Carte> carteClass) {
        return trouveCarteDeType(carteClass, true);
    }

    private int trouveCarteDeType(Class<? extends Carte> carteClass, boolean check) {
        for (int i = 0; i < getMain().size(); i++) {
            Carte carte = getMain().get(i);

            if (carteClass.isAssignableFrom(carte.getClass()) && checkCartesPossible(i, check)) {
                return i;
            }
        }

        return -1;
    }

    private boolean checkCartesPossible(int carteN, boolean check) {
        if (check && nCartesRestantes.contains(carteN)) {
            return true;
        }
        return !check;
    }

    private int trouveMinBorne() {
        return trouveMinBorne(Integer.MAX_VALUE, true);
    }

    private int trouveMinBorne(int max) {
        return trouveMinBorne(max, true);
    }

    private int trouveMinBorne(int max, boolean check) {
        Borne borne = null;
        int index = -1;

        for (int i = 0; i < getMain().size(); i++) {
            Carte carte = getMain().get(i);

            if (carte instanceof Borne && checkCartesPossible(i, check)) {

                if (borne == null || (borne.km < ((Borne) carte).km && ((Borne) carte).km <= max)) {
                    borne = (Borne) carte;
                    index = i;
                }
            }
        }

        return index;
    }

    private int trouveMaxBorne(int max) {
        return trouveMaxBorne(max, true);
    }

    private int trouveMaxBorne(int max, boolean check) {
        Borne borne = null;
        int index = -1;

        for (int i = 0; i < getMain().size(); i++) {
            Carte carte = getMain().get(i);

            if (carte instanceof Borne && checkCartesPossible(i, check)) {
                if (borne == null || (max >= ((Borne) carte).km && ((Borne) carte).km < borne.km)) {
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

            if (carte instanceof Parade && checkCartesPossible(i, true)) {
                if (((Parade) carte).contre(attaque)) {
                    return i;
                }
            }

            if (carte instanceof Botte && checkCartesPossible(i, true)) {
                if (((Botte) carte).contre(attaque)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
