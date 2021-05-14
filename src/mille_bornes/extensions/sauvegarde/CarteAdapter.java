package mille_bornes.extensions.sauvegarde;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import mille_bornes.cartes.Borne;
import mille_bornes.cartes.Carte;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CarteAdapter extends TypeAdapter<Carte> {
    @Override
    public void write(JsonWriter jsonWriter, Carte carte) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("class").value(carte.getClass().getName());

        if (carte instanceof Borne) {
            jsonWriter.name("km").value(((Borne) carte).km);
        }

        jsonWriter.endObject();
    }

    @Override
    public Carte read(JsonReader jsonReader) throws IOException {
        String className = null;
        int km = -1;
        Carte carte = null;

        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equalsIgnoreCase("class")) {
                className = jsonReader.nextString();
            } else if (name.equalsIgnoreCase("km")) {
                km = jsonReader.nextInt();
            }
        }

        try {
            if (className != null) {
                Class<?> clazz = Class.forName(className);

                if(clazz.isAssignableFrom(Carte.class)) {
                    var borneClass = clazz.asSubclass(Carte.class);

                    if(clazz.isAssignableFrom(Borne.class)) {
                        carte = borneClass.getConstructor(Integer.TYPE).newInstance(km);
                    } else {
                        carte = borneClass.getConstructor().newInstance();
                    }
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        jsonReader.endObject();
        return carte;
    }
}
