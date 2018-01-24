package in.idk.tattoomaniacs.activity.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.idk.tattoomaniacs.Aboutus;
import in.idk.tattoomaniacs.Disclaimer;
import in.idk.tattoomaniacs.Help;
import in.idk.tattoomaniacs.R;
import in.idk.tattoomaniacs.activity.adapter.GalleryAdapter;
import in.idk.tattoomaniacs.activity.model.Image;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    View view_Group;
    private AdView mAdView;
    private DrawerLayout mDrawerLayout;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<String> listDataHeader;
    private String TAG = MainActivity.class.getSimpleName();

    HashMap<String, List<String>> listDataChild;

    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    //Icons, use as you want
    static int[] icon = {R.drawable.alienicon, R.drawable.lionicon,
            R.drawable.birdicon, R.drawable.demonicicon,
            R.drawable.flowersiconn, R.drawable.boysnew, R.drawable.newgirls,
            R.drawable.loveicon, R.drawable.quotesicon, R.drawable.religiousicon,
            R.drawable.scullicon, R.drawable.tribalicon,R.drawable.othericon};


    //picasa json

    private MenuItem refreshitem;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expandableList.setIndicatorBounds(expandableList.getRight() - 80, expandableList.getWidth());
        } else {
            expandableList.setIndicatorBoundsRelative(expandableList.getRight() - 80, expandableList.getWidth());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//        //requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);



        mAdView = (AdView) findViewById(R.id.adView);

        MobileAds.initialize(this, "ca-app-pub-6222232916855878~1661932946");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);





        Bundle bundle = new Bundle();
        bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6376804581768793409?alt=json");

        Fragment fragment = new ChildGallery();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


