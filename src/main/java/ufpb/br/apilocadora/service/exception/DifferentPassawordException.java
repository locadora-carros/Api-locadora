package ufpb.br.apilocadora.service.exception;

public class DifferentPassawordException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public DifferentPassawordException(String msg) {
        super(msg);
    }
}
