package com.os.onestep.utilities.dialogutils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.os.onestep.R;
import com.os.onestep.utilities.Constants;
import com.os.onestep.utilities.SharedPreferenceUtils;
import com.os.onestep.utilities.httpconnection.ConnectionManager;
import com.os.onestep.utilities.httpconnection.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddRoomDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ConnectionManager connectionManager;
    private NetworkUtility networkUtility;
    private Spinner jobTypeSpinner;
    private Spinner estJobTypeSpinner;
    private Button applyButton;
    private Button cancelButton;
    private ArrayList<String> typeArray, estJobTypeArray, estJobIdArray;

    public AddRoomDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_addroomdialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        try {
            findViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setListeners();
    }


    private void findViews() throws JSONException {
        connectionManager = new ConnectionManager(mContext);
        networkUtility = new NetworkUtility();
        typeArray = new ArrayList<>();
        jobTypeSpinner = (Spinner) findViewById(R.id.jobTypeSpinner);
        estJobTypeSpinner = (Spinner) findViewById(R.id.estJobTypeSpinner);
        applyButton = (Button) findViewById(R.id.applyButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        if ("".equals(SharedPreferenceUtils.getLocalStorage(Constants.TYPE_JSON, mContext))) {
            if (connectionManager.isConnectingToInternet()) {
                new getTypeCombo().execute("");
            } else {
                DialogUtils.showalert(mContext.getString(R.string.no_internet_string), mContext);
            }
        } else {
            parseTypeData(SharedPreferenceUtils.getLocalStorage(Constants.TYPE_JSON, mContext));
        }
    }

    private void setListeners() {
        applyButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        jobTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String jobType = jobTypeSpinner.getSelectedItem().toString();
                    new getEstJobTypeCombo().execute(jobType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void parseTypeData(String result) throws JSONException {
        JSONObject resultObject = new JSONObject(result);
        JSONArray dataArray = resultObject.getJSONArray(Constants.DATA_KEY);
        int resultLength = dataArray.length();
        for (int i = 0; i < resultLength; i++) {
            typeArray.add(dataArray.getString(i));
        }
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, typeArray);
        jobTypeSpinner.setAdapter(statusAdapter);
    }

    private void parseEstTypeData(String result) throws JSONException {
        JSONArray dataArray = new JSONArray(result);
        estJobIdArray = new ArrayList<>();
        estJobTypeArray = new ArrayList<>();
        int resultLength = dataArray.length();
        for (int i = 0; i < resultLength; i++) {
            JSONObject resultObject = dataArray.getJSONObject(i);
            estJobIdArray.add(resultObject.getString(Constants.ID_KEY));
            estJobTypeArray.add(resultObject.getString(Constants.JOB_NAME_KEY));
        }
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, estJobTypeArray);
        estJobTypeSpinner.setAdapter(statusAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == applyButton) {
            // Handle clicks for applyButton

        } else if (v == cancelButton) {
            dismiss();
        }
    }

    public class getTypeCombo extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loading..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return networkUtility.postHttp(Constants.DROP_TYPE_URL, strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (null != result && !TextUtils.isEmpty(result)) {
                try {
                    SharedPreferenceUtils.setLocalStorage(Constants.TYPE_JSON, result, mContext);
                    parseTypeData(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtils.showalert(mContext.getString(R.string.technical_issue_string), mContext);
            }
        }
    }

    public class getEstJobTypeCombo extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loading..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = Constants.DROPDOWN_URL + "param=estimates_jobs&parent_id=" + strings[0];
            url = url.replaceAll(" ","%20");
            return networkUtility.postHttp(url, strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (null != result && !TextUtils.isEmpty(result)) {
                try {
                    parseEstTypeData(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtils.showalert(mContext.getString(R.string.technical_issue_string), mContext);
            }
        }
    }
}