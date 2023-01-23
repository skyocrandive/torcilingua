package com.example.texttospeech;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Locale;

public class LivelloActivity extends AppCompatActivity {
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private TextView parlatoText;
    private TextView sciogliText;
    private Button homeButton;
    private ImageView micButton;
    private Chip nextButton;
    private TextView accuracyText;

    String testoSciogli = "Trentatré Trentini entrarono a Trento tutti e trentatré trotterellando";

    float precisione(String testo, String parlato){
        String[] paroleTesto = testo.split(" ");
        String[] paroleParlato = parlato.split(" ");
        int corrette = 0;
        int i = 0;
        for (i = 0; i< paroleTesto.length && i<paroleParlato.length; i++){
            if(paroleTesto[i].equalsIgnoreCase(paroleParlato[i])){
                corrette++;
            }
        }
        float dividendo = i < paroleTesto.length ? paroleTesto.length : paroleParlato.length;
        return ((float)corrette/dividendo)*100;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livello);
        sciogliText = findViewById(R.id.scioglilingua);
        sciogliText.setText(testoSciogli);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        parlatoText = findViewById(R.id.parlato);
        micButton = findViewById(R.id.mic);
        homeButton = findViewById(R.id.goHome);
        nextButton = findViewById(R.id.nextBut);
        accuracyText = findViewById(R.id.accuracyText);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ITALY.toString()); //it sets language recognition to the language of the device

        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                parlatoText.setText("");
                parlatoText.setHint("Sto ascoltando...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                micButton.setImageResource(R.mipmap.mic_def);
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                micButton.setImageResource(R.mipmap.mic_def);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String res = data.get(0);
                float accuracy = precisione(testoSciogli, res);
                parlatoText.setText(res);
                accuracyText.setText("Accuracy: "+ accuracy+"%");
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }

        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micButton.setImageResource(R.mipmap.mic_green);

                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

        homeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(LivelloActivity.this,MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
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
