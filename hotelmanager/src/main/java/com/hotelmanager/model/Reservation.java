package com.hotelmanager.model;

import java.time.LocalDate;

public class Reservation {
    private String reservationID;
    private Room room;
    private String customerDetails;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public Reservation(String reservationID, Room room, String customerDetails, LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationID = reservationID;
        this.room = room;
        this.customerDetails = customerDetails;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getReservationID() { return reservationID; }
    public Room getRoom() { return room; }
    public String getCustomerDetails() { return customerDetails; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }

    public String getReservationDetails() {
        return "ReservationID: " + reservationID +
               ", Room: " + room.getRoomNumber() +
               ", Customer: " + customerDetails +
               ", Check-in: " + checkInDate +
               ", Check-out: " + checkOutDate;
    }
}
