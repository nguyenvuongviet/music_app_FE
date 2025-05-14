package com.example.music_app.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.models.RegisterRequest;
import com.example.music_app.models.RegisterResponse;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.utils.Validate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText firstNameTxt, lastNameTxt, emailTxt, phoneNumberTxt, passwordTxt, passwordAgainTxt;
    private TextInputLayout firstNameLayout, lastNameLayout, emailLayout, phoneNumberLayout, passwordLayout, passwordAgainLayout;
    private TextView loginText;
    private RadioGroup radioGroup;
    private MaterialButton btnSignUp;
    private APIService apiService;
    private ProgressBar progressBar;
    private FrameLayout overlay;
    private int gender = 1;
    private Validate validate = new Validate();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mapping();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSuccess()) {
                    progressBar.setVisibility(View.VISIBLE);
                    overlay.setBackgroundColor(Color.argb(89, 0, 0, 0));
                    overlay.setVisibility(View.VISIBLE);
                    overlay.setFocusable(true);
                    overlay.setClickable(true);
                    signUp();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = findViewById(i);
                if(checkedRadioButton != null) {
                    String tag = checkedRadioButton.getTag().toString();
                    gender = Integer.parseInt(tag);
                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        handleEvent();
    }

    private boolean checkSuccess() {
        return firstNameLayout.getError() == null && lastNameLayout.getError() == null && phoneNumberLayout.getError() == null && passwordLayout.getError() == null && emailLayout.getError() == null && passwordAgainLayout.getError() == null;
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
                    if(type.equals("email") && !validate.validateEmail(inp)) {
                        textInputLayout.setError(getText(R.string.error_invalid_email));
                    } else if(type.equals("phoneNumber") && !validate.validatePhoneNumber(inp)) {
                        textInputLayout.setError(getText(R.string.error_invalid_phone));
                    } else if(type.equals("password") && !validate.validatePassword(inp)) {
                        textInputLayout.setError(getText(R.string.error_invalid_password));
                    } else if(type.equals("passwordAgain") && !passwordTxt.getText().toString().equals(inp)) {
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
                        if(type.equals("email") && !validate.validateEmail(inp)) {
                            textInputLayout.setError(getText(R.string.error_invalid_email));
                        } else if(type.equals("phoneNumber") && !validate.validatePhoneNumber(inp)) {
                            textInputLayout.setError(getText(R.string.error_invalid_phone));
                        } else if(type.equals("password") && !validate.validatePassword(inp)) {
                            textInputLayout.setError(getText(R.string.error_invalid_password));
                        } else if(type.equals("passwordAgain") && !passwordTxt.getText().toString().equals(inp)) {
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

    private void handleEvent() {
        setEvent(firstNameLayout, firstNameTxt, "");
        setEvent(lastNameLayout, lastNameTxt, "");
        setEvent(phoneNumberLayout, phoneNumberTxt, "phoneNumber");
        setEvent(passwordLayout, passwordTxt, "password");
        setEvent(emailLayout, emailTxt, "email");
        setEvent(passwordAgainLayout, passwordAgainTxt, "passwordAgain");
    }

    private void mapping() {
        firstNameTxt = (TextInputEditText) findViewById(R.id.firstNameTxt);
        lastNameTxt = (TextInputEditText) findViewById(R.id.lastNameTxt);
        emailTxt = (TextInputEditText) findViewById(R.id.emailTxt);
        phoneNumberTxt = (TextInputEditText) findViewById(R.id.phoneNumberTxt);
        passwordTxt = (TextInputEditText) findViewById(R.id.passwordTxt);
        passwordAgainTxt = (TextInputEditText) findViewById(R.id.passwordAgainTxt);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnSignUp = (MaterialButton) findViewById(R.id.btnSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        firstNameLayout = (TextInputLayout) findViewById(R.id.firstNameLayout);
        lastNameLayout = (TextInputLayout) findViewById(R.id.lastNameLayout);
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        phoneNumberLayout = (TextInputLayout) findViewById(R.id.phoneNumberLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        passwordAgainLayout = (TextInputLayout) findViewById(R.id.passwordAgainLayout);
        loginText = (TextView) findViewById(R.id.loginText);
        overlay = (FrameLayout) findViewById(R.id.overlay);
    }

    private void signUp() {
        RegisterRequest registerModel = new RegisterRequest();
        registerModel.setFirstName(firstNameTxt.getText().toString());
        registerModel.setLastName(lastNameTxt.getText().toString());
        registerModel.setEmail(emailTxt.getText().toString());
        registerModel.setPhoneNumber(phoneNumberTxt.getText().toString());
        registerModel.setPassword(passwordTxt.getText().toString());
        registerModel.setGender(gender);

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.register(registerModel).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                overlay.setVisibility(View.INVISIBLE);
                overlay.setFocusable(false);
                overlay.setClickable(false);
                Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, OtpVerifyActivity.class);
                intent.putExtra("type", "confirm");
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                overlay.setVisibility(View.INVISIBLE);
                overlay.setFocusable(false);
                overlay.setClickable(false);
                Toast.makeText(SignUpActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}