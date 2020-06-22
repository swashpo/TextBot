// Description: Uses MarkovModel to generate predictive text.


public class TextGenerator {
    public static void main(String[] args) {

        int k = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        String text = StdIn.readAll();

        MarkovModel model = new MarkovModel(text, k);

        String kgram = text.substring(0, k);
        StdOut.print(kgram);

        for (int i = 0; i < T - k; i++) {
            // generate next letter and print it
            char result = model.random(kgram);
            StdOut.print(result);

            // append that letter and remove first letter
            kgram += result;
            kgram = kgram.substring(1);
        }
    }
}
