package com.hotelmanager;

import java.util.Scanner;
import com.hotelmanager.UI.HotelRoomManagerUI;
import com.hotelmanager.UI.TextbasedUI;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose UI to run:");
        System.out.println("1. HotelRoomManagerUI (GUI)");
        System.out.println("2. TextbasedUI (Console)");
        System.out.print("Enter option (1 or 2): ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                HotelRoomManagerUI.main(args);
                break;
            case "2":
                TextbasedUI.main(args);
                break;
            default:
                System.out.println("Invalid option.");
        }
        scanner.close();
    }
}
