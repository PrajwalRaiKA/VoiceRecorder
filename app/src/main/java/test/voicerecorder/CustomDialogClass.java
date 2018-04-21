package test.voicerecorder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    private Context context;
    private String fileName;
    private EditText etNewName;
    private Button create, cancle;

    public CustomDialogClass(@NonNull Context context, String fileName) {
        super(context);
        this.context = context;
        this.fileName = fileName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.naming_dialog);
        etNewName = findViewById(R.id.et_recording_name);
        findViewById(R.id.bt_cancle).setOnClickListener(this);
        findViewById(R.id.bt_create).setOnClickListener(this);

        etNewName.setText(fileName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancle:
                File directory1 = new File(Environment.getExternalStorageDirectory(), "VoiceRecorder");
                File from1 = new File(directory1, fileName);
                from1.delete();
                CustomDialogClass.this.dismiss();
                break;
            case R.id.bt_create:
                File directory = new File(Environment.getExternalStorageDirectory(), "VoiceRecorder");
                File from = new File(directory, fileName);
                String newName;
                if (etNewName.getText().toString().contains(".wav")) {
                    newName = etNewName.getText().toString();
                } else {
                    newName = etNewName.getText().toString() + ".wav";
                }
                File to = new File(directory, newName);
                boolean success = from.renameTo(to);
                if (success) {
                    Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
                CustomDialogClass.this.dismiss();
                break;
        }
    }
}