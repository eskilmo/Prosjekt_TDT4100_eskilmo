package chatforum;

import java.util.regex.Pattern;

public class Bruker {
    private String brukernavn;
    private String epost;
    private String passord;

    public Bruker(String brukernavn, String epost, String passord) {
        if (!testBrukernavn(brukernavn)) {
            throw new IllegalArgumentException("Brukernavn er ugyldig");
        }
        if (!testEmail(epost)) {
            throw new IllegalArgumentException("Emailen er av feil format");
        }
        if (!testPassord(passord)) {
            throw new IllegalArgumentException("Passord må bestå av minst 8 gyldige tegn, med store og små bokstaver, tall, og et annet tegn.");
        }
        this.brukernavn = brukernavn;
        this.epost=epost;
        this.passord = passord;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    protected String getPassord() {
        return passord;
    }

    public String getEpost() {
        return epost;
    }

    public void testRiktigPassord (String passord) {
        if (!this.passord.equals(passord)) {
            throw new IllegalArgumentException("Feil passord");
        }
    }

    public void endreBrukeravn(String brukernavn) {
        if (!testBrukernavn(brukernavn)) {
            throw new IllegalArgumentException("Brukernavn er ugyldig");
        }
        this.brukernavn = brukernavn;
    }

    public void endrePassord(String gammeltPassord, String nyttPassord) {
        if (!gammeltPassord.equals(passord)) {
            throw new IllegalArgumentException("Gammelt passord er feil");
        }
        if (!testPassord(nyttPassord)) {
            throw new IllegalArgumentException("Passord må bestå av minst 8 gyldige tegn, med store og små bokstaver, tall, og et annet tegn.");
        }
        this.passord = nyttPassord;
    }

    private boolean testBrukernavn(String brukernavn) {
        String regex = "^[\\w]{3,22}$";

        return Pattern.compile(regex).matcher(brukernavn).matches();
    }

    private boolean testPassord(String passord) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&-+=[{}]():;'?,.*/<>~])(?!.*\\s).{8,30}$";

        return Pattern.compile(regex).matcher(passord).matches();
    }

    private boolean testEmail(String email) {
        try {
            //Følgende regex uttrykk er hentet fra https://www.baeldung.com/java-email-validation-regex
            String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

            return Pattern.compile(regex).matcher(email).matches();
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return getBrukernavn();
    }

}
