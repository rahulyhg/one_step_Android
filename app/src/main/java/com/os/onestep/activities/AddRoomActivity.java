package com.os.onestep.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.os.onestep.R;
import com.os.onestep.adapters.JobsListAdapter;
import com.os.onestep.beans.EstJobBean;
import com.os.onestep.utilities.Constants;
import com.os.onestep.utilities.SharedPreferenceUtils;
import com.os.onestep.utilities.dialogutils.AddRoomDialog;
import com.os.onestep.utilities.dialogutils.DialogUtils;
import com.os.onestep.utilities.dialogutils.ServiceUtils;
import com.os.onestep.utilities.httpconnection.ConnectionManager;
import com.os.onestep.utilities.httpconnection.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddRoomActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;
    ArrayList<EstJobBean> estJobArrayList;
    private Spinner spaceTypeSpinner;
    private EditText lengthText;
    private EditText widthText;
    private EditText heightText;
    private Button addRoomButton;
    private NetworkUtility networkUtility;
    private ConnectionManager connectionManager;
    private ArrayList<String> spaceTypeIdArray, spaceTypeArray;
    private AddRoomDialog addRoomDialog;
    private JobsListAdapter jobsListAdapter;
    private ListView estJobsListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newroom);
        try {
            findViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findViews() throws JSONException {
        connectionManager = new ConnectionManager(this);
        networkUtility = new NetworkUtility();
        spaceTypeArray = new ArrayList<>();
        spaceTypeIdArray = new ArrayList<>();
        addRoomDialog = new AddRoomDialog(this);
        progressDialog = new ProgressDialog(AddRoomActivity.this);
        estJobsListView = (ListView) findViewById(R.id.jobsListView);
        jobsListAdapter = new JobsListAdapter(this, new ArrayList<EstJobBean>());
        spaceTypeSpinner = (Spinner) findViewById(R.id.spaceTypeSpinner);
        lengthText = (EditText) findViewById(R.id.lengthText);
        widthText = (EditText) findViewById(R.id.widthText);
        heightText = (EditText) findViewById(R.id.heightText);
        addRoomButton = (Button) findViewById(R.id.addRoomButton);
        if ("".equals(SharedPreferenceUtils.getLocalStorage(Constants.SPACE_TYPE_JSON, this))) {
            if (connectionManager.isConnectingToInternet()) {
                new getSpaceTypeCombo().execute("");
            } else {
                DialogUtils.showalert(getString(R.string.no_internet_string), this);
            }
        } else {
            parseSpaceTypeData(SharedPreferenceUtils.getLocalStorage(Constants.SPACE_TYPE_JSON, this));
        }
        addRoomButton.setOnClickListener(this);
    }

    private void parseSpaceTypeData(String result) throws JSONException {
        JSONArray resultArray = new JSONArray(result);
        int resultLength = resultArray.length();
        for (int i = 0; i < resultLength; i++) {
            JSONObject innerObject = resultArray.getJSONObject(i);
            spaceTypeIdArray.add(innerObject.getString(Constants.ID_KEY));
            spaceTypeArray.add(innerObject.getString(Constants.NAME_KEY));
        }
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spaceTypeArray);
        spaceTypeSpinner.setAdapter(statusAdapter);
        String uName = SharedPreferenceUtils.getLocalStorage("USERNAME", this);
        String pwd = SharedPreferenceUtils.getLocalStorage("PASSWORD", this);
        String params = ServiceUtils.getEstimateListData(uName, pwd, "1", "1");
        new getRoomsListData().execute(params);
    }

    private void parseRoomsData(String result) throws JSONException {
        JSONObject resultObject = new JSONObject(result);
        JSONArray dataArray = resultObject.getJSONArray(Constants.DATA_KEY);
        estJobArrayList = new ArrayList<>();
        int dataLength = dataArray.length();
        for (int i = 0; i < dataLength; i++) {
            JSONObject innerObject = dataArray.getJSONObject(i);
            EstJobBean estJobBean = new EstJobBean(innerObject.getString("id"), innerObject.getString("project_id"),
                    innerObject.getString("status"),
                    innerObject.getString("job_id"), innerObject.getString("estimate_id"),
                    innerObject.getString("Quantity"), innerObject.getString("Unit"), innerObject.getString("Coats"),
                    innerObject.getString("Finish"), innerObject.getString("Rates"), innerObject.getString("Gals"),
                    innerObject.getString("Time"), innerObject.getString("created_date"),innerObject.getString("job"));
            estJobArrayList.add(estJobBean);
        }
        resetAdapter();
    }

    private void resetAdapter() {
        jobsListAdapter = new JobsListAdapter(this, estJobArrayList);
        estJobsListView.setAdapter(jobsListAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == addRoomButton) {
            // Handle clicks for addRoomButton
            addRoomDialog.show();
        }
    }

    public class getSpaceTypeCombo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return networkUtility.postHttp(Constants.DROPDOWN_URL + "param=room_types", strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null != result && !TextUtils.isEmpty(result)) {
                try {
                    SharedPreferenceUtils.setLocalStorage(Constants.SPACE_TYPE_JSON, result, AddRoomActivity.this);
                    parseSpaceTypeData(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtils.showalert(getString(R.string.technical_issue_string), AddRoomActivity.this);
            }
        }
    }

    public class getRoomsListData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            return networkUtility.postHttp(Constants.FETCH_EST_URL + strings[0], strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (null != result && !TextUtils.isEmpty(result)) {
                try {
                    parseRoomsData(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtils.showalert(getString(R.string.technical_issue_string), AddRoomActivity.this);
            }
        }
    }
}
