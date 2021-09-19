package com.example.cloneorganizze1.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigurateFirebase {

    private static FirebaseAuth auth;
    private static DatabaseReference fireDBRef;

    public static DatabaseReference getFireDBRef(){

        if(fireDBRef==null){
            fireDBRef = FirebaseDatabase.getInstance().getReference();
        }

        return fireDBRef;
    }


    public static FirebaseAuth getFirebaseAuth(){

        if(auth==null){
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }

}
