package mille_bornes.application;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * Permet de charger les images qu'une seule fois tout au long de l'éxécution de l'application
 */
public enum Asset {
    // Les attaques
    CARTE_ACCIDENT("/images/cartes/Accident.jpg"),
    CARTE_PANNE_D_ESSENCE("/images/cartes/Panne_Essence.jpg"),
    CARTE_CREVAISON("/images/cartes/Creve.jpg"),
    CARTE_FEU_ROUGE("/images/cartes/Stop.jpg"),
    CARTE_LIMITE_VITESSE("/images/cartes/Limite_50.jpg"),

    // Les bottes
    CARTE_AS_DU_VOLANT("/images/cartes/As_Volant.jpg"),
    CARTE_CITERNE("/images/cartes/Citerne.jpg"),
    CARTE_INCREVABLE("/images/cartes/Increvable.jpg"),
    CARTE_VEHICULE_PRIORITAIRE("/images/cartes/Prioritaire.jpg"),

    // Les parades
    CARTE_ESSENCE("/images/cartes/Essence.jpg"),
    CARTE_FEU_VERT("/images/cartes/Roulez.jpg"),
    CARTE_FIN_DE_LIMITE("/images/cartes/Fin_limite.jpg"),
    CARTE_REPARATION("/images/cartes/Reparation.jpg"),
    CARTE_ROUE_DE_SECOURS("/images/cartes/Secours.jpg"),

    // Les bornes
    CARTE_BORNE_25("/images/cartes/Speed25.jpg"),
    CARTE_BORNE_50("/images/cartes/Speed50.jpg"),
    CARTE_BORNE_75("/images/cartes/Speed75.jpg"),
    CARTE_BORNE_100("/images/cartes/Speed100.jpg"),
    CARTE_BORNE_200("/images/cartes/Speed200.jpg"),

    // Les cartes additionelles pour l'affichage
    CARTE_VIDE("/images/cartes/Vide.jpg"),
    CARTE_DEFAULT("/images/cartes/Null.jpg"),
    ICON("/images/Mille_Bornes.png"),
    FOND_BOITE_APROPOS("/images/boite.jpg");

    private final Image image;

    Asset(String path) {
        Image tempImage = null;
        try {
            tempImage = new Image(Objects.requireNonNull(getClass().getResource(path)).toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("\n\nImpossible de charger les images du projet. Il est donc impossible de lancer le " +
                               "projet.");
            System.err.println("Lire le README.md pour comprendre le soucis.");
            System.exit(0);
        }
        this.image = tempImage;
    }

    public Image getImage() {
        return image;
    }
}
