package de.coldcore.homework1ds.homework1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class TextToSpeechActivity extends Activity implements View.OnClickListener, TextToSpeech.OnInitListener {


    public static final int REQUEST_CODE_TTS_CHECK = 1;
    private TextToSpeech mTts;
    private Button btnStartTalking;
    private EditText txtTextToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        btnStartTalking = (Button) findViewById(R.id.btnStartToSpeech);
        if(btnStartTalking != null) {
            btnStartTalking.setOnClickListener(this);
        }
        txtTextToSpeech = (EditText) findViewById(R.id.txtTextToSpeech);

        Intent checkTts = new Intent();
        checkTts.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTts, REQUEST_CODE_TTS_CHECK);
    }

    @Override
    public void onClick(View view) {
        //If text to speech button is clicked
        if(view.getId() == R.id.btnStartToSpeech) {
            //start talking!!
            if(txtTextToSpeech != null && mTts != null && txtTextToSpeech.getText().length() > 0 && !mTts.isSpeaking()) {
                mTts.speak(txtTextToSpeech.getText().toString(), TextToSpeech.QUEUE_ADD, null);
            }
        }
    }

    @Override
    public void onInit(int i) {
        //init okay
        if(i == TextToSpeech.SUCCESS) {
            //since we have our lecture in english ;)
            mTts.setLanguage(Locale.US);
        } else {
            //something went wrong so disable button...
            btnStartTalking.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_TTS_CHECK) {
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //tts is ready create it
                mTts = new TextToSpeech(this,this);
            } else {
                //something missing, then install it
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onPause();
        if(mTts != null) {
            mTts.shutdown();
        }
    }
}
