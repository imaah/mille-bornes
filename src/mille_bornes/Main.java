package mille_bornes;

import mille_bornes.extensions.sauvegarde.Saver;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.runGame();
    }

    private void runGame() {
        Scanner scanner = new Scanner(System.in);
        File file = new File("save.dat");
        boolean loadFile = false;
        Saver saver = new Saver();
        Jeu jeu;

        if (file.exists()) {
            while (true) {
                System.out.print("Une partie a été trouvée, voulez-vous la lancer ? [Y/n]: ");
                String line = scanner.nextLine();

                if (line.trim().equalsIgnoreCase("Y")) {
                    loadFile = true;
                    break;
                } else if (line.trim().equalsIgnoreCase("N")) {
                    break;
                }
            }
        }

        if (loadFile) {
            try {
                jeu = saver.loadObjectFromFile(file, Jeu.class);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }
        } else {
            List<String> noms = new ArrayList<>();

            System.out.print("Entrez le nombre de joueurs (entre 1 et 4): ");
            int nombreJoueurs = readInt(scanner, "Veuillez entrer un entier valide entre 2 et 4", 1, 4);

            int nombreBots;
            int nbBotsPotentiels = 4 - nombreJoueurs;
            if (nbBotsPotentiels > 0) {
                System.out.print("Entrez le nombre de bots (entre 1 et " + nbBotsPotentiels + "): ");
                nombreBots = readInt(scanner, "Veuillez entrer un entier valide entre 1 et " + nbBotsPotentiels + ")", 0, nbBotsPotentiels);
            } else {
                nombreBots = 0;
            }

            Joueur[] joueurs = new Joueur[nombreJoueurs + nombreBots];

            for (int i = 0; i < nombreJoueurs + nombreBots; i++) {
                System.out.print("Entrez le nom du " +
                        (i < nombreJoueurs ? "joueur" : "bot") +
                        " n°" + (i + 1) + ": ");
                String nom = scanner.nextLine().trim();
                if(nom.equalsIgnoreCase("annuler") || nom.equalsIgnoreCase("")) {
                    System.err.println("Nom invalide !");
                    i--;
                } else if(noms.contains(nom.toLowerCase())) {
                    System.err.println("Le nom entré a déjà été utilisé !");
                    i--;
                } else {
                    if (i < nombreJoueurs) {
                        joueurs[i] = new Joueur(nom);
                    } else {
                        joueurs[i] = new Bot(nom);
                    }
                    noms.add(nom.toLowerCase());
                }
            }

            jeu = new Jeu(joueurs);

            jeu.prepareJeu();
        }

        do {
            try {
                saver.saveIntoFile(file, jeu);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(jeu.estPartieFinie()) {
                System.out.printf("Victoire de %s ! \uD83C\uDF89%n", jeu.getGagnant().stream().map(joueur -> joueur.nom).collect(Collectors.joining(",")));
                break;
            }
        } while (!jeu.joue());
    }

    private int readInt(Scanner scanner, String error) {
        return readInt(scanner, error, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    // Pour la praticité de lecture d'entiers
    private int readInt(Scanner scanner, String error, int min, int max) {
        boolean valid = false;
        int value = 0;

        while (!valid) {
            try {
                value = Integer.parseInt(scanner.nextLine());

                if(min <= value && value <= max) {
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
