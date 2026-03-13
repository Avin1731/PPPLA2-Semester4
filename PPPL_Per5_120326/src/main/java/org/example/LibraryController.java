package org.example;

public class LibraryController {
    private final LibraryModel libraryModel;
    private final NotificationService notificationService;

    public LibraryController(LibraryModel libraryModel, NotificationService notificationService) {
        this.libraryModel = libraryModel;
        this.notificationService = notificationService;
    }

    // Task 1: Cek ketersediaan buku
    public boolean checkAvailability(String id) {
        return libraryModel.isBookAvailable(id);
    }

    // Task 2 & 3: Proses peminjaman dan notifikasi
    public void processBorrowBook(String id, String title, String username) {
        if (libraryModel.isBookAvailable(id)) {
            libraryModel.borrowBook(id);

            // Eksekusi berurutan sesuai skenario
            libraryModel.getAllBooks();
            libraryModel.Peminjaman(title, username);
            notificationService.sendNotification(username, title);
        }
    }
}