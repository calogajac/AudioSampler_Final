package com.example.caleblong.audiosampler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.MediaRecorder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;

import static android.graphics.Color.rgb;

public class Sampler extends Activity {

    private SoundPool sp;
    private int sound1, sound2, sound3 = 0;
    private int p1, p2, p3;
    private MediaRecorder mRecorder = null;
    private String file1, file2, file3 = null;
    private boolean isRecording, isPlaying1, isPlaying2, isPlaying3 = false;
    Button uS1, uS2, uS3, record;
    CheckBox l1, l2, l3;
    SeekBar pitch1, pitch2, pitch3;
    RadioButton s1, s2, s3;
    private boolean s1Checked, s2Checked, s3Checked, l1Checked, l2Checked, l3Checked;
    private float min = (float) 0.5;
    private File f1, f2, f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampler);
        uS1 = (Button) findViewById(R.id.unloadSlot1);
        uS2 = (Button) findViewById(R.id.unloadSlot2);
        uS3 = (Button) findViewById(R.id.unloadSlot3);
        l1 = (CheckBox) findViewById(R.id.loop1);
        l2 = (CheckBox) findViewById(R.id.loop2);
        l3 = (CheckBox) findViewById(R.id.loop3);
        record = (Button) findViewById(R.id.recordButton);
        pitch1 = (SeekBar) findViewById(R.id.pitch1);
        pitch2 = (SeekBar) findViewById(R.id.pitch2);
        pitch3 = (SeekBar) findViewById(R.id.pitch3);
        s1 = (RadioButton) findViewById(R.id.slot1);
        s2 = (RadioButton) findViewById(R.id.slot2);
        s3 = (RadioButton) findViewById(R.id.slot3);
        pitch1.setProgress(pitch1.getMax()/2);
        pitch2.setProgress(pitch2.getMax()/2);
        pitch3.setProgress(pitch3.getMax()/2);
        sp = new SoundPool(3,AudioManager.STREAM_MUSIC,0);
        file1 = Environment.getExternalStorageDirectory()+"/file1.3gpp";
        file2 = Environment.getExternalStorageDirectory()+"/file2.3gpp";
        file3 = Environment.getExternalStorageDirectory()+"/file1.3gpp";
        f1 = new File(file1);
        f2 = new File(file2);
        f3 = new File(file3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sampler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void recordButtonClick(View view){
        s1Checked = s1.isChecked();
        s2Checked = s2.isChecked();
        s3Checked = s3.isChecked();
        if(isRecording){
            mRecorder.stop();
            if(s1Checked){
                s1.setEnabled(false);
                if(sound2 == 0){
                    s2.setEnabled(true);
                }
                if(sound3 == 0){
                    s3.setEnabled(true);
                }
                sound1 = sp.load(file1, 1);
            } else if(s2Checked){
                s2.setEnabled(false);
                if(sound1 == 0){
                    s1.setEnabled(true);
                }
                if(sound3 == 0){
                    s3.setEnabled(true);
                }
                sound2 = sp.load(file2, 1);
            } else {
                s3.setEnabled(false);
                if(sound1 == 0){
                    s1.setEnabled(true);
                }
                if(sound2 == 0){
                    s2.setEnabled(true);
                }
                sound3 = sp.load(file3, 1);
            }
            record.setBackgroundColor(rgb(220,221,221));
            record.setTextColor(rgb(0,0,0));
            mRecorder.release();
            mRecorder = null;
            isRecording = false;
        } else {
            int slot = 0;
            if(s1Checked){
                s2.setEnabled(false);
                s3.setEnabled(false);
                slot = 1;
            } else if(s2Checked){
                s1.setEnabled(false);
                s3.setEnabled(false);
                slot = 2;
            } else if(s3Checked){
                s2.setEnabled(false);
                s1.setEnabled(false);
                slot = 3;
            } else {
                mRecorder = null;
            }
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            if(slot == 1){
                mRecorder.setOutputFile(file1);
            } else if(slot == 2){
                mRecorder.setOutputFile(file2);
            } else {
                mRecorder.setOutputFile(file3);
            }
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
            record.setBackgroundColor(rgb(255,0,0));
            record.setTextColor(rgb(255,255,255));
            isRecording = true;
        }
    }

    public void sample1Click(View view){
        s1Checked = s1.isChecked();
        l1Checked = l1.isChecked();
        p1 = pitch1.getProgress();
        if(sound1!= 0){
            if(isPlaying1){
                if(l1Checked){
                    if(p1 == 0){
                        sp.stop(sound1);
                        sp.play(sound1, 1, 1, 0, -1, min);
                    } else if(p1 == 1){
                        sp.stop(sound1);
                        sp.play(sound1, 1, 1, 0, -1, 1);
                    } else {
                        sp.stop(sound1);
                        sp.play(sound1, 1, 1, 0, -1, 2);
                    }
                } else {
                    if(p1 == 0){
                        sp.stop(sound1);
                        sp.play(sound1, 1, 1, 0, 0, min);
                        isPlaying1 = false;
                    } else if(p1 == 1){
                        sp.stop(sound1);
                        sp.play(sound1, 1, 1, 0, 0, 1);
                        isPlaying1 = false;
                    } else {
                        sp.stop(sound1);
                        sp.play(sound1, 1, 1, 0, 0, 2);
                        isPlaying1 = false;
                    }
                }
            } else {
                if(l1Checked){
                    if(p1 == 0){
                        sp.play(sound1, 1, 1, 0, -1, min);
                        isPlaying1 = true;
                    }
                    if(p1 == 1){
                        sp.play(sound1, 1, 1, 0, -1, 1);
                        isPlaying1 = true;
                    } else {
                        sp.play(sound1, 1, 1, 0, -1, 2);
                        isPlaying1 = true;
                    }
                } else {
                    if(p1 == 0){
                        sp.play(sound1, 1, 1, 0, 0, min);
                    } else if(p1 == 1){
                        sp.play(sound1, 1, 1, 0, 0, 1);
                    } else {
                        sp.play(sound1, 1, 1, 0, 0, 2);
                    }
                }
            }
        }
    }

    public void sample2Click(View view){
        s2Checked = s2.isChecked();
        l2Checked = l2.isChecked();
        p2 = pitch2.getProgress();
        if(sound1!= 0){
            if(isPlaying2){
                if(l2Checked){
                    if(p2 == 0){
                        sp.stop(sound2);
                        sp.play(sound2, 1, 1, 0, -1, min);
                    } else if(p1 == 2){
                        sp.stop(sound2);
                        sp.play(sound2, 1, 1, 0, -1, 1);
                    } else {
                        sp.stop(sound2);
                        sp.play(sound2, 1, 1, 0, -1, 2);
                    }
                } else {
                    if(p2 == 0){
                        sp.stop(sound2);
                        sp.play(sound2, 1, 1, 0, 0, min);
                        isPlaying2 = false;
                    } else if(p2 == 1){
                        sp.stop(sound2);
                        sp.play(sound2, 1, 1, 0, 0, 1);
                        isPlaying2 = false;
                    } else {
                        sp.stop(sound2);
                        sp.play(sound2, 1, 1, 0, 0, 2);
                        isPlaying2 = false;
                    }
                }
            } else {
                if(l2Checked){
                    if(p2 == 0){
                        sp.play(sound2, 1, 1, 0, -1, min);
                        isPlaying2 = true;
                    }
                    if(p2 == 1){
                        sp.play(sound2, 1, 1, 0, -1, 1);
                        isPlaying2 = true;
                    } else {
                        sp.play(sound2, 1, 1, 0, -1, 2);
                        isPlaying2 = true;
                    }
                } else {
                    if(p2 == 0){
                        sp.play(sound2, 1, 1, 0, 0, min);
                    } else if(p2 == 1){
                        sp.play(sound2, 1, 1, 0, 0, 1);
                    } else {
                        sp.play(sound2, 1, 1, 0, 0, 2);
                    }
                }
            }
        }
    }

    public void sample3Click(View view){
        s3Checked = s3.isChecked();
        l3Checked = l3.isChecked();
        p3 = pitch3.getProgress();
        if(sound3!= 0){
            if(isPlaying3){
                if(l3Checked){
                    if(p3 == 0){
                        sp.stop(sound3);
                        sp.play(sound3, 1, 1, 0, -1, min);
                    } else if(p3 == 1){
                        sp.stop(sound3);
                        sp.play(sound3, 1, 1, 0, -1, 1);
                    } else {
                        sp.stop(sound3);
                        sp.play(sound3, 1, 1, 0, -1, 2);
                    }
                } else {
                    if(p3 == 0){
                        sp.stop(sound3);
                        sp.play(sound3, 1, 1, 0, 0, min);
                        isPlaying3 = false;
                    } else if(p3 == 1){
                        sp.stop(sound3);
                        sp.play(sound3, 1, 1, 0, 0, 1);
                        isPlaying3 = false;
                    } else {
                        sp.stop(sound3);
                        sp.play(sound3, 1, 1, 0, 0, 2);
                        isPlaying3 = false;
                    }
                }
            } else {
                if(l3Checked){
                    if(p3 == 0){
                        sp.play(sound3, 1, 1, 0, -1, min);
                        isPlaying3 = true;
                    }
                    if(p3 == 1){
                        sp.play(sound3, 1, 1, 0, -1, 1);
                        isPlaying3 = true;
                    } else {
                        sp.play(sound3, 1, 1, 0, -1, 2);
                        isPlaying3 = true;
                    }
                } else {
                    if(p3 == 0){
                        sp.play(sound3, 1, 1, 0, 0, min);
                    } else if(p3 == 1){
                        sp.play(sound3, 1, 1, 0, 0, 1);
                    } else {
                        sp.play(sound3, 1, 1, 0, 0, 2);
                    }
                }
            }
        }
    }

    public void unload(View view){
        switch(view.getId()){
            case R.id.unloadSlot1:
                if(sound1 != 0){
                    sp.unload(sound1);
                    sound1 = 0;
                    s1.setEnabled(true);
                }
                break;
            case R.id.unloadSlot2:
                if(sound2 != 0){
                    sp.unload(sound2);
                    sound2 = 0;
                }
                sp.unload(sound2);
                sound2 = 0;
                s2.setEnabled(true);
                uS2.setEnabled(false);
                break;
            case R.id.unloadSlot3:
                sp.unload(sound3);
                sound3 = 0;
                s3.setEnabled(true);
                uS3.setEnabled(false);
                break;
            default:
                break;
        }
    }

    public void stopLoop(View view){
        if(isPlaying1){
            sp.stop(sound1);
            isPlaying1 = false;
        } else if(isPlaying2){
            sp.stop(sound2);
            isPlaying2 = false;
        } else if(isPlaying3){
            sp.stop(sound3);
            isPlaying3 = false;
        } else {

        }
    }
}
