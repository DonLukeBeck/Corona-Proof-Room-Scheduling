package nl.tudelft.sem.identity.design.pattern;

import nl.tudelft.sem.identity.entity.TokenRole;

/**
 * Shared API for all Validators.
 */
public interface Validator {

    void setNext(Validator handler);

    boolean handle(TokenRole tokenRole) throws Exception;

}
