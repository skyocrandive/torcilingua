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
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final Integer RecordAudioRequestCode = 1;
    private static final Integer NumLivelli = 5;
    private Chip[] chips = new Chip[NumLivelli];
    private TextView[] completeTesti = new TextView[NumLivelli];
    private CircularProgressIndicator[] barProgressi = new CircularProgressIndicator[NumLivelli];
    private TextView[] percProgs = new TextView[NumLivelli];

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

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
            int finalI = i;
            chips[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Create Intent
                    Intent intent = new Intent(MainActivity.this, LivelloActivity.class);
                    intent.putExtra("livello",finalI);
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
                barProgressi[livello].setProgress(totAcc);
                percProgs[livello].setText(totAcc+"%");
                if(totAcc==100){
                    completeTesti[livello].setVisibility(View.VISIBLE);
                }
            }
            //System.out.println("STAMMI A SENTIRE, ALLORA LO STATO DELLA Accuracy = "+totAcc);
        }
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
