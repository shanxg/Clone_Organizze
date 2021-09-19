package com.example.cloneorganizze1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cloneorganizze1.R;
import com.example.cloneorganizze1.config.ConfigurateFirebase;
import com.example.cloneorganizze1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText inputLoginEmail, inputLoginPW;
    private Button btnLogin;

    private FirebaseAuth loginFBAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLoginEmail = findViewById(R.id.inputLoginEmail);
        inputLoginPW = findViewById(R.id.inputLoginPw);
        btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateLogin();
            }
        });
    }

    public void validateLogin(){

        String textLoginEmail = inputLoginEmail.getText().toString();
        String textLoginPW = inputLoginPW.getText().toString();

        if( !textLoginEmail.isEmpty() ){
            if( !textLoginPW.isEmpty() ){

                User user = new User();
                user.setEmail(textLoginEmail);
                user.setPassword(textLoginPW);

                loginAuth(user);

            }else {

                Toast.makeText(
                        LoginActivity.this,
                        "Password is empty.",
                        Toast.LENGTH_LONG)
                        .show();
            }

        }else {

            Toast.makeText(
                    LoginActivity.this,
                    "Email is empty.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }


    public void loginAuth(User user){
        String userEmail = user.getEmail();
        String userPW = user.getPassword();


        loginFBAuth = ConfigurateFirebase.getFirebaseAuth();
        loginFBAuth.signInWithEmailAndPassword(userEmail,userPW)

                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            finish();

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login successful!",
                                    Toast.LENGTH_SHORT)
                                    .show();

                        }else {

                            String exception ;
                            try{

                                throw task.getException();

                            }catch (FirebaseAuthInvalidUserException e){
                                exception = e.getMessage();

                            }catch (FirebaseAuthInvalidCredentialsException e){
                                exception = e.getMessage();

                            }catch (Exception e){
                                exception = e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login failure: \n"+ exception,
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }
}
