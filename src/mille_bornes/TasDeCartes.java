package mille_bornes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mille_bornes.cartes.Borne;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.attaques.*;
import mille_bornes.cartes.bottes.AsDuVolant;
import mille_bornes.cartes.bottes.Citerne;
import mille_bornes.cartes.bottes.Increvable;
import mille_bornes.cartes.bottes.VehiculePrioritaire;
import mille_bornes.cartes.parades.*;
import mille_bornes.extensions.sauvegarde.Sauvegardable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TasDeCartes implements Serializable, Sauvegardable {
    private static final long serialVersionUID = 5828284262241991397L;
    private final List<Carte> cartes = new ArrayList<>();

    public TasDeCartes(boolean creerLesCartes) {
        if (creerLesCartes) {
            creeLesCartes();
        }
    }

    public TasDeCartes(JsonObject json) {
        if(json.has("cartes")) {
            JsonArray ca = json.getAsJsonArray("cartes");
            ca.forEach(element -> cartes.add(Carte.deserialize(element.getAsJsonObject())));
        } else {
            throw new IllegalArgumentException("Propriétés manquantes dans l'objet json.");
        }
    }

    private void creeLesCartes() {
        // Protection contre les bottes
        cartes.add(new Citerne());
        cartes.add(new Increvable());
        cartes.add(new AsDuVolant());
        cartes.add(new VehiculePrioritaire());

        // Ajout des autres cartes
        int i;
        for (i = 0; i < 14; i++) {
            if (i < 3) {
                cartes.add(new Accident());
                cartes.add(new Crevaison());
                cartes.add(new PanneEssence());
            }

            if (i < 4) {
                cartes.add(new Borne(200, "assets/cartes/Speed200.jpg"));
                cartes.add(new LimiteVitesse());
            }

            if (i < 5) {
                cartes.add(new FeuRouge());
            }

            if (i < 6) {
                cartes.add(new Essence());
                cartes.add(new Reparations());
                cartes.add(new FinDeLimite());
                cartes.add(new RoueDeSecours());
            }

            if (i < 10) {
                cartes.add(new Borne(25, "assets/cartes/Speed25.jpg"));
                cartes.add(new Borne(50, "assets/cartes/Speed50.jpg"));
                cartes.add(new Borne(75, "assets/cartes/Speed75.jpg"));
            }

            if (i < 12) {
                cartes.add(new Borne(100, "assets/cartes/Speed100.jpg"));
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
        return this.cartes.isEmpty();
    }

    public Carte regarde() {
        if (this.estVide()) {
            return null;
        }
        return this.cartes.get(0);
    }

    public Carte prend() {
        return this.cartes.remove(0);
    }

    public void pose(Carte carte) {
        this.cartes.add(0, carte);
    }

    /*
     * Cette méthode permet de vérifier qu'il ne reste plus de borne dans le tas de cartes car c'est l'une des
     * condition de fin de jeu.
     */
    public boolean contientBornes() {
        for (Carte carte : cartes) {
            if (carte instanceof Borne) {
                return true;
            }
        }
        return false;
    }

    @Override
    public JsonObject sauvegarder() {
        JsonObject json = new JsonObject();
        JsonArray cartesArray = new JsonArray();

        cartes.stream().map(Carte::sauvegarder).forEach(cartesArray::add);
        json.add("cartes", cartesArray);

        return json;
    }
}