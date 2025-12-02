package com.example.arena.error;

public class InvalidArraySize extends RuntimeException  {
	private static final long serialVersionUID = 1L;

	public InvalidArraySize(int badSize) {
        super("Invalid array size: " + badSize);
    }

    public InvalidArraySize(String message) {
        super(message);
    }
}
