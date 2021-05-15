package mille_bornes;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int nombreJoueursMax = 4;
        String erreurNombreInvalide = "Veuillez entrer un entier valide";
        Scanner scanner = new Scanner(System.in);


        System.out.print("Entrez le nombre de joueurs : ");
        int nombreJoueurs = readInt(scanner, erreurNombreInvalide, nombreJoueursMax);

        int nombreBots;
        if (nombreJoueurs < 4) {
            System.out.print("Entrez le nombre de bots : ");
            nombreBots = readInt(scanner, erreurNombreInvalide, nombreJoueursMax - nombreJoueurs);
        } else {
            nombreBots = 0;
        }


        Joueur[] joueurs = new Joueur[nombreJoueurs + nombreBots];

        for (int i = 0; i < nombreJoueurs; i++) {
            System.out.print("Entrez le nom du joueur n°" + (i + 1) + ": ");
            joueurs[i] = new Joueur(scanner.nextLine());
        }

        for (int i = 0; i < nombreBots; i++) {
            System.out.print("Entrez le nom du bot n°" + (i + 1) + ": ");
            joueurs[i + nombreJoueurs] = new Bot(scanner.nextLine());
        }

        Jeu jeu = new Jeu(joueurs);

        jeu.prepareJeu();

        while (!jeu.estPartieFinie()) {
            if(jeu.joue()) {
                System.out.printf("Victoire de %s !", jeu.getJoueurActif().nom);
            }
        }
    }

    private static int readInt(Scanner scanner, String error, int max) {
        if (max <= 0) return 0;

        boolean valid = false;
        int value = 0;

        while (!valid) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value <= max) {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.err.println(error);
            }
        }

        return value;
    }
}
