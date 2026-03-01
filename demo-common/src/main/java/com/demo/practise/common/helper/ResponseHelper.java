package com.demo.practise.common.helper;

import com.demo.practise.common.resp.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author owen
 * @date 2024/10/21 23:41
 * @description
 */
public class ResponseHelper {
    public static ResponseEntity<Object> successful(byte[] data, HttpHeaders headers) {
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Message<T>> successful() {
        return successful(null);
    }

    public static <T> ResponseEntity<Message<T>> successful(T data) {
        Message<T> result = new Message<>();
        result.setData(data);
        result.setMessage(HttpStatus.OK.name());
        result.setCode(200);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Message<T>> failed(String message) {
        return failed(500, message, HttpStatus.OK, null);
    }

    public static <T> ResponseEntity<Message<T>> failed(int code, String message) {
        return failed(code, message, HttpStatus.OK, null);
    }

    public static <T> ResponseEntity<Message<T>> failed(int code, String message, T data) {
        return failed(code, message, HttpStatus.OK, data);
    }

    public static <T> ResponseEntity<Message<T>> failed(int code, String message, HttpStatus httpStatus, T data) {
        Message<T> result = new Message<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return new ResponseEntity<>(result, httpStatus);
    }

    public static <T> ResponseEntity<Message<T>> failed(String message, HttpStatus httpStatus) {
        return failed(httpStatus.value(), message, httpStatus, null);
    }
}
