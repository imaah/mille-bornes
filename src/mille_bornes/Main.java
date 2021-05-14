package mille_bornes;

import mille_bornes.extensions.sauvegarde.JsonSaver;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File jsonFile = new File("save.json");
        boolean loadFile = false;
        Jeu jeu;
        JsonSaver jsonSaver = new JsonSaver();


        if (jsonFile.exists()) {
            while (true) {
                System.out.println("Une partie a été trouvée, voulez-vous la lancer ? [Y/n] : ");
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
                jeu = jsonSaver.loadFromFile(jsonFile, Jeu.class);
                jeu.prepareJeu();
            } catch (IOException e) {
                e.printStackTrace();
//                System.exit(-1);
                return;
            }
        } else {
            System.out.print("Entrez le nombre de joueurs : ");
            int nombreJoueur = readInt(scanner, "Veuillez entre un entier valide.");

            Joueur[] joueurs = new Joueur[nombreJoueur];

            for (int i = 0; i < nombreJoueur; i++) {
                System.out.print("Entrez le nom du joueur n°" + (i + 1) + ": ");
                joueurs[i] = new Joueur(scanner.nextLine());
            }

            jeu = new Jeu(joueurs);

            jeu.prepareJeu();
        }

        while (!jeu.estPartieFinie()) {
            if (jeu.joue()) {
                System.out.printf("Victoire de %s !", jeu.getJoueurActif().nom);
            }
            try {
//                saver.saveToFile(file, jeu);
                jsonSaver.saveIntoFile(jeu, jsonFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int readInt(Scanner scanner, String error) {
        boolean valid = false;
        int value = 0;

        while (!valid) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.err.println(error);
            }
        }

        return value;
    }
}
