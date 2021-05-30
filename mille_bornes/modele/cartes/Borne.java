package mille_bornes.modele.cartes;

import com.google.gson.JsonObject;
import mille_bornes.modele.EtatJoueur;
import mille_bornes.modele.Jeu;
import mille_bornes.modele.cartes.attaques.FeuRouge;

import static mille_bornes.modele.Jeu.MAX_VITESSE_SOUS_LIMITE;

public class Borne extends Carte {
    private static final long serialVersionUID = -5391072928856424396L;
    public final int km;


    public Borne(int km, String imagePath) {
        super("\u001B[34m" + km + "km\u001B[0m", Categorie.BORNE, imagePath);
        this.km = km;
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur etatjoueur) {
        if (etatjoueur.getLimiteVitesse() && this.km > MAX_VITESSE_SOUS_LIMITE) {
            // Si le joueur a une limitation de vitesse, il ne peut pas la dépasser
            throw new IllegalStateException(
                    String.format("Vous ne pouvez pas aller au delà de %d km/h !",
                            MAX_VITESSE_SOUS_LIMITE)
            );
        } else if (etatjoueur.getBataille() instanceof FeuRouge) {
            // De même s'il est au feu rouge
            throw new IllegalStateException("Vous ne pouvez pas placer de borne, vous êtes à l'arrêt!");
        } else if(etatjoueur.getKm() + this.km > 1000) {
            // De même si le total avec la carte à jouer fait un total de + de 1000km
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
    public JsonObject sauvegarder() {
        JsonObject json = super.sauvegarder();
        json.addProperty("km", km);
        return json;
    }
}
