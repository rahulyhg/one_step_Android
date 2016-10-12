package com.os.onestep.utilities.dialogutils;

import android.text.TextUtils;

import java.util.ArrayList;

public class ServiceUtils {

    public static String getEstimateListData(String username, String password, String proId, String estId) {
        ArrayList<String> list = new ArrayList<>();
        list.add("&username=" + username);
        list.add("&password=" + password);
        list.add("&project_id=" + proId);
        list.add("&estimate_id=" + estId);
        return TextUtils.join("", list).replaceAll(" ", "%20");
    }

}
