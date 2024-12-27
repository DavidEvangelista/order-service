package br.com.project.orderservice.core.exceptions;

public class FeignClientInternalServerErrorException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "Internal Server Error";

    public FeignClientInternalServerErrorException(){
        super(DEFAULT_MESSAGE);
    }

    public FeignClientInternalServerErrorException(String message){
        super(message);
    }

}

