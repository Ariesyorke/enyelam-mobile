package com.nyelam.android.doshop;

import com.nyelam.android.data.DoShopCartReturn;

/**
 * Created by Aprilian Nur Wakhid Daini on 10/30/2018.
 */
public interface CheckoutListener {
    void proceedToCheckOut(DoShopCartReturn cartReturn);
    void proceedToChoosePayment();
    void proceedPayment();
    void stepView(int pos);
    void setTitle(String title);
}
