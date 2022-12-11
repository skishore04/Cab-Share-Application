package com.example.mycabshare.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycabshare.FoundActivity;
import com.example.mycabshare.MapsActivity2;
import com.example.mycabshare.R;
import com.example.mycabshare.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    Button find;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        find =(Button) root.findViewById(R.id.button);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity2.class);
                startActivity(intent);
            }
        });
        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

        

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}