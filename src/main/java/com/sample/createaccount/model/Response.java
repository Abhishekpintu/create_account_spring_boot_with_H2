package com.sample.createaccount.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class Response {

    private String businessMessage;
    private Boolean status;
    private Object data = Optional.empty();

    public static Response successResponse(Object data, String message) {
        Response response = new Response();
        response.setStatus(Boolean.TRUE);
        response.setBusinessMessage(message);
        response.setData(data);
        return response;
    }
}
