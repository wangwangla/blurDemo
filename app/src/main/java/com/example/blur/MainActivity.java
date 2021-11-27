package com.example.blur;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    protected int scalex = 200;
    protected int scaley = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GLSurfaceView surfaceView = findViewById(R.id.image);
        surfaceView.setEGLContextClientVersion(2);
        final MyRender myRender = new MyRender(this);
        surfaceView.setRenderer(myRender);
        SeekBar width1 = findViewById(R.id.width);
        SeekBar height = findViewById(R.id.height);
        SeekBar sigma = findViewById(R.id.sigma);
        SeekBar radius = findViewById(R.id.radius);

        width1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println(i+"aaaaaaaa");
                scalex = i;
                myRender.renderObject.setScaleSize(scalex,scaley);
                myRender.change();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println(i+"zzzzzzzzzzzzzzzzzzzz");
                scaley = i;
                myRender.renderObject.setScaleSize(scalex,scaley);
                myRender.change();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println(i+"------------");
                myRender.renderObject.setBlurRadius(i);
                myRender.change();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sigma.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println(i+"=============");
                myRender.renderObject.setSigma(i);
                myRender.change();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}