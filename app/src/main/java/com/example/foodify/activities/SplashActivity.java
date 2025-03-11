// SplashActivity.java
package com.example.foodify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.example.foodify.R;
import com.example.foodify.utils.PreferenceManager;

/**
 * Entry point activity that handles app startup and directs users
 * to the appropriate screen based on their authentication status
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1500; // 1.5 seconds
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize preference manager
        preferenceManager = new PreferenceManager(this);

        // Delay to show splash screen, then check auth status
        new Handler(Looper.getMainLooper()).postDelayed(this::checkAuthAndProceed, SPLASH_DELAY);
    }

    private void checkAuthAndProceed() {
        // Check if user is signed in
        if (mAuth.getCurrentUser() != null && preferenceManager.isUserLoggedIn()) {
            // User is signed in
            if (preferenceManager.isProfileComplete()) {
                // Profile setup is complete, go to main app
                startActivity(new Intent(this, MainActivity.class));
            } else {
                // User is authenticated but needs to complete profile
                startActivity(new Intent(this, UserProfileActivity.class));
            }
        } else {
            // User is not signed in, go to onboarding
            startActivity(new Intent(this, OnboardingActivity.class));
        }
        finish();
    }
}