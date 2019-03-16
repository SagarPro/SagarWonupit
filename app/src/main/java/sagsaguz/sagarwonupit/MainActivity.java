package sagsaguz.sagarwonupit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sagsaguz.sagarwonupit.adapter.CardsAdapter;
import sagsaguz.sagarwonupit.fragment.ThreadFragment;
import sagsaguz.sagarwonupit.listener.RecyclerClickListener;
import sagsaguz.sagarwonupit.model.Card;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ConstraintLayout clMain;

    private List<String> titleList = new ArrayList<>();
    private List<String> descList = new ArrayList<>();
    private List<Integer> colorList = new ArrayList<>();
    private List<String> actionList = new ArrayList<>();

    private List<Integer> selectedColorList = new ArrayList<>();

    private List<Card> cardList = new ArrayList<>();
    private RecyclerView rvCards;
    private CardsAdapter cardsAdapter;

    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("WonUpIt Test App");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpCards();
                pullToRefresh.setRefreshing(false);
            }
        });

        rvCards = findViewById(R.id.rvCards);
        rvCards.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCards.setLayoutManager(linearLayoutManager);
        cardsAdapter = new CardsAdapter(MainActivity.this, cardList);
        rvCards.setAdapter(cardsAdapter);

        setUpCards();

        rvCards.addOnItemTouchListener(new RecyclerClickListener(getApplicationContext(), rvCards, new RecyclerClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                selectedPosition = position;

                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("Colors", (ArrayList<Integer>) selectedColorList);
                bundle.putInt("Color", selectedColorList.get(position));
                ThreadFragment myObj = new ThreadFragment();
                myObj.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.your_placeholder, myObj).addToBackStack( null ).commit();

            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.section1) {
            // Handle the camera action
        } else if (id == R.id.section2) {

        } else if (id == R.id.section3) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpCards(){

        setTitleList();
        setDescList();
        setColorList();
        setActionList();

        cardList.clear();
        for (int i=0; i<25; i++){
            Card card = new Card();
            card.setTitle(generateRandomTitle());
            card.setDescription(generateRandomDesc());
            card.setTime(generateRandomTime());
            card.setAction(generateRandomAction());
            card.setColor(generateRandomColor());
            cardList.add(card);
            selectedColorList.add(card.getColor());
            colorList.remove(colorList.indexOf(card.getColor()));
        }
        cardsAdapter.notifyDataSetChanged();
    }

    private String generateRandomTitle(){
        Random r = new Random();
        int number = r.nextInt(4);
        return titleList.get(number);
    }

    private String generateRandomDesc(){
        Random r = new Random();
        int number = r.nextInt(2);
        return descList.get(number);
    }

    private int generateRandomColor(){
        Random r = new Random();
        int number = r.nextInt(colorList.size());
        return colorList.get(number);
    }

    private String generateRandomAction(){
        Random r = new Random();
        int number = r.nextInt(2);
        return actionList.get(number);
    }

    private String generateRandomTime(){
        String time, hour, min, aa, timeZone = "IST";
        Random r1 = new Random();
        int number1 = r1.nextInt(2);
        if (number1 == 0)
            aa = "AM";
        else aa = "PM";
        Random r2 = new Random();
        int number2 = r2.nextInt(3);
        switch (number2){
            case 0: timeZone = "IST";
                break;
            case 1: timeZone = "EST";
                break;
            case 2: timeZone = "GMT";
                break;
        }
        Random r3 = new Random();
        int number3 = r3.nextInt(12 - 1);
        hour = String.valueOf(number3);
        Random r4 = new Random();
        int number4 = r4.nextInt(2);
        if (number4 == 0)
            min = "00";
        else min = "30";
        time = hour+":"+min+" "+aa+" "+timeZone;
        return time;
    }

    private void setTitleList(){
        titleList.clear();
        titleList.add("Text from 2 to 40 characters");
        titleList.add("In this case it is 40 characters of text");
        titleList.add("Short text");
        titleList.add("Random text string");
    }

    private void setDescList(){
        descList.clear();
        descList.add("Some cards will have one line of text");
        descList.add("Some cards will have two lines of text. Just like this. This is long text");
    }

    private void setColorList(){
        selectedColorList.clear();
        colorList.clear();
        colorList.add(R.color.color1);
        colorList.add(R.color.color2);
        colorList.add(R.color.color3);
        colorList.add(R.color.color4);
        colorList.add(R.color.color5);
        colorList.add(R.color.color6);
        colorList.add(R.color.color7);
        colorList.add(R.color.color8);
        colorList.add(R.color.color9);
        colorList.add(R.color.color10);
        colorList.add(R.color.color11);
        colorList.add(R.color.color12);
        colorList.add(R.color.color13);
        colorList.add(R.color.color14);
        colorList.add(R.color.color15);
        colorList.add(R.color.color16);
        colorList.add(R.color.color17);
        colorList.add(R.color.color18);
        colorList.add(R.color.color19);
        colorList.add(R.color.color20);
        colorList.add(R.color.color21);
        colorList.add(R.color.color22);
        colorList.add(R.color.color23);
        colorList.add(R.color.color24);
        colorList.add(R.color.color25);
    }

    private void setActionList(){
        actionList.clear();
        actionList.add("Action Taken");
        actionList.add("No Action");
    }

    public void setCardColor(int color){
        Card card = new Card();
        card.setTitle(cardList.get(selectedPosition).getTitle());
        card.setDescription(cardList.get(selectedPosition).getDescription());
        card.setTime(cardList.get(selectedPosition).getTime());
        card.setAction(cardList.get(selectedPosition).getAction());
        card.setColor(color);
        selectedColorList.set(selectedPosition, color);
        cardList.set(selectedPosition, card);
        cardsAdapter.notifyDataSetChanged();
    }

}
