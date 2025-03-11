// OnboardingActivity.java
package com.example.foodify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.example.foodify.R;
import com.example.foodify.utils.PreferenceManager;

public class OnboardingActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private PreferenceManager preferenceManager;
    private SignInButton signInButton;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    handleSignInResult(data);
                } else {
                    // User cancelled the sign-in flow
                    Toast.makeText(this, "Google Sign-In is required to use Foodify app",
                            Toast.LENGTH_LONG).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize preference manager
        preferenceManager = new PreferenceManager(this);

        // Check if user is already signed in
        if (isUserSignedIn()) {
            navigateToNextScreen();
            return;
        }

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set up Google Sign-In button
        signInButton = findViewById(R.id.google_sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(view -> signIn());

        // Add welcome text and explanation why Google Sign-In is required
        // This would be set in the XML layout
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);

            // Google Sign-In successful, authenticate with Firebase
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            Toast.makeText(this, "Google Sign-In failed: " + e.getStatusCode(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        preferenceManager.setUserLoggedIn(true);
                        navigateToNextScreen();
                    } else {
                        // Sign in fails
                        Toast.makeText(OnboardingActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isUserSignedIn() {
        return preferenceManager.isUserLoggedIn() && mAuth.getCurrentUser() != null;
    }

    private void navigateToNextScreen() {
        // If first login, go to user profile setup
        if (!preferenceManager.isProfileComplete()) {
            startActivity(new Intent(this, UserProfileActivity.class));
        } else {
            // Otherwise go to main app
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}