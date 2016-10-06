package com.goddess.ec.manage.data;

import java.io.Serializable;

import com.goddess.ec.manage.model.Session;

public class Data implements Serializable {

    private Session session;

    private Object data;

    private Object player;
    
    /**
     * sessionを取得する<br>
     *
     * @return session
     */
    public Session getSession() {
        return session;
    }

    /**
     * sessionを設定する<br>
     *
     * @param session session
     */
    public void setSession(Session session) {
        this.session = session;
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
     * @param data data
     */
    public void setData(Object data) {
        this.data = data;
    }

	public Object getPlayer() {
		return player;
	}

	public void setPlayer(Object player) {
		this.player = player;
	}
}
