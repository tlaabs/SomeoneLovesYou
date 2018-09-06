package com.slu.security;

import java.io.Serializable;

public class  JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String userid;
    private String userpwd;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String userid, String userpwd) {
        this.setUserid(userid);
        this.setUserpwd(userpwd);
    }

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

   
}
