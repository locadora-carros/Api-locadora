package ufpb.br.apilocadora.service.exception;

public class InvalidUserException extends Exception{

    public InvalidUserException() {
        super();
    }

    public InvalidUserException(String s) {
        super(s);
    }
}