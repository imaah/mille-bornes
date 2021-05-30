package mille_bornes.modele;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import mille_bornes.modele.extensions.bots.DumbBot;
import mille_bornes.modele.extensions.bots.NaiveBot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.chargerArguments(args);
        main.runGame();
    }

    /**
     * Permet de charger les différents arguments passés au lancemant
     *
     * @param args les arguments passés au lancement
     */
    private void chargerArguments(String[] args) {
        Pattern pattern = Pattern.compile("^--(.+)=(.+)$");

        for (String arg : args) {
            Matcher matcher = pattern.matcher(arg.trim());

            if (matcher.matches()) {
                System.setProperty(matcher.group(1), matcher.group(2));
            }
        }
    }

    /**
     * Demande à l'utilisateur s'il veut charger la partie sauvegardée
     *
     * @param scanner un {@link Scanner} vers l'utilisateur.
     * @return s'il faut charger ou non le fichier.
     */
    private boolean demanderOuvertureSauvegarde(Scanner scanner) {
        while (true) {
            System.out.println("Une partie a été trouvée, voulez-vous la lancer ? [Y/n]: ");
            System.out.print("\u001B[36m>>\u001B[0m ");

            String line = scanner.nextLine();

            if (line.trim().equalsIgnoreCase("Y")) {
                return true;
            } else if (line.trim().equalsIgnoreCase("N")) {
                return false;
            }
        }
    }

    /**
     * Charge la partie enregistrée dans le fichier de sauvegarde.
     *
     * @param sauvegarde fichier de sauvegarde
     * @return La partie enregistrée dans le fichier de sauvegarde
     * @throws IOException            S'il y a une problème lors de la lecture du fichier
     * @throws ClassNotFoundException Si la classe {@link Jeu} n'existe pas
     */
    private Jeu chargerPartie(File sauvegarde) throws IOException, ClassNotFoundException {
        Gson gson = new Gson();
        FileReader lecteur = new FileReader(sauvegarde);
        JsonObject json = gson.fromJson(lecteur, JsonObject.class);
        return new Jeu(json);
//        Serialiseur serialiseur = new Serialiseur();
//        return serialiseur.chargerDepuisFichier(sauvegarde, Jeu.class);
    }

    /**
     * Enregistre l'état de la partie dans un fichier de sauvegarde
     *
     * @param sauvegarde fichier de sauvegarde
     * @param partie     la partie à enregistrer
     * @throws IOException S'il y a un problème lors de l'écriture du fichier
     */
    private void sauvegarderPartie(File sauvegarde, Jeu partie) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter ecrivain = new FileWriter(sauvegarde);

        gson.toJson(partie.sauvegarder(), ecrivain);

        ecrivain.flush();
        ecrivain.close();
