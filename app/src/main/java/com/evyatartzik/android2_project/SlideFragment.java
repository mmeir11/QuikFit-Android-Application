package com.evyatartzik.android2_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SlideFragment extends Fragment {

    public static SlideFragment newInstance(int num) {

        SlideFragment slideAdapter = new SlideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("step",num);
        slideAdapter.setArguments(bundle);
        return slideAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slide_layout,container,false);
        TextView header = root.findViewById(R.id.slide_heading);
        TextView desc = root.findViewById(R.id.slide_desc);
        LottieAnimationView lottieAnimationView= root.findViewById(R.id.slide_image);
        Step step = Step.values()[getArguments().getInt("step")];
        int backgrounds[] = {R.drawable.backgroud_step1, R.drawable.backgroud_step1, R.drawable.backgroud_step1};

        switch(step)
        {
            case Step1:
                header.setText(R.string.header1);
                desc.setText(R.string.description1);
                lottieAnimationView.setAnimation("loading.json");
                root.setBackgroundResource(backgrounds[0]);
                break;
            case Step2:
                root.setBackgroundResource(backgrounds[1]);
                header.setText(R.string.header2);
                desc.setText(R.string.description2);
                lottieAnimationView.setAnimation("sport.json");
                break;
            case Step3:
                root.setBackgroundResource(backgrounds[2]);
                header.setText(R.string.header3);
                desc.setText(R.string.description3);
                lottieAnimationView.setAnimation("done.json");
                break;
        }
        return root;
    }
}
