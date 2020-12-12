package com.artifly.sharedplatform;

public class BoardInfo { //DB에서 받아오는 정보
    private String id; //킥보드 식별번호
    private String type; //킥보드가 이용중인지 아닌지 나타냄
    private String longitude;    // 위도
    private String latitude;    // 경도

    BoardInfo(String id, String type, String longitude, String latitude){
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
