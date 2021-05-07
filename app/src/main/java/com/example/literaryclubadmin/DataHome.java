package com.example.literaryclubadmin;

import android.view.View;

public class DataHome {
    String Ename,Edesc,Eimage;
    String Like;
    View.OnClickListener act;

    public DataHome(String ename, String edesc, String eimage, String like , View.OnClickListener act) {
        Ename = ename;
        Edesc = edesc;
        Eimage = eimage;
        Like = like;
        this.act = act;
    }

    public String getEimage() {
        return Eimage;
    }
}
