package uno.dos.tres.service.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import uno.dos.tres.bean.RegInfo;

public class ValidatorImpl implements Validator {

    private ValidatorImpl() {
    }

    private static final Validator instance = new ValidatorImpl();

    public static Validator getInstance() {
        return instance;
    }

    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final RegexValidator usernameValidator = new RegexValidator("^[a-zA-Z0-9]{2,15}$");
    private final RegexValidator passwordValidator = new RegexValidator("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    @Override
    public void checkRegInfo(RegInfo regInfo) throws ValidatorException {
        String userName = regInfo.getUserName();
        String password = regInfo.getPassword();
        String email = regInfo.getEmail();
        if (!emailValidator.isValid(email) && !usernameValidator.isValid(userName) && !passwordValidator.isValid(password)) {
            throw new ValidatorException("Invalid username, email, password");
        } else if (!emailValidator.isValid(email) && !usernameValidator.isValid(userName)) {
            throw new ValidatorException("Invalid username, email");
        } else if (!emailValidator.isValid(email) && !passwordValidator.isValid(password)) {
            throw new ValidatorException("Invalid password, email");
        } else if (!passwordValidator.isValid(password) && !usernameValidator.isValid(userName)) {
            throw new ValidatorException("Invalid password, username");
        } else if (!usernameValidator.isValid(userName)) {
            throw new ValidatorException("Invalid username");
        } else if (!emailValidator.isValid(email)) {
            throw new ValidatorException("Invalid email");
        } else if (!passwordValidator.isValid(password)) {
            throw new ValidatorException("Invalid password");
        }
    }
}
