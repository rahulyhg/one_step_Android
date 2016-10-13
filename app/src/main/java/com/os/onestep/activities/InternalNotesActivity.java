package com.os.onestep.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.os.onestep.R;
import com.os.onestep.adapters.ViewNotesAdapter;
import com.os.onestep.beans.ViewNotesBean;
import com.os.onestep.utilities.Constants;
import com.os.onestep.utilities.MySingleton;
import com.os.onestep.utilities.SharedPreferenceUtils;
import com.os.onestep.utilities.dialogutils.DialogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dhruvishah on 11/10/16.
 */
public class InternalNotesActivity extends AppCompatActivity  {
    ImageButton  imgbtn_addnotes;
    ArrayList<ViewNotesBean>notesBeanArrayList;
    ViewNotesAdapter notesAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internalnotes);
        getContentID();
    }

    private void getContentID()
    {
        imgbtn_addnotes= (ImageButton) findViewById(R.id.imgbtn_addnotes);
        imgbtn_addnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InternalNotesActivity.this,AddNotesActivity.class));
            }
        });
        notesBeanArrayList= new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
         notesAdapter=new ViewNotesAdapter(new ArrayList<ViewNotesBean>(),this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getNotes();
    }
    // Method to get video duration
    private void getNotes() {
        final ProgressDialog progressDialog2;
        progressDialog2 = new ProgressDialog(InternalNotesActivity.this);
        progressDialog2.setMessage("Loading..");
        progressDialog2.setCancelable(false);
        progressDialog2.show();
        String url = getViewNotesUrl();
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            progressDialog2.dismiss();
                            if (response != null && "1".equals(response.getString(Constants.SUCCESS_KEY))) {
                                JSONArray resultArray = response.getJSONArray(Constants.DATA_KEY);
                                int resultArrayLength = resultArray.length();
                                if (resultArrayLength > 0) {
                                    for (int i = 0; i < resultArrayLength; i++) {
                                        JSONObject jsonObject = resultArray.getJSONObject(i);
                                        ViewNotesBean notesBean = new ViewNotesBean();
                                        notesBean.setId(jsonObject.getString("id"));
                                        notesBean.setDate(jsonObject.getString("created_date"));
                                        notesBean.setNoteHeading(jsonObject.getString("notes_name"));
                                        notesBean.setNoteDescription(jsonObject.getString("Description"));
                                        notesBean.setStatus(jsonObject.getString("status"));
                                        notesBean.setRoom(jsonObject.getString("Room"));
                                        notesBean.setFurniture(jsonObject.getString("Furniture"));
                                        notesBean.setWindow_Treatment(jsonObject.getString("Window_Treatment"));
                                        notesBean.setMask(jsonObject.getString("Mask"));
                                        notesBean.setRemove_WP(jsonObject.getString("Remove_WP"));
                                        notesBean.setPrep(jsonObject.getString("Prep"));
                                        notesBean.setWalls(jsonObject.getString("Walls"));
                                        notesBean.setCeiling(jsonObject.getString("Ceiling"));
                                        notesBean.setDoors(jsonObject.getString("Doors"));
                                        notesBean.setFrames(jsonObject.getString("Frames"));
                                        notesBean.setFrench_Doors(jsonObject.getString("French_Doors"));
                                        notesBean.setWindows(jsonObject.getString("Windows"));
                                        notesBean.setFrench_Windows(jsonObject.getString("French_Windows"));
                                        notesBean.setCabinets(jsonObject.getString("Cabinets"));
                                        notesBean.setBase(jsonObject.getString("Base"));
                                        notesBean.setCrown(jsonObject.getString("Crown"));
                                        notesBean.setCloset(jsonObject.getString("Closet"));
                                        notesBean.setNotes(jsonObject.getString("Notes"));

                                        notesBeanArrayList.add(notesBean);

                                    }
                                    notesAdapter.updateData(notesBeanArrayList);
                                } else {

                                    DialogUtils.showalert(getString(R.string.technical_issue_string), InternalNotesActivity.this);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            progressDialog2.dismiss();
                            DialogUtils.showalert(getString(R.string.technical_issue_string), InternalNotesActivity.this);
                        }
                    }
//
//
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // To make sure Activity is still in the foreground
                        progressDialog2.dismiss();
                    }
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(Constants.ARG_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).getRequestQueue().add(request);

    }
    private String getViewNotesUrl() {

//        String url="http://divsinfotech.com/OneStep/api/insertintnotes?username=aki@gmail.com&password=123456&project_id=1&notes_name=bathroom&Room=Room&Furniture=Furniture&Window_Treatment=Window_Treatment&Mask=Mask&Remove_WP=Remove_WP&Prep=Prep&Walls=Walls&Ceiling=Ceiling&Doors=Doors&Frames=Frames&French_Doors=French_Doors&Windows=Windows&French_Windows=French_Windows" +
//                "&Cabinets=Cabinets&Base=Base&Crown=Crown&Closet=Closet&Notes=Notes&Description=Description";
        StringBuilder url = new StringBuilder();
        url.append(Constants.FETCH_NOTES_URL);
        url.append("username=");
        url.append(SharedPreferenceUtils.getLocalStorage("USERNAME", this));
        url.append("&password=");
        url.append(SharedPreferenceUtils.getLocalStorage("PASSWORD", this));
        url.append("&project_id=");
        url.append(SharedPreferenceUtils.getLocalStorage(Constants.PROJECT_ID, this));
        return url.toString().replace(" ", "%20");
    }
}
