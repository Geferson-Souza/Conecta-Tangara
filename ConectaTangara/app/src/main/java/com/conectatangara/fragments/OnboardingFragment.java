package com.conectatangara.fragments; // Ou seu pacote de fragments

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatangara.R; // Adicione este import se não estiver lá

public class OnboardingFragment extends Fragment {

    private static final String ARG_IMAGE_RES = "image_res";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";

    public static OnboardingFragment newInstance(@DrawableRes int imageRes, String title, String description) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RES, imageRes);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_screen, container, false); // Use o layout genérico

        ImageView imageView = view.findViewById(R.id.iv_onboarding_image);
        TextView titleView = view.findViewById(R.id.tv_onboarding_title);
        TextView descView = view.findViewById(R.id.tv_onboarding_description);

        if (getArguments() != null) {
            imageView.setImageResource(getArguments().getInt(ARG_IMAGE_RES));
            titleView.setText(getArguments().getString(ARG_TITLE));
            descView.setText(getArguments().getString(ARG_DESC));
        }

        return view;
    }
}