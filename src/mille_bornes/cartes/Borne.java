package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.attaques.FeuRouge;

import java.util.Objects;

public class Borne extends Carte {
    public final int km;

    public Borne(int km) {
        super(km + "km", Categorie.BORNE);
        this.km = km;
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur etatjoueur) {
        if(etatjoueur.getLimiteVitesse() && this.km > EtatJoueur.MAX_VITESSE_SOUS_LIMITE) {
            throw new IllegalStateException(
                    String.format("Vous ne pouvez pas aller au delà de %d km/h !",
                    EtatJoueur.MAX_VITESSE_SOUS_LIMITE)
            );
        } else if(etatjoueur.getBataille() instanceof FeuRouge) {
            throw new IllegalStateException("Vous ne pouvez pas placer de borne, vous êtes à l'arrêt!");
        } else if(etatjoueur.getKm() + this.km > 1000) {
            throw new IllegalStateException("Vous ne pouvez pas dépasser les 1000 bornes! " +
                    "Vous devez atteindre exactement ce score.");
        }

        etatjoueur.ajouteKm(km);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Borne)) return false;
        if (!super.equals(o)) return false;
        Borne borne = (Borne) o;
        return km == borne.km;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), km);
    }
}
