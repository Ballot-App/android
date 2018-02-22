package com.biz.tizzy.eballot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by tizzy on 2/22/18.
 */

public class VotesPagerActivity extends AppCompatActivity {

    private static final String EXTRA_ELECID = "com.biz.tizzy.eballot.elecid";
    private static final String EXTRA_ELECNAME = "com.biz.tizzy.eballot.elecname";

    private String mElecID;
    private String mElecName;

    public static Intent newIntent(Context packageContext, String elecID, String elecName) {
        Intent intent = new Intent(packageContext, VotesPagerActivity.class);
        intent.putExtra(EXTRA_ELECID, elecID);
        intent.putExtra(EXTRA_ELECNAME, elecName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votes_pager);

        mElecID = (String) getIntent().getSerializableExtra(EXTRA_ELECID);
        mElecName = (String) getIntent().getSerializableExtra(EXTRA_ELECNAME);

        ViewPager pager = (ViewPager) findViewById(R.id.votes_view_pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return AbstainFragment.newInstance(mElecID, mElecName);
                case 1: return NoAbstainFragment.newInstance(mElecID, mElecName);
                //case 2: return AVFragment.newInstance("ThirdFragment, Instance 1");
               default: return AbstainFragment.newInstance(mElecID, mElecName);
          }
        }

        @Override
        public int getCount() {
            return 2; // should be number of votes
        }
    }
}


