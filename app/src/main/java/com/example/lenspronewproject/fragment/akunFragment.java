package com.example.lenspronewproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenspronewproject.R;

import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class akunFragment extends Fragment {
    TextView tvName,tvEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_akun, container, false);

        // edge-to-edge padding (optional)
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });


        // grab the TextViews
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvEmail = view.findViewById(R.id.tvEmail);

        // load from Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference ref = FirebaseDatabase
                    .getInstance("https://lenspro-7d702-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Users")
                    .child(currentUser.getUid());

            ref.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            User u = snapshot.getValue(User.class);
                            if (u != null) {
                                tvName.setText(u.username);
                                tvEmail.setText(u.gmail);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Optional: handle error
                        }
                    }
            );
        }

        return view;
    }

    // User class
    public static class User {
        public String username, gmail;

        public User() {
            // Default constructor required for Firebase
        }

        public User(String username, String gmail) {
            this.username = username;
            this.gmail = gmail;
        }
    }
}
