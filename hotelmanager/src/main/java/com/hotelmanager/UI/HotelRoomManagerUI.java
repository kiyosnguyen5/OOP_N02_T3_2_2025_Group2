package com.hotelmanager.UI;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.time.format.DateTimeFormatter;
import com.hotelmanager.model.Reservation;
import com.hotelmanager.model.Room;
import com.hotelmanager.service.HotelRoomManager;

public class HotelRoomManagerUI extends JFrame {
    private HotelRoomManager manager;
    private DefaultListModel<String> roomListModel;
    private JList<String> roomList;
    private JTextArea outputArea;

    private JTextField floorInput;
    private JCheckBox bookedCheck, availableCheck, singleCheck, doubleCheck;
    private JButton filterBtn;

    public HotelRoomManagerUI() {
        manager = new HotelRoomManager();
        manager.loadRoomsFromFile("data/rooms.txt");
        manager.loadReservationsFromFile("data/reservations.txt");

        setTitle("Hotel Room Manager");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(48, 51, 57));
        JLabel titleLabel = new JLabel("Hotel Room Manager");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(titleLabel);

        // Room list panel
        roomListModel = new DefaultListModel<>();
        roomList = new JList<>(roomListModel);
        roomList.setFont(font);
        roomList.setBackground(new Color(30, 32, 36));
        roomList.setForeground(Color.WHITE);
        JScrollPane roomScrollPane = new JScrollPane(roomList);
        roomScrollPane.setPreferredSize(new Dimension(350, 0));

