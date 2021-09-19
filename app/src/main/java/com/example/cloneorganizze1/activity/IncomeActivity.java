package com.example.cloneorganizze1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cloneorganizze1.R;
import com.example.cloneorganizze1.config.ConfigurateFirebase;
import com.example.cloneorganizze1.helper.Base64Custom;
import com.example.cloneorganizze1.helper.DateUtil;
import com.example.cloneorganizze1.model.Transaction;
import com.example.cloneorganizze1.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class IncomeActivity extends AppCompatActivity {

    private ImageView btnClose;
    private TextInputEditText inputIncomeDateText, inputIncomeDescText, inputIncomeCategoryText;
    private EditText inputValueText;
    private Transaction transaction;

    private DatabaseReference fireDB = ConfigurateFirebase.getFireDBRef();
    private FirebaseAuth fireAuth = ConfigurateFirebase.getFirebaseAuth();

    private double incomeTotal, updatedIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        btnClose = findViewById(R.id.btnClose);
        inputIncomeDateText = findViewById(R.id.inputDateTextIncome);
        inputIncomeDescText = findViewById(R.id.inputIncomeDescText);
        inputIncomeCategoryText = findViewById(R.id.inputIncomeCategoryText);
        inputValueText = findViewById(R.id.editValueTextIncome);

        //Preenche o campo data com a date atual
        inputIncomeDateText.setText(DateUtil.actualDate());

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getIncomeTotal();
    }

    public void saveIncome(View view){

        if(validateFields()){

            transaction = new Transaction();

            String date = inputIncomeDateText.getText().toString();
            double newIncome = Double.parseDouble(inputValueText.getText().toString());


            transaction.setTransactionValue(newIncome);
            transaction.setCategory(inputIncomeCategoryText.getText().toString());
            transaction.setDescription(inputIncomeDescText.getText().toString());
            transaction.setDate(date);
            transaction.setType("r");
            transaction.save(date);

            updatedIncome = newIncome + incomeTotal;
            updateIncomeTotal(updatedIncome);

            finish();
        }
    }

    public boolean validateFields(){

        String textDate, textValue, textCateg, textDesc;
        textDate = inputIncomeDateText.getText().toString();
        textValue = inputValueText.getText().toString();
        textCateg = inputIncomeCategoryText.getText().toString();
        textDesc = inputIncomeDescText.getText().toString();

        if(!textDate.isEmpty()){
            if(!textValue.isEmpty()){
                if(!textCateg.isEmpty()){
                    if(!textDesc.isEmpty()){
                        Toast.makeText(
                                IncomeActivity.this,
                                "Receita salva.",
                                Toast.LENGTH_LONG).show();


                        return true;
                    }
                    else {
                        Toast.makeText(
                                IncomeActivity.this,
                                "Validation Error:" +
                                        "\n Empty Description.",
                                Toast.LENGTH_LONG).show();

                        return false;
                    }
                }
                else {
                    Toast.makeText(
                            IncomeActivity.this,
                            "Validation Error:" +
                                    "\n Empty Category.",
                            Toast.LENGTH_LONG).show();

                    return false;
                }
            }
            else {
                Toast.makeText(
                        IncomeActivity.this,
                        "Validation Error:" +
                                "\n Empty Value.",
                        Toast.LENGTH_LONG).show();

                return false;
            }
        }
        else {
            Toast.makeText(
                    IncomeActivity.this,
                    "Validation Error:" +
                            "\n Empty Date.",
                    Toast.LENGTH_LONG).show();

            return false;
        }
    }

    public void getIncomeTotal(){

        String userId = Base64Custom.encodeBase64( fireAuth.getCurrentUser().getEmail() );

        DatabaseReference userRef = fireDB.child("users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                incomeTotal = user.getIncomeTotal();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateIncomeTotal(Double updatedIncome){

        String userId = Base64Custom.encodeBase64( fireAuth.getCurrentUser().getEmail() );

        DatabaseReference userRef = fireDB.child("users").child(userId);

        userRef.child("incomeTotal").setValue(updatedIncome);
    }
}
