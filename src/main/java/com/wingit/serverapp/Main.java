package com.wingit.serverapp;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

public class Main {


    public static void addDataTest(Firestore db){

        DocumentReference docRef = db.collection("test").document("alovelace");
        // Add document data  with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("first", "Ada");
        data.put("last", "Lovelace");
        data.put("born", 1815);
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        // ...
        // result.get() blocks on response
        //System.out.println("Result : " + result.get().toString());


        DocumentReference docRef2 = db.collection("test").document("aturing");
        // Add document data with an additional field ("middle")
        Map<String, Object> data2 = new HashMap<String, Object>();
        data.put("first", "Alan");
        data.put("middle", "Mathison");
        data.put("last", "Turing");
        data.put("born", 1912);

        ApiFuture<WriteResult> result2 = docRef.set(data);
        //System.out.println("Update time : " + result2.get().getUpdateTime());
    }


    public static Firestore initializeAdminSDK() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("ServiceAccountKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://wingit-76ee6.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
        Firestore db = FirestoreClient.getFirestore();

        return db;
    }



    public static void main(String[] args) {

        try{
            Firestore db = initializeAdminSDK();
            addDataTest(db);
        } catch(IOException e){
            System.out.println("Exception: " + e);
        }
    }
}
