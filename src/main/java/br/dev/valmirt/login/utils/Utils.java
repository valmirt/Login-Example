package br.dev.valmirt.login.utils;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isNullOrEmpty(Object object) {
        return object == null
                || (object.getClass() == String.class && object.toString().isEmpty())
                || (object instanceof Collection && ((Collection) object).isEmpty());
    }


    public static Boolean isValidEmail(String email ){
        String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Boolean isValidPassword(String senha) {
        int MIN_SIZE = 6;
        int MAX_SIZE = 15;

        String PASSWORD_PATERN = "^(?=.*\\d)(?=.*[a-z])(?!.*\\s).*$";
        Pattern pattern = Pattern.compile(PASSWORD_PATERN);
        Matcher matcher = pattern.matcher(senha);

        return matcher.matches() && (senha.length() >= MIN_SIZE && senha.length() <= MAX_SIZE);

    }

}
