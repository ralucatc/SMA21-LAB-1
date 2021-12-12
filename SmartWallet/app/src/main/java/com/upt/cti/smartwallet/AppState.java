package com.upt.cti.smartwallet;

import com.google.firebase.firestore.FirebaseFirestore;

public class AppState {
    private static AppState singletonObject;

    public static synchronized AppState get() {
        if (singletonObject == null) {
            singletonObject = new AppState();
        }
        return singletonObject;
    }

    private FirebaseFirestore databaseReference;
    private Payment currentPayment;

    public FirebaseFirestore getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(FirebaseFirestore databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void setCurrentPayment(Payment currentPayment) {
        this.currentPayment = currentPayment;
    }

    public Payment getCurrentPayment() {
        return currentPayment;
    }

    public void updateLocalBackup(Context context, Payment payment, boolean toAdd) {
        String fileName = payment.timestamp;

        try {
            if (toAdd) {
                // save to file
            } else {
                context.deleteFile(fileName);
            }
        } catch (IOException e) {
            Toast.makeText(context, "Cannot access local data.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hasLocalStorage(Context context) {
        return context.getFilesDir().listFiles().length > 0;
    }

    public List<Payment> loadFromLocalBackup(Context context, String month) {
        try {
            List<Payment> payments = new ArrayList<>();

            for (File file : context.getFilesDir().listFiles()) {
                if (/*only own files*/) {
                    // ...

                    if (/* current month only */)
                        payments.add(payment);
                }
            }

            return payments;
        } catch (IOException e) {
            Toast.makeText(context, "Cannot access local data.", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}