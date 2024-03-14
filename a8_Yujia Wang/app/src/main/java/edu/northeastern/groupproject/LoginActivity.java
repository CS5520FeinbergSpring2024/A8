package edu.northeastern.groupproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etUsername;
    private Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Make sure to create this layout

        mAuth = FirebaseAuth.getInstance();
        etUsername = findViewById(R.id.etUsername);
        btnProceed = findViewById(R.id.btnProceed);

        if (mAuth.getCurrentUser() != null) {
            // User is already signed in, check if username is associated
            checkUsernameAndProceed();
        } else {
            // No user is signed in, proceed with anonymous authentication
            signInAnonymously();
        }

        btnProceed.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            if (!username.isEmpty()) {
                associateUsernameWithUser(username);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter a username.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign-in success
                Toast.makeText(LoginActivity.this, "Authenticated anonymously.", Toast.LENGTH_SHORT).show();
            } else {
                // If sign-in fails
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUsernameAndProceed() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("usernames").child(uid);
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Username is associated, proceed to Sticker Activity
                        navigateToStickerActivity();
                    } else {
                        // No username associated, stay and prompt for a username
                        Toast.makeText(LoginActivity.this, "Please enter a username to proceed.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("LoginActivity", "checkUsername:onCancelled", databaseError.toException());
                    Toast.makeText(LoginActivity.this, "Failed to check username. Try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void associateUsernameWithUser(String username) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("usernames").child(uid);
            databaseRef.setValue(username).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Username set successfully.", Toast.LENGTH_SHORT).show();
                    navigateToStickerActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to set username. Try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToStickerActivity() {
        Intent intent = new Intent(LoginActivity.this, StickerActivity.class);
        startActivity(intent);
        finish(); // Optional: Finish LoginActivity so the user doesn't return to it on pressing back from StickerActivity
    }

}

