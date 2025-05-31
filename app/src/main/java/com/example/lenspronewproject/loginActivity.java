package com.example.lenspronewproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {

    private EditText editTextUserOrEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewGoToRegister;
    private FirebaseAuth firebaseAuth;
    private static final String DB_URL = "https://lenspro-7d702-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1) Find views
        editTextUserOrEmail = findViewById(R.id.username);
        editTextPassword    = findViewById(R.id.password);
        buttonLogin         = findViewById(R.id.submitButton);
        textViewGoToRegister= findViewById(R.id.textViewGoToRegister);

        // 2) Get FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // 3) Login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userOrEmail = editTextUserOrEmail.getText().toString().trim();
                String password    = editTextPassword.getText().toString().trim();

                if (userOrEmail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                            loginActivity.this,
                            "Isi semua field",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                if (userOrEmail.contains("@")) {
                    // Langsung treat as email
                    signInWithEmail(userOrEmail, password);
                } else {
                    // Treat as username: fetch email mapping
                    DatabaseReference ref = FirebaseDatabase
                            .getInstance(DB_URL)
                            .getReference("UsernameToEmail")
                            .child(userOrEmail);

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String email = snapshot.getValue(String.class);
                                signInWithEmail(email, password);
                            } else {
                                Toast.makeText(
                                        loginActivity.this,
                                        "Username tidak ditemukan",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(
                                    loginActivity.this,
                                    "Gagal ambil data: " + error.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                }
            }
        });

        // 4) Go to Register screen
        textViewGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(
                                loginActivity.this,
                                register.class
                        )
                );
            }
        });
    }

    /**
     * Sign in menggunakan email dan password
     */
    private void signInWithEmail(String email, String password) {
        firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login berhasil: pindah ke MainActivity
                            Intent intent = new Intent(
                                    loginActivity.this,
                                    MainActivity.class
                            );
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(
                                    loginActivity.this,
                                    "Login gagal: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                });
    }
}
