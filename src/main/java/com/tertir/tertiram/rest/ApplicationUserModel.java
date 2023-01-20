package com.tertir.tertiram.rest;

public class ApplicationUserModel {
    private String id;
    private String username;
    private String IDBankNumber;
    private String otherBankNumber;
    private Integer randomCode;
    private Integer number5;
    private Integer number4;
    private Integer number3;
    private Integer number2;
    private Integer number1;
    private Double sum;

    public ApplicationUserModel() {
    }

    public ApplicationUserModel(String id,
                                String username,
                                String IDBankNumber,
                                String otherBankNumber,
                                Integer randomCode,
                                Integer number5,
                                Integer number4,
                                Integer number3,
                                Integer number2,
                                Integer number1,
                                Double sum) {
        this.id = id;
        this.username = username;
        this.IDBankNumber = IDBankNumber;
        this.otherBankNumber = otherBankNumber;
        this.randomCode = randomCode;
        this.number5 = number5;
        this.number4 = number4;
        this.number3 = number3;
        this.number2 = number2;
        this.number1 = number1;
        this.sum = sum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIDBankNumber() {
        return IDBankNumber;
    }

    public void setIDBankNumber(String IDBankNumber) {
        this.IDBankNumber = IDBankNumber;
    }

    public String getOtherBankNumber() {
        return otherBankNumber;
    }

    public void setOtherBankNumber(String otherBankNumber) {
        this.otherBankNumber = otherBankNumber;
    }

    public Integer getRandomCode() {
        return randomCode;
    }

    public void setRandomCode(Integer randomCode) {
        this.randomCode = randomCode;
    }

    public Integer getNumber5() {
        return number5;
    }

    public void setNumber5(Integer number5) {
        this.number5 = number5;
    }

    public Integer getNumber4() {
        return number4;
    }

    public void setNumber4(Integer number4) {
        this.number4 = number4;
    }

    public Integer getNumber3() {
        return number3;
    }

    public void setNumber3(Integer number3) {
        this.number3 = number3;
    }

    public Integer getNumber2() {
        return number2;
    }

    public void setNumber2(Integer number2) {
        this.number2 = number2;
    }

    public Integer getNumber1() {
        return number1;
    }

    public void setNumber1(Integer number1) {
        this.number1 = number1;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}
