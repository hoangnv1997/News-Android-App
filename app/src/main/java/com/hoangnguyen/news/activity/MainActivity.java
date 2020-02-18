package com.hoangnguyen.news.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hoangnguyen.news.R;
import com.hoangnguyen.news.fragment.NewsFragment;
import com.hoangnguyen.news.fragment.WeatherFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.fragment_container);

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        //       bottomNavigationView.setSelectedItemId(R.id.nav_weather);

        loadFragment(new NewsFragment());

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());


    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment;
            switch (menuItem.getItemId()) {

                case R.id.nav_home:
                    fragment = new NewsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_weather:
                    fragment = new WeatherFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

        public BottomNavigationBehavior() {
            super();
        }

        public BottomNavigationBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationView child, View dependency) {
            boolean dependsOn = dependency instanceof FrameLayout;
            return dependsOn;
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child, View directTargetChild, View target, int nestedScrollAxes) {
            return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child, View target, int dx, int dy, int[] consumed) {
            if (dy < 0) {
                showBottomNavigationView(child);
            } else if (dy > 0) {
                hideBottomNavigationView(child);
            }
        }

        private void hideBottomNavigationView(BottomNavigationView view) {
            view.animate().translationY(view.getHeight());
        }

        private void showBottomNavigationView(BottomNavigationView view) {
            view.animate().translationY(0);
        }
    }
}
