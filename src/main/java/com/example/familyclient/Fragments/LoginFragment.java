package com.example.familyclient.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.familyclient.ActivityHelpers.CallBackFragment;
import com.example.familyclient.R;

import AsyncTasks.LoginTask;
import AsyncTasks.RegisterTask;
import Model.UserMod;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;

public class LoginFragment extends Fragment implements LoginTask.LoginContext, RegisterTask.RegisterContext{

    private RadioGroup radioGroup;
    private Button register, login;
    private EditText username, password, email, firstName, lastName, serverPort, serverHost;
    private RadioButton genderMale;
    private String usernameStr, passwordStr, emailStr, firstNameStr, lastNameStr, serverPortStr, serverHostStr;
    private String gender;
    private UserMod userMod = null;
    private LoginRequest loginRequest = null;
    private RegisterRequest registerRequest = null;
    private LoginResult loginResult = null;

    private CallBackFragment callBackFragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerRequest = new RegisterRequest(null,null,null,null,null,null);
        loginRequest = new LoginRequest(null,null);
    }

    public void setCallBackFragment(CallBackFragment callBackFragment){
        this.callBackFragment = callBackFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isLoginActivated();
                isRegisterActivated();
            }
        };

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        firstName = view.findViewById(R.id.FirstName);
        lastName = view.findViewById(R.id.LastName);


        register = view.findViewById(R.id.register);
        login = view.findViewById(R.id.login);

        radioGroup = view.findViewById(R.id.radioGroup2);
        genderMale = view.findViewById(R.id.MaleButton);

        serverHost = view.findViewById(R.id.ServerHost);
        serverPort = view.findViewById(R.id.ServerPort);

        username.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);
        email.addTextChangedListener(watcher);
        firstName.addTextChangedListener(watcher);
        lastName.addTextChangedListener(watcher);
        serverHost.addTextChangedListener(watcher);
        serverPort.addTextChangedListener(watcher);

        initializeStrings();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initializeStrings();

                RegisterRequest registerRequest = new RegisterRequest(usernameStr,passwordStr,emailStr,firstNameStr,lastNameStr,gender);
                RegisterTask registerTask = new RegisterTask(serverHostStr,serverPortStr,LoginFragment.this, LoginFragment.this);
                registerTask.execute(registerRequest);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initializeStrings();
                loginRequest = new LoginRequest(usernameStr,passwordStr);
                LoginTask loginTask = new LoginTask(serverPort.getText().toString(),serverHost.getText().toString(),LoginFragment.this, LoginFragment.this);
                loginTask.execute(loginRequest);
//
            }
        });


        return view;

    }

    public void initializeStrings(){
        usernameStr = username.getText().toString();
        passwordStr = password.getText().toString();
        emailStr = email.getText().toString();
        firstNameStr = firstName.getText().toString();
        lastNameStr = lastName.getText().toString();
        serverPortStr = serverPort.getText().toString();
        serverHostStr = serverHost.getText().toString();
        if (genderMale.isChecked()) {
            gender = "m";
        } else {
            gender = "f";
        }

    }

    public void isRegisterActivated(){
        initializeStrings();
        if(usernameStr.equals("")|| passwordStr.equals("") || emailStr.equals("") ||
                firstNameStr.equals("")|| lastNameStr.equals("") ||
                serverHostStr.equals("") || serverPortStr.equals("")){
            register.setEnabled(false);
        }

        else{
            register.setEnabled(true);
        }
    }

    public void isLoginActivated(){

        if(usernameStr.equals("")|| passwordStr.equals("") ||
                serverHostStr.equals("") || serverPortStr.equals("")){
            login.setEnabled(false);
        }

        else{
            login.setEnabled(true);
        }

    }

    @Override
    public void ExecuteComplete(String toastMessage) {
        Toast.makeText(getContext(),toastMessage,Toast.LENGTH_SHORT).show();
        callBackFragment.changeFragment();
    }
}