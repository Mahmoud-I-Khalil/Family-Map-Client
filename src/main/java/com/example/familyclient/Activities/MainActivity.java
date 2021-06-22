package com.example.familyclient.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.familyclient.ActivityHelpers.CallBackFragment;
import com.example.familyclient.Fragments.LoginFragment;
import com.example.familyclient.Fragments.MapFragment;
import com.example.familyclient.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;

public class MainActivity extends AppCompatActivity implements CallBackFragment {

    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LoginFragment loginFragment;
    //Datacache datacache = Datacache.initialize();

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new IoniconsModule())
                .with(new FontAwesomeModule());

        addFragment();

    }

    public void addFragment(){
        LoginFragment loginFragment = new LoginFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        loginFragment.setCallBackFragment(this);
        fragmentTransaction.add(R.id.fragmentContainer, loginFragment);
        fragmentTransaction.commit();

    }

    public void replaceFragment(){

        Fragment mapFragment = new MapFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, mapFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void changeFragment() {

        replaceFragment();
    }
}