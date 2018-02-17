package com.biz.tizzy.eballot.models;

/**
 * Created by tizzy on 2/17/18.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Electorate {
    public String voteID;

    public Electorate() {}

    public Electorate(String voteID) {
        this.voteID = voteID;
    }
}
