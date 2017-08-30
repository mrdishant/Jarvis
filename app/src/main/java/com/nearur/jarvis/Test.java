package com.nearur.jarvis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIConfiguration;
import ai.api.AIListener;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class Test extends AppCompatActivity implements AIListener{

    private AIService aiService;
    private AIDataService aiDataService;
    final ai.api.android.AIConfiguration config = new       ai.api.android.AIConfiguration("bb1c2f3fb8b24619b31abd641e184459",
            AIConfiguration.SupportedLanguages.English,
            ai.api.android.AIConfiguration.RecognitionEngine.System);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        aiDataService = new AIDataService(this, config);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        AIRequest aiRequest=new AIRequest();
        aiRequest.setQuery("Hello");
        aiService.startListening();
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString =result.getFulfillment().getSpeech();
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();
            }
        }

        // Show results in TextView.
        Toast.makeText(this,"Query:" + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
