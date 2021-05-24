package mille_bornes.extensions.bots;

import java.util.Random;

public class DumbBot extends Bot {
    private static final long serialVersionUID = 8821210449673272211L;

    public DumbBot(String nom) {
        super(nom);
    }

    @Override
    public int choisitCarte() {
        // Après la désérialisation random sera null, alors on verifie s'il est null.
        if(random == null) random = new Random();
        // Si il reste des cartes non testées
        if (nCartesRestantes.size() != 0) {
            int n = nCartesRestantes.get(random.nextInt(nCartesRestantes.size()));
            nCartesRestantes.remove(Integer.valueOf(n));
            return n;
        } else {
            return -(random.nextInt(7) + 1);
        }
    }
}
