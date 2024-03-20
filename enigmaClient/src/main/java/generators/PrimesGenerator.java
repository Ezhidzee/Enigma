package generators;

import java.io.*;

public class PrimesGenerator {
    public static Long getPrime() throws IOException {
        FileInputStream fstream = new FileInputStream("C:/Users/atrem/IdeaProjects/Enigma/enigmaClient/SophieGermainPrimes.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String sequenceLine = br.readLine();
        String[] sequence = sequenceLine.split(" ");
        return Long.parseLong(sequence[((int) (Math.random() * 1000000000) % sequence.length)]);
    }
}
