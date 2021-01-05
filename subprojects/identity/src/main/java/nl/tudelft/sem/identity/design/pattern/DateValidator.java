package nl.tudelft.sem.identity.design.pattern;

import nl.tudelft.sem.identity.entity.TokenRole;

public class DateValidator extends BaseValidator {

    @Override
    public boolean handle(TokenRole tokenRole) {
        try {
            claims(tokenRole.getToken());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
