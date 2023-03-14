package account;

import java.time.LocalDate;

//*******************
//DO NOT REFACTOR
//*******************
public class MainApp {

    public static void main(String[] args) throws Exception {
        LocalDate localDate = LocalDate.of(1979, 4, 10);
        Account account =  new AccountCreationService().createNewAccount("John", "Smith", 1234567891234L, localDate);
    }

}
