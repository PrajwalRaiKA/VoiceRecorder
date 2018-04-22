package test.voicerecorder;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;
import omrecorder.WriteAction;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "SharedPref";
    Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording, pauseResumeButton;

    TextView timer;
    boolean isRecording = false;
    Random random;
    Handler handler;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    boolean isPaused = false;
    private CircleBarVisualizer mWaveView;
    private String timerText;

    Recorder recorder;
    private String lastFileName;
    private int counter;

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    long startTime = 0L;
    private Handler customHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        glSurfaceView = findViewById(R.id.gl_surface);
//        visualizerView = findViewById(R.id.visual_view);
//        mHorizon = new Horizon(glSurfaceView, getResources().getColor(R.color.background),
//                RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_ENCODING_BIT);

        mWaveView = findViewById(R.id.sample_wave_view);

//        mHorizon.setMaxVolumeDb(MAX_DECIBELS);
        buttonStart = (Button) findViewById(R.id.button);
        buttonStop = (Button) findViewById(R.id.button2);
        pauseResumeButton = findViewById(R.id.button_pause_resume);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3);
        buttonStopPlayingRecording = (Button) findViewById(R.id.button4);

        timer = (TextView) findViewById(R.id.tv_timer);
        timerText = "00:00:00";

        buttonStop.setEnabled(true);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);


        random = new Random();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {
                    setupRecorder();
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    recorder.startRecording();
                    isRecording = true;
                    pauseResumeButton.setEnabled(true);
                    buttonStop.setEnabled(true);
                    buttonStart.setEnabled(false);
                    buttonPlayLastRecordAudio.setEnabled(false);
                    buttonStopPlayingRecording.setEnabled(false);
                } else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecordingStop();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);
                mediaPlayer = new MediaPlayer();
                mWaveView.setPlayer(mediaPlayer.getAudioSessionId());
                try {
                    File directory1 = new File(Environment.getExternalStorageDirectory(), "VoiceRecorder");
                    File from1 = new File(directory1, lastFileName);
                    FileInputStream is = new FileInputStream(from1);
                    mediaPlayer.setDataSource(is.getFD());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                Toast.makeText(MainActivity.this, "Recording Playing", Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            }
        });

        pauseResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recorder == null) {
                    Toast.makeText(MainActivity.this, "Please start recording first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                buttonPlayLastRecordAudio.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(false);
                if (!isPaused) {
                    pauseResumeButton.setText(getString(R.string.resume_recording));
                    recorder.pauseRecording();
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                   /* mWaveView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mWaveView.addAmplitude(0);
                            mWaveView.invalidate();
                        }
                    }, 500);*/
                    isRecording = false;
                } else {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    pauseResumeButton.setText(getString(R.string.pause_recording));
                    recorder.resumeRecording();
                    isRecording = true;
                }
                isPaused = !isPaused;
            }
        });
        handler = new Handler();
    }

    private void onRecordingStop() {
        try {
            isRecording = false;
            buttonStop.setEnabled(false);
            buttonStart.setEnabled(true);
            pauseResumeButton.setEnabled(false);
            recorder.stopRecording();
            startTime = SystemClock.uptimeMillis();
            timeSwapBuff = 0;
            timeInMilliseconds = 0;
            customHandler.removeCallbacks(updateTimerThread);
            renameFile();
            /*mWaveView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWaveView.addAmplitude(0);
                    mWaveView.invalidate();
                }
            }, 500);*/
            buttonPlayLastRecordAudio.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renameFile() {
        CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, lastFileName);
        cdd.show();
    }

    private void setupRecorder() {
//        recorder = OmRecorder.wav(
//                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
//                    @Override
//                    public void onAudioChunkPulled(AudioChunk audioChunk) {
//                        int maxPeak = (int) audioChunk.maxAmplitude();
////                        mHorizon.updateView(audioChunk.toBytes());
//                        /*mWaveView.addAmplitude(maxPeak);
//                        mWaveView.invalidate();*/
//                        byte[] copyBytes = audioChunk.toBytes();
//                        mWaveView.setBytes(copyBytes);
//                        mWaveView.setTimeValue(timerText);
////                        mWaveView.setAmplitude(maxPeak);
//                        mWaveView.invalidate();
//
//                    }
//                }), file());

        recorder = OmRecorder.wav(
                new PullTransport.Noise(mic(),
                        new PullTransport.OnAudioChunkPulledListener() {
                            @Override public void onAudioChunkPulled(AudioChunk audioChunk) {
                                int maxPeak = (int) audioChunk.maxAmplitude();
//                        mHorizon.updateView(audioChunk.toBytes());
                        /*mWaveView.addAmplitude(maxPeak);
                        mWaveView.invalidate();*/
                                byte[] copyBytes = audioChunk.toBytes();
                                mWaveView.setBytes(copyBytes);
                                mWaveView.setTimeValue(timerText);
//                        mWaveView.setAmplitude(maxPeak);
                                mWaveView.invalidate();
                            }
                        },
                        new WriteAction.Default(),
                        new Recorder.OnSilenceListener() {
                            @Override public void onSilence(long silenceTime) {
                                Log.e("silenceTime", String.valueOf(silenceTime));
                                Toast.makeText(MainActivity.this, "silence of " + silenceTime + " detected",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, 200
                ), file()
        );
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private PullableSource mic() {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_STEREO, 44100
                )
        );
    }

    public PullableSource.NoiseSuppressor getMic(){
        return new PullableSource.NoiseSuppressor(
                new PullableSource.Default(
                        new AudioRecordConfig.Default(
                                MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                                AudioFormat.CHANNEL_IN_MONO, 44100
                        )
                )
        );
    }

    @NonNull
    private File file() {
        File audioFolder = new File(Environment.getExternalStorageDirectory(),
                "VoiceRecorder");
        if (!audioFolder.exists()) {
            boolean success = audioFolder.mkdir();
            if (success) {
                return new File(audioFolder, makeFileName());
            }
        } else {
            return new File(audioFolder, makeFileName());
        }
        return new File(Environment.getExternalStorageDirectory(), "Sample.wav");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private String makeFileName() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int count = prefs.getInt("count", 1);
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String fileName = "Recording" + count + "(" + date + ").wav";

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("count", ++count);
        editor.apply();

        lastFileName = fileName;
        return fileName;
    }


    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerText = "" + mins + ":"
                    + String.format("%02d", secs) /*+ ":"
                    + String.format("%03d", milliseconds)*/;
            customHandler.postDelayed(this, 0);
        }
    };



}
