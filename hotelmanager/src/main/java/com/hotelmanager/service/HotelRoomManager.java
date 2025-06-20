package com.hotelmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hotelmanager.model.Reservation;
import com.hotelmanager.model.Room;

public class HotelRoomManager {
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public Reservation bookRoom(Room room, String customerDetails) {
        if (!rooms.contains(room) || !"available".equalsIgnoreCase(room.getRoomStatus())) {
            return null;
        }
        String reservationID = UUID.randomUUID().toString();
        Reservation reservation = new Reservation(reservationID, room, customerDetails);
        reservations.add(reservation);
        room.setRoomStatus("booked");
        return reservation;
    }

    public void cancelBooking(Reservation reservation) {
        if (reservations.remove(reservation)) {
            reservation.getRoom().setRoomStatus("available");
        }
    }

    public List<Room> viewRooms() {
        return new ArrayList<>(rooms);
    }
}