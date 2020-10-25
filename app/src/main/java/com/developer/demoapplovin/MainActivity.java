package com.developer.demoapplovin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements MaxAdListener {

    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this);
        this.createInterstitialAd();
    }

    void createInterstitialAd() {
        interstitialAd = new MaxInterstitialAd("YOUR_AD_UNIT_ID", this);
        interstitialAd.setListener(this);

        // Load the first ad
        interstitialAd.loadAd();
    }

    @Override
    public void onAdLoaded(MaxAd ad) {
        retryAttempt = 0;
    }

    @Override
    public void onAdLoadFailed(String adUnitId, int errorCode) {
        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                interstitialAd.loadAd();
            }
        }, delayMillis);
    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {
        interstitialAd.loadAd();
    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, int errorCode) {
        interstitialAd.loadAd();
    }
}