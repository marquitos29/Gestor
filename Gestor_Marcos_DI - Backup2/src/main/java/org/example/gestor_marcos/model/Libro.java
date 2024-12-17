package org.example.gestor_marcos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Libro {

    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;
    private int bookRank;
    private String bookImage;
    private String downloadLink;

    public Libro(int bookRank, String bookTitle, String bookAuthor, String bookPublisher, String bookImage, String downloadLink) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.bookRank = bookRank;
        this.bookImage = bookImage;
        this.downloadLink = downloadLink;
    }

    @Override
    public String toString() {
        return "Título: " + bookTitle +
                "\nAutor: " + bookAuthor +
                "\nEditorial: " + bookPublisher +
                "\nImagen: " + bookImage +
                (downloadLink != null ? "\nLink de descarga: " + downloadLink : "");
    }
}
