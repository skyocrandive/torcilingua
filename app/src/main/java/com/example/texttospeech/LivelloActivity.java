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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    private CircularProgressIndicator ratio;
    private LinearProgressIndicator levelProg;
    private int posizione = 0;
    private int lunghezzaLivello = 0;
    private int totAcc = 0;
    private int accuracy=0;

    String testoSciogli = "Trentatré Trentini entrarono a Trento tutti e trentatré trotterellando";


    int precisione(String testo, String parlato){
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
        return (int) (100*corrette/dividendo);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        System.out.println("ciao mondo!!!!");
        System.out.println("avvio il livello n. "+getIntent().getIntExtra("livello",1));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livello);

        int livello = getIntent().getIntExtra("livello", 1);

        String[] scioglilinguas = getIntent().getStringArrayExtra("scioglilingua");
        List testi = new ArrayList(Arrays.asList(scioglilinguas));
        Collections.shuffle(testi);
        posizione = 0;
        lunghezzaLivello = testi.size();

        testoSciogli = (String) testi.get(0);

        sciogliText = findViewById(R.id.scioglilingua);
        sciogliText.setText(testoSciogli);

        parlatoText = findViewById(R.id.parlato);
        micButton = findViewById(R.id.mic);
        homeButton = findViewById(R.id.goHome);
        nextButton = findViewById(R.id.nextBut);
        accuracyText = findViewById(R.id.accuracyText);
        ratio = findViewById(R.id.accuracy);
        levelProg = findViewById(R.id.levProg);
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
                micButton.setImageResource(R.mipmap.mic_green);
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
                parlatoText.setHint("riprova");
            }

            @Override
            public void onResults(Bundle bundle) {

                micButton.setImageResource(R.mipmap.mic_def);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String res = data.get(0);
                accuracy = precisione(testoSciogli, res);
                parlatoText.setText(res);
                accuracyText.setText(accuracy+"%");
                ratio.setProgress(accuracy);

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
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                posizione++;
                totAcc =(accuracy+totAcc)/posizione;
                levelProg.setProgress(100*posizione/lunghezzaLivello);
                testoSciogli= (String) testi.get(posizione);
                sciogliText.setText(testoSciogli);
                parlatoText.setText("");
                accuracyText.setText("");
                ratio.setProgress(0);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totAcc = (accuracy+totAcc)/posizione;
                Intent intent = new Intent(LivelloActivity.this, MainActivity.class);
                intent.putExtra("livello",livello);
                intent.putExtra("totAcc", totAcc);
                setResult(RESULT_OK, intent);
                finish();
                //startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

}
