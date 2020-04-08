package lincete.galaxyegg.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import lincete.galaxyegg.R;


public class OldMainActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    int x;
    boolean musica,animacion = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Resources res = getResources();
        x= res.getInteger(R.integer.trolaso);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_main);
        TextView hola = (TextView) findViewById(R.id.textoArriba);
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        x = prefe.getInt("x",x);


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("45911882A55FD5CC2EE1FE195FAC0771")
                .build();

       mAdView.loadAd(adRequest);
        hola.setText("" + x);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            x = savedInstanceState.getInt("x", x);
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6174269253938832/2941077908");
        mInterstitialAd.loadAd(adRequest);


        TiempoAnuncio();
        }

    public void Animacion(){

        TextView Arriba = (TextView)findViewById(R.id.textoArriba);
        if(x>0) {
            x--;
            Arriba.setText(""+x);

        }
        else{
            Arriba.setText(R.string.ganado);
        }

        final ImageView img = (ImageView)findViewById(R.id.Imagen);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.peropot);


        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                animacion = false;
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                animacion = true;
            }
        });

        if(animacion) {
            img.startAnimation(animation1);
        }
    }

public void onClick(View v)
{



    Animacion();
    Musica();

}




    public void TiempoAnuncio()
    {
        new CountDownTimer(850000, 40000) {

            public void onTick(long millisUntilFinished) {

                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
            }

            public void onFinish() {
                TiempoAnuncio();

            }
        }.start();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();

            }
        });

    }










    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()

                .build();

        mInterstitialAd.loadAd(adRequest);
    }
    public void onSaveInstanceState (Bundle savedInstanceState){
            savedInstanceState.putInt("x", x);
            super.onSaveInstanceState(savedInstanceState);
        }

        public void Musica() {


            MediaPlayer mp = MediaPlayer.create(this, R.raw.blop2);
            if (!musica) {
                mp.stop();
                mp.release();
            } else {
                if(musica) {

                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();

                        }
                    });
                    Log.d("musica",""+!mp.isPlaying());
                }
            }











    }
        public void buttonOnClick2(View v) {
            ImageView imagen2 = (ImageView)findViewById(R.id.imageView);
        if(musica)
        {
            imagen2.setImageResource(R.drawable.voff);
            musica = false;
        }
            else
        {
            imagen2.setImageResource(R.drawable.von);
            musica = true;
        }
        }


    @Override
    protected void onStop(){

        super.onStop();

        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putInt("x", x);
        editor.apply();
        }



    @Override
    public void onDestroy() {

        super.onDestroy();
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putInt("x", x);
        editor.apply();

    }

        }





