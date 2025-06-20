package com.hotelmanager.model;

public class Reservation {
    public String reservationID;
    private Room room;
    private String customerDetails;

    public Reservation(String reservationID, Room room, String customerDetails) {
        this.reservationID = reservationID;
        this.room = room;
        this.customerDetails = customerDetails;
    }

    public String getReservationID() {
        return reservationID;
    }

    public Room getRoom() {
        return room;
    }

    public String getCustomerDetails() {
        return customerDetails;
    }

    public String getReservationDetails() {
        return "Reservation ID: " + reservationID +
               ", Room: [" + room.getRoomInfo() + "]" +
               ", Customer: " + customerDetails;
    }
}
