package br.com.project.orderservice.core.exceptions;

public class FeignClientNotFoundException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "Not found";

    public FeignClientNotFoundException(){
        super(DEFAULT_MESSAGE);
    }

    public FeignClientNotFoundException(String message){
        super(message);
    }

}

