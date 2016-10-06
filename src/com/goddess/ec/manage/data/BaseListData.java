package com.goddess.ec.manage.data;

import java.io.Serializable;

public class BaseListData implements Serializable {

    private Status status;

    private Object data;

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
    public Object getData() {
        return data;
    }

    /**
     * dataを設定する<br>
     *
     * @param Object data
     */
    public void setData(Object data) {
        this.data = data;
    }

}
