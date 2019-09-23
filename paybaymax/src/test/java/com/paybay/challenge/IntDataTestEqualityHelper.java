package com.paybay.challenge;

import java.util.Random;

public class IntDataTestEqualityHelper {

    private int data;

    public IntDataTestEqualityHelper() {
        data = new Random().nextInt();
    }

    public IntDataTestEqualityHelper(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setData(int value) {
        data = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntDataTestEqualityHelper)) {
            return false;
        }

        IntDataTestEqualityHelper other = (IntDataTestEqualityHelper)obj;
        return data == other.data;
    }

    @Override
    public int hashCode() {
        return data;
    }

}