package edu.northeastern.groupproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Locale;

public class StickerActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etFriendUsername, etStickerCode;
    private Button btnSendSticker, btnViewHistory, btnViewCounts;
    private TextView tvStickerCounts, tvStickerHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker); // Make sure you have a corresponding layout

        mAuth = FirebaseAuth.getInstance();
        etFriendUsername = findViewById(R.id.etFriendUsername);
        etStickerCode = findViewById(R.id.etStickerCode);
        btnSendSticker = findViewById(R.id.btnSendSticker);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        btnViewCounts = findViewById(R.id.btnViewCounts);
        tvStickerCounts = findViewById(R.id.tvStickerCounts);
        tvStickerHistory = findViewById(R.id.tvStickerHistory);
        ImageView ivSticker = findViewById(R.id.ivSticker);


        signInAnonymously();
        listenForStickers();

        btnSendSticker.setOnClickListener(v -> sendSticker());
        btnViewHistory.setOnClickListener(v -> viewStickerHistory());
        btnViewCounts.setOnClickListener(v -> viewStickerCounts());
    }
    private void displaySticker(String stickerIdentifier) {
        ImageView ivSticker = findViewById(R.id.ivSticker); // Reference the ImageView
        int resourceId = getResources().getIdentifier(stickerIdentifier, "drawable", getPackageName());
        if (resourceId != 0) {
            ivSticker.setImageResource(resourceId);
        } else {
            Log.e("StickerActivity", "Sticker not found: " + stickerIdentifier);
        }
    }

    private void listenForStickers() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String currentUserId = user.getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("sticker_messages");
            dbRef.orderByChild("receiverUid").equalTo(currentUserId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                StickerMessage stickerMessage = snapshot.getValue(StickerMessage.class);
                                if (stickerMessage != null) {
                                    // Handle each received sticker, e.g., display it
                                    Log.d("StickerActivity", "Received sticker: " + stickerMessage.getStickerIdentifier());
                                    displaySticker(stickerMessage.getStickerIdentifier());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("StickerActivity", "Failed to listen for stickers", databaseError.toException());
                        }
                    });
        }
    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(StickerActivity.this, "Authenticated anonymously.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StickerActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface UidCallback {
        void onUidReceived(String uid);
        void onError(String message);
    }

    private void getReceiverUidByUsername(String username, UidCallback callback) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("usernames");
        databaseRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username found, return the associated UID
                    String uid = dataSnapshot.getValue(String.class);
                    callback.onUidReceived(uid);
                } else {
                    // Username not found
                    callback.onError("Username not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                callback.onError("Database error.");
            }
        });
    }

    private void sendSticker() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String senderUid = user.getUid();
            String receiverUsername = etFriendUsername.getText().toString();
            String stickerIdentifier = etStickerCode.getText().toString();

            getReceiverUidByUsername(receiverUsername, new UidCallback() {
                @Override
                public void onUidReceived(String receiverUid) {
                    StickerMessage stickerMessage = new StickerMessage(senderUid, receiverUid, stickerIdentifier, System.currentTimeMillis());
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("sticker_messages");
                    dbRef.push().setValue(stickerMessage)
                            .addOnSuccessListener(aVoid -> Toast.makeText(StickerActivity.this, "Sticker sent successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(StickerActivity.this, "Failed to send sticker", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(StickerActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }



    private void viewStickerHistory() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("stickers").child("sent");
            databaseRef.orderByChild("receiver").equalTo(user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            StringBuilder historyBuilder = new StringBuilder();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Sticker sticker = snapshot.getValue(Sticker.class);
                                if (sticker != null) {
                                    historyBuilder.append("From: ").append(sticker.sender)
                                            .append(", Sticker: ").append(sticker.stickerCode)
                                            .append("\n");
                                }
                            }
                            tvStickerHistory.setText(historyBuilder.toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("StickerActivity", "loadStickerHistory:onCancelled", databaseError.toException());
                        }
                    });
        }
    }


    private void viewStickerCounts() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("stickers").child("sent");
            databaseRef.orderByChild("sender").equalTo(user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int count = (int) dataSnapshot.getChildrenCount();
                            tvStickerCounts.setText(String.format(Locale.getDefault(), "Stickers sent: %d", count));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("StickerActivity", "loadStickerCounts:onCancelled", databaseError.toException());
                        }
                    });
        }
    }

    // Example method to display a sticker in an ImageView
    private void displaySticker(ImageView imageView, String stickerIdentifier) {
        int resourceId = getResources().getIdentifier(stickerIdentifier, "drawable", getPackageName());
        if (resourceId != 0) {
            imageView.setImageResource(resourceId);
        } else {
            // Handle the case where the sticker identifier does not correspond to a drawable resource
            Log.e("StickerActivity", "Sticker not found: " + stickerIdentifier);
            Toast.makeText(this, "Sticker not found: " + stickerIdentifier, Toast.LENGTH_SHORT).show();
        }
    }
    // Example callback where a new sticker message is processed
    public void onNewStickerReceived(String stickerIdentifier) {
        displaySticker(stickerIdentifier);
    }


    // Additional methods for handling the stickers, counts, and history retrieval from Firebase
}



