package com.example.literaryclubadmin;

import android.view.View;

public class DataGallery {
    String Ecom,Eimage;
    View.OnClickListener act;

    public DataGallery(String ecom, String eimage, View.OnClickListener act) {
        Ecom = ecom;
        Eimage = eimage;
        this.act = act;
    }
}
