package com.upt.cti.smartwallet;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    TextView message, month;
    EditText income, expenses;
    Button bUpdate, bSearch;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = findViewById(R.id.message);
        income = findViewById(R.id.income);
        expenses = findViewById(R.id.expenses);
        bUpdate = findViewById(R.id.bUpdate);
        month = findViewById(R.id.month);
        bSearch = findViewById(R.id.bSearch);

    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.bSearch:
                DocumentReference docRef = db.collection("calendar").document(month.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                MonthlyExpenses monthlyExpense = document.toObject(MonthlyExpenses.class);
                                message.setText(monthlyExpense.getMonth() + " was found");
                            } else {
                                message.setText("Couldn't be found");
                            }
                        }
                    }
                });
                break;
            case R.id.bUpdate:
                DocumentReference monthReference = db.collection("calendar").document(month.getText().toString());
                Integer updatedIncome = Integer.parseInt(income.getText().toString());
                Integer updatedExpenses = Integer.parseInt(expenses.getText().toString());

                monthReference
                        .update(
                                "income", updatedIncome,
                                "expenses", updatedExpenses
                        )
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                message.setText(month.getText().toString() + " was updated");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                message.setText("Error while updating");
                            }
                        });
                break;
        }
    }
}