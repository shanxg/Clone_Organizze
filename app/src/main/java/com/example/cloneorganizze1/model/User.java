package com.example.cloneorganizze1.model;


import com.example.cloneorganizze1.config.ConfigurateFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class User {

    private String name, email, password, userID;
    private  double incomeTotal = 0.00;
    private  double expenseTotal = 0.00;

    public User() {
    }

    @Exclude
    public String getUserID() {
        return userID;
    }

    public double getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(double incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    public void save(){
        DatabaseReference fireDB = ConfigurateFirebase.getFireDBRef();

       fireDB.child("users").child(this.userID).setValue( this);

    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
