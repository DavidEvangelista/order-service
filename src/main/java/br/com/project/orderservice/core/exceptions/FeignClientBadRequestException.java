package br.com.project.orderservice.core.exceptions;

public class FeignClientBadRequestException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "Bad request";

    public FeignClientBadRequestException(){
        super(DEFAULT_MESSAGE);
    }

    public FeignClientBadRequestException(String message){
        super(message);
    }

}

