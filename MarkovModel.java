/* *****************************************************************************
 *  Name:    Richard Ma
 *  NetID:   rm44
 *  Precept: P03
 *
 *
 *  Description: Generates a Markov model and includes methods for accessing and
 *  manipulating Markov models.
 *
 **************************************************************************** */

public class MarkovModel {

    private String kgram; // the k-gram
    private final int chunk; // length of k-gram
    private int textIndex; // index of text to look at kgrams
    private int howMany; // number of times kgram occurs
    private char nextChar; // what follows k-gram
    private int charConvert; // character to ascii
    private int span; // span of k-gram

    // st of kgrams and their frequencies
    private final ST<String, Integer> kGrams = new ST<String, Integer>();

    private int overlap; // measures how much kgram needs to circle back
    private static final int ASCII = 128; // constant

    // st of kgrams and characters that follow kgram
    private final ST<String, int[]> kchar = new ST<String, int[]>();

    // creates a Markov model of order k for the specified text
    public MarkovModel(String text, int k) {

        chunk = k;
        textIndex = 0;
        span = textIndex + k;

        for (int i = 0; i < text.length(); i++) {
            // take textIndex, add length k
            // if it goes past stringlength, we need special circular case
            if (span < text.length()) {
                kgram = text.substring(textIndex, span); // find k-gram

                // find character after k-gram

                nextChar = text.charAt(span);

            }
            else { // circle back
                kgram = text.substring(textIndex);
                overlap = span - text.length();
                kgram += text.substring(0, overlap);

                // find next character
                nextChar = text.charAt(overlap);
            }

            // put kgram into first symbol table
            if (!kGrams.contains(kgram)) {
                howMany = 1;
                kGrams.put(kgram, howMany); // put kgram in
            }
            else {
                howMany = kGrams.get(kgram);
                kGrams.put(kgram, howMany + 1);
            }

            // assigns to each kgram an array of characters to keep track
            // of what comes next
            charConvert = (int) nextChar;
            if (!kchar.contains(kgram)) {
                kchar.put(kgram, new int[ASCII]);
                kchar.get(kgram)[charConvert]++;
            }
            else
                kchar.get(kgram)[charConvert]++;

            textIndex++;
            span = textIndex + k;
        }
    }

    // returns the order k of this Markov model
    public int order() {
        return chunk;
    }

    // returns a string representation of the Markov model
    public String toString() {

        String result = "";
        for (String key : kchar.keys()) {
            result += key + ": ";

            // get the character frequency array
            int[] frequency = kchar.get(key);

            // for each non-zero entry, append the character and the frequency
            for (int i = 0; i < frequency.length; i++) {
                if (frequency[i] != 0) {
                    result += (char) i + " " + frequency[i] + " ";
                }
            }

            // append a newline character
            result += "\n";
        }
        return result;
    }

    // returns the number of times the specified kgram appears in the text
    public int freq(String kgram) {
        if (kgram.length() != chunk)
            throw new IllegalArgumentException("kgram is not length k");
        if (!kGrams.contains(kgram))
            return 0;

        return kGrams.get(kgram);
    }

    // returns the number of times the character c follows the specified
    // kgram in the text
    public int freq(String kgram, char c) {
        if (kgram.length() != chunk)
            throw new IllegalArgumentException("kgram is not length k");
        if (!kGrams.contains(kgram))
            return 0;

        return kchar.get(kgram)[(int) c];
    }

    // returns a random character that follows the specified kgram in the text,
    // chosen with weight proportional to the number of times that character
    // follows the specified kgram in the text
    public char random(String kgram) {
        if (kgram.length() != chunk)
            throw new IllegalArgumentException("kgram is not length k");
        if (!kGrams.contains(kgram))
            throw new IllegalArgumentException("This is not a valid kgram!");

        int n = StdRandom.discrete((kchar.get(kgram)));
        return (char) n;
    }

    // tests this class by directly calling all instance methods
    public static void main(String[] args) {
        MarkovModel text = new MarkovModel("hellociaonihaoaloha", 2);
        StdOut.print(text.order());
        StdOut.print(text.toString());
        StdOut.print(text.freq("ha"));
        StdOut.print(text.freq("ha", 'o'));
        StdOut.print(text.random("ha"));

        String text1 = "banana";
        MarkovModel model1 = new MarkovModel(text1, 2);
        StdOut.println("freq(\"an\", 'a')    = " + model1.freq("an", 'a'));
        StdOut.println("freq(\"na\", 'b')    = " + model1.freq("na", 'b'));
        StdOut.println("freq(\"na\", 'a')    = " + model1.freq("na", 'a'));
        StdOut.println("freq(\"na\")         = " + model1.freq("na"));
        StdOut.println();

        String text3 = "one fish two fish red fish blue fish";
        MarkovModel model3 = new MarkovModel(text3, 4);
        StdOut.println("freq(\"ish \", 'r') = " + model3.freq("ish ", 'r'));
        StdOut.println("freq(\"ish \", 'x') = " + model3.freq("ish ", 'x'));
        StdOut.println("freq(\"ish \")      = " + model3.freq("ish "));
        StdOut.println("freq(\"tuna\")      = " + model3.freq("tuna"));

        for (int i = 0; i < 20; i++) {
            StdOut.print(model3.random("sh"));
        }
    }
}