//        String endpoint = "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375369972424830321?alt=json";
//        fetchImages(endpoint);


        // Getting the albums from shared preferences


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=" Hi,there...!!! \n Love Tattoos??? Then TattooManiacs designed Only for you. Get it From Link Given Below \n https://play.google.com/store/apps/details?id=in.idk.tattoomaniacs";
                Intent implicitIntent = new Intent(Intent.ACTION_SEND);
                implicitIntent.putExtra(Intent.EXTRA_TEXT, message);
                implicitIntent.setType("text/plain");

                startActivity(Intent.createChooser(implicitIntent, "Share TattooManiacs using"));
            }
        });

        // Start AsyncTask

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        prepareListData();
        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);


        //Listeners in menuitems
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView,
                                        View view,
                                        int groupPosition,
                                        int childPosition, long id) {
                //Log.d("DEBUG", "submenu item clicked");

                if (groupPosition == 0 && childPosition == 0) {
                        getSupportActionBar().setTitle("Aliens-B/W");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375731209142895073?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
//                    String url ="";
//                     fetchImages(url);

                }

                if (groupPosition == 0 && childPosition == 1) {
                    getSupportActionBar().setTitle("Aliens-Colourful");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375727866457190353?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
//                    String url ="";
//                    fetchImages(url);

                }
                if (groupPosition == 1 && childPosition == 0) {
                    getSupportActionBar().setTitle("Animals-Dragon");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375732879006100209?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 1 && childPosition == 1) {
                    getSupportActionBar().setTitle("Animals-Lion");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375733806500804129?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();




                }

                if (groupPosition == 1 && childPosition == 2) {
                    getSupportActionBar().setTitle("Animals-Wolf");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375736704505049457?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();




                }

                if (groupPosition == 1 && childPosition == 3) {
                    getSupportActionBar().setTitle("Animals-Other");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375735642456808609?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


                if (groupPosition == 2 && childPosition == 0) {
                    getSupportActionBar().setTitle("Birds");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375737344983189089?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 2 && childPosition == 1) {
                    getSupportActionBar().setTitle("ButterFlies");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375738514812091905?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


                if (groupPosition == 3 && childPosition == 0) {
                    getSupportActionBar().setTitle("Angels");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375739657929270241?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
                if (groupPosition == 3 && childPosition == 1) {
                    getSupportActionBar().setTitle("Demons");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375741771362476321?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 4 && childPosition == 0) {
                    getSupportActionBar().setTitle("Flowers-B/W");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375744082915227361?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
                if (groupPosition == 4 && childPosition == 1) {
                    getSupportActionBar().setTitle("Flowers-Colourful");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375744810902631137?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }

                if (groupPosition == 5 && childPosition == 0) {
                    getSupportActionBar().setTitle("Boys-Arms");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375747976026147185?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 5 && childPosition == 1) {
                    getSupportActionBar().setTitle("Boys-Back");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375751306629602561?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }

                if (groupPosition == 5 && childPosition == 2) {
                    getSupportActionBar().setTitle("Boys-Chest");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375753110948377441?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


                if (groupPosition == 5 && childPosition == 3) {
                    getSupportActionBar().setTitle("Boys-Other");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375745646873948417?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
                if (groupPosition == 5 && childPosition == 4) {
                    getSupportActionBar().setTitle("Bikes");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375752480765784129?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


                if (groupPosition == 5 && childPosition == 5) {
                    getSupportActionBar().setTitle("Sailor");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375747166958590113?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 6 && childPosition == 0) {
                    getSupportActionBar().setTitle("Girls-Ankle");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375755450305712593?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 6 && childPosition == 1) {
                    getSupportActionBar().setTitle("Girls-Back");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375756435240497329?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
                if (groupPosition == 6 && childPosition == 2) {
                    getSupportActionBar().setTitle("Girls-Fingers");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375757062720322817?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 6 && childPosition == 3) {
                    getSupportActionBar().setTitle("Girls-Legs");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375757675026079585?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


                if (groupPosition == 6 && childPosition == 4) {
                    getSupportActionBar().setTitle("Girls-Wrist");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375758149480203185?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 6 && childPosition == 5) {
                    getSupportActionBar().setTitle("Girls-Other");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375754640113584113?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 7 && childPosition == 0) {
                    getSupportActionBar().setTitle("Heart");

                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375767729623331361?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 7 && childPosition == 1) {
                    getSupportActionBar().setTitle("Love-Quotes");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375768787778255617?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }



                if (groupPosition == 8 && childPosition == 0) {
                    getSupportActionBar().setTitle("English-Quotes");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375769925060903009?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


                if (groupPosition == 8 && childPosition == 1) {
                    getSupportActionBar().setTitle("Sanskrit-Quotes");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375771229384595169?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 8 && childPosition == 2) {
                    getSupportActionBar().setTitle("Kanji-Quates");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375770734797298161?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 9 && childPosition == 0) {
                    getSupportActionBar().setTitle("Christian");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375772851749087489?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
                if (groupPosition == 9 && childPosition == 1) {
                    getSupportActionBar().setTitle("Budhdha");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375771874072799889?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


                if (groupPosition == 9 && childPosition == 2) {
                    getSupportActionBar().setTitle("Hindu");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375773779962911361?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


                if (groupPosition == 9 && childPosition == 3) {
                    getSupportActionBar().setTitle("Muslim");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375774200587887233?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


                if (groupPosition == 10 && childPosition == 0) {
                    getSupportActionBar().setTitle("Skull-B/W");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375775668471587921?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 10 && childPosition == 1) {
                    getSupportActionBar().setTitle("Skull-Colourful");

                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375775253604168145?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

                if (groupPosition == 11 && childPosition == 0) {
                    getSupportActionBar().setTitle("Triable-Designs");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375776768439212657?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }



                if (groupPosition == 12 && childPosition == 0) {
                    getSupportActionBar().setTitle("Miscellaneous");
                    Bundle bundle = new Bundle();
                    bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375769308239989121?alt=json");

                    Fragment fragment = new ChildGallery();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }




                view.setSelected(true);
                if (view_Group != null) {
                    view_Group.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                view_Group = view;
                view_Group.setBackgroundColor(Color.parseColor("#DDDDDD"));
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
                return false;
            }
        });

    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding data header
        listDataHeader.add("Aliens");
        listDataHeader.add("Animals");
        listDataHeader.add("Birds");
        listDataHeader.add("Demonic");
        listDataHeader.add("Flowers");
        listDataHeader.add("For Boys");
        listDataHeader.add("For Girls");
        listDataHeader.add("Love");
        listDataHeader.add("Quotes");
        listDataHeader.add("Religious");
        listDataHeader.add("Skull Designs");
        listDataHeader.add("Tribal Designs");
        listDataHeader.add("Other Designs");


        // Adding child data
        List<String> heading1 = new ArrayList<String>();
        heading1.add("Black & White");
        heading1.add("Colourful");


        List<String> heading2 = new ArrayList<String>();
        heading2.add("Dragon");
        heading2.add("Lion");
        heading2.add("Wolf");
        heading2.add("Other");


        List<String> heading3 = new ArrayList<String>();
        heading3.add("Birds");
        heading3.add("ButterFlies");

        List<String> heading4 = new ArrayList<String>();
        heading4.add("Angel");
        heading4.add("Demon");

        List<String> heading5 = new ArrayList<String>();
        heading5.add("Black & White");
        heading5.add("Colourful");

        List<String> heading6 = new ArrayList<String>();
        heading6.add("Arm");
        heading6.add("Back");
        heading6.add("Chest");
        heading6.add("Other");
        heading6.add("Bike");
        heading6.add("Sailor");


        List<String> heading7 = new ArrayList<String>();
        heading7.add("Ankle");
        heading7.add("Back");
        heading7.add("Fingers");
        heading7.add("Legs");
        heading7.add("Wrist");
        heading7.add("Other");

        List<String> heading8 = new ArrayList<String>();
        heading8.add("Heart");
        heading8.add("Quotes");


        List<String> heading9 = new ArrayList<String>();
        heading9.add("English");
        heading9.add("Sanskrit");
        heading9.add("Kanji");



        List<String> heading10 = new ArrayList<String>();
        heading10.add("Christian");
        heading10.add("Budhdha");
        heading10.add("Hindu");
        heading10.add("Muslim");


        List<String> heading11 = new ArrayList<String>();
        heading11.add("Black & White");
        heading11.add("Colourful");



        List<String> heading12 = new ArrayList<String>();
        heading12.add("Tribal Designs");



        List<String> heading13 = new ArrayList<String>();
        heading13.add("Other Designs");






        listDataChild.put(listDataHeader.get(0), heading1);// Header, Child data
        listDataChild.put(listDataHeader.get(1), heading2);
        listDataChild.put(listDataHeader.get(2), heading3);
        listDataChild.put(listDataHeader.get(3), heading4);
        listDataChild.put(listDataHeader.get(4), heading5);
        listDataChild.put(listDataHeader.get(5), heading6);
        listDataChild.put(listDataHeader.get(6), heading7);
        listDataChild.put(listDataHeader.get(7), heading8);
        listDataChild.put(listDataHeader.get(8), heading9);
        listDataChild.put(listDataHeader.get(9), heading10);
        listDataChild.put(listDataHeader.get(10), heading11);
        listDataChild.put(listDataHeader.get(11), heading12);
        listDataChild.put(listDataHeader.get(12), heading13);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("TattooManiacs");

            alertDialog.setMessage("Are you Really want to QUIT???");
            alertDialog.setIcon(R.drawable.play_icon);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yep", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();

                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nope", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
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
        if (id == R.id.action_rateus) {
            showRateDialog(this);
            return true;
        }
        if (id == R.id.action_Feedback) {
            sendFeedbacDialog(this);
            return true;
        }
        if (id == R.id.action_moreapp) {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Santosh+Anarase"));
            startActivity(i);
        }
        if (id == R.id.action_disclemer) {
            Intent intent=new Intent(MainActivity.this, Disclaimer.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_help) {
            Intent intent=new Intent(MainActivity.this, Help.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_about_us) {
            Intent intent=new Intent(MainActivity.this, Aboutus.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.refresh) {
            getSupportActionBar().setTitle("TattooManiacs");


            Bundle bundle = new Bundle();
            bundle.putString("url", "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6376804581768793409?alt=json");

            Fragment fragment = new ChildGallery();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment);

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



        public static void showRateDialog (final Context context)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("Rate application")
                    .setMessage("Please, Rate the App at PlayStore...")
                    .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (context != null) {
                                String link = "market://details?id=";
                                try {
                                    // play market available
                                    context.getPackageManager()
                                            .getPackageInfo("com.android.vending", 0);
                                    // not available
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                    // should use browser
                                    link = "https://play.google.com/store/apps/details?id=";
                                }
                                // starts external action
                                context.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(link + context.getPackageName())));
                            }
                        }
                    })
                    .setNegativeButton("CANCEL", null);
            builder.show();
        }
    public static void sendFeedbacDialog (final Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Send FeedBack")
                .setMessage("Please comment about TattooManiacs on Google Play")
                .setPositiveButton("COMMENT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (context != null) {
                            String link = "market://details?id=";
                            try {
                                // play market available
                                context.getPackageManager()
                                        .getPackageInfo("com.android.vending", 0);
                                // not available
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                                // should use browser
                                link = "https://play.google.com/store/apps/details?id=";
                            }
                            // starts external action
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(link + context.getPackageName())));
                        }
                    }
                })
                .setNegativeButton("CANCEL", null);
        builder.show();
    }






    }



