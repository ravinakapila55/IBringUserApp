package com.iBring_user.app.Models;

import java.io.Serializable;

public class CancalReasonModel implements Serializable
{

    String id;
    String reason;

    public String getRefund_type() {
        return refund_type;
    }

    public void setRefund_type(String refund_type) {
        this.refund_type = refund_type;
    }

    String refund_type;

   /* public CancalReasonModel(String id, String reason, boolean flag) {
        this.id = id;
        this.reason = reason;
        this.flag = flag;
    }*/

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
