package org.example.dto;

public record ApiResponse<T>(String message, T data) {}
