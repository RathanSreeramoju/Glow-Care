package com.example.glowcare;


import androidx.appcompat.app.AppCompatActivity;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glowcare.ui.home.HomeFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Login activity has the code to authenticate the user using mysql database
 */
public class LoginActivity extends AppCompatActivity {
    Button login;
    TextView signuphere;
    EditText emailid,password;
    Node loginapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     *
     * @param savedInstanceState
     * @retrofit is an instance of Retrofit to make a connection with nodeapi
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        signuphere = findViewById(R.id.sign);
        emailid = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Retrofit retrofit = RetrofitClient.getApiClient();
        loginapi = retrofit.create(Node.class);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser(emailid.getText().toString(),password.getText().toString());
            }
        });
        signuphere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     *
     * @param emailid as passed from the ui
     * @param password as passed from the ui
     * loginuser method checks whether the details are available in the database. If the details are available
     *                 it goes to in the home page
     */
    private void loginuser(String emailid, String password) {

        compositeDisposable.add(loginapi.loginUser(emailid,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.v("api text","rathan"+s);
                        if (s.contains("Valid user")){
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);}
                        else
                            Toast.makeText(LoginActivity.this,"login failure",Toast.LENGTH_LONG).show();
                    }
                }));
    }
}
