package com.example.music_app.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.api.exceptions.ApiException;
import com.example.music_app.R;
import com.example.music_app.activities.LibraryActivity;
import com.example.music_app.activities.MainActivity;
import com.example.music_app.internals.SharePrefManagerAccount;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.ForgotPassword;
import com.example.music_app.models.LoginRequest;
import com.example.music_app.models.LoginResponse;
import com.example.music_app.models.RegisterRequest;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.utils.Const;
import com.example.music_app.utils.Validate;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView signUpText, forgotPasswordText;
    private TextInputEditText emailTxt, passwordTxt;
    private TextInputLayout emailLayout, passwordLayout;
    private MaterialButton btnLogin, btnGetOtp, btnLoginWithGoogle;
    private ProgressBar progressBar;
    private CheckBox checkBoxRemember;
    private FrameLayout overlay;
    private Validate validate = new Validate();
    private APIService apiService;
    private String email = "";

    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mapping();

        fillText();

        FirebaseApp.initializeApp(this);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                        .requestEmail()
                                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, options);
        auth = FirebaseAuth.getInstance();

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginRequest req = new LoginRequest();
                req.setEmail(emailTxt.getText().toString());
                req.setPassword(passwordTxt.getText().toString());
                req.setRole("USER");
                if(checkSuccess()) {
                    openOverlay();
                    if(checkBoxRemember.isChecked()) {
                        SharePrefManagerAccount.getInstance(getApplicationContext()).remember(req);
                    } else {
                        SharePrefManagerAccount.getInstance(getApplicationContext()).clear();
                    }
                    login(req);
                }
            }
        });

        btnLoginWithGoogle.setOnClickListener(new View.OnClickListener() {
            Context context;
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });

        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.length() != 0) {
                    openOverlay();
                    sendOtp();
                }
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        handleEvent();
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                auth = FirebaseAuth.getInstance();
                                Log.d("LoginWithGoogle", auth.getCurrentUser().getDisplayName());
                                Log.d("LoginWithGoogle", auth.getCurrentUser().getEmail());
                                Log.d("LoginWithGoogle", String.valueOf(auth.getCurrentUser().getPhotoUrl()));
                                loginOAuth(auth.getCurrentUser().getEmail(), auth.getCurrentUser().getDisplayName(), String.valueOf(auth.getCurrentUser().getPhotoUrl()));
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Failed to sign in" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
                catch (ApiException e){
                    e.printStackTrace();
                }
            }
        }
    });

    private void loginOAuth(String email, String name, String image) {
        Log.d("LoginToken", "Code chay vo day");
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setLastName(name);
        registerRequest.setEmail(email);
        registerRequest.setAvatar(image);

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.authenticateOAuth(registerRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse res = response.body();
                if(res == null) {
                    return;
                }
                if(res.isSuccess()) {
                    Log.d("LoginToken", res.getAccessToken());
                    User user = new User();
                    user.setFirstName(res.getFirstName());
                    user.setLastName(res.getLastName());
                    user.setAvatar(res.getAvatar());
                    user.setEmail(res.getEmail());
                    user.setGender(res.getGender());
                    user.setId(res.getId());
                    user.setAccessToken(res.getAccessToken());
                    Const.setAccessToken(res.getAccessToken());
                    user.setRefreshToken(res.getRefreshToken());
                    user.setProvider(res.getProvider());
                    // Handle data for realtime
                    Log.d("ProviderLogin", res.getProvider());
                    Log.d("LoginToken", res.getAccessToken());
                    saveTokenToServer(res.getId());
                    SharePrefManagerUser.getInstance(getApplicationContext()).loginSuccess(user);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(LoginActivity.this, getText(R.string.toast_loged_in), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideOverlay() {
        overlay.setVisibility(View.INVISIBLE);
        overlay.setFocusable(false);
        overlay.setClickable(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void openOverlay() {
        overlay.setBackgroundColor(Color.argb(89, 0, 0, 0));
        overlay.setVisibility(View.VISIBLE);
        overlay.setFocusable(true);
        overlay.setClickable(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void sendOtp() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setEmail(email);
        apiService.sendOtp(forgotPassword).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                hideOverlay();
                ResponseMessage res = new ResponseMessage();
                res = response.body();
                if(res == null) {
                    Toast.makeText(LoginActivity.this, "Encounter Error!", Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println(res.getMessage());
                Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                if(res.isSuccess()) {
                    Intent intent = new Intent(LoginActivity.this, OtpVerifyActivity.class);
                    intent.putExtra("type", "confirm");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                hideOverlay();
                Toast.makeText(LoginActivity.this, "Call API Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillText() {
        boolean isRemember = SharePrefManagerAccount.getInstance(getApplicationContext()).isRemember();
        if(isRemember) {
            LoginRequest req = SharePrefManagerAccount.getInstance(getApplicationContext()).getRemenember();
            emailTxt.setText(req.getEmail());
            passwordTxt.setText(req.getPassword());
            checkBoxRemember.setChecked(true);
        }
    }

    private void login(LoginRequest req) {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.authenticate(req).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideOverlay();
                LoginResponse res = response.body();
                if(res == null) {
                    Toast.makeText(LoginActivity.this, getText(R.string.error_wrong_account_infor), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(res.isSuccess()) {
                    User user = new User();
                    user.setFirstName(res.getFirstName());
                    user.setLastName(res.getLastName());
                    user.setAvatar(res.getAvatar());
                    user.setEmail(res.getEmail());
                    user.setGender(res.getGender());
                    user.setId(res.getId());
                    user.setAccessToken(res.getAccessToken());
                    Const.setAccessToken(res.getAccessToken());
                    user.setRefreshToken(res.getRefreshToken());
                    user.setProvider(res.getProvider());
                    SharePrefManagerUser.getInstance(getApplicationContext()).loginSuccess(user);
                    saveTokenToServer(res.getId());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                if(res.getType() != null && res.getType().equals("confirm")) {
                    btnGetOtp.setVisibility(View.VISIBLE);
                    email = res.getEmail();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideOverlay();
                Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkSuccess() {
        return emailLayout.getError() == null && passwordLayout.getError() == null;
    }

    private void handleEvent() {
        setEvent(emailLayout, emailTxt, "email");
        setEvent(passwordLayout, passwordTxt, "");
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

    private void mapping() {
        signUpText = (TextView) findViewById(R.id.signUpText);
        forgotPasswordText = (TextView) findViewById(R.id.forgotPasswordText);
        emailTxt = (TextInputEditText) findViewById(R.id.emailTxt);
        passwordTxt = (TextInputEditText) findViewById(R.id.passwordTxt);
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        btnLogin = (MaterialButton) findViewById(R.id.btnLogin);
        btnLoginWithGoogle =(MaterialButton) findViewById(R.id.btnLoginWithGoogle);
        checkBoxRemember = (CheckBox) findViewById(R.id.checkBoxRemember);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        overlay = (FrameLayout) findViewById(R.id.overlay);
        btnGetOtp = (MaterialButton) findViewById(R.id.btnGetOtp);
    }

    private void saveTokenToServer(int id) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                String token = task.getResult();
                Log.e("saveTokenToServer", token);
                String userId = String.valueOf(id);
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference tokenRef = database.getReference("tokenPhone");
                tokenRef.child(userId).setValue(token);
            }
        });
    }
}