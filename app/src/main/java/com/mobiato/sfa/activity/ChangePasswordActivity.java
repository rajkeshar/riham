package com.mobiato.sfa.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.mobiato.sfa.App;
import com.mobiato.sfa.BaseActivity;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.ActivityChangePasswordBinding;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.LoadingSpinner;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONObject;

import retrofit2.Call;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, true);
        me = this;
        setTitle("Change Password");


        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (isValid()) {
                    callAPI();
                }
                break;
        }
    }

    private boolean isValid() {
        if (binding.etOldPwd.getText().toString().isEmpty()) {
            UtilApp.displayAlert(ChangePasswordActivity.this, "Please enter old password");
            return false;
        } else if (binding.etNewPwd.getText().toString().isEmpty()) {
            UtilApp.displayAlert(ChangePasswordActivity.this, "Please enter new password");
            return false;
        } else if (binding.etConfirmPwd.getText().toString().isEmpty()) {
            UtilApp.displayAlert(ChangePasswordActivity.this, "Please enter confirm password");
            return false;
        } else if (!binding.etConfirmPwd.getText().toString().equalsIgnoreCase(binding.etNewPwd.getText().toString())) {
            UtilApp.displayAlert(ChangePasswordActivity.this, "New password and confirm password does not match!");
            return false;
        }
        return true;
    }


    private void callAPI() {

        LoadingSpinner progressDialog = new LoadingSpinner(ChangePasswordActivity.this);
        progressDialog.show();

        final Call<JsonObject> labelResponse = ApiClient.getService().callChangePassword(App.CHANGE_PASSWORD, binding.etOldPwd.getText().toString(),
                binding.etNewPwd.getText().toString(), Settings.getString(App.SALESMANID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                progressDialog.hide();
                Log.e("Change PWD Response", response.toString());

                if (response.body() != null) {
                    try {
                        UtilApp.logData(ChangePasswordActivity.this, "Change Pwd Response : " + response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if (progressDialog.isShowing()) {
                            progressDialog.hide();
                        }

                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChangePasswordActivity.this);
                                alertDialogBuilder.setTitle("Alert")
                                        .setMessage("Password Change Successfully!")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ChangePasswordActivity.this.finish();
                                                dialog.dismiss();
                                            }
                                        });
                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                // show it
                                alertDialog.show();
                            } else {
                                UtilApp.displayErrorDialog(ChangePasswordActivity.this, jsonObject.getString("MESSAGE"));
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Msg", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                progressDialog.hide();
                Log.e("Change PWD Fail", error.getMessage());
                UtilApp.logData(ChangePasswordActivity.this, "Change PWD Response Fail: API FAIL");
            }
        });

    }

}
