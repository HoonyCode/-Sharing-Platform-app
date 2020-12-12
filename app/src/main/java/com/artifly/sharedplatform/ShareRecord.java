package com.artifly.sharedplatform;

public class ShareRecord {
    private String id;  // 식별번호 - 자기 사용기록 조회할때는 의미없음
    private String timestamp;   // 대여 시작/종료 시간
    private String target;  // 대여한 킥보드 ID
    private String type;    // start = 시작, end = 종료
    private String location;    // 위치를 주소로 나타낸 것
    private String longitude;    // 위도
    private String latitude;    // 경도

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
