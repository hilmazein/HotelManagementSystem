package newpackage;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

// Class representing a single room
class Singleroom {
    String customerName; // Name of the customer
    int roomNumber; // Room number

    // Constructor to initialize the customer name and room number
    public Singleroom(String customerName, int roomNumber) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
    }
}

// Stack Node for recent booking and cancellation history
class HistoryNode {
    String action; // "Booked" or "Cancelled"
    String customerName; // Name of the customer involved in the action
    int roomNumber; // Room number associated with the action
    HistoryNode next; // Reference to the next node in the stack

    // Constructor for HistoryNode
    HistoryNode(String action, String customerName, int roomNumber) {
        this.action = action;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
    }
}

// Stack class for booking/cancellation history
class HistoryStack {
    private HistoryNode top; // Top of the stack

    // Push a new action onto the history stack
    public void push(String action, String customerName, int roomNumber) {
        HistoryNode newNode = new HistoryNode(action, customerName, roomNumber);
        newNode.next = top; // Point new node to the previous top
        top = newNode; // Update top to the new node
    }

    // Display the history of bookings and cancellations
    public void display() {
        HistoryNode temp = top; // Start from the top of the stack
        while (temp != null) {
            System.out.println(temp.action + " - " + temp.customerName + " (Room: " + temp.roomNumber + ")");
            temp = temp.next; // Move to the next node
        }
    }
}

// BST Node for room/customer lookup
class CustomerNode {
    int roomNumber; // Room number
    Singleroom singleRoom; // Single room object containing customer information
    CustomerNode left, right; // Left and right child nodes for BST structure

    // Constructor for CustomerNode
    public CustomerNode(int roomNumber, Singleroom singleRoom) {
        this.roomNumber = roomNumber;
        this.singleRoom = singleRoom;
    }
}

// BST class for efficient customer searching
class RoomBST {
    private CustomerNode root; // Root of the BST

    // Insert a new room into the BST
    public void insert(int roomNumber, Singleroom singleRoom) {
        root = insertRec(root, roomNumber, singleRoom); // Call recursive insert method
    }

    // Recursive method to insert a node in the BST
    private CustomerNode insertRec(CustomerNode root, int roomNumber, Singleroom singleRoom) {
        if (root == null) { // If we reach a null node, insert here
            root = new CustomerNode(roomNumber, singleRoom);
            return root;
        }
        // Traverse left or right based on room number
        if (roomNumber < root.roomNumber)
            root.left = insertRec(root.left, roomNumber, singleRoom);
        else if (roomNumber > root.roomNumber)
            root.right = insertRec(root.right, roomNumber, singleRoom);

        return root; // Return the unchanged root node
    }

    // Search for a room in the BST
    public CustomerNode search(int roomNumber) {
        return searchRec(root, roomNumber); // Call recursive search method
    }

    // Recursive method to search for a room number
    private CustomerNode searchRec(CustomerNode root, int roomNumber) {
        if (root == null || root.roomNumber == roomNumber) // Base case
            return root; 
        // Traverse left or right based on room number
        if (root.roomNumber > roomNumber)
            return searchRec(root.left, roomNumber);
        return searchRec(root.right, roomNumber);
    }

    // Delete a room from the BST
    public CustomerNode delete(int roomNumber) {
        root = deleteRec(root, roomNumber); // Call recursive delete method
        return root; // Return the updated root
    }

    // Recursive method to delete a room
    private CustomerNode deleteRec(CustomerNode root, int roomNumber) {
        if (root == null) return root; // Base case
        // Traverse left or right based on room number
        if (roomNumber < root.roomNumber)
            root.left = deleteRec(root.left, roomNumber);
        else if (roomNumber > root.roomNumber)
            root.right = deleteRec(root.right, roomNumber);
        else { // Node to be deleted found
            // Node with only one child or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;
            // Node with two children: Get the inorder successor (smallest in the right subtree)
            root.roomNumber = minValue(root.right);
            // Delete the inorder successor
            root.right = deleteRec(root.right, root.roomNumber);
        }
        return root; // Return the updated root
    }

    // Find the minimum value node in a subtree
    private int minValue(CustomerNode root) {
        int minv = root.roomNumber; // Initialize min value
        while (root.left != null) { // Traverse to the leftmost node
            minv = root.left.roomNumber;
            root = root.left;
        }
        return minv; // Return the minimum value
    }
}

// Queue class for waiting list
class WaitingQueue {
    private Queue<Singleroom> queue; // Queue to hold waiting rooms

    // Constructor to initialize the queue
    public WaitingQueue() {
        this.queue = new LinkedList<>();
    }

    // Enqueue a room to the waiting list
    public void enqueue(Singleroom room) {
        queue.add(room);
    }

