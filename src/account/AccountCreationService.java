package account;

import credit.CreditScore;
import credit.CreditScoreService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;


public class AccountCreationService {

    public Account createNewAccount(String firstName, String lastName, long fiscalNumber, LocalDate birthdate) throws Exception {


       if(firstName == null || firstName.isBlank()) {
           throw new Exception("First name is empty");
       }
       if(lastName == null || lastName.isBlank()) {
           throw new Exception("Last name is empty");
       }
       if(String.valueOf(fiscalNumber).length() != 13) {
           throw new Exception("Invalid fiscal number");
       }

       int years = Period.between(birthdate, LocalDate.now()).getYears();
       if (years < 18) {
           throw new Exception("Cannot open a bank account for a minor, must attach account to an adult parent");
       }



       CreditScore creditscore = new CreditScoreService().getCreditScore(fiscalNumber);


       int creditLimit = 0;
       String sql = "";
       String clientType = "";

       if(creditscore == CreditScore.BAD) {
           clientType = "Ivory";

           creditLimit = 0;
           sql =  "INSERT INTO IVORYACCOUNT (FIRSTNAME, LASTNAME, FISCALNUM, BIRTHDATE, CREDITSCORE) " +
                   "VALUES (" + firstName + "," + lastName + "," + fiscalNumber + "," + birthdate.toEpochDay()+ "," + creditscore  + ")";
           //ivory accounts have credit limit 0. No need to insert the value into the table.
       }

       else if(creditscore== CreditScore.GOOD) {
           clientType = "Silver";
           sql =  "INSERT INTO SILVERACCOUNT (FIRSTNAME, LASTNAME, FISCALNUM, BIRTHDATE, CREDITSCORE, CREDITLIMIT) " +
                   "VALUES (" + firstName + "," + lastName + "," + fiscalNumber + "," + birthdate.toEpochDay()+ "," + creditscore +"," + 1000 + ")";
       }
        else if(creditscore== CreditScore.EXCELLENT) {
           clientType = "Gold";
           sql =  "INSERT INTO GOLDACCOUNT (FIRSTNAME, LASTNAME, FISCALNUM, BIRTHDATE, CREDITSCORE, CREDITLIMIT) " +
                   "VALUES (" + firstName + "," + lastName + "," + fiscalNumber + "," + birthdate.toEpochDay()+ "," + creditscore +"," + 5000 + ")";

       }


        int id= 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://PRODUCTION_SERVER:3306/PROD_DB")) {
            Statement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate(sql);

            ResultSet generatedKeysResult = stmt.getGeneratedKeys();
             id = generatedKeysResult.getInt(1);
        }



        Account account = new Account(id, firstName, lastName, fiscalNumber, birthdate, creditscore, clientType, creditLimit);
        return account;

    }
}
