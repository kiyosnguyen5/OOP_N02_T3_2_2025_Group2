package com.hotelmanager;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JButton viewRoomsBtn = new JButton("View Rooms");
        JButton addRoomBtn = new JButton("Add Room");
        JButton bookRoomBtn = new JButton("Book Room");
        JButton exitBtn = new JButton("Exit");

        viewRoomsBtn.addActionListener(e -> viewRooms());
        addRoomBtn.addActionListener(e -> addRoom());
        bookRoomBtn.addActionListener(e -> bookRoom());
        exitBtn.addActionListener(e -> {
            manager.saveRoomsToFile("data/rooms.txt");
            manager.saveReservationsToFile("data/reservations.txt");
            System.exit(0);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewRoomsBtn);
        buttonPanel.add(addRoomBtn);
        buttonPanel.add(bookRoomBtn);
        buttonPanel.add(exitBtn);

        add(new JScrollPane(outputArea), BorderLayout.CENTER);
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
        Reservation reservation = manager.bookRoom(roomToBook, customer.trim());
        if (reservation != null) {
            outputArea.setText("Reservation successful:\n" + reservation.getReservationDetails());
        } else {
            outputArea.setText("Booking failed. Room may not be available.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HotelRoomManagerUI().setVisible(true);
        });
    }
}