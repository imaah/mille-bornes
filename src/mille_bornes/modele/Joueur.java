package mille_bornes.modele;

import com.google.gson.JsonObject;
import mille_bornes.modele.cartes.*;
import mille_bornes.modele.extensions.sauvegarde.Sauvegardable;
import mille_bornes.modele.utils.JsonUtils;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class Joueur implements Sauvegardable {
    public final String nom;
    private final EtatJoueur etat;
    private final Scanner input = new Scanner(System.in);
    private Joueur prochainJoueur;
    private String prochainJoueurNom;

    public Joueur(String nom) {
        this.nom = nom;
        this.etat = new EtatJoueur(this);
    }

    public Joueur(JsonObject json) {
        if (JsonUtils.verifieExiste(json, "nom", "etat", "prochainJoueur")) {
            this.nom = json.get("nom").getAsString();
            this.etat = new EtatJoueur(this, json.get("etat").getAsJsonObject());
            this.prochainJoueurNom = json.get("prochainJoueur").getAsString();
        } else {
            throw new IllegalArgumentException("Propriétés manquantes dans l'objet json.");
        }
    }

    public Joueur getProchainJoueur() {
        return prochainJoueur;
    }

    public static Joueur deserialize(JsonObject json) {
        if (JsonUtils.verifieExiste(json, "nom", "etat", "prochainJoueur", "class")) {
            try {
                Class<?> clazz = Class.forName(json.get("class").getAsString());
                if (Joueur.class.isAssignableFrom(clazz)) {

                    Class<? extends Joueur> carteClass = clazz.asSubclass(Joueur.class);
                    Object obj = carteClass.getConstructor(JsonObject.class)
                                           .newInstance(json);
                    return carteClass.cast(obj);
                } else {
                    throw new IllegalArgumentException("Joueur: La classe donnée dans le json n'est pas de type" +
                            "Joueur");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Joueur: Les propriétés dans le json sont incorrect.");
            }
        } else {
            throw new IllegalArgumentException("Joueur: Propriétés manquantes dans l'objet json.");
        }
    }

    public void setProchainJoueur(Joueur joueur) {
        prochainJoueur = joueur;
        prochainJoueurNom = joueur.nom;
    }

    public String getProchainJoueurNom() {
        return prochainJoueurNom;
    }

    public List<Carte> getMain() {
        return etat.getMain();
    }

    public int getKm() {
        return etat.getKm();
    }

    public boolean getLimiteVitesse() {
        return etat.getLimiteVitesse();
    }

    public int choisitCarte() {
        boolean correcte = false;
        int valeur = 0;
        System.out.println("Choisissez une carte avec son numéro " +
                           "(-7 <= numéro < 0 pour la défausse, 0 < numéro <= 7 pour jouer)");
        do {
            try {
                System.out.print("\u001B[36m>>\u001B[0m ");
                String line = input.nextLine();
                valeur = Integer.parseInt(line);
                // Vérification de la valeur entrée
                if (-7 <= valeur && valeur <= 7) {
                    correcte = true;
                }
            } catch (NoSuchElementException e) {
                System.err.println("La valeur que vous avez entrée n'est pas correcte.\n" +
                                   "Choisissez une carte avec son numéro " +
                                   "(-7 <= numéro < 0 pour la défausse, 0 < numéro <= 7 pour jouer)");
                correcte = false;
            } catch (NumberFormatException e) {
                System.err.println("Veuillez entrer un entier valide !");
                correcte = false;
            }
        } while (!correcte);

        return valeur;
    }

    public Joueur choisitAdversaire(Carte carte) {
        if (!(carte instanceof Attaque)) throw new IllegalArgumentException();

        Joueur cible = null;
        boolean estValide = false;

        while (!estValide) {
            System.out.println("Quel joueur voulez-vous attaquer ? ([a]nnuler pour annuler) ");
            System.out.print("\u001B[36m>>\u001B[0m ");
            String nomDuJoueur = input.nextLine().trim();

            if (nomDuJoueur.equalsIgnoreCase("annuler") || nomDuJoueur.equalsIgnoreCase("a"))
                throw new IllegalStateException("Attaque annulée");

            cible = getProchainJoueur();

            // On boucle dans tout les joueurs
            while (!cible.nom.equalsIgnoreCase(nomDuJoueur) && !cible.equals(this)) {
                cible = cible.getProchainJoueur();
            }

            if (cible.equals(this)) {
                System.err.println("Vous ne pouvez pas vous attaquer vous-même !");
                estValide = false;
            } else {
                estValide = true;
            }
        }

        return cible;
    }

    public void prendCarte(Carte carte) {
        etat.prendCarte(carte);
    }

    public void joueCarte(Jeu jeu, Carte carte) {
        int index = getMain().indexOf(carte);

        if (index != -1) {
            joueCarte(jeu, index);
        } else {
            throw new IllegalStateException("La main du joueur ne contient pas cette carte");
        }
    }

    public void joueCarte(Jeu jeu, Carte carte, Joueur joueur) {
        int index = getMain().indexOf(carte);

        if (index != -1) {
            joueCarte(jeu, index, joueur);
        } else {
            throw new IllegalStateException("La main du joueur ne contient pas cette carte");
        }
    }

    public void joueCarte(Jeu jeu, int i) {
        etat.joueCarte(jeu, i);
    }

    public void joueCarte(Jeu jeu, int i, Joueur joueur) {
        etat.joueCarte(jeu, i, joueur);
    }

    public void defausseCarte(Jeu jeu, Carte carte) {
        int index = getMain().indexOf(carte);

        if (index != -1) {
            defausseCarte(jeu, index);
        } else {
            throw new IllegalStateException("La main du joueur ne contient pas cette carte");
        }
    }

    public void defausseCarte(Jeu jeu, int i) {
        etat.defausseCarte(jeu, i);
    }

    public void attaque(Jeu jeu, Attaque attaque) {
        etat.attaque(jeu, attaque);
    }

    public Bataille getBataille() {
        return etat.getBataille();
    }

    public String ditPourquoiPeutPasAvancer() {
        return etat.ditPourquoiPeutPasAvancer();
    }

    public String toString() {
        return nom + " : " + etat.toString();
    }

    public List<Botte> getBottes() {
        return etat.getBottes();
    }

    @Override
    public JsonObject sauvegarder() {
        JsonObject json = new JsonObject();

        json.addProperty("nom", nom);
        json.add("etat", etat.sauvegarder());
        json.addProperty("prochainJoueur", prochainJoueur.nom);
        json.addProperty("class", getClass().getName());

        return json;
    }

    public String getNom() {
        return this.nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Joueur)) return false;
        Joueur joueur = (Joueur) o;
        return Objects.equals(nom, joueur.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
}
