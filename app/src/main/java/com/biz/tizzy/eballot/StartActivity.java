package com.biz.tizzy.eballot;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() { return new StartFragment(); }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, StartActivity.class);
        return intent;
    }
}
