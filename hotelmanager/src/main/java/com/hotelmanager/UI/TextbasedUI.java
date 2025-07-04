package com.hotelmanager.UI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.hotelmanager.model.Reservation;
import com.hotelmanager.model.Room;
import com.hotelmanager.service.HotelRoomManager;

public class TextbasedUI {
    public static void main(String[] args) {
        HotelRoomManager manager = new HotelRoomManager();
        Scanner scanner = new Scanner(System.in);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Load data from files
        manager.loadRoomsFromFile("rooms.txt");
        manager.loadReservationsFromFile("reservations.txt");

        while (true) {
            System.out.println("\n--- Hotel Room Manager ---");
            System.out.println("1. Add Room");
            System.out.println("2. View Rooms");
            System.out.println("3. Book Room");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. List Reservations");
            System.out.println("6. Search Reservations");
            System.out.println("7. Check Available Rooms by Date");
            System.out.println("8. Exit");
            System.out.println("9. Remove Room");
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
                    // Show all rooms before booking
                    List<Room> allRooms = manager.viewRooms();
                    if (allRooms.isEmpty()) {
                        System.out.println("No rooms available to book.");
                        break;
                    } else {
                        System.out.println("Available rooms:");
                        for (Room room : allRooms) {
                            System.out.println(room.getRoomInfo());
                        }
                    }
                    System.out.print("Enter room number to book: ");
                    String bookNumber = scanner.nextLine();
                    Room roomToBook = null;
                    for (Room room : allRooms) {
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

                    LocalDate checkIn = null, checkOut = null;
                    while (true) {
                        try {
                            System.out.print("Enter check-in date (dd-MM-yyyy): ");
                            checkIn = LocalDate.parse(scanner.nextLine(), formatter);
                            break;
                        } catch (Exception ex) {
                            System.out.println("Invalid date format! Please enter as dd-MM-yyyy (e.g., 05-02-2005).");
                        }
                    }
                    while (true) {
                        try {
                            System.out.print("Enter check-out date (dd-MM-yyyy): ");
                            checkOut = LocalDate.parse(scanner.nextLine(), formatter);
                            break;
                        } catch (Exception ex) {
                            System.out.println("Invalid date format! Please enter as dd-MM-yyyy (e.g., 05-02-2005).");
                        }
                    }
                    Reservation reservation = manager.bookRoom(roomToBook, customer, checkIn, checkOut);
                    if (reservation != null) {
                        System.out.println("Reservation successful:");
                        System.out.println(reservation.getReservationDetails());
                    } else {
                        System.out.println("Booking failed. Room may not be available for the selected dates.");
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
                    List<Reservation> reservations = manager.getReservations();
                    if (reservations.isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        for (Reservation res : reservations) {
                            System.out.println(res.getReservationDetails());
                        }
                    }
                    break;
                case 6:
                    System.out.print("Enter customer name to search (leave blank for any): ");
                    String searchCustomer = scanner.nextLine();
                    if (searchCustomer.isBlank()) searchCustomer = null;
                    System.out.print("Enter date to search (dd-MM-yyyy, leave blank for any): ");
                    String dateStr = scanner.nextLine();
                    LocalDate searchDate = null;
                    if (!dateStr.isBlank()) searchDate = LocalDate.parse(dateStr, formatter);
                    List<Reservation> found = manager.searchReservations(searchCustomer, searchDate);
                    if (found.isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        for (Reservation res : found) {
                            System.out.println(res.getReservationDetails());
                        }
                    }
                    break;
                case 7:
                    LocalDate avCheckIn = null, avCheckOut = null;
                    while (true) {
                        try {
                            System.out.print("Enter check-in date (dd-MM-yyyy): ");
                            String checkInStr = scanner.nextLine();
                            avCheckIn = LocalDate.parse(checkInStr, formatter);
                            break;
                        } catch (Exception ex) {
                            System.out.println("Invalid date format! Please enter as dd-MM-yyyy (e.g., 05-02-2005).");
                        }
                    }
                    while (true) {
                        try {
                            System.out.print("Enter check-out date (dd-MM-yyyy): ");
                            String checkOutStr = scanner.nextLine();
                            avCheckOut = LocalDate.parse(checkOutStr, formatter);
                            break;
                        } catch (Exception ex) {
                            System.out.println("Invalid date format! Please enter as dd-MM-yyyy (e.g., 05-02-2005).");
                        }
                    }
                    List<Room> availableRooms = manager.getAvailableRooms(avCheckIn, avCheckOut);
                    if (availableRooms.isEmpty()) {
                        System.out.println("No rooms available for the selected dates.");
                    } else {
                        System.out.println("Available rooms:");
                        for (Room r : availableRooms) {
                            System.out.print(r.getRoomInfo());
                            boolean bookedFound = false;
                            for (Reservation res : manager.getReservations()) {
                                if (res.getRoom().getRoomNumber().equals(r.getRoomNumber())) {
                                    if (!(avCheckOut.isBefore(res.getCheckInDate()) || avCheckIn.isAfter(res.getCheckOutDate()))) {
                                        // Format the dates as dd-MM-yyyy
                                        String checkInFormatted = res.getCheckInDate().format(formatter);
                                        String checkOutFormatted = res.getCheckOutDate().format(formatter);
                                        System.out.print(" [BOOKED: " + checkInFormatted + " to " + checkOutFormatted + "]");
                                        bookedFound = true;
                                        break;
                                    }
                                }
                            }
                            if (!bookedFound && r.getRoomStatus().equalsIgnoreCase("booked")) {
                                System.out.print(" [BOOKED]");
                            }
                            System.out.println();
                        }
                    }
                    break;
                case 8:
                    // Save data to files before exit
                    manager.saveRoomsToFile("rooms.txt");
                    manager.saveReservationsToFile("reservations.txt");
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                case 9:
                    // Remove a room
                    List<Room> currentRooms = manager.viewRooms();
                    if (currentRooms.isEmpty()) {
                        System.out.println("No rooms to remove.");
                        break;
                    }
                    System.out.println("Current rooms:");
                    for (Room room : currentRooms) {
                        System.out.println(room.getRoomInfo());
                    }
                    System.out.print("Enter room number to remove: ");
                    String removeNumber = scanner.nextLine();
                    Room roomToRemove = null;
                    for (Room room : currentRooms) {
                        if (room.getRoomNumber().equals(removeNumber)) {
                            roomToRemove = room;
                            break;
                        }
                    }
                    if (roomToRemove == null) {
                        System.out.println("Room not found.");
                    } else {
                        manager.deleteRoom(roomToRemove);
                        System.out.println("Room removed.");
                    }
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}