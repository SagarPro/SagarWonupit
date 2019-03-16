package sagsaguz.sagarwonupit.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sagsaguz.sagarwonupit.MainActivity;
import sagsaguz.sagarwonupit.R;
import sagsaguz.sagarwonupit.adapter.ThumbnailAdapter;
import sagsaguz.sagarwonupit.adapter.ViewPagerAdapter;
import sagsaguz.sagarwonupit.listener.RecyclerClickListener;

public class ThreadFragment extends Fragment {

    private List<Integer> colorList = new ArrayList<>();
    private List<Float> opacityList = new ArrayList<>();
    private ThumbnailAdapter thumbnailAdapter;

    private int colorSelected;
    private int selectedPosition;

    private ViewPager vpPost;
    private TextView rgbValue;

    private LinearLayout llBottom, llBottom1;
    private Toolbar toolbar;

    private int selectedColor = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.thread_view_layout, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Thread");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        if (getArguments() != null) {
            colorList = getArguments().getIntegerArrayList("Colors");
            colorSelected = getArguments().getInt("Color");
        }

        Collections.shuffle(colorList);

        rgbValue = view.findViewById(R.id.rgbValue);

        for (int i=0; i<colorList.size(); i++)
            opacityList.add(0.2f);

        for (int i=0; i<colorList.size(); i++){
            if (colorSelected == colorList.get(i)){
                selectedPosition = i;
                opacityList.set(selectedPosition, 1.0f);
                break;
            }
        }

        RecyclerView rvThumbnails = view.findViewById(R.id.thumbnailView);
        rvThumbnails.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvThumbnails.setLayoutManager(linearLayoutManager);
        thumbnailAdapter = new ThumbnailAdapter(getActivity(), colorList, opacityList);
        rvThumbnails.setAdapter(thumbnailAdapter);

        rvThumbnails.getLayoutManager().scrollToPosition(selectedPosition);
        rgbValue.setText("RGB value is ("+getRGBValue(colorList.get(selectedPosition))+")");

        vpPost = view.findViewById(R.id.vpPost);
        vpPost.setSaveFromParentEnabled(false);
        setViewPager(vpPost);

        rvThumbnails.addOnItemTouchListener(new RecyclerClickListener(getActivity(), rvThumbnails, new RecyclerClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                for (int i=0; i<colorList.size(); i++)
                    opacityList.set(i, 0.2f);
                opacityList.set(position, 1.0f);
                thumbnailAdapter.notifyDataSetChanged();
                vpPost.setCurrentItem(position, true);
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        llBottom = view.findViewById(R.id.llBottom);
        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedColor < 0){
                    llBottom.setVisibility(View.GONE);
                    llBottom1.setVisibility(View.VISIBLE);
                    selectedColor = vpPost.getCurrentItem();
                    ((MainActivity) getActivity()).setCardColor(colorList.get(selectedColor));
                } else {
                    Toast.makeText(getActivity(), "Color is already selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llBottom1 = view.findViewById(R.id.llBottom1);
        llBottom1.setOnClickListener(null);
        llBottom1.setVisibility(View.GONE);

        return view;
    }

    private void setViewPager(ViewPager viewPager){

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(), colorList);
        for (int i=0; i<colorList.size(); i++)
            viewPagerAdapter.addFragment(PostFragment.newInstance(colorList.get(i)));
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setCurrentItem(selectedPosition);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                rgbValue.setText("RGB value is ("+getRGBValue(colorList.get(position))+")");
                if (position == selectedColor){
                    llBottom.setVisibility(View.GONE);
                    llBottom1.setVisibility(View.VISIBLE);
                } else {
                    llBottom.setVisibility(View.VISIBLE);
                    llBottom1.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private String getRGBValue(int colorID){
        int color = getResources().getColor(colorID);
        int red=   (color >> 16) & 0xFF;
        int green= (color >> 8) & 0xFF;
        int blue=  (color) & 0xFF;
        return red+","+green+","+blue;
    }

}
