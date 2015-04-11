package me.theglassboard.teamhub;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Kevin on 11/04/2015.
 */
public class ObjectManager {

    private Context context;

    public ObjectManager(Context context) {

        this.context = context;
    }

    public void saveObject(String jsonString, String fileName) {

        try {
            // If the file does not exists, it is created.
            File file = new File(context.getFilesDir(), fileName + ".txt");

            if (!file.exists())
                file.createNewFile();

            // Adds a line to the trace file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(jsonString);
            writer.close();

        }
        catch(IOException e) {

            Log.d("doingTheThing()", "Unable to write to the " + fileName + ".txt file.");
        }
    }

    public Object readObject(String fileName) {

        String ret;

        File file = new File(context.getFilesDir(), fileName + ".txt");

        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            ret = reader.readLine();
            Log.d("Read from file", ret);
            reader.close();
            return ret;

        } catch (FileNotFoundException e) {

            Log.d("doingTheThing()", "Unable to read the " + fileName + ".txt file.");

        } catch (IOException e) {

            Log.d("doingTheThing()", "Unable to read the " + fileName + ".txt file.");

        }


        return null;
    }

    public boolean fileExists(String fileName) {

        File file = new File(context.getFilesDir(), fileName + ".txt");

        return file.exists();

    }
}
