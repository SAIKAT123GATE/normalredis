package com.example.redisdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "user")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User implements Serializable {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
}
