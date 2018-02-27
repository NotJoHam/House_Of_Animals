package vanlandingham.friendimals;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.transition.Fade;
import android.transition.Slide;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.fragments.featured_fragment;
import vanlandingham.friendimals.fragments.home_fragment;
import vanlandingham.friendimals.fragments.profile_fragment;
import vanlandingham.friendimals.fragments.upload_fragment;

public class Home extends AppCompatActivity {


    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView username_TextView;
    private DrawerLayout mDrawerLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private NavigationView nvDrawer;
    private TextView toolbar_text;
    private Typeface type;
    private MenuItem menu_item;
    private android.support.v4.app.Fragment fragment = null;
    private android.support.v4.app.Fragment previousFragment = null;
    private ImageButton messages_button;
    private Menu menu;
    private User user;
    private User curr_user;

    private Bundle bundle;
    private Bundle bundle1;
    private String username;

    private Fade fade;
    private Fade enter_fade;
    private FirebaseAuth mFirebaseAuth;

    //Class to switch between fragments
    public Class fragmentClass;

    public int call_class;

    //Fragment manager to actually switch the fragments
    private android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {
            user = data.getExtras().getParcelable("user");
            call_class = data.getIntExtra("call_class", 0);

        }

        if (call_class == 1) {
            ChangeFragment(profile_fragment.class, user);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
        Intent mainIntent = getIntent();

        Bundle mainBundle = mainIntent.getExtras();
        curr_user = mainBundle.getParcelable("curr_user");

        username = curr_user.getUsername();

        setContentView(R.layout.activity_home);

        setTitle(R.string.home);


        Runnable task = new Runnable() {
            @Override
            public void run() {
                fade = new Fade();
                fade.setDuration(250);

                enter_fade = new Fade();
                getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

                enter_fade.setStartDelay(250);
                enter_fade.setDuration(250);

                mFirebaseAuth = FirebaseAuth.getInstance();


                //Initialize the toolbar and set it


                fragmentClass = home_fragment.class;

                try {
                    fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                bundle = new Bundle();
                bundle.putParcelable("curr_user", curr_user);
                bundle.putParcelable("user", curr_user);
                bundle.putString("username", username);


                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                return;

            }
        };
        new Thread(task).start();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nvDrawer = findViewById(R.id.nvView);
        mDrawerLayout = findViewById(R.id.drawerLayout);

        //Initialize the toggle
        mDrawerToggle = setupDrawerToggle();


        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.camera);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException nullpointer) {
            Toast.makeText(this, "Error: NullPointerException", Toast.LENGTH_SHORT).show();
        }


        //mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

                previousFragment = fragmentManager.findFragmentById(R.id.flContent);

                //Attempt to create a new fragment class
                try {
                    fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        previousFragment.setExitTransition(fade);
                        fragment.setEnterTransition(enter_fade);
                        Log.d("Fragment thread: ", "run: Transition");

                    }
                };
                new Thread(runnable).start();

                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        menu = nvDrawer.getMenu();


        menu_item = menu.findItem(R.id.Home);
        View headerView = nvDrawer.getHeaderView(0);

        username_TextView = headerView.findViewById(R.id.userName_textView);
        username_TextView.setText(username);
        setTitle(null);

        toolbar_text = toolbar.findViewById(R.id.toolbar_text);
        type = Typeface.createFromAsset(getAssets(), "fonts/Marbre_Sans_Bold.otf");
        toolbar_text.setText(R.string.app_name);
        toolbar_text.setTypeface(type);

        //To make the Header clickable, we add a OnClickListener to the Relative layout.


        RelativeLayout header_layout = headerView.findViewById(R.id.profileBox);
        username_TextView = headerView.findViewById(R.id.userName_textView);

        header_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar_text.setText(username);
                toolbar_text.setTypeface(type);
                fragmentClass = profile_fragment.class;


                menu_item.setChecked(false);
                mDrawerLayout.closeDrawers();
            }
        });

        ImageButton search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);

                startActivityForResult(intent, 2);

            }
        });

        // Populate the Navigation Drawer with options
        mDrawerPane = findViewById(R.id.drawerPane);

        setupDrawerContent(nvDrawer);

        messages_button = findViewById(R.id.messages);

        messages_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setUpWindowAnimations();
                Intent intent = new Intent(Home.this, Messages.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    //Used to initialize the Drawers content from the NavigationView
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        selectItemFromDrawer(menuItem);
                        return true;
                    }
                });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {


        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

    }

    //Adds the back arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;

            default:
                // Handle your other action bar items...
                return super.onOptionsItemSelected(item);
        }

        // Handle your other action bar items...

    }


    //Used to show the correct indicator for side menu (hamburger/arrow)
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    /*
* Called when a particular item from the navigation drawer
* is selected.
* */
    private void selectItemFromDrawer(MenuItem menuItem) {


        switch (menuItem.getItemId()) {

            case R.id.Home:

                fragmentClass = home_fragment.class;


                toolbar_text.setText(R.string.app_name);
                toolbar_text.setTypeface(type);
                menu_item = menuItem;
                break;

            case R.id.featured:
                fragmentClass = featured_fragment.class;

                toolbar_text.setText(menuItem.getTitle());
                toolbar_text.setTypeface(type);
                menu_item = menuItem;
                break;


            case R.id.upload:

                fragmentClass = upload_fragment.class;
                toolbar_text.setText(menuItem.getTitle());
                toolbar_text.setTypeface(type);
                menu_item = menuItem;
                break;

            case R.id.logout:
                Logout();
                break;

            case R.id.help:

                break;

            case R.id.feedback:

                break;

            case R.id.about:

                break;


        }

        //Set the current item to be checked, meaning it is already selected.
        menuItem.setChecked(true);

        // Close the drawer
        mDrawerLayout.closeDrawers();


    } //End SelectItemFromDrawer


    //For when the layout changes from portrait to landscape
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public void ChangeFragment(Class fragmentClass, @Nullable final User this_user) {

        try {
            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragmentClass == profile_fragment.class) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelable("user", this_user);
                    bundle1.putParcelable("curr_user", curr_user);
                    fragment.setArguments(bundle1);
                    return;
                }
            };

            new Thread(runnable).start();

            menu_item.setChecked(false);
        }
        toolbar_text.setText(this_user.getUsername());
        toolbar_text.setTypeface(type);
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

    }

    public void Logout() {

        mFirebaseAuth.signOut();

        Intent intent = new Intent(Home.this, LoginActivity.class);
        startActivity(intent);

    }


    private void setUpWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(2000);
        getWindow().setEnterTransition(fade);

        Slide slide = new Slide();
        slide.setDuration(2000);
        getWindow().setExitTransition(slide);

    }

}
