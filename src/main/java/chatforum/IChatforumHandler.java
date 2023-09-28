package chatforum;

import java.io.FileNotFoundException;

public interface IChatforumHandler {
    
    public void lagreProgram(Chatforum chatforum, String filename) throws FileNotFoundException;
    public Chatforum skrivProgram(String filename) throws FileNotFoundException;

}
