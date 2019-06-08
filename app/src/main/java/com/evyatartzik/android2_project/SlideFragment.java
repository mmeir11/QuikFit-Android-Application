package com.evyatartzik.android2_project;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SlideFragment extends Fragment {

    public static SlideFragment newInstance( List<String> animations, int num) {

        SlideFragment slideAdapter = new SlideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("step",num);
        slideAdapter.setArguments(bundle);
        return slideAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String animation;
        View root = inflater.inflate(R.layout.slide_layout,container,false);
        TextView header = root.findViewById(R.id.slide_heading);
        TextView desc = root.findViewById(R.id.slide_desc);
        LottieAnimationView lottieAnimationView= root.findViewById(R.id.slide_image);
        Step step = Step.values()[getArguments().getInt("step")];
        switch(step)
        {
            case Step1:
                header.setText("Header1");
                desc.setText("Description1");
                lottieAnimationView.setAnimation("loading.json");
                break;
            case Step2:
                header.setText("Header2");
                desc.setText("Description2");
                lottieAnimationView.setAnimation("sport.json");
                break;
            case Step3:
                header.setText("Header3");
                desc.setText("Description3");
                lottieAnimationView.setAnimation("done.json");
                break;
        }
        return root;
    }
}
