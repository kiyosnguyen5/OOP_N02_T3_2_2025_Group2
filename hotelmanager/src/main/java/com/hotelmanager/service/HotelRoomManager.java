package com.hotelmanager.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public void saveRoomsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Room room : rooms) {
                writer.write(room.getRoomNumber() + "," + room.getRoomType() + "," + room.getRoomStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving rooms: " + e.getMessage());
        }
    }

    public void loadRoomsFromFile(String filename) {
        rooms.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    rooms.add(new Room(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            // File may not exist on first run, that's OK
        }
    }

    public void saveReservationsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Reservation res : reservations) {
                writer.write(res.getReservationID() + "," +
                             res.getRoom().getRoomNumber() + "," +
                             res.getCustomerDetails());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving reservations: " + e.getMessage());
        }
    }

    public void loadReservationsFromFile(String filename) {
        reservations.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    Room room = findRoomByNumber(parts[1]);
                    if (room != null) {
                        reservations.add(new Reservation(parts[0], room, parts[2]));
                        room.setRoomStatus("booked");
                    }
                }
            }
        } catch (IOException e) {
            // File may not exist on first run, that's OK
        }
    }

    private Room findRoomByNumber(String number) {
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(number)) {
                return room;
            }
        }
        return null;
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }
}