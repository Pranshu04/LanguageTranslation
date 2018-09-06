package com.example.pranshu.languagetranslation;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    TextView input;
    TextView output;
    FloatingActionButton fab;
    String s;
    private static final String API_KEY = "Your Api Key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.tv_input);
        output = findViewById(R.id.tv_output);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);

        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Intent Problem", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                input.setText(result.get(0));

                final Handler textViewHandler = new Handler();

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        TranslateOptions options = TranslateOptions.newBuilder()
                                .setApiKey(API_KEY)
                                .build();
                        Translate translate = options.getService();
                        final Translation translation =
                                translate.translate(input.getText().toString(),
                                        Translate.TranslateOption.targetLanguage("hi"));
                        textViewHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (input != null) {
                                    output.setText(translation.getTranslatedText());
                                }
                            }
                        });
                        return null;
                    }
                }.execute();

            }
        }
    }
}
