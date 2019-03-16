package sagsaguz.sagarwonupit.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import sagsaguz.sagarwonupit.R;
import sagsaguz.sagarwonupit.animation.FlipView;
import sagsaguz.sagarwonupit.listener.DoubleClickListener;

public class PostFragment extends Fragment {

    private CardView postView;
    private static int colorID;
    private int colorId1;

    private FlipView flipView;

    private TextView hexValue;

    public PostFragment(){}

    public static PostFragment newInstance(int color) {
        PostFragment fragment = new PostFragment();
        colorID = color;
        return fragment;
    }

    public void setColorID(int color){
        colorId1 = color;
    }

    public int getColorID(){
        return colorId1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_item, container, false);

        postView = view.findViewById(R.id.postView);
        postView.setBackgroundColor(getResources().getColor(colorId1));

        flipView = view.findViewById(R.id.flipView);

        hexValue = view.findViewById(R.id.hexValue);
        hexValue.setText("HEX value is #" + Integer.toHexString(ContextCompat.getColor(getActivity(), colorId1) & 0x00ffffff));

        view.setOnClickListener(new DoubleClickListener(){
            @Override
            public void onSingleClick(View v) {
            }
            @Override
            public void onDoubleClick(View v) {
                flipView.flipTheView();
            }
        });

        return view;
    }
}
