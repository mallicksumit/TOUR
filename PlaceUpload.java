package com.example.kon_boot.tour;

public class PlaceUpload {
    String place,vicinity,loc,img1uri,img2uri,img3uri;

    public PlaceUpload()
    {

    }
    public  PlaceUpload(String place,String vicinity,String loc,String img1uri,String img2uri,String img3uri)
    {
        this.img1uri=img1uri;
        this.img2uri =img2uri;
        this.img3uri = img3uri;
        this.place = place;
        this.vicinity = vicinity;
        this.loc = loc;

    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getImg1uri() {
        return img1uri;
    }

    public void setImg1uri(String img1uri) {
        this.img1uri = img1uri;
    }

    public String getImg2uri() {
        return img2uri;
    }

    public void setImg2uri(String img2uri) {
        this.img2uri = img2uri;
    }

    public String getImg3uri() {
        return img3uri;
    }

    public void setImg3uri(String img3uri) {
        this.img3uri = img3uri;
    }
}
