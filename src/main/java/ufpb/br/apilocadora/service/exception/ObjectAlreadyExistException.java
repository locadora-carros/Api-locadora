package ufpb.br.apilocadora.service.exception;

public class ObjectAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ObjectAlreadyExistException(String msg) {
        super(msg);
    }

    public ObjectAlreadyExistException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
