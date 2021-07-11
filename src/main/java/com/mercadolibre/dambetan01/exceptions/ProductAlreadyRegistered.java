package com.mercadolibre.dambetan01.exceptions;

public class ProductAlreadyRegistered extends RuntimeException{
    public ProductAlreadyRegistered(String message) {
        super(message);
    }
}
