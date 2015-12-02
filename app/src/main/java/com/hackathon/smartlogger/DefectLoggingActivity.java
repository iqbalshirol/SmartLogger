package com.hackathon.smartlogger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.hackathon.smartlogger.gateway.listener.RequestCallbackListener;
import com.hackathon.smartlogger.gateway.request.DefectRequest;
import com.hackathon.smartlogger.gateway.response.DefectResponse;

import java.util.ArrayList;
import java.util.UUID;

public class DefectLoggingActivity extends Activity implements View.OnClickListener {

    private static final int RESULT_SPEECH = 1001;
    // private TextView txtText;
    private EditText editTextSub;
    private EditText editTextDesc;
    private Button btnSpeak;
    private RadioGroup radioGroupPriority;
    private RadioGroup radioGroupSeverity;
    private Button btnSubmit;
    private Switch switchScreenshot;
    private DefectDTO defectDTO;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logger);
        startService(new Intent(this, ChatHeadService.class));

        editTextSub = (EditText) findViewById(R.id.edittextSubject);
        editTextDesc = (EditText) findViewById(R.id.edittextDesc);
        radioGroupPriority = (RadioGroup) findViewById(R.id.radioGroupPriority);
        radioGroupSeverity = (RadioGroup) findViewById(R.id.radioGroupSeverity);

        btnSpeak = (Button) findViewById(R.id.btnSpeak);

        switchScreenshot = (Switch) findViewById(R.id.attachScreenshot);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
        btnSpeak.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (editTextSub.getText().toString().equals(""))
                        editTextSub.setText(text.get(0));
                    else
                        editTextDesc.setText(editTextDesc.getText().toString() + "\n" + text.get(0));
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSpeak:
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
                break;

            case R.id.btnSubmit:
                defectDTO = new DefectDTO();
                submitData();
                break;

            default:
                break;

        }
    }

    private void submitData() {
        String priority = "";
        switch (radioGroupPriority.getCheckedRadioButtonId()) {
            case R.id.radioP1:
                priority = "P1-High";
                break;
            case R.id.radioP2:
                priority = "P2-Medium";
                break;
            case R.id.radioP3:
                priority = "P3-Low";
                break;
            default:
                break;
        }
        defectDTO.setPriority(priority);

        String severity = "";
        switch (radioGroupSeverity.getCheckedRadioButtonId()) {
            case R.id.radioS1:
                severity = "S1-Crash";
                break;
            case R.id.radioS2:
                severity = "S2-Major";
                break;
            case R.id.radioS3:
                severity = "S3-Minor";
                break;
            default:
                break;
        }
        defectDTO.setSeverity(severity);

        String screenshotData = null;
        if (switchScreenshot.isChecked())
            screenshotData = IOUtils.getLastCapturedScreenshot();

        defectDTO.setImageData(screenshotData);

        String defectSubject = editTextSub.getText().toString();
        String defectDescription = editTextDesc.getText().toString();

        defectDTO.setSubject(defectSubject);
        defectDTO.setDescription(defectDescription);

        defectDTO.setAuthorEmail("iqbal.shirol@techendeavour.com");
        defectDTO.setAuthor("Iqbal Shirol");
        defectDTO.setImageName(UUID.randomUUID() + ".png");
        defectDTO.setProjectID("PROJ_12345");

        if (defectSubject.equals("") || defectDescription.equals("")) {
            Toast.makeText(this, "Subject/Description cannot be empty", Toast.LENGTH_SHORT).show();
        } else {

            if (NetworkUtils.isNetworkAvailable(DefectLoggingActivity.this)) {
                showLoader();
                new DefectRequest(defectDTO, new RequestCallbackListener() {
                    @Override
                    public void onResponseReceived(final DefectDTO responseData) {
                        Log.i("DefectLoggingResponse ", "   " + responseData.getResponseObj().toString());
                        DefectLoggingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DefectResponse response = (DefectResponse) responseData.getResponseObj();
                                if (response.getServiceResponseStatus().equals("1")) {
                                    Toast.makeText(DefectLoggingActivity.this, "Defect posted successfully !!", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(DefectLoggingActivity.this, "Defect posting fail", Toast.LENGTH_LONG).show();
                                }
                                hideLoader();
                            }
                        });

                    }

                    @Override
                    public void onError(int code, String message) {
                        DefectLoggingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoader();
                            }
                        });
                        Log.i("DefectLoggingResponse ", "   " + message);
                    }
                }).execute();
            }
        }

    }

    public void showLoader() {
        progressBar = new ProgressDialog(DefectLoggingActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }

    public void hideLoader() {
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }
    }
}
