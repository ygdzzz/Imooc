package com.example.ygd.imooc.Activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.ygd.imooc.R;
import com.example.ygd.imooc.fragment.CommunityFragment;
import com.example.ygd.imooc.fragment.CourseFragment;
import com.example.ygd.imooc.fragment.DownloadFragment;
import com.example.ygd.imooc.fragment.MineFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

/**
 * Created by iiro on 7.6.2016.
 */
public class FiveColorChangingTabsActivity extends AppCompatActivity {
    private BottomBar mBottomBar;
//    private TextView mMessageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
//        mMessageView = (TextView) findViewById(R.id.messageView);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                FragmentManager fm=getFragmentManager();
                switch (menuItemId) {
                    case R.id.menu_course:
                        CourseFragment courseFragment=new CourseFragment();
        //                Bundle bundle=new Bundle();
        //                bundle.putString("args","CallBack Fragment2");
        //                two.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.frame,courseFragment).commit();
                        break;
                    case R.id.menu_download:
                        DownloadFragment downloadFragment=new DownloadFragment();
                        fm.beginTransaction().replace(R.id.frame,downloadFragment).commit();
                        break;
                    case R.id.menu_community:
                        CommunityFragment communityFragment=new CommunityFragment();
                        fm.beginTransaction().replace(R.id.frame,communityFragment).commit();
                        break;
                    case R.id.menu_mine:
                        MineFragment mineFragment=new MineFragment();
                        fm.beginTransaction().replace(R.id.frame,mineFragment).commit();
                        break;
                }



            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorCourse));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.colorDownload));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.colorCommunity));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.colorMine));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }
}