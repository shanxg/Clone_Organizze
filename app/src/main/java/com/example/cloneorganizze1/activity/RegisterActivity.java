package com.example.cloneorganizze1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cloneorganizze1.R;
import com.example.cloneorganizze1.config.ConfigurateFirebase;
import com.example.cloneorganizze1.helper.Base64Custom;
import com.example.cloneorganizze1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText inputRegUserName, inputRegUserEmail, inputRegUserPW;
    private Button btnRegUser;

    private FirebaseAuth userAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputRegUserName = findViewById(R.id.inputRegUserName);
        inputRegUserEmail = findViewById(R.id.inputRegUserEmail);
        inputRegUserPW = findViewById(R.id.inputRegUserPW);

        btnRegUser = findViewById(R.id.btnRegUser);

        btnRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textValidate();

            }
        });
    }

    public void textValidate(){

        String textUserName = inputRegUserName.getText().toString();
        String textUserEmail = inputRegUserEmail.getText().toString();
        String textUserPW = inputRegUserPW.getText().toString();


        if( !textUserName.isEmpty() ) {
            if ( !textUserEmail.isEmpty() ){
                if ( !textUserPW.isEmpty() ){

                    User user = new User(textUserName, textUserEmail, textUserPW);

                    String userId = Base64Custom.encodeBase64(textUserEmail);
                    user.setUserID(userId);


                    user.save();
                    registerUser(user);

                }else{

                    Toast.makeText(
                            RegisterActivity.this,
                            "Password is empty.",
                            Toast.LENGTH_SHORT)
                            .show();
                }

            }else {

                Toast.makeText(
                        RegisterActivity.this,
                        "Email is empty.",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }else {

            Toast.makeText(
                    RegisterActivity.this,
                    "Name is empty.",
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void registerUser(User user){

        String userEmail = user.getEmail();
        String userPassword = user.getPassword();

        userAuth = ConfigurateFirebase.getFirebaseAuth();
        userAuth.createUserWithEmailAndPassword(userEmail, userPassword)

                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            finish();
                        }else {

                            String exception ;
                            try{

                                throw task.getException();

                            }catch (FirebaseAuthWeakPasswordException e){
                                exception = e.getMessage();

                            }catch (FirebaseAuthInvalidCredentialsException e){
                                exception = e.getMessage();

                            }catch (FirebaseAuthUserCollisionException e){
                                exception = e.getMessage();

                            }catch (Exception e){
                                exception = e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Registration failure: \n"+ exception,
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }
}
