package com.example.aplikasi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class brightness extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Kode permintaan untuk galeri

    private ImageView imageView;
    private SeekBar seekBarBrightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness);

        // Hubungkan view dengan ID
        imageView = findViewById(R.id.imageView);
        seekBarBrightness = findViewById(R.id.seekBarBrightness);

        // Atur nilai awal SeekBar
        seekBarBrightness.setMax(200); // Nilai max 200 (100 = normal brightness)
        seekBarBrightness.setProgress(100); // Nilai awal (normal brightness)

        // Tambahkan listener untuk SeekBar
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Tidak digunakan
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Tidak digunakan
            }
        });

        // Memilih gambar dari galeri
        imageView.setOnClickListener(view -> openGallery());
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

    private void adjustBrightness(int progress) {
        // Hitung faktor kecerahan
        float brightnessFactor = progress / 100f; // Dari 0.0 (gelap) hingga 2.0 (terang)

        // Buat ColorMatrix untuk kecerahan
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(brightnessFactor, brightnessFactor, brightnessFactor, 1); // R, G, B, A

        // Terapkan ColorFilter ke ImageView
        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }
}
