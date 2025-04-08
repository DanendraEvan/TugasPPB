package com.example.aplikasi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    Button kamerabtn, galeribtn, editbtn, brightbtn, filterbtn;
    Uri gambarUri;
    static final int KODE_KAMERA = 100;
    private static final int IZIN_KAMERA = 101;
    private static final int IZIN_STORAGE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tombol-tombol yang bakalan dipencet user
        kamerabtn = findViewById(R.id.kameraBtn);
        galeribtn = findViewById(R.id.galeriBtn);
        editbtn = findViewById(R.id.editBtn);
        brightbtn = findViewById(R.id.kecerahanBtn);
        filterbtn = findViewById(R.id.filterbtn);

        // Kalo user klik tombol kamera
        kamerabtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Minta izin kamera, kalo belom dikasih
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, IZIN_KAMERA);
            } else {
                // Langsung buka kamera, coy!
                bukaKamera();
            }
        });

        // Kalo klik tombol galeri, masuk ke activity Album
        galeribtn.setOnClickListener(v -> {
            Intent galeriIntent = new Intent(MainActivity.this, Album.class);
            startActivity(galeriIntent);
        });

        // Klik tombol edit, pindah ke Crop
        editbtn.setOnClickListener(v -> {
            Intent editImg = new Intent(MainActivity.this, Crop.class);
            startActivity(editImg);
        });

        // Tombol buat kecerahan
        brightbtn.setOnClickListener(v -> {
            Intent brightImg = new Intent(MainActivity.this, brightness.class);
            startActivity(brightImg);
        });

        // Tombol filter
        filterbtn.setOnClickListener(v -> {
            Intent brightImg = new Intent(MainActivity.this, FIlter.class);
            startActivity(brightImg);
        });

        // Cek izin storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, IZIN_STORAGE);
        }
    }

    private void bukaKamera() {
        Intent kameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (kameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(kameraIntent, KODE_KAMERA);
        } else {
            Toast.makeText(this, "Kamera ga ada, bro", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == IZIN_KAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bukaKamera();
            } else {
                Toast.makeText(this, "Izin kamera itu kudu, bro!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == IZIN_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin storage dikasih, mantap!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Izin storage penting buat simpen gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == KODE_KAMERA && resultCode == RESULT_OK) {
            Bitmap foto = (Bitmap) data.getExtras().get("data");
            gambarUri = simpenGambar(foto);

            Intent bukaAlbumIntent = new Intent(MainActivity.this, Album.class);
            bukaAlbumIntent.putExtra("ImageUri", gambarUri.toString());
            startActivity(bukaAlbumIntent);
        }
    }

    private Uri simpenGambar(Bitmap foto) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), foto, "Foto", null);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path)));
        return Uri.parse(path);
    }
}
