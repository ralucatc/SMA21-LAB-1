package com.upt.cti.smartwallet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

private FirebaseAuth mAuth;
private FirebaseAuth.AuthStateListener mAuthListener;
private static final int REQ_SIGNIN = 3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Payment;
import ui.PaymentAdapter;

public class MainActivity extends AppCompatActivity  {

    private int currentMonth;
    private List<Payment> payments = new ArrayList<>();
    private DatabaseReference databaseReference;

    private TextView tStatus;
    private Button bPrevious;
    private Button bNext;
    private FloatingActionButton fabAdd;
    private ListView listPayments;

    private ValueEventListener databaseListener = null;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tStatus = (TextView) findViewById(R.id.tStatus);
        bPrevious = (Button) findViewById(R.id.bPrevious);
        bNext = (Button) findViewById(R.id.bNext);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        listPayments = (ListView) findViewById(R.id.listPayments);
        final PaymentAdapter adapter = new PaymentAdapter(this, R.layout.item_payment, payments);
        listPayments.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-d5bb5-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference();
        databaseReference.child("wallet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot,String previousChildName) {
                Payment newPayment = snapshot.getValue(Payment.class);
                if (newPayment != null) {
                    newPayment.timestamp = snapshot.getKey();
                    if (!payments.contains(newPayment))
                    {
                        payments.add(newPayment);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            // setup authentication
            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if (user != null) {
                        TextView tLoginDetail = (TextView) findViewById(R.id.tLoginDetail);
                        TextView tUser = (TextView) findViewById(R.id.tUser);
                        tLoginDetail.setText("Firebase ID: " + user.getUid());
                        tUser.setText("Email: " + user.getEmail());

                        AppState.get().setUserId(user.getUid());
                        attachDBListener(user.getUid());
                    } else {
                        startActivityForResult(new Intent(getApplicationContext(),
                                SignupActivity.class), REQ_SIGNIN);
                    }
                }
            };
        }

            @Override
            public void onChildChanged(DataSnapshot snapshot,String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot,String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

        if (!AppState.isNetworkAvailable(this)) {
            // has local storage already
            if (AppState.get().hasLocalStorage(this)) {
                payments = /**/
                        tStatus.setText("Found " + payments.size() + " payments for " +
                                Month.intToMonthName(currentMonth) + ".");
            } else {
                Toast.makeText(this, "This app needs an internet connection!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    private void attachDBListener(String uid) {
        // setup firebase database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        AppState.get().setDatabaseReference(databaseReference);

        databaseReference.child("wallet").child(uid).addChildEventListener(new ChildEventListener() {
            //...
        });
    }

    }
}