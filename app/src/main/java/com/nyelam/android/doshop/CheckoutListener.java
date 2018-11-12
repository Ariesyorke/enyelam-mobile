package com.nyelam.android.doshop;

/**
 * Created by Aprilian Nur Wakhid Daini on 10/30/2018.
 */
public interface CheckoutListener {
    void proceedToCheckOut();
    void proceedToChoosePayment();
    void proceedPayment();
    void stepView(int pos);
}
