package com.biz.tizzy.eballot;

import java.io.Serializable;

/**
 * Created by tizzy on 2/22/18.
 */

public class Ballot implements Serializable {
    private String mID;
    private String mDescription;
    private String mVoteType;

    public Ballot(String ID, String description, String voteType) {
        mID = ID;
        mDescription = description;
        mVoteType = voteType;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getVoteType() {
        return mVoteType;
    }

    public void setVoteType(String voteType) {
        mVoteType = voteType;
    }

    @Override
    public String toString() {
        return mID;
    }

}
