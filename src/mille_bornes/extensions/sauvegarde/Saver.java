package mille_bornes.extensions.sauvegarde;

import java.io.*;

public class Saver {
    public void saveToFile(File file, Serializable object) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }

    public Object loadObjectFromFile(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Object object = ois.readObject();
        ois.close();
        return object;
    }

    public <T extends Serializable> T loadObjectFromFile(File file, Class<T> clazz) throws IOException, ClassNotFoundException {
        Object object = loadObjectFromFile(file);
        if (object != null && clazz.isAssignableFrom(object.getClass())) {
            return clazz.cast(object);
        } else {
            throw new IllegalStateException("Read object is not assignable from " + clazz.getName());
        }
    }
}
