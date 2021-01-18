package com.iBring_user.app.Models;

import java.io.Serializable;

public class ParcelTypeModel implements Serializable {


    String id,parcelName;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getParcelName()
    {
        return parcelName;

    }

    public void setParcelName(String parcelName)
    {
        this.parcelName = parcelName;
    }
}
