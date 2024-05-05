package Business_logic.Validators;

import Models.Clients;

import java.util.regex.Pattern;

public class PhoneValidator implements Validator<Clients> {
    private static final String PHONE_PATTERN = "\\d{10}";
    public void validate(Clients t) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        if (!pattern.matcher(t.getPhone()).matches()) {
            throw new IllegalArgumentException("Phone number is not valid!");
        }
    }
}
