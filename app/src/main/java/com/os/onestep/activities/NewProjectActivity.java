package com.os.onestep.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.os.onestep.R;
import com.os.onestep.utilities.Constants;
import com.os.onestep.utilities.DateUtils;
import com.os.onestep.utilities.SharedPreferenceUtils;
import com.os.onestep.utilities.dialogutils.DialogUtils;
import com.os.onestep.utilities.httpconnection.ConnectionManager;
import com.os.onestep.utilities.httpconnection.NetworkUtility;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class NewProjectActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    ConnectionManager connectionManager;
    String emailPattern = "[a-zA-Z0-9.+_-]+@[a-z]+\\.+[a-z]+";
    String countryId, stateId, cityId;
    Button createButton, cancelBtn;
    private TextView userNameString;
    private EditText projectDateText;
    private EditText clientNameText;
    private EditText addressText;
    private Spinner countrySpinner, stateSpinner, citySpinner;
    private EditText zipCodeText;
    private EditText phoneNumText;
    private EditText alternatePhoneText;
    private EditText emailText;
    private EditText userText;
    private ArrayList<String> countryIdList, stateIdList, cityIdList;
    private NetworkUtility networkUtility;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproject);
        findViews();
        setListeners();
    }

    private void findViews() {
        countryId = "";
        stateId = "";
        cityId = "";
        userNameString = (TextView) findViewById(R.id.userNameString);
        projectDateText = (EditText) findViewById(R.id.projectDateText);
        clientNameText = (EditText) findViewById(R.id.clientNameText);
        addressText = (EditText) findViewById(R.id.addressText);
        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        zipCodeText = (EditText) findViewById(R.id.zipCodeText);
        phoneNumText = (EditText) findViewById(R.id.phoneNumText);
        alternatePhoneText = (EditText) findViewById(R.id.alternatePhoneText);
        emailText = (EditText) findViewById(R.id.emailText);
        userText = (EditText) findViewById(R.id.userText);
        createButton = (Button) findViewById(R.id.createButton);
        cancelBtn = (Button) findViewById(R.id.resetButton);
        connectionManager = new ConnectionManager(this);
        networkUtility = new NetworkUtility();
        countryIdList = new ArrayList<>();
        stateIdList = new ArrayList<>();
        cityIdList = new ArrayList<>();
        new getCountryData().execute("");
    }

    private void setListeners() {
        projectDateText.setOnClickListener(this);
        createButton.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        countrySpinner.post(new Runnable() {
            @Override
            public void run() {
                countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            countryId = countryIdList.get(position - 1);
                            if (connectionManager.isConnectingToInternet()) {
                                new getStatesData().execute("");
                            } else {
                                DialogUtils.showalert(getString(R.string.no_internet_string), NewProjectActivity.this);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        });

        stateSpinner.post(new Runnable() {
            @Override
            public void run() {
                stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            stateId = stateIdList.get(position - 1);
                            if (connectionManager.isConnectingToInternet()) {
                                new getCityData().execute("");
                            } else {
                                DialogUtils.showalert(getString(R.string.no_internet_string), NewProjectActivity.this);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        citySpinner.post(new Runnable() {
            @Override
            public void run() {
                citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            cityId = cityIdList.get(position - 1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    private boolean isValidProject() {
        String proNameString = projectDateText.getText().toString();
        String clientNameString = clientNameText.getText().toString();
        String addressString = addressText.getText().toString();
        String zipCodeString = zipCodeText.getText().toString();
        String phoneNumString = phoneNumText.getText().toString();
        String alterPhoneString = alternatePhoneText.getText().toString();
        String emailString = emailText.getText().toString();
        if ("".equals(proNameString)) {
            DialogUtils.showalert(getString(R.string.projectname_validation), this);
            return false;
        } else if ("".equals(clientNameString)) {
            DialogUtils.showalert(getString(R.string.clientname_validation), this);
            return false;
        } else if ("".equals(addressString)) {
            DialogUtils.showalert(getString(R.string.address_validation), this);
            return false;
        } else if ("".equals(zipCodeString)) {
            DialogUtils.showalert(getString(R.string.zipcode_validation), this);
            return false;
        } else if ("".equals(phoneNumString) || phoneNumString.length() < 10) {
            DialogUtils.showalert(getString(R.string.phone_validation), this);
            return false;
        } else if ("".equals(alterPhoneString) || alterPhoneString.length() < 10) {
            DialogUtils.showalert(getString(R.string.alter_phone_validation), this);
            return false;
        } else if ("".equals(emailString)) {
            DialogUtils.showalert(getString(R.string.email_validation), this);
            return false;
        } else if (!emailString.matches(emailPattern)) {
            DialogUtils.showalert(getString(R.string.email_validation), this);
            return false;
        } else if (countrySpinner.getSelectedItemPosition() == 0) {
            DialogUtils.showalert(getString(R.string.country_validation), this);
            return false;
        } else if ("".equals(stateId)) {
            DialogUtils.showalert(getString(R.string.state_validation), this);
            return false;
        } else if ("".equals(cityId)) {
            DialogUtils.showalert(getString(R.string.city_validation), this);
            return false;
        }
        return true;
    }

    private void makeHTTPCall() {
        String proNameString = projectDateText.getText().toString();
        String clientNameString = clientNameText.getText().toString();
        String addressString = addressText.getText().toString();
        String zipCodeString = zipCodeText.getText().toString();
        String phoneNumString = phoneNumText.getText().toString();
        String alterPhoneString = alternatePhoneText.getText().toString();
        String emailString = emailText.getText().toString();
        String userString = userText.getText().toString();

        String params = "name=" + clientNameString + "&phonenumber=" + phoneNumString +
                "&email=" + emailString + "&Date=" + proNameString + "&Address=" + addressString +
                "&City=" + cityId + "&State=" + stateId + "&Zip=" + zipCodeString + "&country=" + countryId + "&alt_phone=" + alterPhoneString +
                "&username=" + SharedPreferenceUtils.getLocalStorage("USERNAME", this) + "&password=" + SharedPreferenceUtils.getLocalStorage("PASSWORD", this);
        new createProject().execute(params);
    }

    private void parseCountryJson(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);
        int jsonArrayLength = jsonArray.length();
        ArrayList<String> countryNameList = new ArrayList<>();
        countryNameList.add("Select Country");
        for (int i = 0; i < jsonArrayLength; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            countryIdList.add(jsonObject.getString("location_id"));
            countryNameList.add(jsonObject.getString("name"));
        }
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, countryNameList);
        countrySpinner.setAdapter(countryAdapter);
    }

    private void parseCityJSON(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);
        int jsonArrayLength = jsonArray.length();
        ArrayList<String> cityNameArrayList = new ArrayList<>();
        cityNameArrayList.add("Select City");
        for (int i = 0; i < jsonArrayLength; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            cityIdList.add(jsonObject.getString("location_id"));
            cityNameArrayList.add(jsonObject.getString("name"));
        }
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cityNameArrayList);
        citySpinner.setAdapter(cityAdapter);
    }

    private void parseStateJSON(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);
        int jsonArrayLength = jsonArray.length();
        ArrayList<String> stateNameArrayList = new ArrayList<>();
        stateNameArrayList.add("Select State");
        for (int i = 0; i < jsonArrayLength; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            stateIdList.add(jsonObject.getString("location_id"));
            stateNameArrayList.add(jsonObject.getString("name"));
        }
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, stateNameArrayList);
        stateSpinner.setAdapter(stateAdapter);
    }

    private void showBirthDateDialog(String dialogTitle, int day, int month, int year) {

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                year,
                month,
                day
        );
        dpd.setMaxDate(Calendar.getInstance());
        dpd.dismissOnPause(false);
        dpd.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dpd.setTitle(dialogTitle);
        dpd.show(this.getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.projectDateText:
                try {
                    int day, month, year;
                    String birthDateString = projectDateText.getText().toString();
                    if (!TextUtils.isEmpty(birthDateString)) {
                        day = DateUtils.getDayFromDateString(birthDateString);
                        month = DateUtils.getMonthFromDateString(birthDateString) - 1;
                        year = DateUtils.getYearFromDateString(birthDateString);
                    } else {
                        Calendar now = Calendar.getInstance();
                        day = now.get(Calendar.DAY_OF_MONTH);
                        month = now.get(Calendar.MONTH);
                        year = now.get(Calendar.YEAR);
                    }
                    showBirthDateDialog("Select BirthDate", day, month, year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.createButton:
                if (isValidProject()) {
                    makeHTTPCall();
                }
                break;
            case R.id.resetButton:
                this.finish();
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String monthString = String.valueOf(monthOfYear + 1);
        String dayString = String.valueOf(dayOfMonth);
        if (dayString.length() == 1) {
            dayString = "0" + dayString;
        }
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }
        String date = dayString + "/" + monthString + "/" + year;
        projectDateText.setText(date);
    }

    public class getCountryData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog1 = new ProgressDialog(NewProjectActivity.this);
            progressDialog1.setMessage("Loading..");
            progressDialog1.setCancelable(false);
            progressDialog1.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return networkUtility.postHttp("http://divsinfotech.com/OneStep/api/fetchdropdown?param=Country", strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog1.isShowing()) {
                progressDialog1.dismiss();
            }
            Log.e("REsult", "result" + result);
            if (null != result && !"".equals(result)) {
                try {
                    parseCountryJson(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtils.showalert(getString(R.string.technical_issue_string), NewProjectActivity.this);
            }
        }
    }

    public class getStatesData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog1 = new ProgressDialog(NewProjectActivity.this);
            progressDialog1.setMessage("Loading..");
            progressDialog1.setCancelable(false);
            progressDialog1.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return networkUtility.postHttp("http://divsinfotech.com/OneStep/api/fetchdropdown?param=State&parent_id=" + countryId, strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog1.isShowing()) {
                progressDialog1.dismiss();
            }
            Log.e("REsult", "result" + result);
            if (null != result && !"".equals(result)) {
                try {
                    parseStateJSON(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtils.showalert(getString(R.string.technical_issue_string), NewProjectActivity.this);
            }
        }
    }

    public class getCityData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog2 = new ProgressDialog(NewProjectActivity.this);
            progressDialog2.setMessage("Loading..");
            progressDialog2.setCancelable(false);
            progressDialog2.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return networkUtility.postHttp("http://divsinfotech.com/OneStep/api/fetchdropdown?param=city&parent_id=" + stateId, strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog2.isShowing()) {
                progressDialog2.dismiss();
            }
            Log.e("REsult", "result" + result);
            if (null != result && !"".equals(result)) {
                try {
                    parseCityJSON(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtils.showalert(getString(R.string.technical_issue_string), NewProjectActivity.this);
            }
        }
    }

    public class createProject extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog2 = new ProgressDialog(NewProjectActivity.this);
            progressDialog2.setMessage("Loading..");
            progressDialog2.setCancelable(false);
            progressDialog2.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return networkUtility.postHttp("http://divsinfotech.com/OneStep/api/createnewproject?" + strings[0], strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog2.isShowing()) {
                progressDialog2.dismiss();
            }
            Log.e("REsult", "result" + result);
            if (null != result && !"".equals(result)) {
                try {
                    JSONObject resultObject = new JSONObject(result);
                    DialogUtils.showalert(resultObject.getString("message"), NewProjectActivity.this);
                    SharedPreferenceUtils.setLocalStorage(Constants.PROJECT_ID, resultObject.getString(Constants.ID_KEY), NewProjectActivity.this);
                    startActivity(new Intent(NewProjectActivity.this, EstimationCategoryActivity.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtils.showalert(getString(R.string.technical_issue_string), NewProjectActivity.this);
            }
        }
    }
}
