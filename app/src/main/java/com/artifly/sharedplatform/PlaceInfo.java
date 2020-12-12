package com.artifly.sharedplatform;

public class PlaceInfo {
    private String id;  // 거치대 아이디
    private String location;    // 위치를 주소로 나타낸 것
    private String longitude;    // 위도
    private String latitude;    // 경도

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
