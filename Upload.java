package com.example.kon_boot.tour;


public class Upload {
    private String mName,phn,email,mUrl;

    public Upload()
    {

    }
    public Upload(String name,String phnno,String Email,String url)
    {
        mName=name;
        phn =phnno;
        email=Email;
        mUrl =url;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
