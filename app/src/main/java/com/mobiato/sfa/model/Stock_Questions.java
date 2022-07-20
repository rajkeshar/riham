package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Stock_Questions implements Serializable {

    @SerializedName("question_id")
    @Expose
    private String QuestionId;
    @SerializedName("question")
    @Expose
    private String Question;
    @SerializedName("question_type")
    @Expose
    private String Question_type;
    @SerializedName("questions_type_select_based")
    @Expose
    private String Question_type_based;
    private String answer;
    private String serveyId;
    private String customerId;
    private String CustomerName;
    private String CustomerEmail;
    private String CustomerPhone;
    private String CustomerAddress;
    private String distributionType;


    public String getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(String QuestionId) {
        this.QuestionId = QuestionId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }
    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String CustomerAddress) {
        this.CustomerAddress = CustomerAddress;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String CustomerEmail) {
        this.CustomerEmail = CustomerEmail;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public void setCustomerPhone(String CustomerPhone) {
        this.CustomerPhone = CustomerPhone;
    }

    public String getQuestion_type() {
        return Question_type;
    }

    public void setQuestion_type(String Question_type) {
        this.Question_type = Question_type;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }

    public String getQuestion_type_based() {
        return Question_type_based;
    }

    public void setQuestion_type_based(String Question_type_based) {
        this.Question_type_based = Question_type_based;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getServeyId() {
        return serveyId;
    }

    public void setServeyId(String serveyId) {
        this.serveyId = serveyId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(String distributionType) {
        this.distributionType = distributionType;
    }
}