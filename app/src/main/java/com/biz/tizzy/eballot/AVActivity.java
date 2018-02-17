package com.biz.tizzy.eballot;

import android.support.v4.app.Fragment;

/**
 * Created by tizzy on 2/17/18.
 */

public class AVActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() { return new AVFragment(); }
}
