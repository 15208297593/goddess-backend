package com.goddess.ec.manage.data;

import java.io.Serializable;

public class BaseData implements Serializable {

    private Status status;

    private Data data;

    /**
     * statusを取得する<br>
     *
     * @return status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * statusを設定する<br>
     *
     * @param status status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * dataを取得する<br>
     *
     * @return data
     */
    public Data getData() {
        return data;
    }

    /**
     * dataを設定する<br>
     *
     * @param data data
     */
    public void setData(Data data) {
        this.data = data;
    }
}
