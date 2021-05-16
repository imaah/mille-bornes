package mille_bornes.extensions.sauvegarde;

import java.io.*;

public class Serialiseur {
    public void sauvegarderDansUnFichier(File file, Serializable object) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }

    public Object chargerDepuisFichier(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Object object = ois.readObject();
        ois.close();
        return object;
    }

    public <T extends Serializable> T chargerDepuisFichier(File file, Class<T> clazz) throws IOException, ClassNotFoundException {
        Object object = chargerDepuisFichier(file);
        if (object != null && clazz.isAssignableFrom(object.getClass())) {
            return clazz.cast(object);
        } else {
            throw new IllegalStateException("Read object is not assignable from " + clazz.getName());
        }
    }
}
