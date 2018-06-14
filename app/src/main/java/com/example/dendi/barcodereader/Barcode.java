package com.example.dendi.barcodereader;

public class Barcode {

    private String barcode;

    private int quantity;


    public Barcode(String barcode, int quantity){

        this.barcode = barcode;

        this.quantity = quantity;

    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
