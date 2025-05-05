package com.example.music_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.activities.auth.LoginActivity;
import com.example.music_app.activities.auth.ResetPasswordActivity;
import com.example.music_app.models.ChangePasswordRequest;
import com.example.music_app.models.ResetPasswordRequest;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.utils.Validate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private TextInputEditText passwordTxt, newPasswordTxt, repeatPasswordTxt;
    private TextInputLayout oldPasswordLayout, newPasswordLayout, repeatPasswordLayout;
    private ProgressBar progressBar;
    private FrameLayout overlay;
    private APIService apiService;
    private Validate validate = new Validate();
    private MaterialButton btnSubmit;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mapping();
        title.setText(getText(R.string.label_change_password));
        sharedPref = getSharedPreferences("user",MODE_PRIVATE);
        String provider = sharedPref.getString("keyprovider","");
        Log.d("provider_user", provider);
        if(!Objects.equals(provider, "DATABASE")){
            Log.d("provider_user", "code chay vao disalbe");
            passwordTxt.setEnabled(false);
            newPasswordTxt.setEnabled(false);
            repeatPasswordTxt.setEnabled(false);
            btnSubmit.setEnabled(false);
        }
        setEvent(oldPasswordLayout, passwordTxt, "password");
        setEvent(newPasswordLayout, newPasswordTxt, "password");
        setEvent(repeatPasswordLayout, repeatPasswordTxt, "passwordAgain");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(oldPasswordLayout.getError() == null && newPasswordLayout.getError() == null && repeatPasswordLayout.getError()== null) {
                    progressBar.setVisibility(View.VISIBLE);
//                    overlay.setBackgroundColor(Color.argb(89, 0, 0, 0));
//                    overlay.setVisibility(View.VISIBLE);
//                    overlay.setFocusable(true);
//                    overlay.setClickable(true);
                    Log.d("provider_user", "code chay vao day");
                    changePassword();
                }
            }
        });
    }

    private void changePassword() {
        Log.d("provider_user","Code chay vao day");
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setOldPassword(passwordTxt.getText().toString());
        req.setNewPassword(newPasswordTxt.getText().toString());
        int userId = sharedPref.getInt("keyid",-1);
        Log.d("provider_user", String.valueOf(userId));
        apiService.changePasswordWithIdUser(userId, req).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                progressBar.setVisibility(View.INVISIBLE);
//                overlay.setVisibility(View.INVISIBLE);
//                overlay.setFocusable(false);
//                overlay.setClickable(false);

                ResponseMessage res = response.body();
                if(res == null) {
                    Toast.makeText(ChangePasswordActivity.this, "Encounter Error!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ChangePasswordActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                if(res.isSuccess()) {
                    Intent intent1 = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
//                overlay.setVisibility(View.INVISIBLE);
//                overlay.setFocusable(false);
//                overlay.setClickable(false);
                Toast.makeText(ChangePasswordActivity.this, "Call API Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEvent(TextInputLayout textInputLayout, TextInputEditText textInput, String type) {
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inp = textInput.getText().toString();
                if(inp.length() == 0) {
                    textInputLayout.setError(getText(R.string.error_required_field));
                } else {
                    if(type.equals("password") && !validate.validatePassword(inp)) {
                        textInputLayout.setError(getText(R.string.error_invalid_password));
                    } else if(type.equals("passwordAgain") && !newPasswordTxt.getText().toString().equals(inp)) {
                        textInputLayout.setError(getText(R.string.error_password_not_match));
                    } else {
                        textInputLayout.setError(null);
                    }
                }
            }
        });

        textInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String inp = textInput.getText().toString();
                if(b) {
                    if(inp.length() != 0) {
                        if(type.equals("password") && !validate.validatePassword(inp)) {
                            textInputLayout.setError(getText(R.string.error_invalid_password));
                        } else if(type.equals("passwordAgain") && !repeatPasswordTxt.getText().toString().equals(inp)) {
                            textInputLayout.setError(getText(R.string.error_password_not_match));
                        }
                        return;
                    }
                    textInputLayout.setError(null);
                } else {
                    if(inp.length() == 0) {
                        textInputLayout.setError(getText(R.string.error_required_field));
                    }
                }
            }
        });
    }

    private void mapping(){
        passwordTxt = (TextInputEditText) findViewById(R.id.passwordTxt_changePassword);
        newPasswordTxt = (TextInputEditText) findViewById(R.id.newPasswordTxt_changePassword);
        repeatPasswordTxt = (TextInputEditText) findViewById(R.id.repeatPasswordTxt_changePassword);
        btnSubmit = (MaterialButton) findViewById(R.id.btnSubmit_changePassword);
        oldPasswordLayout = (TextInputLayout) findViewById(R.id.passwordLayout_changePassword);
        newPasswordLayout = (TextInputLayout) findViewById(R.id.newPasswordLayout_changePassword);
        repeatPasswordLayout = (TextInputLayout) findViewById(R.id.repeatPasswordLayout_changePassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        overlay = (FrameLayout) findViewById(R.id.overlay);
        title = (TextView) findViewById(R.id.title_appbar_with_back_icon);
    }
}