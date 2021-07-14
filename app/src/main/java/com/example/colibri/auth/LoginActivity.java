package com.example.colibri.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.colibri.MainActivity;
import com.example.colibri.R;
import com.example.colibri.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {
    Button sendButton, verifyButton;
    EditText inputPhoneNumber;
    EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    View bottomSheetView;
    ProgressBar progressBar;
    String phoneId;
    BottomSheetDialog bottomSheetDialog;
    String code, codeNumber;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        initView();
        clicButton();
        callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeNumber = s;
                Toast.makeText(LoginActivity.this, "Sizga tasdiqlash kodi jo'natildi", Toast.LENGTH_SHORT).show();
            }
        };


    }


    private void clicButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputPhoneNumber.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Telefon nomeringizni kiriting", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progressBar.setVisibility(VISIBLE);
                    sendButton.setVisibility(View.INVISIBLE);

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber("+998"+inputPhoneNumber.getText().toString())
                            .setTimeout(60L,TimeUnit.SECONDS)
                            .setActivity(LoginActivity.this)
                            .setCallbacks(callback)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                    sendNumberShowDialog();


                }
            }
        });
    }

    private void initView() {
        bottomSheetDialog = new BottomSheetDialog(LoginActivity.this, R.style.BottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(LoginActivity.this)
                .inflate(R.layout.bottom_sheet, findViewById(R.id.bottomSheet));

        sendButton = findViewById(R.id.sendButton);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
        progressBar = bottomSheetView.findViewById(R.id.progres_bar);
        verifyButton = bottomSheetView.findViewById(R.id.verifyButton);

    }

    private void sendNumberShowDialog() {


            inputCode();

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readCode();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeNumber, code);
                    signIn(credential);


                    Toast.makeText(LoginActivity.this, "Bosildi", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                }

            });
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
            bottomSheetDialog.setCancelable(false);
        }



    private void inputCode() {
        inputCode1 = bottomSheetView.findViewById(R.id.inputCode1);
        inputCode2 = bottomSheetView.findViewById(R.id.inputCode2);
        inputCode3 = bottomSheetView.findViewById(R.id.inputCode3);
        inputCode4 = bottomSheetView.findViewById(R.id.inputCode4);
        inputCode5 = bottomSheetView.findViewById(R.id.inputCode5);
        inputCode6 = bottomSheetView.findViewById(R.id.inputCode6);

        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                        inputCode6.setFocusable(true);
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void readCode() {
        if (inputCode1.getText().toString() == "" || inputCode2.getText().toString() == "" || inputCode3.getText().toString() == "" ||
                inputCode4.getText().toString() == "" || inputCode5.getText().toString() == "" || inputCode6.getText().toString() == "") {
            Toast.makeText(this, "Tasdiqlash kodini kiriting", Toast.LENGTH_SHORT).show();
            return;
        } else {
            code = inputCode1.getText().toString() + inputCode2.getText().toString() +
                    inputCode3.getText().toString() + inputCode4.getText().toString() +
                    inputCode5.getText().toString() + inputCode6.getText().toString();

            progressBar.setVisibility(VISIBLE);
//            verifyButton.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser User = mAuth.getCurrentUser();
        if(User != null){

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

        }
    }
    private void signIn(PhoneAuthCredential credential){

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    phoneId = "+998"+inputPhoneNumber.getText().toString();
                    Users user = new Users(phoneId);
                    FirebaseUser user1 = task.getResult().getUser();

                    long creationTimestamp = user1.getMetadata().getCreationTimestamp();
                    long lastSignInTimestamp = user1.getMetadata().getLastSignInTimestamp();
                    if (creationTimestamp == lastSignInTimestamp) {
                        //do create new user

                        database.getReference().child("Users").child(phoneId).setValue(user);


                        Intent intent = new Intent(LoginActivity.this,UserDetails.class);
                        intent.putExtra("id",phoneId);
                        startActivity(intent);
                        finish();
                    } else {
                       Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("id",phoneId);
                        startActivity(intent);
                        finish();
                    }


                } else {
                    Toast.makeText(LoginActivity.this, "Xato", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}