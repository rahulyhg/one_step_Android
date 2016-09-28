package com.os.onestep.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.os.onestep.R;
import com.os.onestep.utilities.Constants;
import com.os.onestep.utilities.dialogutils.DialogUtils;
import com.os.onestep.utilities.httpconnection.ConnectionManager;
import com.os.onestep.utilities.httpconnection.NetworkUtility;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ConnectionManager connectionManager;
    private NetworkUtility networkUtility;
    private EditText firstNameText;
    private EditText LastNameText;
    private EditText emailIdText;
    private EditText passwordText;
    private EditText repeatPasswdText;
    private EditText mobilenumText;
    private EditText addressText;
    private EditText pincodeText;
    private EditText birthdateText;
    private EditText cityText;
    private Button signUpButton;
    private Button resetButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
    }

    private void findViews() {
        connectionManager = new ConnectionManager(this);
        networkUtility = new NetworkUtility();
        firstNameText = (EditText) findViewById(R.id.firstNameText);
        LastNameText = (EditText) findViewById(R.id.LastNameText);
        emailIdText = (EditText) findViewById(R.id.emailIdText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        repeatPasswdText = (EditText) findViewById(R.id.repeatPasswdText);
        mobilenumText = (EditText) findViewById(R.id.mobilenumText);
        addressText = (EditText) findViewById(R.id.addressText);
        pincodeText = (EditText) findViewById(R.id.pincodeText);
        birthdateText = (EditText) findViewById(R.id.birthdateText);
        cityText = (EditText) findViewById(R.id.cityText);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        resetButton = (Button) findViewById(R.id.resetButton);

        signUpButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    private boolean isValidUser() {
        String firstNameString = firstNameText.getText().toString().trim();
        String lastnameString = LastNameText.getText().toString().trim();
        String emailIdString = emailIdText.getText().toString().trim();
        String phonenumberString = mobilenumText.getText().toString().trim();
        String passwordString = passwordText.getText().toString().trim();
        String repeatpasswdString = repeatPasswdText.getText().toString().trim();
        String birthDateString = birthdateText.getText().toString().trim();
        String addressString = addressText.getText().toString().trim();
        String city = cityText.getText().toString().trim();
        String postalCode = pincodeText.getText().toString().trim();

        if (TextUtils.isEmpty(firstNameString)) {
            DialogUtils.showalert("Please enter valid first name!", this);
            return false;
        } else if (TextUtils.isEmpty(lastnameString)) {
            DialogUtils.showalert("Please enter valid last name!", this);
            return false;
        } else if (TextUtils.isEmpty(emailIdString)) {
            DialogUtils.showalert("Please enter valid email id!", this);
            return false;
        } else if (!emailIdString.matches(emailPattern)) {
            DialogUtils.showalert("Please enter valid email id!", this);
            return false;
        } else if (TextUtils.isEmpty(phonenumberString)) {
            DialogUtils.showalert("Please enter valid mobile number!", this);
            return false;
        } else if (phonenumberString.length() < 10) {
            DialogUtils.showalert("Please enter valid mobile number!", this);
            return false;
        } else if (TextUtils.isEmpty(passwordString)) {
            DialogUtils.showalert(getString(R.string.password_validation), this);
            return false;
        } else if (passwordString.length() < 6) {
            DialogUtils.showalert(getString(R.string.password_validation), this);
            return false;
        } else if (!passwordString.equals(repeatpasswdString)) {
            DialogUtils.showalert("Enter password does not matches!", this);
            return false;
        } else if (TextUtils.isEmpty(birthDateString)) {
            DialogUtils.showalert("Please enter valid birthdate!", this);
            return false;
        } else if (TextUtils.isEmpty(addressString)) {
            DialogUtils.showalert("Please enter valid address!", this);
            return false;
        } else if (TextUtils.isEmpty(city)) {
            DialogUtils.showalert("Please enter valid city!", this);
            return false;
        } else if (!"".equals(postalCode) && postalCode.length() < 3) {
            DialogUtils.showalert("Please enter valid pincode!", this);
            return false;
        }
        return true;
    }

    private void makeHttpCall() {
        String firstNameString = firstNameText.getText().toString().trim();
        String lastnameString = LastNameText.getText().toString().trim();
        String emailIdString = emailIdText.getText().toString().trim();
        String phonenumberString = mobilenumText.getText().toString().trim();
        String passwordString = passwordText.getText().toString().trim();
        String repeatpasswdString = repeatPasswdText.getText().toString().trim();
        String birthDateString = birthdateText.getText().toString().trim();
        String addressString = addressText.getText().toString().trim();
        String city = cityText.getText().toString().trim();
        String postalCode = pincodeText.getText().toString().trim();
        String params = "first_name=" + firstNameString + "&last_name=" + lastnameString +
                "&email=" + emailIdString + "&password=" + passwordString + "&gender=male&phonenumber=" + phonenumberString + "&address=" + addressString + "&postalcode=" + postalCode +
                "&birth_date=" + birthDateString + "&country=india&state=gujarat&city=" + city + "&status=1";
        if(connectionManager.isConnectingToInternet()) {
            new getRegisterDetails().execute(params);
        } else {
            DialogUtils.showalert(getString(R.string.no_internet_string), this);
        }
    }

    private void parseSignUpJson(String result) throws JSONException {
        JSONObject responseObject = new JSONObject(result);
        String resCodeString = responseObject.getString(Constants.SUCCESS_KEY);
        if (!"0".equals(resCodeString)) {
            Toast.makeText(getApplicationContext(), responseObject.getString(Constants.MESSSAGE_KEY), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            DialogUtils.showalert(responseObject.getString(Constants.MSG_KEY), this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signUpButton) {
            if (isValidUser()) {
                makeHttpCall();
            }
        } else if (v == resetButton) {
            finish();
        }
    }

    public class getRegisterDetails extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return networkUtility.postHttp(Constants.SIGNUP_URL + strings[0], strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (null != result && !TextUtils.isEmpty(result)) {
                try {
                    parseSignUpJson(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtils.showalert(getString(R.string.technical_issue_string), RegisterActivity.this);
            }
        }
    }
}
