package com.example.cloneorganizze1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.cloneorganizze1.R;
import com.example.cloneorganizze1.adapter.AdapterMoves;
import com.example.cloneorganizze1.config.ConfigurateFirebase;
import com.example.cloneorganizze1.helper.Base64Custom;
import com.example.cloneorganizze1.model.Transaction;
import com.example.cloneorganizze1.model.User;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class AppMainActivity extends AppCompatActivity {

    private String monthID;
    private double userIncome, userExpense, userBalance;

    //private User Data

    private TextView textHello, textBalance;
    private RecyclerView recyclerMoves;

    private DatabaseReference fireDB = ConfigurateFirebase.getFireDBRef();
    private FirebaseAuth userAuth = ConfigurateFirebase.getFirebaseAuth();

    private DatabaseReference userRef,transRef;
    private ValueEventListener userEventListener, transEventListener;

    private MaterialCalendarView calendarView;
    private AdapterMoves adapterMoves;

    private List<Transaction> transactions = new ArrayList<>();
    private Transaction transSwiped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textHello = findViewById(R.id.textHello);
        textBalance = findViewById(R.id.textSaldo);
        recyclerMoves = findViewById(R.id.recyclerMoves);
        calendarView = findViewById(R.id.calendarView);

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2010,1,1))
                .commit();

        CalendarDay currentDate = calendarView.getCurrentDate();
        monthID = String.valueOf(currentDate.getMonth()+1)+currentDate.getYear();

        appActivity();

        adapterMoves = new AdapterMoves(transactions, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMoves.setLayoutManager(layoutManager);
        recyclerMoves.setHasFixedSize(true);
        recyclerMoves.setAdapter(adapterMoves);

        FloatingActionButton fabIncome = findViewById(R.id.menu_receita);
        fabIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewIncome();
            }
        });

        FloatingActionButton fabExpense = findViewById(R.id.menu_depesa);
        fabExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExpense();
            }
        });
    }

    // APPLICATION ACTIVITY

    public void appActivity(){

        configureCalendarView();
        getUserData();
        getTransactionsData();
        swipe();
    }

    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START;
                //int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                deleteTransaction(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerMoves);

    }

    // TOOLBAR

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /* The action bar will automatically handle clicks on the Home/Up button, so long
         as you specify a parent activity in AndroidManifest.xml. */

        // Handle action bar item clicks here.
        switch (item.getItemId()){

            case R.id.action_sign_out:

                userSignOut();

                Toast.makeText(
                        AppMainActivity.this,
                        "User disconnected",
                        Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    // CALENDAR VIEW

    public void configureCalendarView(){

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                monthID = String.valueOf(date.getMonth()+1)+ date.getYear();

                getTransactionsData();
            }
        });
    }

    // USER STATEMENTS

    private String getUserID(){

        String userEmail = userAuth.getCurrentUser().getEmail();
        String userId = Base64Custom.encodeBase64( userEmail );

        return userId;
    }

    public void getTransactionsData(){

        transRef = fireDB.child("transactions")
                .child(getUserID())
                .child(monthID);

        transEventListener = transRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                transactions.clear();

                for(DataSnapshot transData : dataSnapshot.getChildren()) {

                    Transaction transaction = transData.getValue(Transaction.class);
                    transaction.setTransactionID(transData.getKey());
                    transactions.add(transaction);
                }

                adapterMoves.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void getUserData(){

        userRef =  fireDB.child("users").child(getUserID());

        userEventListener =  userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                textHello.setText( "Olá, "+ user.getName() +"!" );

                userExpense = user.getExpenseTotal();
                userIncome = user.getIncomeTotal();
                userBalance = userIncome - userExpense;

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String formattedBalance = decimalFormat.format(userBalance);

                textBalance.setText("R$ "+formattedBalance);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void updateBalance(){

        userRef =  fireDB.child("users").child(getUserID());

        if(transSwiped.getType().equals("r")){

            userIncome = userIncome - transSwiped.getTransactionValue();
            userRef.child("incomeTotal").setValue(userIncome);

        }

        if(transSwiped.getType().equals("d")){

            userExpense = userExpense - transSwiped.getTransactionValue();
            userRef.child("expenseTotal").setValue(userExpense);

        }
    }

    public void deleteTransaction(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Delete transaction:");
        alertDialog.setMessage("Are you sure, you want to delete this transaction?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int itemPosition = viewHolder.getAdapterPosition();
                transSwiped =  transactions.get(itemPosition);

                transRef = fireDB.child("transactions")
                        .child(getUserID())
                        .child(monthID);

                transRef.child(transSwiped.getTransactionID()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    updateBalance();
                                    Toast.makeText(
                                            AppMainActivity.this,
                                            "TRANSACTION DELETED.",
                                            Toast.LENGTH_SHORT).show();

                                }else {

                                    String exception ;
                                    try{

                                        throw task.getException();

                                    }catch (Exception e){
                                        exception = e.getMessage();
                                        e.printStackTrace();
                                    }

                                    Toast.makeText(
                                            AppMainActivity.this,
                                            "Delete failure: \n"+ exception,
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
                adapterMoves.notifyItemRemoved(itemPosition);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(
                        AppMainActivity.this,
                        "CANCELED",
                        Toast.LENGTH_SHORT).show();

                adapterMoves.notifyDataSetChanged();
            }
        });

        AlertDialog alert =  alertDialog.create();
        alert.show();
    }


    public void userSignOut(){

        userRef.removeEventListener(userEventListener);
        transRef.removeEventListener(transEventListener);

        userAuth.signOut();
        finish();
    }

    // FABS

    public void addNewIncome(){
        startActivity(new Intent(this, IncomeActivity.class));
    }

    public void addNewExpense(){
        startActivity(new Intent(this, ExpensesActivity.class));
    }


    //ACTIVITY LIFE CYCLE

    @Override
    protected void onStart() {
        super.onStart();
        appActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(userEventListener);
        transRef.removeEventListener(transEventListener);
    }
}
