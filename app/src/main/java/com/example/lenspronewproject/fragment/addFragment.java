package com.example.lenspronewproject.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lenspronewproject.R;
import com.example.lenspronewproject.photoAdapter;
import com.example.lenspronewproject.photoRepostory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class addFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;

    private RecyclerView photoGrid;
    private FloatingActionButton fabAddPhoto;
    private photoAdapter adapter;
    public static final List<Uri> photoUris = new ArrayList<>();

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        photoGrid = view.findViewById(R.id.photo_grid);
        fabAddPhoto = view.findViewById(R.id.fab_add_photo);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3);
        photoGrid.setLayoutManager(layoutManager);

        adapter = new photoAdapter(requireContext());
        photoGrid.setAdapter(adapter);

        // Load photo list dari repository
        photoUris.clear();
        photoUris.addAll(photoRepostory.getInstance(requireContext()).getPhotoUris());
        adapter.setPhotos(photoUris);

        // Daftarkan launcher untuk mem-pick image
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();

                        if (selectedImageUri != null) {
                            // Simpan ke repository global
                            photoRepostory.getInstance(requireContext()).addPhoto(selectedImageUri);

                            // Update lokal dan UI
                            photoUris.add(selectedImageUri);
                            adapter.notifyItemInserted(photoUris.size() - 1);
                            photoGrid.smoothScrollToPosition(photoUris.size() - 1);
                        }
                    }
                }
        );

        fabAddPhoto.setOnClickListener(v -> checkPermissionsAndOpenGallery());

        return view;
    }

    private void checkPermissionsAndOpenGallery() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
                Toast.makeText(requireContext(), "Gallery access is needed to select photos", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    new String[]{permission},
                    PERMISSION_REQUEST_CODE
            );
        } else {
            launchGalleryIntent();
        }
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchGalleryIntent();
            } else {
                Toast.makeText(requireContext(), "Permission denied. Cannot access gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
