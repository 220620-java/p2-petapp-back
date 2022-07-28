package com.revature.petapp.exceptions;

// because this extends Exception (rather than RuntimeException),
// it is a checked exception, meaning that we have to handle it
// using a try-catch or throws.
public class UsernameAlreadyExistsException extends Exception {

}
