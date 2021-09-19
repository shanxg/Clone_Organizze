package com.example.cloneorganizze1.model;

import com.example.cloneorganizze1.config.ConfigurateFirebase;
import com.example.cloneorganizze1.helper.Base64Custom;
import com.example.cloneorganizze1.helper.DateUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Transaction {

    private String date, type, description, category, transactionID;
    private double transactionValue;



    public Transaction() {
    }

    public void save(String chosenDate){

        FirebaseAuth userAuth = ConfigurateFirebase.getFirebaseAuth();

        String userID = Base64Custom.encodeBase64(userAuth.getCurrentUser().getEmail());
        String date = DateUtil.monthID(chosenDate);

        DatabaseReference fireDB = ConfigurateFirebase.getFireDBRef();
        fireDB.child("transactions")
                .child(userID)
                .child(date)
                .push()
                .setValue(this);

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(double transactionValue) {
        this.transactionValue = transactionValue;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
}
