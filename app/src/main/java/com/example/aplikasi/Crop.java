package com.example.aplikasi;

import android.Manifest;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Crop extends AppCompatActivity {

    private static final int PILIH_GAMBAR = 1;
    private static final int KODE_IJIN_PENYIMPANAN = 100;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        imageView = findViewById(R.id.imageView);

        // Periksa izin penyimpanan saat runtime
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, KODE_IJIN_PENYIMPANAN);
        } else {
            pilihGambarDariGaleri();
        }
    }

    // Fungsi untuk memilih gambar dari galeri
    private void pilihGambarDariGaleri() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PILIH_GAMBAR);
    }

    // Menangani hasil pemilihan gambar dan memulai pemangkasan dengan UCrop
    @Override
    protected void onActivityResult(int kodePermintaan, int kodeHasil, Intent data) {
        super.onActivityResult(kodePermintaan, kodeHasil, data);

        if (kodePermintaan == PILIH_GAMBAR && kodeHasil == RESULT_OK && data != null) {
            Uri gambarTerpilih = data.getData();
            mulaiPemangkasanGambar(gambarTerpilih);
        }

        if (kodePermintaan == UCrop.REQUEST_CROP && kodeHasil == RESULT_OK) {
            Uri uriHasil = UCrop.getOutput(data);
            imageView.setImageURI(uriHasil);  // Menampilkan gambar yang sudah dipangkas

            // Simpan hasil crop ke galeri
//            simpanGambarKeGaleri(uriHasil);  // Menyimpan gambar hasil crop ke galeri
        } else if (kodePermintaan == UCrop.REQUEST_CROP && kodeHasil == UCrop.RESULT_ERROR) {
            Throwable kesalahanPemangkasan = UCrop.getError(data);
            kesalahanPemangkasan.printStackTrace();
        }
    }

    // Fungsi untuk memulai pemangkasan gambar menggunakan UCrop
    private void mulaiPemangkasanGambar(Uri uri) {
        String namaFileTujuan = System.currentTimeMillis() + ".jpg";
        Uri uriTujuan = Uri.fromFile(new File(getCacheDir(), namaFileTujuan));

        UCrop.of(uri, uriTujuan)
                .withAspectRatio(1, 1)  // Menentukan rasio aspek gambar
                .withMaxResultSize(1000, 1000)  // Resolusi maksimal gambar hasil crop
                .start(this);  // Memulai pemangkasan
    }

    // Fungsi untuk menyimpan gambar ke galeri
//    private void simpanGambarKeGaleri(Uri uri) {
//        try {
//            // Mendapatkan path asli dari URI
//            String pathGambar = ambilPathDariURI(uri);
//            File fileSumber = new File(pathGambar);
//
//            // Tentukan nama file gambar yang akan disalin
//            String namaFileTujuan = "Aplikasi_" + System.currentTimeMillis() + ".jpg";
//
//            // Tentukan lokasi penyimpanan menggunakan MediaStore untuk Android 10 dan yang lebih baru
//            ContentValues values = new ContentValues();
//            values.put(MediaStore.Images.Media.DISPLAY_NAME, namaFileTujuan);
//            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Album"); // Menyimpan ke folder MyApp
//
//            Uri uriEksternal = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//            if (uriEksternal != null) {
//                // Salin file gambar ke MediaStore
//                try (InputStream inputStream = new FileInputStream(fileSumber);
//                     OutputStream outputStream = getContentResolver().openOutputStream(uriEksternal)) {
//                    byte[] buffer = new byte[1024];
//                    int panjang;
//                    while ((panjang = inputStream.read(buffer)) > 0) {
//                        outputStream.write(buffer, 0, panjang);
//                    }
//
//                    // Menambahkan gambar ke galeri secara otomatis
//                    MediaScannerConnection.scanFile(this, new String[]{uriEksternal.getPath()}, null, null);
//
//                    Toast.makeText(this, "Gambar berhasil disimpan ke galeri!", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Gagal menyimpan gambar!", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Gagal menyimpan gambar!", Toast.LENGTH_SHORT).show();
//        }
//    }

    // Fungsi untuk mendapatkan path file dari URI
//    private String ambilPathDariURI(Uri uri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
////        Cursor cursor = loader.loadInBackground();
////        int indexKolom = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
////        cursor.moveToFirst();
////        return cursor.getString(indexKolom);
//}
}
