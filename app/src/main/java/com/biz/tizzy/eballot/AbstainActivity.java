package com.biz.tizzy.eballot;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by tizzy on 2/17/18.
 */

public class AbstainActivity extends SingleFragmentActivity {

    private static final String EXTRA_ELECID = "com.biz.tizzy.eballot.elecid";
    private static final String EXTRA_ELECNAME = "com.biz.tizzy.eballot.elecname";

    @Override
    protected Fragment createFragment() {

        String elecID = (String) getIntent().getSerializableExtra(EXTRA_ELECID);
        String elecName = (String) getIntent().getSerializableExtra(EXTRA_ELECNAME);
        return AbstainFragment.newInstance(elecID, elecName);
    }

    public static Intent newIntent(Context packageContext, String elecID, String elecName) {
        Intent intent = new Intent(packageContext, AbstainActivity.class);
        intent.putExtra(EXTRA_ELECID, elecID);
        intent.putExtra(EXTRA_ELECNAME, elecName);
        return intent;
    }

}
