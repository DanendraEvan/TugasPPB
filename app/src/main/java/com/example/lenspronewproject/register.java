package com.example.lenspronewproject;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    EditText editTextUsername, editTextPassword, editTextGmail;
    Button buttonRegister;
    ImageView buttonBack;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        editTextGmail = findViewById(R.id.email);
        buttonRegister = findViewById(R.id.submitButton);


         // kembali ke halaman login

        buttonRegister.setOnClickListener(view -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String gmail = editTextGmail.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || gmail.isEmpty()) {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(gmail, password)
                    .addOnCompleteListener(task -> {
                        // di onCompleteListener saat registrasi berhasil
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            // 1. Simpan data user (username & email)
                            FirebaseDatabase.getInstance("https://lenspro-7d702-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference("Users")
                                    .child(user.getUid())
                                    .setValue(new User(username, gmail));

                            // 2. **Simpan mapping username → email**
                            FirebaseDatabase.getInstance("https://lenspro-7d702-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference("UsernameToEmail")
                                    .child(username)
                                    .setValue(gmail);

                            Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, loginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // Kelas model untuk simpan ke Realtime Database
    public static class User {
        public String username, gmail;

        public User() {} // diperlukan oleh Firebase
        public User(String username, String gmail) {
            this.username = username;
            this.gmail = gmail;
        }
    }
}
