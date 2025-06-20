package com.hotelmanager.UI;

import java.util.List;
import java.util.Scanner;

import com.hotelmanager.model.Reservation;
import com.hotelmanager.model.Room;
import com.hotelmanager.service.HotelRoomManager;

public class TextbasedUI {
    public static void main(String[] args) {
        HotelRoomManager manager = new HotelRoomManager();
        Scanner scanner = new Scanner(System.in);

        // Load data from files
        manager.loadRoomsFromFile("rooms.txt");
        manager.loadReservationsFromFile("reservations.txt");

        while (true) {
            System.out.println("\n--- Hotel Room Manager ---");
            System.out.println("1. Add Room");
            System.out.println("2. View Rooms");
            System.out.println("3. Book Room");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter room number: ");
                    String number = scanner.nextLine();
                    System.out.print("Enter room type: ");
                    String type = scanner.nextLine();
                    manager.addRoom(new Room(number, type, "available"));
                    System.out.println("Room added.");
                    break;
                case 2:
                    List<Room> rooms = manager.viewRooms();
                    if (rooms.isEmpty()) {
                        System.out.println("No rooms available.");
                    } else {
                        for (Room room : rooms) {
                            System.out.println(room.getRoomInfo());
                        }
                    }
                    break;
                case 3:
                    System.out.print("Enter room number to book: ");
                    String bookNumber = scanner.nextLine();
                    Room roomToBook = null;
                    for (Room room : manager.viewRooms()) {
                        if (room.getRoomNumber().equals(bookNumber)) {
                            roomToBook = room;
                            break;
                        }
                    }
                    if (roomToBook == null) {
                        System.out.println("Room not found.");
                        break;
                    }
                    System.out.print("Enter customer name: ");
                    String customer = scanner.nextLine();
                    Reservation reservation = manager.bookRoom(roomToBook, customer);
                    if (reservation != null) {
                        System.out.println("Reservation successful:");
                        System.out.println(reservation.getReservationDetails());
                    } else {
                        System.out.println("Booking failed. Room may not be available.");
                    }
                    break;
                case 4:
                    System.out.print("Enter reservation ID to cancel: ");
                    String resId = scanner.nextLine();
                    Reservation toCancel = null;
                    for (Reservation res : manager.getReservations()) {
                        if (res.getReservationID().equals(resId)) {
                            toCancel = res;
                            break;
                        }
                    }
                    if (toCancel != null) {
                        manager.cancelBooking(toCancel);
                        System.out.println("Reservation cancelled.");
                    } else {
                        System.out.println("Reservation not found.");
                    }
                    break;
                case 5:
                    // Save data to files before exit
                    manager.saveRoomsToFile("rooms.txt");
                    manager.saveReservationsToFile("reservations.txt");
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}