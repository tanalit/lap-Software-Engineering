package com.app.library.controllers;

import com.app.library.models.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getBooksInitiallyEmpty() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void addBookThenGetById() throws Exception {
        String bookJson = "{\"id\":1,\"title\":\"Test\",\"author\":\"A\",\"publicationYear\":2020,\"genre\":\"Fiction\",\"availableCopies\":1}";
        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(bookJson))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk());
    }
}
