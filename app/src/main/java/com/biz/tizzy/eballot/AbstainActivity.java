package com.biz.tizzy.eballot;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by tizzy on 2/17/18.
 */

public class AbstainActivity extends SingleFragmentActivity {

    private static final String EXTRA_ELECID = "com.biz.tizzy.eballot.elecid";

    @Override
    protected Fragment createFragment() {

        String elecID = (String) getIntent().getSerializableExtra(EXTRA_ELECID);

        return AbstainFragment.newInstance(elecID);
    }

    public static Intent newIntent(Context packageContext, String elecID) {
        Intent intent = new Intent(packageContext, AbstainActivity.class);
        intent.putExtra(EXTRA_ELECID, elecID);

        return intent;
    }

}
