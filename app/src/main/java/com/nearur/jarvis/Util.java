
package com.nearur.jarvis;

import android.net.Uri;

/**
 * Created by mrdis on 7/22/2017.
 */

public class Util {

    public static final String db="Jarvis";
    public static final int vers=1;

    public static final String tab="Remember";
    public static final String tab1="Requests";
    public static final String date="Date";
    public static final String thing="Thing";

    public static final String query="create table Remember(" +
            "Date text," +
            "Thing text" +
            ")";

    public static final String query1="create table Requests(" +
            "Message text," +
            "Owner int" +
            ")";

    public static final Uri u=Uri.parse("content://com.nearur.jarvis.stark/"+tab);

    public static final Uri u1=Uri.parse("content://com.nearur.jarvis.stark/"+tab1);
}
