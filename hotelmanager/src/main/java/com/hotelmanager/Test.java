package com.hotelmanager;

import com.hotelmanager.model.Reservation;
import com.hotelmanager.model.Room;
import com.hotelmanager.service.HotelRoomManager;

public class Test {
    public static void main(String[] args) {
        HotelRoomManager manager = new HotelRoomManager();

        // Add rooms
        Room room1 = new Room("101", "Single", "available");
        Room room2 = new Room("102", "Double", "available");
        manager.addRoom(room1);
        manager.addRoom(room2);

        // View all rooms
        System.out.println("All rooms:");
        for (Room room : manager.viewRooms()) {
            System.out.println(room.getRoomInfo());
        }

        // Book a room
        Reservation reservation = manager.bookRoom(room1, "Phong Dz");
        if (reservation != null) {
            System.out.println("\nReservation successful:");
            System.out.println(reservation.getReservationDetails());
        } else {
            System.out.println("\nBooking failed.");
        }

        // View all rooms after booking
        System.out.println("\nRooms after booking:");
        for (Room room : manager.viewRooms()) {
            System.out.println(room.getRoomInfo());
        }

        // Cancel the reservation
        manager.cancelBooking(reservation);
        System.out.println("\nReservation cancelled.");

        // View all rooms after cancellation
        System.out.println("\nRooms after cancellation:");
        for (Room room : manager.viewRooms()) {
            System.out.println(room.getRoomInfo());
        }
    }
}
