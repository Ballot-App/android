package com.biz.tizzy.eballot;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by tizzy on 2/17/18.
 */

public class NoAbstainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() { return new NoAbstainFragment(); }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, NoAbstainActivity.class);

        return intent;
    }

}
