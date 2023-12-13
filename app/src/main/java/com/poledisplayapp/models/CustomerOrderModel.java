package com.poledisplayapp.models;

public class CustomerOrderModel {

    String custid;
    String station;
    String price;
    String store;
    String description;
    String qty;
    String size;
    String sizeColumnFlag;
    String subTotal;
    String tax;
    String total;
    String firebasekey;
    String BottleDipositAmt;
    public boolean IsEndTransaction;
    public boolean IsDisplayItemOnEndTransaction;
    public boolean IsTenderScreen;
    String tax3;
    String IsConvFee;
    String ConvFeeName;

    public String getCustid() {
        return custid;
    }

    public CustomerOrderModel setCustid(String custid) {
        this.custid = custid;
        return this;
    }

    public String getStation() {
        return station;
    }

    public CustomerOrderModel setStation(String station) {
        this.station = station;
        return this;
    }

    public String getQty() {
        return qty;
    }

    public CustomerOrderModel setQty(String qty) {
        this.qty = qty;
        return this;
    }

    public String getSize() {
        return size;
    }

    public CustomerOrderModel setSize(String size) {
        this.size = size;
        return this;
    }

    public String getSizeColumnFlag() {
        return sizeColumnFlag;
    }

    public CustomerOrderModel setSizeColumnFlag(String sizeColumnFlag) {
        this.sizeColumnFlag = sizeColumnFlag;
        return this;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public CustomerOrderModel setSubTotal(String subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public String getTax() {
        return tax;
    }

    public CustomerOrderModel setTax(String tax) {
        this.tax = tax;
        return this;
    }

    public String getTotal() {
        return total;
    }

    public CustomerOrderModel setTotal(String total) {
        this.total = total;
        return this;
    }


    public String getPrice() {
        return price;
    }

    public CustomerOrderModel setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getStore() {
        return store;
    }

    public CustomerOrderModel setStore(String store) {
        this.store = store;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CustomerOrderModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getFirebasekey() {
        return firebasekey;
    }

    public CustomerOrderModel setFirebasekey(String firebasekey) {
        this.firebasekey = firebasekey;
        return this;
    }
//
////    public boolean isEndTransaction() {
////        return IsEndTransaction;
////    }
////
////    public CustomerOrderModel setIsEndTransaction(boolean IsEndTransaction) {
////        IsEndTransaction = IsEndTransaction;
////        return this;
////    }


    public boolean isEndTransaction() {
        return IsEndTransaction;
    }

    public CustomerOrderModel setEndTransaction(boolean endTransaction) {
        IsEndTransaction = endTransaction;
        return this;
    }


    public boolean isDisplayItemOnEndTransaction() {
        return IsDisplayItemOnEndTransaction;
    }

    public CustomerOrderModel setDisplayItemOnEndTransaction(boolean displayItemOnEndTransaction) {
        IsDisplayItemOnEndTransaction = displayItemOnEndTransaction;
        return this;
    }

    public boolean isTenderScreen() {
        return IsTenderScreen;
    }

    public CustomerOrderModel setTenderScreen(boolean tenderScreen) {
        IsTenderScreen = tenderScreen;
        return this;
    }

    public String getBottleDipositAmt() {
        return BottleDipositAmt;
    }

    public void setBottleDipositAmt(String bottleDipositAmt) {
        BottleDipositAmt = bottleDipositAmt;
    }

    public String getTax3() {
        return tax3;
    }

    public CustomerOrderModel setTax3(String tax3) {
        this.tax3 = tax3;
        return this;
    }

    public String getIsConvFee() {
        return IsConvFee;
    }

    public CustomerOrderModel setIsConvFee(String IsConvFee) {
        this.IsConvFee = IsConvFee;
        return this;
    }

    public String getConvFeeName() {
        return ConvFeeName;
    }

    public CustomerOrderModel setConvFeeName(String ConvFeeName) {
        this.ConvFeeName = ConvFeeName;
        return this;
    }
}
