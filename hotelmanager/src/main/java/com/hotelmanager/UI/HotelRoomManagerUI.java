package com.hotelmanager.UI;

import com.formdev.flatlaf.FlatLightLaf; // Add this import

import java.awt.*;
import javax.swing.*;

import java.util.List;

import com.hotelmanager.model.Reservation;
import com.hotelmanager.model.Room;
import com.hotelmanager.service.HotelRoomManager;

public class HotelRoomManagerUI extends JFrame {
    private HotelRoomManager manager;
    private JTextArea outputArea;

    public HotelRoomManagerUI() {
        manager = new HotelRoomManager();
        manager.loadRoomsFromFile("data/rooms.txt");
        manager.loadReservationsFromFile("data/reservations.txt");

        setTitle("Hotel Room Manager");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Modern font
        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243)); // Material blue
        JLabel titleLabel = new JLabel("Hotel Room Manager");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(titleLabel);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(font);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons
        JButton viewRoomsBtn = new JButton("View Rooms");
        JButton addRoomBtn = new JButton("Add Room");
        JButton bookRoomBtn = new JButton("Book Room");
        JButton exitBtn = new JButton("Exit");

        JButton[] buttons = {viewRoomsBtn, addRoomBtn, bookRoomBtn, exitBtn};
        for (JButton btn : buttons) {
            btn.setFont(font);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(33, 150, 243));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }

        viewRoomsBtn.addActionListener(e -> viewRooms());
        addRoomBtn.addActionListener(e -> addRoom());
        bookRoomBtn.addActionListener(e -> bookRoom());
        exitBtn.addActionListener(e -> {
            manager.saveRoomsToFile("data/rooms.txt");
            manager.saveReservationsToFile("data/reservations.txt");
            System.exit(0);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        for (JButton btn : buttons) {
            buttonPanel.add(btn);
        }

        // Main layout
        setLayout(new BorderLayout(0, 0));
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void viewRooms() {
        List<Room> rooms = manager.viewRooms();
        StringBuilder sb = new StringBuilder();
        if (rooms.isEmpty()) {
            sb.append("No rooms available.\n");
        } else {
            for (Room room : rooms) {
                sb.append(room.getRoomInfo()).append("\n");
            }
        }
        outputArea.setText(sb.toString());
    }

    private void addRoom() {
        JTextField numberField = new JTextField();
        JTextField typeField = new JTextField();
        Object[] fields = {
            "Room Number:", numberField,
            "Room Type:", typeField
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Add Room", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String number = numberField.getText().trim();
            String type = typeField.getText().trim();
            if (!number.isEmpty() && !type.isEmpty()) {
                manager.addRoom(new Room(number, type, "available"));
                outputArea.setText("Room added.\n");
            } else {
                outputArea.setText("Invalid input.\n");
            }
        }
    }

    private void bookRoom() {
        String roomNumber = JOptionPane.showInputDialog(this, "Enter room number to book:");
        if (roomNumber == null) return;
        Room roomToBook = null;
        for (Room room : manager.viewRooms()) {
            if (room.getRoomNumber().equals(roomNumber.trim())) {
                roomToBook = room;
                break;
            }
        }
        if (roomToBook == null) {
            outputArea.setText("Room not found.\n");
            return;
        }
        String customer = JOptionPane.showInputDialog(this, "Enter customer name:");
        if (customer == null) return;

        JTextField checkInField = new JTextField();
        JTextField checkOutField = new JTextField();
        Object[] fields = {
            "Check-in date (dd-MM-yyyy):", checkInField,
            "Check-out date (dd-MM-yyyy):", checkOutField
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Enter Dates", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
            java.time.LocalDate checkIn = java.time.LocalDate.parse(checkInField.getText().trim(), formatter);
            java.time.LocalDate checkOut = java.time.LocalDate.parse(checkOutField.getText().trim(), formatter);

            Reservation reservation = manager.bookRoom(roomToBook, customer.trim(), checkIn, checkOut);
            if (reservation != null) {
                outputArea.setText("Reservation successful:\n" + reservation.getReservationDetails());
            } else {
                outputArea.setText("Booking failed. Room may not be available for the selected dates.\n");
            }
        } catch (Exception ex) {
            outputArea.setText("Invalid date format. Please use dd-MM-yyyy.\n");
        }
    }

    public static void main(String[] args) {
        // Set FlatLaf look and feel
        try {
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        SwingUtilities.invokeLater(() -> {
            (new HotelRoomManagerUI()).setVisible(true);
        });
    }
}