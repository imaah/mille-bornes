package mille_bornes.extensions.bots;

public class DumbBot extends Bot {
    private static final long serialVersionUID = 5572469609476004597L;

    public DumbBot(String nom) {
        super(nom);
    }

    @Override
    public int choisitCarte() {
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
