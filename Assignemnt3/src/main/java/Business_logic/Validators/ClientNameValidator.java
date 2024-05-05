package Business_logic.Validators;

import Models.Clients;

import java.util.regex.Pattern;

public class ClientNameValidator implements Validator<Clients>{

    private static final String NAME_PATTERN="[A-Z][a-z]*(-[A-Z][a-z]*)? [A-Z][a-z]*(-[A-Z][a-z]*)?";

    public void validate(Clients t) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        if (!pattern.matcher(t.getName()).matches()) {
            throw new IllegalArgumentException("Name is not a valid name!");
        }
    }

}
