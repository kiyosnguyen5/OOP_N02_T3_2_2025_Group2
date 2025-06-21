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
import java.time.LocalDate;

public class HotelRoomManager {
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public Reservation bookRoom(Room room, String customerDetails, LocalDate checkIn, LocalDate checkOut) {
        // Prevent double-booking
        for (Reservation res : reservations) {
            if (res.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                if (!(checkOut.isBefore(res.getCheckInDate()) || checkIn.isAfter(res.getCheckOutDate()))) {
                    return null; // Overlapping reservation
                }
            }
        }
        String reservationID = UUID.randomUUID().toString();
        Reservation reservation = new Reservation(reservationID, room, customerDetails, checkIn, checkOut);
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
                             res.getCustomerDetails() + "," +
                             res.getCheckInDate() + "," +
                             res.getCheckOutDate());
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
                String[] parts = line.split(",", 5);
                if (parts.length == 5) {
                    Room room = findRoomByNumber(parts[1]);
                    if (room != null) {
                        LocalDate checkIn = LocalDate.parse(parts[3]);
                        LocalDate checkOut = LocalDate.parse(parts[4]);
                        reservations.add(new Reservation(parts[0], room, parts[2], checkIn, checkOut));
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

    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            boolean isAvailable = true;
            for (Reservation res : reservations) {
                if (res.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                    if (!(checkOut.isBefore(res.getCheckInDate()) || checkIn.isAfter(res.getCheckOutDate()))) {
                        isAvailable = false;
                        break;
                    }
                }
            }
            if (isAvailable) available.add(room);
        }
        return available;
    }

    public List<Reservation> searchReservations(String customer, LocalDate date) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation res : reservations) {
            if ((customer == null || res.getCustomerDetails().toLowerCase().contains(customer.toLowerCase())) &&
                (date == null || (!date.isBefore(res.getCheckInDate()) && !date.isAfter(res.getCheckOutDate())))) {
                result.add(res);
            }
        }
        return result;
    }

    // Add methods for editRoom, deleteRoom, etc. as needed
}