package com.example.texttospeech;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final Integer RecordAudioRequestCode = 1;
    private static final Integer NumLivelli = 5;
    private Chip[] chips = new Chip[NumLivelli];
    private TextView[] completeTesti = new TextView[NumLivelli];
    private CircularProgressIndicator[] barProgressi = new CircularProgressIndicator[NumLivelli];
    private TextView[] percProgs = new TextView[NumLivelli];
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        sh = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sh.edit();

        //String progressoFile = null;
        //List<Progresso> progressi =null;
        //try {
            //progressoFile = Utils.readProgress(getApplicationContext());

            //Type listProgressoType = new TypeToken<List<Progresso>>() { }.getType();
            //progressi = gson.fromJson(progressoFile, listProgressoType);

        /*} catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("FILE NOT FOUND!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        } catch (IOException e) {
            e.printStackTrace();
        }*/


        //
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "scioglilingua.json");
        Gson gson = new Gson();
        Type listLivelloType = new TypeToken<List<Livello>>() { }.getType();
        List<Livello> livelli = gson.fromJson(jsonFileString, listLivelloType);

        chips[0] = findViewById(R.id.lev1);
        completeTesti[0] = findViewById(R.id.compl1);
        barProgressi[0] = findViewById(R.id.prog1);
        percProgs[0] = findViewById(R.id.progText1);

        chips[1] = findViewById(R.id.lev2);
        completeTesti[1] = findViewById(R.id.compl2);
        barProgressi[1] = findViewById(R.id.prog2);
        percProgs[1] = findViewById(R.id.progText2);

        chips[2] = findViewById(R.id.lev3);
        completeTesti[2] = findViewById(R.id.compl3);
        barProgressi[2] = findViewById(R.id.prog3);
        percProgs[2] = findViewById(R.id.progText3);

        chips[3] = findViewById(R.id.lev4);
        completeTesti[3] = findViewById(R.id.compl4);
        barProgressi[3] = findViewById(R.id.prog4);
        percProgs[3] = findViewById(R.id.progText4);

        chips[4] = findViewById(R.id.lev5);
        completeTesti[4] = findViewById(R.id.compl5);
        barProgressi[4] = findViewById(R.id.prog5);
        percProgs[4] = findViewById(R.id.progText5);



        for (int i = 0; i<NumLivelli; i++){
            //ripristina progresso
            int accuratezzaLivello = sh.getInt(Integer.toString(i), 0);

            barProgressi[i].setProgress(accuratezzaLivello);
            percProgs[i].setText(accuratezzaLivello+"%");
            if(accuratezzaLivello==100){
                completeTesti[i].setVisibility(View.VISIBLE);
            }


            int finalI = i;
            chips[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Create Intent
                    Intent intent = new Intent(MainActivity.this, LivelloActivity.class);
                    intent.putExtra("livello",finalI);
                    intent.putExtra("scioglilingua", livelli.get(finalI).getScioglilingua());
                    //startActivity(intent);
                    startActivityForResult(intent, 1);
                }
            });
        }

    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("IL RISULTATO E STATO RITORNATO reqcode = "+requestCode+" result code = "+resultCode);
        // Checking the result status
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int totAcc = data.getIntExtra("totAcc", -1);
            int livello = data.getIntExtra("livello", -1);
            if(livello>=0 && totAcc>=0){
                //save progress
                editor.putInt(Integer.toString(livello), totAcc);
                editor.apply();

                //update indicators
                barProgressi[livello].setProgress(totAcc);
                percProgs[livello].setText(totAcc+"%");
                if(totAcc==100){
                    completeTesti[livello].setVisibility(View.VISIBLE);
                }
            }
        }

        //save progressi on json file
        /*
        List<Progresso> updatedProgressi = new ArrayList<>();
        for (int i = 0; i<NumLivelli; i++) {
            Progresso p = new Progresso(i, barProgressi[i].getProgress());
            updatedProgressi.add(p);
        }
        Progresso[] progressiAgg = updatedProgressi.toArray(new Progresso[updatedProgressi.size()]);
        Utils.writeProgress(getApplicationContext(), progressiAgg);
        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }


}
