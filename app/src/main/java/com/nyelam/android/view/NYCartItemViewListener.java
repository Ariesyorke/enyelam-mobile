package com.nyelam.android.view;

public interface NYCartItemViewListener {
    void onQuantityChange(String productCartId, String quantity);
    void onRemoveItem(String productCartId);
}
