package mille_bornes.extensions.sauvegarde;

import java.io.*;

/**
 * Une classe qui permet la serialisation d'objets en Java.
 */
public class Serialiseur {

    /**
     * Sauvegarde l'etat de l'objet dans un fichier.
     * @param file le fichier dans lequel enregistrer l'objet.
     * @param object L'objet à sauvegarder
     * @throws IOException S'il y a un problème lors de l'écriture du fichier.
     */
    public void sauvegarderDansUnFichier(File file, Serializable object) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }

    /**
     * Charge le fichier depuis un fichier.
     * @param file Le fichier à lire
     * @return L'objet lu dans le fichier
     * @throws IOException S'il y a un problème lors de la lecture du fichier.
     * @throws ClassNotFoundException Si la classe de l'objet stocké dans le fichier est inconnue.
     */
    public Object chargerDepuisFichier(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Object object = ois.readObject();
        ois.close();
        return object;
    }

    /**
     * Charge le fichier depuis un fichier.
     * @param file Le fichier à lire
     * @param clazz la classe supposée de l'objet stocké dans le fichier.
     * @param <T> Le type de l'objet supposé.
     * @return L'objet lu dans le fichier
     * @throws IOException S'il y a un problème lors de la lecture du fichier.
     * @throws ClassNotFoundException Si la classe de l'objet stocké dans le fichier est inconnue.
     * @throws IllegalStateException Si la classe lu n'est pas de type T
     */
      public <T extends Serializable> T chargerDepuisFichier(File file, Class<T> clazz) throws IOException, ClassNotFoundException {
        Object object = chargerDepuisFichier(file);
        if (object != null && clazz.isAssignableFrom(object.getClass())) {
            return clazz.cast(object);
        } else {
            throw new IllegalStateException("L'objet lu n'est pas de type " + clazz.getName());
        }
    }
}
