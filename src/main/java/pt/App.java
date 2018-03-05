package pt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Payment Tracker
 *
 */

public class App
{
    // main hash map for storing payments
    private static Map<String, List<Object>> payments = new LinkedHashMap<String, List<Object>>();

    public static void main(String[] args) {
        System.out.println("\nWelcome to Payment Tracker.");

        if (args.length > 1) {
            System.out.println("\nInvalid number of arguments. You can input only one argument - filename.");
        }

        if (args.length == 1) {
            String file = args[0];
            ReadFile(file);    // read file if it is specified in arguments
        }

        ProcessPayments();     // input and output of payments
    }

    private static void ProcessPayments() {
        OutputPayments(payments);              // output payments from HashMap

        Scanner sc = new Scanner(System.in);
        while (true) {
            String payment = sc.nextLine();
            CheckPayment(payment);             // read next input in command line and check if it is valid payment
        }
    }

    private static void CheckPayment(String str) {
        String[] split = str.split(" ");    // split input string based on space delimiter
        String key = split[0];
        if (split.length == 1) {
            if (key.equals("quit")) {
                System.exit(0);             // if user types quit, program closes
            }
        }

        // if one of these assumptions is true, program will output error message
        if ((split.length == 1) ||            // there can't be only one element in array unless it's quit
                (split.length > 3) ||         // there can't be more than 3 elements in array
                CheckCode(split[0]) ||        // first element of array must be 3 upper case letters
                CheckAmount(split[1]) ||      // second element of array must be a positive or negative number
                CheckRate(split)) {           // third element of array must be a float
            System.out.println("Wrong input. Please enter payment in the requiring format. For example: \'EUR 1000 1.56\' ");
        } else {
            AddPayment(split);            // if all assumptions are correct, add payment to the payments map
        }
    }

    private static boolean CheckCode(String code) {
        int len = code.length();
        boolean notCode = false;

        if (len != 3) {
            notCode = true;
        } else {
            for (int i = 0; i < len; i++) {
                char ch = code.charAt(i);
                if (!(ch >= 'A' && ch <= 'Z')) {  // check if every char in code is upper case letter
                    notCode = true;
                }
            }
        }
        return notCode;
    }

    private static boolean CheckAmount(String amount) {
        int len = amount.length();
        boolean notNum = false;

        // check every char in amount string
        for (int i = 0; i < len; i++) {
            char ch = amount.charAt(i);
            if (i == 0) {
                if (!(ch >= '0' && ch <= '9') && !(ch == '-')) {
                    notNum = true;
                }
            } else {
                if (!(ch >= '0' && ch <= '9')) {
                    notNum = true;
                }
            }
        }
        return notNum;
    }

    private static boolean CheckRate(String[] str) {
        boolean notFloat = false;

        if (str.length == 3) {
            String rate = str[2];
            int len = rate.length();

            // check every char in rate string
            for (int i = 0; i < len; i++) {
                char ch = rate.charAt(i);
                if ((i == 0) || (i == (len - 1))) {
                    if (!(ch >= '0' && ch <= '9')) {
                        notFloat = true;
                    }
                } else {
                    if (!(ch >= '0' && ch <= '9') && !(ch == '.')) {
                        notFloat = true;
                    }
                }
            }
        }
        return notFloat;
    }

    private static void ReadFile(String str) {
        File file = new File(str);

        // try to read lines from file
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                CheckPayment(line);
            }
            System.out.println("\nFile " + str + " successfully loaded.");
        } catch (FileNotFoundException e) {
            System.out.println("\nFile " + str + " not found.");
        }
    }

    private static void AddPayment(String[] str) {
        String key = str[0];
        List<Object> values = new ArrayList<Object>();
        int amount = Integer.parseInt(String.valueOf(str[1]));
        float rate = 0;

        if (str.length == 3) {
            if (key.equals("USD")) {
                System.out.println("Wrong input. You can't specify USD exchange rate for USD!");
            } else {
                rate = Float.parseFloat(str[2]);
            }
        }
        // add amount and rate to values list
        values.add(amount);
        values.add(rate);

        if (!payments.containsKey(key)) {
            payments.put(key, values);          // add new currency to payments map
        } else {
            Object CurrentAmount = payments.get(key).get(0);
            int add = Integer.parseInt(String.valueOf(CurrentAmount)) +
                    Integer.parseInt(String.valueOf(values.get(0)));     // add amount to current amount
            values.set(0, add);
            payments.put(key, values);
        }
    }

    private static void OutputPayments(final Map<String, List<Object>> map) {
        TimerTask output = new TimerTask() {

            @Override
            public void run() {
                System.out.println();
                Set<String> keys = map.keySet();

                for (String key : keys) {
                    List<Object> values = map.get(key);      // get values for every currency in payments map
                    int amount = Integer.parseInt(String.valueOf(values.get(0)));
                    float rate = Float.parseFloat(String.valueOf(values.get(1)));

                    // display current amount of currency if it is not 0
                    if (amount != 0) {
                        if ((rate != 0) && (!key.equals("USD"))) {
                            float converted = amount / rate;                             // convert to USD
                            float rounded = (float) Math.round(converted * 100) / 100;   // round to two decimal places
                            System.out.println(key + " " + amount + " (USD " + rounded + ")");
                        } else {
                            System.out.println(key + " " + amount);
                        }
                    }
                }
            }
        };

        // display currency amounts once per minute
        Timer timer = new Timer();
        timer.schedule(output, 0, 60000);
    }
}