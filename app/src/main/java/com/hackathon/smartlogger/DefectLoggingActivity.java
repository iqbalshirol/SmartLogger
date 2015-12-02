package com.hackathon.smartlogger;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import com.hackathon.smartlogger.R;

public class DefectLoggingActivity extends Activity implements View.OnClickListener {

    private static final int RESULT_SPEECH = 1001;
    // private TextView txtText;
    private EditText editTextSub;
    private EditText editTextDesc;
    private ImageButton btnSpeak;
    private RadioGroup radioGroupPriority;
    private RadioGroup radioGroupSeverity;
    private Button btnSubmit;
    private CheckBox checkBoxScreenshot;
    private DefectDTO defectDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, ChatHeadService.class));

        editTextSub = (EditText) findViewById(R.id.edittextSubject);
        editTextDesc = (EditText) findViewById(R.id.edittextDesc);
        radioGroupPriority = (RadioGroup) findViewById(R.id.radioGroupPriority);
        radioGroupSeverity = (RadioGroup) findViewById(R.id.radioGroupSeverity);

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        checkBoxScreenshot = (CheckBox) findViewById(R.id.attachScreenshot);
        btnSubmit = (Button) findViewById(R.id.btmSubmit);

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

            case R.id.btmSubmit:
                defectDTO = new DefectDTO();
                submitData();
                break;

            default:
                break;

        }
    }

    private void submitData() {
        int priority = 0;
        switch (radioGroupPriority.getCheckedRadioButtonId()) {
            case R.id.radioP1:
                priority = 1;
                break;
            case R.id.radioP2:
                priority = 2;
                break;
            case R.id.radioP3:
                priority = 3;
                break;
            default:
                break;
        }
        defectDTO.setPriority(priority);

        int severity = 0;
        switch (radioGroupSeverity.getCheckedRadioButtonId()) {
            case R.id.radioS1:
                severity = 1;
                break;
            case R.id.radioS2:
                severity = 2;
                break;
            case R.id.radioS3:
                severity = 3;
                break;
            default:
                break;
        }
        defectDTO.setSeverity(severity);

        String screenshotData = null;
        if (checkBoxScreenshot.isChecked())
            screenshotData = IOUtils.getLastCapturedScreenshot();

        defectDTO.setImageData(screenshotData);

        String defectSubject = editTextSub.getText().toString();
        String defectDescription = editTextDesc.getText().toString();

        defectDTO.setSubject(defectSubject);
        defectDTO.setDescription(defectDescription);

        if (defectSubject.equals("") || defectDescription.equals(""))
            Toast.makeText(this, "Subject/Description cannot be empty", Toast.LENGTH_SHORT).show();
        else
            NetworkManager.submitdataToServer(defectDTO);

    }
}
