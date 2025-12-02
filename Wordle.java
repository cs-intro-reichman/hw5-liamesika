public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {

        In in = new In(filename); // read the file dictionary

        String all_dictionary = in.readAll();
        String[] words = all_dictionary.split("\\s+");

        return words;
    }

    // Choose a random secret word from the dictionary.
    // Hint: Pick a random index between 0 and dict.length (not including) using Math.random()
    public static String chooseSecretWord(String[] dict) {
        int dict_length = dict.length;         // the number of words in the dict
        // index should be in [0, dict_length - 1]
        int index = (int) (Math.random() * dict_length);
        String the_word = dict[index];         // return the word at that index

        return the_word;
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise false.
    public static boolean containsChar(String secret, char c) {

        int the_len = secret.length();
        for (int i = 0; i < the_len; i++) {
            char the_letter = secret.charAt(i);
            if (c == the_letter) {
                return true;
            }
        }
        return false;
    }

    // Compute feedback for a single guess into resultRow.
    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
        int len = secret.length();

        // Assume secret and guess have the same length as resultRow
        for (int j = 0; j < len; j++) {
            char guessChar = guess.charAt(j);
            char secretChar = secret.charAt(j);

            if (guessChar == secretChar) {
                resultRow[j] = 'G';                       // exact match
            } else if (containsChar(secret, guessChar)) {
                resultRow[j] = 'Y';                       // letter exists elsewhere
            } else {
                resultRow[j] = '_';                       // not in word
            }
        }
    }

    // Store guess string (chars) into the given row of guesses 2D array.
    public static void storeGuess(String guess, char[][] guesses, int row) {
        int the_len = guess.length();
        for (int i = 0; i < the_len; i++) {
            guesses[row][i] = guess.charAt(i);
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
        int the_len = resultRow.length;
        for (int i = 0; i < the_len; i++) {
            if (resultRow[i] != 'G') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;

        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);

        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[MAX_ATTEMPTS][WORD_LENGTH];
        char[][] results = new char[MAX_ATTEMPTS][WORD_LENGTH];

        // Prepare to read from the standard input
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            // Loop until you read a valid guess
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                guess = inp.readString();

                if (guess == null || guess.length() != WORD_LENGTH) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }

            // Store guess and compute feedback
            storeGuess(guess, guesses, attempt);
            computeFeedback(secret, guess, results[attempt]);

            // Print board
            printBoard(guesses, results, attempt);

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        if (!won) {
            System.out.println("Sorry, you did not guess the word");
            System.out.println("The secret word was: " + secret);
        }

        inp.close();
    }
}
