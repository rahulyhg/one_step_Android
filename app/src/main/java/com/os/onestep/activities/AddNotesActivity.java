package com.os.onestep.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.os.onestep.R;
import com.os.onestep.beans.ViewNotesBean;
import com.os.onestep.utilities.Constants;
import com.os.onestep.utilities.MySingleton;
import com.os.onestep.utilities.SharedPreferenceUtils;
import com.os.onestep.utilities.dialogutils.DialogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddNotesActivity extends AppCompatActivity {
    Spinner sp_FurnitureHrs, sp_WindowTreatHrs, sp_MaskHrs, sp_RemoveWpHrs,
            sp_PrepHrs, sp_WallsHrs, sp_CeilingHrs, sp_DoorsHrs,
            sp_FramesHrs, sp_FrenchDoorsHrs, sp_WindowsHrs, sp_FrenchWindowsHrs,
            sp_CabinetsHrs, sp_BaseHrs, sp_CrownHrs, sp_ClosetHrs;
    EditText et_Furniture, et_WindowTreat, et_Mask, et_RemoveWp,
            et_Prep, et_Walls, et_Ceiling, et_Doors,
            et_Frames, et_FrenchDoors, et_Windows, et_FrenchWindows,
            et_Cabinets, et_Base, et_Crown, et_Closet, et_notename, et_room, et_size, et_notes, et_description;
    Button btn_saveNotes;
    ViewNotesBean viewNotesBean;
    Boolean isFromEdit=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        getContentId();
    }

    private void getContentId() {
        sp_FurnitureHrs = (Spinner) findViewById(R.id.sp_addnote_furnitureHrs);
        sp_WindowTreatHrs = (Spinner) findViewById(R.id.sp_addnote_windowtreatment);
        sp_MaskHrs = (Spinner) findViewById(R.id.sp_addnote_maskHrs);
        sp_RemoveWpHrs = (Spinner) findViewById(R.id.sp_addnote_removewpHrs);
        sp_PrepHrs = (Spinner) findViewById(R.id.sp_addnote_prepHrs);
        sp_WallsHrs = (Spinner) findViewById(R.id.sp_addnote_wallsHrs);
        sp_CeilingHrs = (Spinner) findViewById(R.id.sp_addnote_celingHrs);
        sp_DoorsHrs = (Spinner) findViewById(R.id.sp_addnote_doorsHrs);
        sp_FramesHrs = (Spinner) findViewById(R.id.sp_addnote_framesHrs);
        sp_FrenchDoorsHrs = (Spinner) findViewById(R.id.sp_addnote_frenchdoorHrs);
        sp_WindowsHrs = (Spinner) findViewById(R.id.sp_addnote_WindowsHrs);
        sp_FrenchWindowsHrs = (Spinner) findViewById(R.id.sp_addnote_frenchwindowsHrs);
        sp_CabinetsHrs = (Spinner) findViewById(R.id.sp_addnote_cabinetHrs);
        sp_BaseHrs = (Spinner) findViewById(R.id.sp_addnote_baseHrs);
        sp_CrownHrs = (Spinner) findViewById(R.id.sp_addnote_crownHrs);
        sp_ClosetHrs = (Spinner) findViewById(R.id.sp_addnote_closetHrs);

        et_Furniture = (EditText) findViewById(R.id.et_addnote_furniture);
        et_WindowTreat = (EditText) findViewById(R.id.et_addnote_windowtreatment);
        et_Mask = (EditText) findViewById(R.id.et_addnote_mask);
        et_RemoveWp = (EditText) findViewById(R.id.et_addnote_removewp);
        et_Prep = (EditText) findViewById(R.id.et_addnote_prep);
        et_Walls = (EditText) findViewById(R.id.et_addnote_walls);
        et_Ceiling = (EditText) findViewById(R.id.et_addnote_celing);
        et_Doors = (EditText) findViewById(R.id.et_addnote_doors);
        et_Frames = (EditText) findViewById(R.id.et_addnote_frames);
        et_FrenchDoors = (EditText) findViewById(R.id.et_addnote_frenchdoor);
        et_Windows = (EditText) findViewById(R.id.et_addnote_window);
        et_FrenchWindows = (EditText) findViewById(R.id.et_addnote_frenchwindow);
        et_Cabinets = (EditText) findViewById(R.id.et_addnote_cabinet);
        et_Base = (EditText) findViewById(R.id.et_addnote_base);
        et_Crown = (EditText) findViewById(R.id.et_addnote_crown);
        et_Closet = (EditText) findViewById(R.id.et_addnote_closet);
        et_notename = (EditText) findViewById(R.id.et_addnote_notename);
        et_room = (EditText) findViewById(R.id.et_addnote_room);
        et_size = (EditText) findViewById(R.id.et_addnote_size);
        et_notes = (EditText) findViewById(R.id.et_addnote_notes);
        et_description = (EditText) findViewById(R.id.et_addnote_desc);

        btn_saveNotes = (Button) findViewById(R.id.btn_savenotes);
        btn_saveNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotes();
            }
        });
        setSpinnerAdapter();
        if(getIntent().getSerializableExtra("notesBean")!=null)
        {
            viewNotesBean=(ViewNotesBean) getIntent().getSerializableExtra("notesBean");
            setEditData(viewNotesBean);
            isFromEdit=true;
            btn_saveNotes.setText("Update");
        }
        else
        {
            isFromEdit=false;
        }



    }

    private void setSpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_hrs));
        sp_FurnitureHrs.setAdapter(adapter);
        sp_WindowTreatHrs.setAdapter(adapter);
        sp_MaskHrs.setAdapter(adapter);
        sp_RemoveWpHrs.setAdapter(adapter);
        sp_PrepHrs.setAdapter(adapter);
        sp_WallsHrs.setAdapter(adapter);
        sp_CeilingHrs.setAdapter(adapter);
        sp_DoorsHrs.setAdapter(adapter);
        sp_FramesHrs.setAdapter(adapter);
        sp_FrenchDoorsHrs.setAdapter(adapter);
        sp_WindowsHrs.setAdapter(adapter);
        sp_FrenchWindowsHrs.setAdapter(adapter);
        sp_CabinetsHrs.setAdapter(adapter);
        sp_BaseHrs.setAdapter(adapter);
        sp_CrownHrs.setAdapter(adapter);
        sp_ClosetHrs.setAdapter(adapter);
    }

    // Method to get video duration
    private void saveNotes() {
        final ProgressDialog progressDialog2;
        progressDialog2 = new ProgressDialog(AddNotesActivity.this);
        progressDialog2.setMessage("Loading..");
        progressDialog2.setCancelable(false);
        progressDialog2.show();
        String url = getSaveNotesUrl();

        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            progressDialog2.dismiss();
                            if (response != null && "1".equals(response.getString(Constants.SUCCESS_KEY))) {
                                DialogUtils.showalert(response.getString("message"), AddNotesActivity.this);
                                Intent myIntent = new Intent(AddNotesActivity.this, InternalNotesActivity.class);
                                myIntent.putExtra("id",response.getString("id"));
                                myIntent.putExtra("isFromAddNotes",true);
                                startActivity(myIntent);
                            } else {

                                DialogUtils.showalert(getString(R.string.technical_issue_string), AddNotesActivity.this);
                            }
                        }
                        catch (Exception e)
                        {
                            DialogUtils.showalert(getString(R.string.technical_issue_string), AddNotesActivity.this);
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

    private String getSaveNotesUrl() {

//        String url="http://divsinfotech.com/OneStep/api/insertintnotes?username=aki@gmail.com&password=123456&project_id=1&notes_name=bathroom&Room=Room&Furniture=Furniture&Window_Treatment=Window_Treatment&Mask=Mask&Remove_WP=Remove_WP&Prep=Prep&Walls=Walls&Ceiling=Ceiling&Doors=Doors&Frames=Frames&French_Doors=French_Doors&Windows=Windows&French_Windows=French_Windows" +
//                "&Cabinets=Cabinets&Base=Base&Crown=Crown&Closet=Closet&Notes=Notes&Description=Description";
        StringBuilder url = new StringBuilder();
        url.append(Constants.SAVE_NOTES_URL);
        url.append("username=");
        url.append(SharedPreferenceUtils.getLocalStorage("USERNAME", this));
        url.append("&password=");
        url.append(SharedPreferenceUtils.getLocalStorage("PASSWORD", this));
        url.append("&project_id=");
        url.append(SharedPreferenceUtils.getLocalStorage(Constants.PROJECT_ID, this));
        url.append("&notes_name=");
        url.append(et_notename.getText().toString().trim());
        url.append("&Room=");
        url.append(et_room.getText().toString().trim());
        url.append("&Furniture=");
        url.append(et_Furniture.getText().toString().trim());
        url.append("&Window_Treatment=");
        url.append(et_WindowTreat.getText().toString().trim());
        url.append("&Mask=");
        url.append(et_Mask.getText().toString().trim());
        url.append("&Remove_WP=");
        url.append(et_RemoveWp.getText().toString().trim());
        url.append("&Prep=");
        url.append(et_Prep.getText().toString().trim());
        url.append("&Walls=");
        url.append(et_Walls.getText().toString().trim());
        url.append("&Ceiling=");
        url.append(et_Ceiling.getText().toString().trim());
        url.append("&Doors=");
        url.append(et_Doors.getText().toString().trim());
        url.append("&Frames=");
        url.append(et_Frames.getText().toString().trim());
        url.append("&French_Doors=");
        url.append(et_FrenchDoors.getText().toString().trim());
        url.append("&Windows=");
        url.append(et_Windows.getText().toString().trim());
        url.append("&French_Windows=");
        url.append(et_FrenchWindows.getText().toString().trim());
        url.append("&Cabinets=");
        url.append(et_Cabinets.getText().toString().trim());
        url.append("&Base=");
        url.append(et_Base.getText().toString().trim());
        url.append("&Crown=");
        url.append(et_Crown.getText().toString().trim());
        url.append("&Closet=");
        url.append(et_Closet.getText().toString().trim());
        url.append("&Notes=");
        url.append(et_notes.getText().toString().trim());
        url.append("&Description=");
        url.append(et_description.getText().toString().trim());
        if(isFromEdit)
        {
            url.append("&id=");
            url.append(viewNotesBean.getId());
        }
        return url.toString().replace(" ", "%20");
    }

    private void setEditData(ViewNotesBean notesBeanData)
    {
       et_description.setText(notesBeanData.getNoteDescription());
        et_notes.setText(notesBeanData.getNotes());
        et_Closet.setText(notesBeanData.getCloset());
        et_Crown.setText(notesBeanData.getCrown());
        et_Base.setText(notesBeanData.getBase());
        et_Cabinets.setText(notesBeanData.getCabinets());
        et_Ceiling.setText(notesBeanData.getCeiling());
        et_Doors.setText(notesBeanData.getDoors());
        et_Frames.setText(notesBeanData.getFrames());
        et_FrenchDoors.setText(notesBeanData.getFrench_Doors());
        et_FrenchWindows.setText(notesBeanData.getFrench_Windows());
        et_Furniture.setText(notesBeanData.getFurniture());
        et_Mask.setText(notesBeanData.getMask());
        et_notename.setText(notesBeanData.getNoteHeading());
        et_Prep.setText(notesBeanData.getPrep());
        et_RemoveWp.setText(notesBeanData.getRemove_WP());
        et_room.setText(notesBeanData.getRoom());
        et_size.setText("");
        et_Walls.setText(notesBeanData.getWalls());
        et_Windows.setText(notesBeanData.getWindows());
        et_WindowTreat.setText(notesBeanData.getWindow_Treatment());
    }
}
