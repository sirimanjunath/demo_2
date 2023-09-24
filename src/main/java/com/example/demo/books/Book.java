package com.example.demo.books;

import jakarta.persistence.*;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.io.Serializable;

@Entity
public class Book {

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String author;

    public Book(SingularAttribute<AbstractPersistable, Serializable> id) {
    }

    public void setTitle(String s) {
    }

    public void setAuthor(String s) {
    }

    public void setId(long l) {
    }

    public String getTitle() {
        return null;
    }
}