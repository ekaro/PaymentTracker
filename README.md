# Payment Tracker

Program that keeps a record of payments. Each payment includes a currency and an amount. Additionally each currency might include USD exchange rate.

The program outputs a list of all the currency and amounts to the console once per minute. Currencies with 0 amount will not be output. Optionally inputs can be loaded from a file when starting up.

### Prerequisites

Make sure you have JDK installed and added to path. For example:

```
set path=%path%;C:\Program Files\Java\jdk-9.0.4\bin
```

### Running from command line

1. Clone project from repository: https://github.com/ekaro/PaymentTracker.git

2. Navigate to cloned project directory and then to "src\main\java\pt". 

3. Compile the program by runing:

```
javac App.java
```

4. Run the program from previous directory:

```
cd ..
```

```
java pt.App
```

5. Optionally you can run the program with file specified:

```
java pt.App pt/payments.txt
```

### Assumptions

Input must be entered in the following format: 'EUR 1000 1.56'

Input must have 2 or 3 elements divided by space character. Third element - rate is optional. 

Currency may be any uppercase 3 letter code.

Amount must be positive or negative integer number.

Rate must be positive decimal number.

If you input "quit" program will close.
