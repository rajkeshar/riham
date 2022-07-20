package com.mobiato.sfa.model;

import java.io.Serializable;

public class DiscountHeader implements Serializable {

    private String name;
    private String priority;
    private String discountKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDiscountKey() {
        return discountKey;
    }

    public void setDiscountKey(String discountKey) {
        this.discountKey = discountKey;
    }
}