//        Serialiseur serialiseur = new Serialiseur();
//        serialiseur.sauvegarderDansUnFichier(sauvegarde, partie);
    }

    /**
     * Demande à l'utilisateur les le nombre de joueurs et de bots dans la partie.
     * Et crée la partie.
     *
     * @param scanner un {@link Scanner} vers l'utilisateur.
     * @return la partie créée.
     */
    private Jeu initialiserPartie(Scanner scanner) {
        System.out.println("Entrez le nombre de joueurs (entre 1 et " + Jeu.MAX_JOUEURS + ") : ");

        int nombreJoueurs = readInt(scanner, "Veuillez entrer un entier valide entre 1 et " + Jeu.MAX_JOUEURS, 0, Jeu.MAX_JOUEURS);
        int nombreBots;
        int nbBotsPotentiels = Jeu.MAX_JOUEURS - nombreJoueurs;

        if (nbBotsPotentiels > 0) {
            System.out.println("Entrez le nombre de bots (entre 1 et " + nbBotsPotentiels + "): ");
            nombreBots = readInt(scanner, "Veuillez entrer un entier valide entre 1 et " + nbBotsPotentiels + ")", 0, nbBotsPotentiels);
        } else {
            nombreBots = 0;
        }

        Joueur[] joueurs = initialiserJoueurs(scanner, nombreJoueurs, nombreBots);

        return new Jeu(joueurs);
    }

    /**
     * Demande le nom de chaque joueurs et pour les bots demande la difficultée
     *
     * @param scanner         un {@link Scanner} vers l'utilisateur.
     * @param nombreDeJoueurs Le nombre de joueurs
     * @param nombreDeBots    Le nombre de bots
     * @return un tableau de joueurs.
     */
    private Joueur[] initialiserJoueurs(Scanner scanner, int nombreDeJoueurs, int nombreDeBots) {
        List<String> noms = new ArrayList<>();
        Joueur[] joueurs = new Joueur[nombreDeJoueurs + nombreDeBots];

        for (int i = 0; i < nombreDeJoueurs + nombreDeBots; i++) {
            System.out.print("Entrez le nom du " +
                             (i < nombreDeJoueurs ? "joueur" : "bot") +
                             " n°" + (i + 1) + ": ");
            String nom = scanner.nextLine().trim();
            if (nom.equalsIgnoreCase("annuler") || nom.equalsIgnoreCase("")) {
                System.err.println("Nom invalide !");
                i--;
            } else if (noms.contains(nom.toLowerCase())) {
                System.err.println("Le nom entré a déjà été utilisé !");
                i--;
            } else {
                if (i < nombreDeJoueurs) {
                    joueurs[i] = new Joueur(nom);
                } else {
                    System.out.println("Entrez la difficulté du bot : \n- Aléatoire (1)\n- Naïf (2)");

                    int difficulte = readInt(scanner, "Veuillez entrer un entier valide entre 1 et 2", 1, 2);

                    if (difficulte == 1) {
                        joueurs[i] = new DumbBot(nom);
                    } else {
                        joueurs[i] = new NaiveBot(nom);
                    }
                }
                noms.add(nom.toLowerCase());
            }
        }

        return joueurs;
    }

    /**
     * Lance une partie.
     */
    private void runGame() {
        Scanner scanner = new Scanner(System.in);
        File sauvegarde = new File("save.json");
        boolean chargerFichier = false;
        Jeu jeu = null;

        if (sauvegarde.exists()) {
            chargerFichier = demanderOuvertureSauvegarde(scanner);
        }

        if (chargerFichier) {
            try {
                jeu = chargerPartie(sauvegarde);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("La partie est corrompue. Il est donc impossible de la charger.");
            }
        }

        if (jeu == null) {
            jeu = initialiserPartie(scanner);
            jeu.prepareJeu();
        }

        do {
            try {
                sauvegarderPartie(sauvegarde, jeu);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!jeu.joue());

        if (jeu.estPartieFinie()) {
            System.out.printf("------------------------%n%n%n");
            System.out.println(jeu);
            System.out.printf("%nVictoire de %s ! \uD83C\uDF89%n", jeu.getGagnant().stream().map(joueur -> joueur.nom).collect(Collectors.joining(",")));
        }
    }

    /**
     * Permet une lecture d'entier plus facile avec vérification.
     *
     * @param scanner un {@link Scanner} vers l'utilisateur.
     * @param error   Le message à afficher en cas d'erreur.
     * @param min     Le minimum
     * @param max     Le maximum
     * @return l'entier entré.
     */
    private int readInt(Scanner scanner, String error, int min, int max) {
        boolean valid = false;
        int value = 0;

        while (!valid) {
            try {
                System.out.print("\u001B[36m>>\u001B[0m ");
                value = Integer.parseInt(scanner.nextLine());

                if (min <= value && value <= max) {
                    valid = true;
                } else {
                    System.err.println(error);
                }
            } catch (NumberFormatException e) {
                System.err.println(error);
            }
        }

        return value;
    }
}
