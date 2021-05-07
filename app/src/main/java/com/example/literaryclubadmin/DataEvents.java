package com.example.literaryclubadmin;

import android.view.View;

public class DataEvents {
    String Ename,Edesc,Eimage,Gform;
    View.OnClickListener act;

    public DataEvents(String ename, String edesc, String eimage, String gform, View.OnClickListener act) {
        Ename = ename;
        Edesc = edesc;
        Eimage = eimage;
        Gform = gform;
        this.act = act;
    }
}
