package com.biz.tizzy.eballot;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by tizzy on 2/17/18.
 */

public class NoAbstainActivity extends SingleFragmentActivity {

    private static final String EXTRA_ELECID = "com.biz.tizzy.eballot.elecID";
    private static final String EXTRA_ELECNAME = "com.biz.tizzy.eballot.elecName";
    private static final String EXTRA_BALLOTID = "com.biz.tizzy.eballot.ballotID";

    @Override
    protected Fragment createFragment() {
        String elecID = (String) getIntent().getSerializableExtra(EXTRA_ELECID);
        String elecName = (String) getIntent().getSerializableExtra(EXTRA_ELECNAME);
        String ballotID = (String) getIntent().getSerializableExtra(EXTRA_BALLOTID);
        return NoAbstainFragment.newInstance(elecID, elecName, ballotID);
    }

    public static Intent newIntent(Context packageContext, String elecID, String elecName, String ballotID) {
        Intent intent = new Intent(packageContext, NoAbstainActivity.class);
        intent.putExtra(EXTRA_ELECID, elecID);
        intent.putExtra(EXTRA_ELECNAME, elecName);
        intent.putExtra(EXTRA_BALLOTID, ballotID);
        return intent;
    }

}
