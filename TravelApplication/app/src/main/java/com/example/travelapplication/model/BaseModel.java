package com.example.travelapplication.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class BaseModel {
    public Object deepClone() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(this), this.getClass());
        } catch (IOException e) {
            throw new ClassCastException("clone失败，clone对象为" + this.getClass());
        }
    }
}
