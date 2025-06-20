package com.hotelmanager.model;

public class Room {
    private String roomNumber;
    private String roomType;
    private String roomStatus;

    public Room(String roomNumber, String roomType, String roomStatus) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomInfo() {
        return "Room Number: " + roomNumber + ", Type: " + roomType + ", Status: " + roomStatus;
    }
}
