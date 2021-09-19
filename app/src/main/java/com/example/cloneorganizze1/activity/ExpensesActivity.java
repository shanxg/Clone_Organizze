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

public class ExpensesActivity extends AppCompatActivity {

    private ImageView btnClose;
    private TextInputEditText inputDateExpenseText, inputExpenseDescText, inputExpenseCategoryText;
    private EditText inputValueText;
    private Transaction transaction;

    private DatabaseReference fireDB = ConfigurateFirebase.getFireDBRef();
    private FirebaseAuth fireAuth = ConfigurateFirebase.getFirebaseAuth();

    private double expenseTotal, updatedExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        btnClose = findViewById(R.id.btnClose);
        inputDateExpenseText = findViewById(R.id.inputDateTextExpense);
        inputExpenseDescText = findViewById(R.id.inputExpenseDescText);
        inputExpenseCategoryText = findViewById(R.id.inputExpenseCategoryText);
        inputValueText = findViewById(R.id.editValueTextExpense);

        //Preenche o campo data com a date atual
        inputDateExpenseText.setText(DateUtil.actualDate());

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getExpenseTotal();
    }

    //

    public void saveExpense(View view){

        if(validateFields()){

            transaction = new Transaction();

            String date = inputDateExpenseText.getText().toString();
            double newExpense = Double.parseDouble(inputValueText.getText().toString());

            transaction.setTransactionValue(newExpense);
            transaction.setCategory(inputExpenseCategoryText.getText().toString());
            transaction.setDescription(inputExpenseDescText.getText().toString());
            transaction.setDate(date);
            transaction.setType("d");
            transaction.save(date);

            updatedExpense = newExpense + expenseTotal;
            updateExpenseTotal(updatedExpense);

            finish();
        }
    }

    public boolean validateFields(){

        String textDate, textValue, textCateg, textDesc;
        textDate = inputDateExpenseText.getText().toString();
        textValue = inputValueText.getText().toString();
        textCateg = inputExpenseCategoryText.getText().toString();
        textDesc = inputExpenseDescText.getText().toString();

        if(!textDate.isEmpty()){
            if(!textValue.isEmpty()){
                if(!textCateg.isEmpty()){
                    if(!textDesc.isEmpty()){
                        Toast.makeText(
                                ExpensesActivity.this,
                                "Despesa salva.",
                                Toast.LENGTH_LONG).show();


                        return true;
                    }
                    else {
                        Toast.makeText(
                                ExpensesActivity.this,
                                "Validation Error:" +
                                        "\n Empty Description.",
                                Toast.LENGTH_LONG).show();

                        return false;
                    }
                }
                else {
                    Toast.makeText(
                            ExpensesActivity.this,
                            "Validation Error:" +
                                    "\n Empty Category.",
                            Toast.LENGTH_LONG).show();

                    return false;
                }
            }
            else {
                Toast.makeText(
                    ExpensesActivity.this,
                    "Validation Error:" +
                            "\n Empty Value.",
                    Toast.LENGTH_LONG).show();

                return false;
            }
        }
        else {
            Toast.makeText(
                ExpensesActivity.this,
                "Validation Error:" +
                        "\n Empty Date.",
                Toast.LENGTH_LONG).show();

            return false;
        }
    }

    public void getExpenseTotal(){

        String userId = Base64Custom.encodeBase64( fireAuth.getCurrentUser().getEmail() );

        DatabaseReference userRef = fireDB.child("users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                expenseTotal = user.getExpenseTotal();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateExpenseTotal(Double updatedExpense){

        String userId = Base64Custom.encodeBase64( fireAuth.getCurrentUser().getEmail() );

        DatabaseReference userRef = fireDB.child("users").child(userId);

        userRef.child("expenseTotal").setValue(updatedExpense);

    }
}
