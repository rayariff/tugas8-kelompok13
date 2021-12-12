package com.base.camp.shippingcharges.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.camp.shippingcharges.R;
import com.base.camp.shippingcharges.fragment.HargaFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    public static final String TAG = "DashboardActivity";
    private InterstitialAd mInterstitialAd;


    private TextView id_harga,id_tracking,id_riwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        LinearLayout frame_harga = findViewById(R.id.frame_harga);

        id_harga = findViewById(R.id.id_harga);




        frame_harga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpBold();
                loadFragment(new HargaFragment(),id_harga );

            }
        });

        setupAds();
    }

    private void setUpBold() {
        id_harga.setTextColor(getResources().getColor(R.color.dark_grey));
        id_harga.setTextSize(11);
        id_harga.setTypeface(null, Typeface.NORMAL);
    }

    private void setupAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {}
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Test ADS
        List<String> testDeviceIds = Arrays.asList("26DC06918F5716E811DB033ED977EC46","D41FF4E34C0094ACC312EC342E1B5BA6");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        //Test ADS

        InterstitialAd.load(this,getResources().getString(R.string.ads_app_Interstisial),
                adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.e(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.e(TAG, "onAdFailedToLoad");

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.e(TAG, "onAdOpened");

            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }


    private void loadFragment(Fragment fragment, TextView textView) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.menu, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        textView.setTextSize(12);
        textView.setTypeface(null, Typeface.BOLD);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.e(TAG, "The interstitial wasn't loaded yet.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
