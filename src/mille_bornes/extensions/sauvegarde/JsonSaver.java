package mille_bornes.extensions.sauvegarde;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class JsonSaver {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public void saveIntoFile(Object object, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        gson.toJson(object, writer);
//        System.out.println(gson.toJson(object));
        writer.flush();
        writer.close();
    }

    public Object loadFromFile(File file) throws IOException {
        FileReader reader = new FileReader(file);
        Object object = gson.fromJson(reader, Object.class);
        reader.close();
        return object;
    }

    public <T> T loadFromFile(File file, Class<T> type) throws IOException {
        FileReader reader = new FileReader(file);
        T object = gson.fromJson(reader, type);
        reader.close();
        return object;
    }
}
