package mille_bornes.modele.extensions.bots;

import java.util.Random;

public class DumbBot extends Bot {
    public DumbBot(String nom) {
        super(nom);
    }

    @Override
    public int choisitCarte() {
        // Après la désérialisation random sera null, alors on verifie s'il est null.
        if (random == null) random = new Random();
        // Si il reste des cartes non testées
        if (!(nCartesRestantes.isEmpty())) {
            int n = nCartesRestantes.get(random.nextInt(nCartesRestantes.size()));
            nCartesRestantes.remove(Integer.valueOf(n));
            return n;
        } else {
            return -(random.nextInt(7) + 1);
        }
    }
}
