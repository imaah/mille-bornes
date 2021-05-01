package mille_bornes;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez le nombre de joueurs : ");
        int nombreJoueur = readInt(scanner, "Veuillez entre un entier valide.");

        Joueur[] joueurs = new Joueur[nombreJoueur];

        for (int i = 0; i < nombreJoueur; i++) {
            System.out.print("Entrez le nom du joueur n°" + (i + 1) + ": ");
            joueurs[i] = new Joueur(scanner.nextLine());
        }

        Jeu jeu = new Jeu(joueurs);

        jeu.prepareJeu();

        while (!jeu.estPartieFinie()) {
            if(jeu.joue()) {
                System.out.printf("Victoire de %s !", jeu.getJoueurActif().nom);
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
