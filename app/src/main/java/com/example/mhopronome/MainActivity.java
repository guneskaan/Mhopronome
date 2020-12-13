package com.example.mhopronome;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView mhopCat = findViewById(R.id.imageView1);


        NumberPicker picker1 = findViewById(R.id.picker1);
        picker1.setMaxValue(220);
        picker1.setMinValue(1);
        final int[] pickerValsInts = IntStream.range(1, 221).toArray();
        final String[] pickerVals = Arrays.stream(pickerValsInts)
                                        .mapToObj(String::valueOf)
                                        .toArray(String[]::new);

        picker1.setDisplayedValues(pickerVals);
        picker1.setValue(120);
        picker1.setWrapSelectorWheel(false);

        Timer timer = new Timer();

        Switch switch1 = findViewById(R.id.switch1);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.mhop_sound);
        mp.setVolume(1.0f, 1.0f);
        final TimerTask[] mhop = new TimerTask[1];

        Runnable mhopBack = () -> mhopCat.setImageResource(R.drawable.mhop1);
        Handler mhopHandler = new Handler();

        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                final int mhopPerMinute = picker1.getValue();
                mhop[0] = new MhopTask(mp, mhopCat, mhopBack, mhopHandler);
                timer.scheduleAtFixedRate(mhop[0], 100, 60000/mhopPerMinute);
                return;
            };

            mhop[0].cancel();
        });

        picker1.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (switch1.isChecked()){
                mhop[0].cancel();
                mhop[0] = new MhopTask(mp, mhopCat, mhopBack, mhopHandler);
                timer.scheduleAtFixedRate(mhop[0], 100, 60000/newVal);
            }
        });
    }
}

class MhopTask extends TimerTask {
    MediaPlayer mp;
    ImageView mhopCat;
    Runnable mhopBack;
    Handler mhopHandler;

    MhopTask(MediaPlayer mp, ImageView mhopCat, Runnable mhopBack, Handler mhopHandler){
        this.mp = mp;
        this.mhopCat = mhopCat;
        this.mhopBack = mhopBack;
        this.mhopHandler = mhopHandler;
    }
    public void run() {
        mp.start();
        mhopCat.setImageResource(R.drawable.mhop2);

        mhopHandler.postDelayed(mhopBack, 100);
    }
}
