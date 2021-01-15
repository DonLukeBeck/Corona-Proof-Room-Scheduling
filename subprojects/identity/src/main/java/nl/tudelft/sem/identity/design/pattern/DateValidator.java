package nl.tudelft.sem.identity.design.pattern;

import nl.tudelft.sem.identity.entity.TokenRole;

/**
 * Concrete handler that contains actual logic to verify the date.
 */
public class DateValidator extends BaseValidator {

    @Override
    public boolean handle(TokenRole tokenRole) {
        try {
            claims(tokenRole.getToken());
        } catch (Exception e) {
            return false;
        }
        return super.checkNext(tokenRole);
    }
}
