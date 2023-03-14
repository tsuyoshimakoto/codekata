package credit;

import credit.CreditScore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//********************
// NOT TO REFACTOR.
//********************
// Consider this code autogenerated, you cannot refactor or change anything in this code
public final class CreditScoreService {

    public CreditScore getCreditScore(long fiscalNumber) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://credit-score-calculrator/creditscore/" + fiscalNumber))
                .GET()
                .build();

        HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        String creditResult = (String) response.body();
        return CreditScore.valueOf(creditResult);
    }
}