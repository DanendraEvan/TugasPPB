package com.example.aplikasi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FIlter extends AppCompatActivity {

    private ImageView imageView;
    private Button btnGrayscale, btnSepia, btnReset, btnSelectImage;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Hubungkan view dengan ID
        imageView = findViewById(R.id.imageView);
        btnGrayscale = findViewById(R.id.btnGrayscale);
        btnSepia = findViewById(R.id.btnSepia);
        btnReset = findViewById(R.id.btnReset);


        // Pilih gambar dari galeri
        imageView.setOnClickListener(v -> openGallery());

        // Filter Grayscale
        btnGrayscale.setOnClickListener(v -> applyGrayscaleFilter());

        // Filter Sepia
        btnSepia.setOnClickListener(v -> applySepiaFilter());

        // Reset ke gambar asli
        btnReset.setOnClickListener(v -> resetFilter());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap); // Tampilkan gambar ke ImageView
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Gagal memuat gambar!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void applyGrayscaleFilter() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0); // Menurunkan saturasi menjadi 0 (grayscale)
        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    private void applySepiaFilter() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(1f, 0.95f, 0.82f, 1f); // Efek sepia dengan menyesuaikan R, G, dan B
        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    private void resetFilter() {
        imageView.clearColorFilter(); // Menghapus semua filter dari ImageView
    }
}
