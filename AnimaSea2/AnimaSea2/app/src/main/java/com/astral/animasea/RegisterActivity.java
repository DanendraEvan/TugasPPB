package com.astral.animasea;

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

public class RegisterActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword, editTextGmail;
    Button buttonRegister;
    ImageView buttonBack;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextGmail = findViewById(R.id.editTextGmail);
        buttonRegister = findViewById(R.id.buttonRegister);


        buttonRegister.setOnClickListener(view -> finish()); // kembali ke halaman login

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
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase.getInstance("https://animasea-project4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference("Users")
                                    .child(user.getUid())
                                    .setValue(new User(username, gmail));

                            Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, LoginActivity.class));
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
