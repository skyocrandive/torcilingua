package com.example.texttospeech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LivelloActivity extends AppCompatActivity {
    //public static final Integer RecordAudioRequestCode = 1;
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
    private int livello=0;
    private String testoSciogli;


    int precisione(String testo, String parlato){
        testo = testo.replaceAll("\\p{Punct}","");
        parlato = parlato.replaceAll("\\p{Punct}","");
        String[] paroleTesto = testo.split(" ");
        String[] paroleParlato = parlato.split(" ");
        int corrette = 0;
        int lenghtTesto = paroleTesto.length;
        int lenghtParlato = paroleParlato.length;
        for (int i = 0; i< lenghtTesto && i<lenghtParlato; i++){
            if(paroleTesto[i].equalsIgnoreCase(paroleParlato[i])){
                corrette++;
            }
        }
        int dividendo = Math.max(lenghtTesto, lenghtParlato);
        return 100*corrette/dividendo;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        System.out.println("ciao mondo!!!!");
        System.out.println("avvio il livello n. "+getIntent().getIntExtra("livello",1));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livello);

        livello = getIntent().getIntExtra("livello", 1);

        String[] scioglilinguas = getIntent().getStringArrayExtra("scioglilingua");
        List<String> testi = new ArrayList<>(Arrays.asList(scioglilinguas));
        Collections.shuffle(testi);
        posizione = 0;
        lunghezzaLivello = testi.size()-1;

        testoSciogli = testi.get(0);

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
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

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
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String res = data.get(0);
                parlatoText.setText(res);
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }

        });

        micButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                speechRecognizer.stopListening();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                speechRecognizer.startListening(speechRecognizerIntent);
            }
            return false;
        });

        nextButton.setOnClickListener(v -> {
            if(posizione==lunghezzaLivello){
                totAcc = (accuracy+totAcc)/(lunghezzaLivello+1);
                Intent intent = new Intent(LivelloActivity.this, MainActivity.class);
                intent.putExtra("livello",livello);
                intent.putExtra("totAcc", totAcc);
                setResult(RESULT_OK, intent);
                finish();
                return;
            }
            posizione++;
            totAcc =accuracy+totAcc;
            levelProg.setProgress(100*posizione/lunghezzaLivello);
            testoSciogli= testi.get(posizione);
            sciogliText.setText(testoSciogli);
            parlatoText.setText("");
            accuracyText.setText("");
            parlatoText.setHint("");
            ratio.setProgress(0);
            accuracy=0;
            if(posizione==lunghezzaLivello){
                nextButton.setText("finisci");
            }
        });

        homeButton.setOnClickListener(v -> {
            totAcc = (accuracy+totAcc)/(lunghezzaLivello+1);
            Intent intent = new Intent(LivelloActivity.this, MainActivity.class);
            intent.putExtra("livello",livello);
            intent.putExtra("totAcc", totAcc);
            setResult(RESULT_OK, intent);
            finish();
            //startActivity(intent);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

}
