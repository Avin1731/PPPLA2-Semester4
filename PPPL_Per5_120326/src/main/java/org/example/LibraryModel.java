package org.example;

import java.util.List;

public interface LibraryModel {
    boolean isBookAvailable(String id);
    void borrowBook(String id);
    List<String> getAllBooks();
    void Peminjaman(String title, String username);
}