package com.os.onestep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.os.onestep.activities.HomeScreenActivity;
import com.os.onestep.activities.RegisterActivity;
import com.os.onestep.utilities.Constants;
import com.os.onestep.utilities.dialogutils.DialogUtils;
import com.os.onestep.utilities.httpconnection.ConnectionManager;
import com.os.onestep.utilities.httpconnection.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton, loginButton;
    private ConnectionManager connectionManager;
    private NetworkUtility networkUtility;
    private EditText userNameText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
    }

    private void findViews() {
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        userNameText = (EditText) findViewById(R.id.userNameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        connectionManager = new ConnectionManager(this);
        networkUtility = new NetworkUtility();
    }

    private void setListeners() {
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private boolean checkUser() {
        String username = userNameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            DialogUtils.showalert(getString(R.string.username_validation), this);
            return false;
        } else if (TextUtils.isEmpty(password)) {
            DialogUtils.showalert(getString(R.string.password_validation), this);
            return false;
        }
        return true;
    }

    private void makeHttpCall() {
        if (connectionManager.isConnectingToInternet()) {
            String username = userNameText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();
            String params = "&username=" + username + "&password=" + password;
            new getLoginDetails().execute(params);
        } else {
            DialogUtils.showalert(getString(R.string.no_internet_string), this);
        }
    }

    private void parseLoginJson(String result) throws JSONException {
        JSONObject responseObject = new JSONObject(result);
        String resCodeString = responseObject.getString(Constants.SUCCESS_KEY);
        if (!"0".equals(resCodeString)) {
            JSONArray userArray = responseObject.getJSONArray("user");
            JSONObject userObject = userArray.getJSONObject(0);
            Intent homeIntent = new Intent(this, HomeScreenActivity.class);
            startActivity(homeIntent);
            userNameText.setText("");
            passwordText.setText("");
        } else {
            DialogUtils.showalert(responseObject.getString(Constants.MSG_KEY), this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.loginButton:
                if(checkUser()) {
                    makeHttpCall();
                }
                break;
        }
    }

    public class getLoginDetails extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return networkUtility.postHttp(Constants.LOGIN_URL + strings[0], strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (null != result && !TextUtils.isEmpty(result)) {
                try {
                    parseLoginJson(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtils.showalert(getString(R.string.technical_issue_string), MainActivity.this);
            }
        }
    }
}
