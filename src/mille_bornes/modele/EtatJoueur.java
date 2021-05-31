package mille_bornes.modele;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mille_bornes.modele.cartes.Attaque;
import mille_bornes.modele.cartes.Bataille;
import mille_bornes.modele.cartes.Botte;
import mille_bornes.modele.cartes.Carte;
import mille_bornes.modele.cartes.attaques.LimiteVitesse;
import mille_bornes.modele.cartes.bottes.AsDuVolant;
import mille_bornes.modele.cartes.bottes.Citerne;
import mille_bornes.modele.cartes.bottes.Increvable;
import mille_bornes.modele.cartes.bottes.VehiculePrioritaire;
import mille_bornes.modele.extensions.sauvegarde.Sauvegardable;

import static mille_bornes.modele.Jeu.AUTORISE_PLUSIEURS_200_BORNES;
import static mille_bornes.modele.Jeu.MAX_VITESSE_SOUS_LIMITE;
import static mille_bornes.modele.utils.JsonUtils.verifieExiste;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class EtatJoueur implements Sauvegardable {
    private final Joueur joueur;
    private final Stack<Bataille> pileBataille = new Stack<>();
    private final List<Carte> main = new LinkedList<>();
    private final List<Botte> bottes = new LinkedList<>();
    private int km = 0;
    private boolean limiteVitesse;
    private boolean borne200KmUtilisee = false;

    public EtatJoueur(Joueur joueur) {
        if (joueur == null) throw new IllegalStateException("joueur ne peut pas être null.");
        this.joueur = joueur;
    }

    public EtatJoueur(Joueur joueur, JsonObject json) {
        this.joueur = joueur;

        if (verifieExiste(json, "pileBataille", "main", "bottes", "km", "limiteVitesse", "borne200KmUtilisee")) {

            JsonArray pb = json.getAsJsonArray("pileBataille");
            pb.forEach(element -> pileBataille.push((Bataille) Carte.deserialize(element.getAsJsonObject())));

            JsonArray m = json.getAsJsonArray("main");
            m.forEach(element -> main.add(Carte.deserialize(element.getAsJsonObject())));

            JsonArray b = json.getAsJsonArray("bottes");
            b.forEach(element -> bottes.add((Botte) Carte.deserialize(element.getAsJsonObject())));

            km = json.get("km").getAsInt();
            limiteVitesse = json.get("limiteVitesse").getAsBoolean();
            borne200KmUtilisee = json.get("borne200KmUtilisee").getAsBoolean();
        } else {
            throw new IllegalArgumentException("Propriétés manquantes dans l'objet json.");
        }
    }

    public int getKm() {
        return km;
    }

    public void ajouteKm(int km) {
        String raison = ditPourquoiPeutPasAvancer();

        // Vérifications d'actions légales
        if (limiteVitesse && km > MAX_VITESSE_SOUS_LIMITE) {
            throw new IllegalStateException("Vous ne pouvez pas vous déplacer a de plus de " + MAX_VITESSE_SOUS_LIMITE +
                                            "km");
        } else if (raison != null) {
            throw new IllegalStateException(raison);
        } else if (this.km + km > 1000) {
            throw new IllegalStateException("Vous ne pouvez pas aller plus loin que 1000 km !");
        } else if (km == 200 && borne200KmUtilisee && !AUTORISE_PLUSIEURS_200_BORNES) {
            throw new IllegalStateException("Vous avez déjà utilisé une borne de 200km !");
        }

        if (km == 200) {
            borne200KmUtilisee = true;
        }

        this.km += km;
    }

    public String ditPourquoiPeutPasAvancer() {
        String message = null;

        if (pileBataille.isEmpty() && !bottes.contains(new VehiculePrioritaire())) {
            message = "Vous n'avez pas encore démarré.";
        }

        if (getBataille() instanceof Attaque) {
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
        jeu.defausse(pileBataille.pop());
    }

    public List<Carte> getMain() {
        return Collections.unmodifiableList(main);
    }

    public void addBotte(Botte botte) {
        if (botte == null) throw new IllegalArgumentException();

        bottes.add(botte);
        // Si on a bien une attaque sur le joueur...
        if (getBataille() != null && getBataille() instanceof Attaque) {
            if (botte.contre((Attaque) getBataille())) {
                // ... et qu'on la contre, on la retire
                pileBataille.pop();
            }
        }
    }

    public void attaque(Jeu jeu, Attaque attaque) {
        if (jeu == null || attaque == null) throw new IllegalArgumentException();

        // Pour chaque carte de la main
        for (int i = 0; i < main.size(); i++) {
            Carte carte = main.get(i);
            // Si le joueur possède une botte
            if (carte instanceof Botte) {
                Botte botte = (Botte) carte;

                // Si cette botte peut contrer l'attaque...
                if (botte.contre(attaque)) {
                    // ...Coup-fourré
                    System.out.println("Votre adversaire sort un coup-fourré! Votre attaque " +
                                       "n'a aucun effet et il récupère la main.");
                    botte.appliqueEffet(jeu, this);
                    main.remove(i);
//                    defausseCarte(jeu, i);
                    jeu.setProchainJoueur(this.joueur);
                    jeu.defausse(attaque);

                    throw new CoupFourreException();
                }
            }
        } // for

        // Impossible d'attaquer si la victime potentielle a la botte
        for (Botte botte : bottes) {
            if (botte.contre(attaque))
                throw new IllegalStateException("Ce joueur possède une botte contre cette attaque! Choisissez une autre cible.");
        }

        if (getBataille() instanceof Attaque && !(attaque instanceof LimiteVitesse)) {
            throw new IllegalStateException("Le joueur se fait déjà attaquer.");
        }

        if (attaque instanceof LimiteVitesse && limiteVitesse) {
            throw new IllegalStateException("Le joueur est déjà sous une limite de vitesse.");
        }

        attaque.appliqueEffet(jeu, this);
    }

    public void prendCarte(Carte carte) {
        if (main.size() > 6) throw new IllegalStateException("Le joueur ne peut pas avoir plus de 6 cartes");

        main.add(carte);
    }

    public void defausseCarte(Jeu jeu, int i) {
        if (jeu == null || !(0 <= i && i <= 6)) throw new IllegalArgumentException();

        Carte carte = main.get(i);
        main.remove(i);
        jeu.defausse(carte);
    }

    public void joueCarte(Jeu jeu, int i) {
        if (jeu == null || !(0 <= i && i <= 6)) throw new IllegalArgumentException();

        // Récupération de la carte à jouer
        Carte carte = main.get(i);
        if (carte instanceof Attaque) {
            // Si c'est une attaque, on choisit qui attaquer
            Joueur cible = joueur.choisitAdversaire(carte);
            joueCarte(jeu, i, cible);
        } else {
            // Sinon on applique son effet
            carte.appliqueEffet(jeu, this);
        }

        // Une fois jouée, on retire la carte
        main.remove(i);
    }

    public void joueCarte(Jeu jeu, int i, Joueur joueur) {
        if (jeu == null || joueur == null || !(0 <= i && i <= 6)) throw new IllegalArgumentException();

        Carte carte = main.get(i);
        if (carte instanceof Attaque) {
            // Si c'est une attaque, on l'applique au joueur cible
            Attaque attaque = (Attaque) carte;
            try {
                joueur.attaque(jeu, attaque);
                main.remove(i);
            } catch (CoupFourreException e) {
                defausseCarte(jeu, i);
                throw new CoupFourreException();
            }
        } else {
            throw new IllegalStateException("La carte n'est pas une attaque, donc ne peut pas être utilisée sur un autre joueur!");
        }
    }

    public List<Botte> getBottes() {
        return Collections.unmodifiableList(bottes);
    }

    @Override
    public String toString() {
        StringBuilder bob = new StringBuilder();

        bob.append(km)
                .append("km ");

        if (getLimiteVitesse()) {
            bob.append("\u001B[31m(\u001B[0m")
                    .append(MAX_VITESSE_SOUS_LIMITE)
                    .append("\u001B[31m)\u001B[0m ");
        }

        bob.append('[')
                .append(bottes.contains(new AsDuVolant()) ? 'A' : '.')
                .append(bottes.contains(new Citerne()) ? 'C' : '.')
                .append(bottes.contains(new Increvable()) ? 'I' : '.')
                .append(bottes.contains(new VehiculePrioritaire()) ? 'V' : '.')
                .append(']');

        if (getBataille() != null) {
            bob.append(", ").append(getBataille().nomColore());
        }

        return bob.toString();
    }

    @Override
    public JsonObject sauvegarder() {
        JsonObject json = new JsonObject();

        JsonArray pileArray = new JsonArray(),
                mainArray = new JsonArray(),
                botteArray = new JsonArray();

        pileBataille.stream().map(Carte::sauvegarder).forEach(pileArray::add);
        main.stream().map(Carte::sauvegarder).forEach(mainArray::add);
        bottes.stream().map(Carte::sauvegarder).forEach(botteArray::add);

        json.add("pileBataille", pileArray);
        json.add("main", mainArray);
        json.add("bottes", botteArray);

        json.addProperty("km", km);
        json.addProperty("limiteVitesse", limiteVitesse);
        json.addProperty("borne200KmUtilisee", borne200KmUtilisee);
        return json;
    }
}