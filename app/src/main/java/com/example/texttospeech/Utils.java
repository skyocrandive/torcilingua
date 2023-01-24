package com.example.texttospeech;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Utils {

    static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    static String readProgress(Context ctx) throws IOException {
        String fileName = "progress.json";
        String dirPath = ctx.getFilesDir().getAbsolutePath() + File.separator + fileName;
        String jsonString;

        InputStream progress = new FileInputStream(dirPath);

        int size = progress.available();
        byte[] buffer = new byte[size];
        progress.read(buffer);
        progress.close();

        jsonString = new String(buffer, "UTF-8");



        return jsonString;
    }

    static void writeProgress(Context ctx, Progresso[] progressiAgg ) {
        String fileName = "progress.json";
        String dirPath = ctx.getFilesDir().getAbsolutePath() + File.separator + fileName;

        Gson gson = new Gson();
        try {
            gson.toJson(progressiAgg, new FileWriter(dirPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