        // ======= KHỞI TẠO FILTER TRƯỚC =======
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(38, 41, 48));
        filterPanel.setPreferredSize(new Dimension(0, 48));

        // Floor input
        filterPanel.add(new JLabel("Floor:"));
        floorInput = new JTextField(3);
        filterPanel.add(floorInput);

        // Status checkboxes
        bookedCheck = new JCheckBox("Booked");
        availableCheck = new JCheckBox("Available");
        filterPanel.add(bookedCheck);
        filterPanel.add(availableCheck);

        // Type checkboxes
        singleCheck = new JCheckBox("Single");
        doubleCheck = new JCheckBox("Double");
        filterPanel.add(singleCheck);
        filterPanel.add(doubleCheck);

        // Nút filter
        filterBtn = new JButton("Filter");
        filterBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterBtn.setBackground(new Color(33, 150, 243));
        filterBtn.setForeground(Color.WHITE);
        filterBtn.setFocusPainted(false);
        filterBtn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        filterPanel.add(filterBtn);

        add(filterPanel, BorderLayout.BEFORE_FIRST_LINE);

        filterBtn.addActionListener(e -> showFilterPopup());

        // ======= GỌI updateRoomList() SAU KHI ĐÃ KHỞI TẠO FILTER =======
        updateRoomList();

        // Show room details and actions when a room is clicked
        roomList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int index = roomList.locationToIndex(evt.getPoint());
                if (index >= 0) {
                    Room selectedRoom = manager.viewRooms().get(index);
                    showRoomOptions(selectedRoom);
                }
            }
        });

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(font);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        outputArea.setBackground(new Color(30, 32, 36));
        outputArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addRoomBtn = new JButton("Add Room");
        JButton saveBtn = new JButton("Save Data");
        JButton exitBtn = new JButton("Exit");

        JButton[] buttons = {filterBtn, addRoomBtn, saveBtn, exitBtn};
        for (JButton btn : buttons) {
            btn.setFont(font);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(33, 150, 243));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }

        addRoomBtn.addActionListener(e -> addRoom());
        saveBtn.addActionListener(e -> {
            manager.saveRoomsToFile("data/rooms.txt");
            manager.saveReservationsToFile("data/reservations.txt");
            outputArea.setText("Data saved successfully.\n");
        });
        exitBtn.addActionListener(e -> {
            manager.saveRoomsToFile("data/rooms.txt");
            manager.saveReservationsToFile("data/reservations.txt");
            System.exit(0);
        });
        filterBtn.addActionListener(e -> updateRoomList()); // Nút filter gọi updateRoomList

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(38, 41, 48));
        for (JButton btn : buttons) {
            buttonPanel.add(btn);
        }

        setLayout(new BorderLayout(0, 0));
        add(headerPanel, BorderLayout.NORTH);

        // Tạo panel trung tâm với room list phía trên, log phía dưới
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Room list phía trên
        centerPanel.add(roomScrollPane, BorderLayout.CENTER);

        // Log/output phía dưới
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.add(scrollPane, BorderLayout.CENTER);
        logPanel.setPreferredSize(new Dimension(0, 150)); // Chiều cao log
        centerPanel.add(logPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);

        // Always show room list and details of the first room (if any)
        if (!manager.viewRooms().isEmpty()) {
            roomList.setSelectedIndex(0);
            showRoomDetails(manager.viewRooms().get(0));
        }
    }

    private void updateRoomList() {
        roomListModel.clear();
        List<Room> rooms = manager.viewRooms();
        List<Reservation> reservations = manager.getReservations();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String floorSel = floorInput.getText().trim();
        boolean filterBooked = bookedCheck.isSelected();
        boolean filterAvailable = availableCheck.isSelected();
        boolean filterSingle = singleCheck.isSelected();
        boolean filterDouble = doubleCheck.isSelected();

        for (Room room : rooms) {
            // Filter theo tầng (user input)
            if (!floorSel.isEmpty()) {
                String roomNum = room.getRoomNumber();
                String floorNum = roomNum.length() > 1 ? roomNum.substring(0, roomNum.length() - 2) : roomNum.substring(0, 1);
                if (!floorNum.equals(floorSel)) continue;
            }
            // Filter theo trạng thái
            boolean isBooked = false;
            for (Reservation res : reservations) {
                if (res.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                    isBooked = true;
                    break;
                }
            }
            if (filterBooked && !isBooked) continue;
            if (filterAvailable && isBooked) continue;

            // Filter theo loại phòng
            String type = room.getRoomType().toLowerCase();
            if (filterSingle && !type.contains("single")) continue;
            if (filterDouble && !type.contains("double")) continue;

            StringBuilder sb = new StringBuilder();
            sb.append(room.getRoomInfo());
            boolean bookedFound = false;
            for (Reservation res : reservations) {
                if (res.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                    sb.append(" [BOOKED: ")
                      .append(res.getCheckInDate().format(formatter))
                      .append(" to ")
                      .append(res.getCheckOutDate().format(formatter))
                      .append("]");
                    bookedFound = true;
                    break;
                }
            }
            if (!bookedFound && room.getRoomStatus().equalsIgnoreCase("booked")) {
                sb.append(" [BOOKED]");
            }
            roomListModel.addElement(sb.toString());
        }
    }

    private void showRoomDetails(Room room) {
        StringBuilder sb = new StringBuilder();
        sb.append(room.getRoomInfo()).append("\n");
        List<Reservation> reservations = manager.getReservations();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Reservation res : reservations) {
            if (res.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                sb.append("BOOKED: ")
                  .append(res.getCheckInDate().format(formatter))
                  .append(" to ")
                  .append(res.getCheckOutDate().format(formatter))
                  .append("\nCustomer: ").append(res.getCustomerDetails())
                  .append("\nReservation ID: ").append(res.getReservationID());
                break;
            }
        }
        outputArea.setText(sb.toString());
    }

    private void showRoomOptions(Room room) {
        String[] options = {"Book Room", "Remove Room", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose an action for room " + room.getRoomNumber(),
                "Room Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (choice == 0) {
            bookRoom(room);
        } else if (choice == 1) {
            removeRoom(room);
        }
        // else Cancel, do nothing
        showRoomDetails(room);
        updateRoomList();
    }

    private void addRoom() {
        JTextField numberField = new JTextField();
        JTextField typeField = new JTextField();
        typeField.setToolTipText("Single/Double"); // hint khi hover
        Object[] fields = {
            "Room Number:", numberField,
            "Room Type (Single/Double):", typeField
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Add Room", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String number = numberField.getText().trim();
            String type = typeField.getText().trim();
            if (type.isEmpty()) type = "single";
            if (!number.isEmpty()) {
                manager.addRoom(new Room(number, type, "available"));
                outputArea.setText("Room added.\n");
                updateRoomList();
            } else {
                outputArea.setText("Invalid input. Room number is required.\n");
            }
        }
    }

    private void bookRoom(Room roomToBook) {
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
        updateRoomList();
    }

    private void removeRoom(Room roomToRemove) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove room " + roomToRemove.getRoomNumber() + "?",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            manager.deleteRoom(roomToRemove);
            outputArea.setText("Room removed.\n");
            updateRoomList();
        }
    }

    private void showFilterPopup() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBackground(new Color(38, 41, 48));

        JTextField floorField = new JTextField(floorInput.getText());
        JCheckBox bookedBox = new JCheckBox("Booked", bookedCheck.isSelected());
        JCheckBox availableBox = new JCheckBox("Available", availableCheck.isSelected());
        JCheckBox singleBox = new JCheckBox("Single", singleCheck.isSelected());
        JCheckBox doubleBox = new JCheckBox("Double", doubleCheck.isSelected());

        panel.add(new JLabel("Floor:"));
        panel.add(floorField);
        panel.add(bookedBox);
        panel.add(availableBox);
        panel.add(singleBox);
        panel.add(doubleBox);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "Filter Rooms", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (result == JOptionPane.OK_OPTION) {
            floorInput.setText(floorField.getText());
            bookedCheck.setSelected(bookedBox.isSelected());
            availableCheck.setSelected(availableBox.isSelected());
            singleCheck.setSelected(singleBox.isSelected());
            doubleCheck.setSelected(doubleBox.isSelected());
            updateRoomList();
        }
    }

    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup();
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        SwingUtilities.invokeLater(() -> {
            (new HotelRoomManagerUI()).setVisible(true);
        });
    }
}