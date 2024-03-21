package com.example.vibechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vibechat.utils.FirebaseUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton searchButton;
    ImageButton createGroupButton;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    ChatFragment chatFragment;
    ProfileFragment profileFragment;
    GroupFragment groupFragment;
    //GroupFragment groupFragment;
    Meetsfragement meetsfragement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();
        groupFragment = new GroupFragment();
        meetsfragement = new Meetsfragement();

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Chats");
                    } else if (position == 1) {
                        tab.setText("Groups");
                    } else if (position == 2) {
                        tab.setText("Meets");
                    }
                }
        ).attach();

        //--Group purpose
        createGroupButton = findViewById(R.id.main_group_btn);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateGroup.class);
                startActivity(intent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchButton = findViewById(R.id.main_search_btn);

        searchButton.setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, SearchUserActivity.class));
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                // Navigate to the corresponding fragment based on the selected menu item
                if (itemId == R.id.menu_chat) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, chatFragment).commit();
                } else if (itemId == R.id.menu_group) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, groupFragment).commit();
                } else if (itemId == R.id.menu_meets) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, meetsfragement).commit();
                } else if (itemId == R.id.menu_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, profileFragment).commit();
                }

                return true;
            }
        });



                /* new code
                if (item.getItemId() == R.id.menu_chat) {
                    viewPager.setCurrentItem(0, true);
                } else if (item.getItemId() == R.id.menu_group) {
                    viewPager.setCurrentItem(1, true);
                } else if (item.getItemId() == R.id.menu_meets) {
                    viewPager.setCurrentItem(2, true);
                }
                return true;
            }
        });

                 */
        bottomNavigationView.setSelectedItemId(R.id.menu_chat);

        getFCMToken();
    }

    void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                FirebaseUtil.currentUserDetails().update("fcmToken", token);
            }
        });
    }
}
