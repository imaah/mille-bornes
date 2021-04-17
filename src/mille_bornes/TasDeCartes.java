package mille_bornes;

import mille_bornes.cartes.Borne;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.attaques.*;
import mille_bornes.cartes.bottes.AsDuVolant;
import mille_bornes.cartes.bottes.Citerne;
import mille_bornes.cartes.bottes.Increvable;
import mille_bornes.cartes.bottes.VehiculePrioritaire;
import mille_bornes.cartes.parades.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TasDeCartes {
    private final List<Carte> cartes = new ArrayList<>();


    public TasDeCartes(boolean creerLesCartes) {
        if (creerLesCartes)
            creeLesCartes();
    }

    private void creeLesCartes() {
        // Protection contre les bottes
        cartes.add(new VehiculePrioritaire());
        cartes.add(new Citerne());
        cartes.add(new Increvable());
        cartes.add(new AsDuVolant());

        // Ajout des autres cartes
        int i;
        for (i = 0; i < 14; i++) {
            if (i < 3) {
                cartes.add(new PanneEssence());
                cartes.add(new Crevaison());
                cartes.add(new Accident());
            }

            if (i < 4) {
                cartes.add(new LimiteVitesse());
                cartes.add(new Borne(200));
            }

            if (i < 5) {
                cartes.add(new FeuRouge());
            }

            if (i < 6) {
                cartes.add(new FinDeLimite());
                cartes.add(new Essence());
                cartes.add(new RoueDeSecours());
                cartes.add(new Reparations());
            }

            if (i < 10) {
                cartes.add(new Borne(25));
                cartes.add(new Borne(50));
                cartes.add(new Borne(75));
            }

            if (i < 12) {
                cartes.add(new Borne(100));
            }

            cartes.add(new FeuVert());
        }
    }

    public void melangeCartes() {
        Collections.shuffle(this.cartes);
    }

    public int getNbCartes() {
        return this.cartes.size();
    }

    public boolean estVide() {
        if (this.cartes.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Carte regarde() {
        return this.cartes.get(0);
    }

    public Carte prend() {
        Carte laCarte = this.cartes.get(0);
        this.cartes.remove(0);
        return laCarte;
    }

    public void pose(Carte carte) {
        this.cartes.add(0, carte);
    }
}