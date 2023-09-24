package com.example.demo;

import com.example.demo.books.Book;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.AssertionErrors;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class SpringBootLiveTestBooks {

    public static final String API_ROOT
            = "http://localhost:8080/api/books";

    private Book createRandomBook() {
        Book book = new Book(id);
        book.setTitle(randomAlphabetic(10));
        book.setAuthor(randomAlphabetic(15));
        return book;
    }

    private String createBookAsUri(Book book) {
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");
    }

    @Test
    public void whenGetAllBooks_thenOK() {
        Response response = RestAssured.get(API_ROOT);

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenCreateNewBook_thenCreated() {
        Book book = createRandomBook();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);

        AssertionErrors.assertEquals("was the book created correctly", HttpStatus.CREATED.toString(), response.getStatusCode());
    }

    @Test
    public void whenInvalidBook_thenError() {
        Book book = createRandomBook();
        book.setAuthor(null);
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);

        AssertionErrors.assertEquals("was there an invalid book", HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    public void whenUpdateCreatedBook_thenUpdated() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        book.setId(Long.parseLong(location.split("api/books/")[1]));
        book.setAuthor("newAuthor");
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .put(location);

        AssertionErrors.assertEquals("was the book updated", HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);

        AssertionErrors.assertEquals("something", HttpStatus.OK.value(), response.getStatusCode());
        AssertionErrors.assertEquals("was there a new author","newAuthor", response.jsonPath().get("author"));
    }

    @Test
    public void whenDeleteCreatedBook_thenOk() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        Response response = RestAssured.delete(location);

        AssertionErrors.assertEquals("was the book deleted", HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        AssertionErrors.assertEquals("something again", HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    public void whenGetBooksByTitle_thenOK() {
        Book book = createRandomBook();
        createBookAsUri(book);
        Response response = RestAssured.get(
                API_ROOT + "/title/" + book.getTitle());

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.as(List.class)
                .size() > 0);
    }
    @Test
    public void whenGetCreatedBookById_thenOK() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        Response response = RestAssured.get(location);

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("was the book created by an id", book.getTitle(), response.jsonPath().get("title"));
    }

    @Test
    public void whenGetNotExistBookById_thenNotFound() {
        Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

}