    // Dequeue a room from the waiting list
    public Singleroom dequeue() {
        return queue.poll(); // Poll to remove and return the head of the queue
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    // Display the waiting list
    public void display() {
        if (queue.isEmpty()) {
            System.out.println("Waiting list is empty."); // If empty, print message
        } else {
            System.out.println(" Waiting list:");
            for (Singleroom room : queue) { // Iterate over the queue
                System.out.println("Room " + room.roomNumber + " - " + room.customerName);
            }
        }
    }
}

// Main class for hotel management system
public class HotelManagementSystem {
    private static HistoryStack historyStack = new HistoryStack(); // Instance of the history stack
    private static RoomBST roomBST = new RoomBST(); // Instance of the room binary search tree
    private static WaitingQueue waitingQueue = new WaitingQueue(); // Instance of the waiting queue

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Scanner for user input
        boolean running = true; // Control variable for main loop

        while (running) {
            // Display menu options
            System.out.println("\nHotel Management System");
            System.out.println("1. Book Room");
            System.out.println("2. Cancel Room");
            System.out.println("3. View Booking History");
            System.out.println("4. Search Room");
            System.out.println("5. View Waiting List");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt(); // Get user choice
            switch (choice) {
                case 1:
                    bookRoom(scanner); // Call booking method
                    break;
                case 2:
                    cancelRoom(scanner); // Call cancellation method
                    break;
                case 3:
                    historyStack.display(); // Display booking history
                    break;
                case 4:
                    searchRoom(scanner); // Call search room method
                    break;
                case 5:
                    waitingQueue.display(); // Display waiting list
                    break;
                case 6:
                    running = false; // Exit the loop
                    break;
                default:
                    System.out.println("Invalid option, please try again."); // Invalid choice handling
            }
        }
        scanner.close(); // Close the scanner
    }

    // Booking function
    private static void bookRoom(Scanner scanner) {
        System.out.print("Enter Room Number: ");
        int roomNumber = scanner.nextInt(); // Get room number from user
        System.out.print("Enter Customer Name: ");
        String customerName = scanner.next(); // Get customer name from user

        // Check if the room is already booked
        if (roomBST.search(roomNumber) != null) {
            System.out.println("Room is occupied. Would you like to be added to the waiting list? (yes/no)");
            String response = scanner.next(); // Get user's response

            if (response.equalsIgnoreCase("yes")) {
                Singleroom singleRoom = new Singleroom(customerName, roomNumber); // Create new Singleroom
                waitingQueue.enqueue(singleRoom); // Add to waiting queue
                System.out.println("You have been added to the waiting list.");
            } else {
                System.out.println("Booking not made."); // Booking not made
            }
        } else {
            Singleroom singleRoom = new Singleroom(customerName, roomNumber); // Create new Singleroom
            roomBST.insert(roomNumber, singleRoom); // Insert into BST
            historyStack.push("Booked", customerName, roomNumber); // Log the action in history stack
            System.out.println("Room booked successfully."); // Success message
        }
    }

    // Cancellation function
    private static void cancelRoom(Scanner scanner) {
        System.out.print("Enter Room Number to Cancel: ");
        int roomNumber = scanner.nextInt(); // Get room number from user
        CustomerNode customer = roomBST.search(roomNumber); // Search for the room in BST

        // If the room is found
        if (customer != null) {
            historyStack.push("Cancelled", customer.singleRoom.customerName, roomNumber); // Log the cancellation
            System.out.println("Booking cancelled for Room: " + roomNumber);
            roomBST.delete(roomNumber); // Delete from BST

            // Check if there is a waiting list
            if (!waitingQueue.isEmpty()) {
                Singleroom nextCustomer = waitingQueue.dequeue(); // Get the next customer from the waiting list
                if (nextCustomer.roomNumber == roomNumber) { // Check if they match the canceled room
                    System.out.println("Room " + roomNumber + " has been booked by " + nextCustomer.customerName);
                    roomBST.insert(roomNumber, nextCustomer); // Book the room for the next customer
                    historyStack.push("Booked", nextCustomer.customerName, roomNumber); // Log the booking
                } else {
                    waitingQueue.enqueue(nextCustomer); // Re-add to waiting queue
                }
            }
        } else {
            System.out.println("Room not found."); // Room not found handling
        }
    }

    // Room search function
    private static void searchRoom(Scanner scanner) {
        System.out.print("Enter Room Number to Search: ");
        int roomNumber = scanner.nextInt(); // Get room number from user
        CustomerNode customer = roomBST.search(roomNumber); // Search for the room in BST

        // If room is found, display customer info
        if (customer != null && customer.singleRoom != null) {
            System.out.println("Room " + roomNumber + " booked by " + customer.singleRoom.customerName);
        } else {
            System.out.println("Room not found or is vacant."); // Room not found handling
        }
    }
}
