package com.astral.animasea;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class  LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewGoToRegister;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1) Find views
        editTextEmail        = findViewById(R.id.editTextUsername);
        editTextPassword     = findViewById(R.id.editTextPassword);
        buttonLogin          = findViewById(R.id.buttonLogin);
        textViewGoToRegister = findViewById(R.id.textViewGoToRegister);

        // 2) Get FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // 3) Login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email    = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                            LoginActivity.this,
                            "Isi semua field",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                // 4) Sign in with Firebase
                firebaseAuth
                        .signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                                LoginActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // ✅ Success! Redirect to MainActivity
                                            Intent intent = new Intent(
                                                    LoginActivity.this,
                                                    MainActivity.class
                                            );
                                            // clear back-stack so user can't go back
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(
                                                    LoginActivity.this,
                                                    "Login gagal: " + task.getException().getMessage(),
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    }
                                }
                        );
            }
        });

        // 5) Go to Register screen
        textViewGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(
                                LoginActivity.this,
                                RegisterActivity.class
                        )
                );
            }
        });
    }
}
