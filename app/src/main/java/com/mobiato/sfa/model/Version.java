package com.mobiato.sfa.model;

import java.io.Serializable;

/**
 * Created by SUGNESH on 1/04/2019.
 */

public class Version implements Serializable, Cloneable {

    private String version;

    public String getVersion() {
        return version;
    }

    public Version setVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
