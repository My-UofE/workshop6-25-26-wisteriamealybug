[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/O4SUn0xn)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=22783158)

# Exceptions, `enum` Type, and Documentation

**NOTE**

This workshop should take about two hours to complete.



# 1. Unchecked exceptions 

## 1.1 Unchecked exceptions


Copy the following code into a new file `Unchecked1App.java`.

```java
public class Unchecked1App{
    public static void main(String args[]){
        System.out.println("### Enter main() ...");
        
        String s1 = null;
        String s2 = null;

        if (args.length > 0) {
            s1 = args[0];
        }

        s2 = s1.toUpperCase();
        
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);   

        System.out.println("### Exit main()!");
  } 
}
```

The program takes a string passed as a command line argument and convert it to upper case. 

Note how we have added messages at the start and end of our `main` method. As we will be experimenting with errors that crash the program, it enables us to see if the code runs to the end of the program successfully.

Compile the program and run it in ther terminal using command:

```bash
java Unchecked1App "hello world"
```

You should see the output:

```
### Enter main() ...
s1: hello world
s2: HELLO WORLD
### Exit main()!
```

Now run the program without passing in a string:

```bash
java Unchecked1App 
```

You should see it produces an error message stating that a `NullPointerException` has been raised, this is because `String` variable `s1` was initialised with `null` and if no command arguments are passed, will not reference a `String` object when line `s2 = s1.toUpperCase();` runs.

This is called an *unchecked* exception because it relates to an issue that has arisen which was not checked for during compilation of the source code.

The detail displayed is called the *stack trace* this shows where the exception was raised, e.g. in the stack trace below we see the exception raised and that it was raised by the `main` method of `Unchecked1App` on line 13.

```
java.lang.NullPointerException: Cannot invoke "String.toUpperCase()" because "<local1>" is null
        at Unchecked1App.main(Unchecked1App.java:13)
```

### Handling Exceptions

As covered in lectures if appropriate we can specify how to handle an exception, so that our program can continue to run.

We can do this with using a `try {} catch() {}` clause.

In this case we can add it to surround the code that will be affected by the exception:

```java
        try {
            s2 = s1.toUpperCase();
        } catch (NullPointerException e) {
            System.out.println("### Exception caught: " + e.getClass().getName()); 
            System.out.println("### Exception message: " + e.getMessage()); 
        }
```

When you rerun this code you should find that instead of ending when the exception is raised, it is handled and we see the code exits the `main` method.

Here we specified our `try-catch` clause to handle  the `NullPointerException` which is a subclass of the `Exception` class. Different classes exist for the different types of exceptions and it is good programming practise to try to anticipate the exception class that can occur and consider how each type should be handled.

**Notes** 

 1. In general it is good practise to minimise the amount of code within a `try-catch` structure (e.g. see this discussion: [StackOverFlow](https://stackoverflow.com/questions/19569766/how-much-code-to-put-in-try-catch-block)).

 2. In addition to getting the exception message using `getMessage()` it has a `toString()` method to display type and message, and you can manually display the stack trace of an exception using:

    ```java
    e.printStackTrace();
    ```

## 1.2 Call stack and exception propagation

Generally we will be working with code that is much more complex, and it is useful to consider how the java *call-stack* works and exceptions propagate

The following code is based on our earlier example but uses mutliple methods to model a more complex piece of code:

```java
public class Unchecked2App{
    public static void main(String args[]){
        System.out.println("### Enter main() ...");
        
        String s1 = null;

        if (args.length > 0) {
            s1 = args[0];
        }
        
        methodA(s1);
        System.out.println("### Exit main()!");
    }

    static void methodA(String s1){
        System.out.println("### Enter methodA() ...");
        methodB(s1);
        System.out.println("### Exit methodA()!");
    }

    static void methodB(String s1) {
        System.out.println("### Enter methodB() ...");
        String s2 = s1.toUpperCase();
        System.out.println("s1: " + s1);   
        System.out.println("s2: " + s2);   
        System.out.println("### Exit methodB()!");
    }
}
```

Compile the program and run it in ther terminal using command:

```bash
java Unchecked2App "hello world"
```

You should see the output:

```
### Enter main() ...
### Enter methodA() ...
### Enter methodB() ...
s1: hello world
s2: HELLO WORLD
### Exit methodB()!
### Exit methodA()!
### Exit main()!
```

From the enter/exit messages printed by the methods we can see how the program runs. During execution a typical application may involve many levels of method calls, which is managed by a so-called method call stack. 

(A stack is a last-in-first-out sequence.)

In the above example, as seen from the output, when running the program, the sequence of events are as follows:

   1. JVM invokes the `main()`.
   2. `main()` pushed onto the stack, it invokes `methodA()`.
   3. `methodA()` pushed onto the stack, it invokes `methodB()`.
   4. `methodB()` pushed onto the stack.
   5. `methodB()` completes and is popped out from the call stack.
   6. `methodA()` completes and is popped out from the call stack.
   7. `main()` completes and is popped out from the call stack.
   8. Program exits.

Now rerun without passing a string, you will find an exception is raised when it tries to convert the case to uppercase in `methodB()`.

The output and stack trace reported should be similar to this:

```bash
java Unchecked2App
```

```
### Enter main() ...
### Enter methodA() ...
### Enter methodB() ...
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.toUpperCase()" because "<parameter1>" is null
        at Unchecked2App.methodB(Unchecked2App.java:24)
        at Unchecked2App.methodA(Unchecked2App.java:18)
        at Unchecked2App.main(Unchecked2App.java:11)
```

It includes the exception type and we can see how the exception is propagated by showing the method call stack trace with line numbers. 

Here:

 1. `methodB()` triggers an `NullPointerException`. As it does not handle the exception, it popped off from the call stack immediately, 
 2. This means the exception propagates to `methodA()`. This does not handle this exception either, so it too is popped off the call stack. 
 3. The exception then propagates to `main()`, which in this case also fails to handle the exception, so is popped off the call stack.
 4. The `main()` method passes back to JVM, which  terminates the program and print the call stack trace.

In this case the exception propagates up through the call stack, and it is possible to catch and handle it at any of these stages i.e.

 - inside the `methodB()` method 
 - inside the `methodA()` method 
 - inside the `main()` method



Re-run the code for each option and compare what is printed on the screen.

Hint: `try-catch` in `methodB()` 
<details>
  <summary>expand to view</summary>

```java
static void methodB(String s1) {
        System.out.println("### Enter methodB() ...");
        try {
            String s2 = s1.toUpperCase();
        } catch (NullPointerException e) {
            ...
        }
        System.out.println("s1: " + s1);   
        System.out.println("s2: " + s2);   
        System.out.println("### Exit methodB()!");
    }
```
</details><br>

Hint: `try-catch` in `methodA()` 
<details>
  <summary>expand to view</summary>

```java
static void methodA(String s1){
        System.out.println("### Enter methodA() ...");
        try {
            methodB(s1);
        } catch (NullPointerException e) {
            ...
        }
        System.out.println("### Exit methodA()!");
    }
```
</details><br>

Hint: `try-catch` in `main()` 

<details>
  <summary>expand to view</summary>

```java
    ...
    try {
        methodA(s1);
    } catch (NullPointerException e) {
        ...
    }
    System.out.println("### Exit main()!");
```
</details><br>


## 2. Checked exceptions 

There are certain operations which may generate issues when a program is run. For example when reading a file it may be that the file cannot be located or when attempting to write to a file we might find it is locked by another process.

Java has been designed to ensure that such runtime errors are properly managed. It does this by checking during compilation that code auther has reported how the checked exceptions are managed:

There are two ways in which this can be done:

   1. *handle the exception* - the operation is enclosed by a `try-catch` clause, so the compiler can see that appropriate action will be taken to handle the exception.
   2. *declare the exception* - the `throws` keyword is used in the method definition to declare which exceptions may be thrown. This indicates that the appropriate action is to propagate the exception up the call stack, where it will be handled, or reach the JRM and terminate the program.

For the next exercise we need to create a text file to read. In the codespace add a new file called `javaOrigin.txt` and paste in the following content:

```
The first version of Java was written in 1991.
It was initially called Oak after an oak tree that 
stood outside one of the founders offices, but
later renamed to Java after the Indonesian coffee.
```

### 2.1 `Checked1App.java`

Now we are ready to write an app to read the file. Put the code below in a new file `Checked1App.java`:

```java
// import libraries from io (input/output)
// package to handle file streams
import java.io.*;

public class Checked1App{
    public static void main(String[] args)  {
        System.out.println("### Enter main() ...");

        // get filename as specified in the command argument
        String fileName = args[0];

        // read and print the file contents
        readTxtFile(fileName);
        
        // reached end of the file
        System.out.println("### Exit main()!");
    }
    public static void readTxtFile(String fileName)  {
        System.out.println("### Enter readTxtFile() ...");
    
        // open a file for reading and pass to a buffer 
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        
        // define a variable which will store the lines as we load them
        String line;
    
        // loop to read and print lines until file end
        line = String.format("Contents of '%s'", fileName);
        while(line!=null) {
            System.out.println(line);
            line = bufferedReader.readLine();
        } 
        
        // close buffer/file io stream
        bufferedReader.close();
        System.out.println("### Exit readTxtFile()!");

    }
}
```

When you compile the code it will fail because the code includes operations for opening, reading and closing a file buffer that trigger compiler checks that the assciociated `FileNotFoundException` and `IOException` exceptions are declared/handled (and these are not yet included).

```
Checked1App.java:22: error: unreported exception FileNotFoundException; must be caught or declared to be thrown
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
                                                           ^
Checked1App.java:31: error: unreported exception IOException; must be caught or declared to be thrown
            line = bufferedReader.readLine();
                                          ^
Checked1App.java:35: error: unreported exception IOException; must be caught or declared to be thrown
        bufferedReader.close();
                            ^
3 errors
```

We can compile the program without writing code to handle the exceptions by declaring the exceptions in the method definitions:

```java
public static void readTxtFile(String fileName) throws IOException {
    ...
}
```

In the above code we do not need to explicitly declare `readTxtFile` may 
throw a `FileNotFoundException` separately because it is a subclass of `IOException`.

You will find that the compile error changes:

```
Checked1App.java:13: error: unreported exception IOException; must be caught or declared to be thrown
        readTxtFile(fileName);
                   ^
1 error
```

The compiler checks now pass for compilation of the `readTxtFile()` method but the exception it throws will then propagate to the `main()` method that calls it. In order for this to pass the compiler checks it must also handle or declare the exception.

Add `throws IOException` to the `main()` method and verify the code will now compile.

Run the code passing the file name `javaOrigin.txt`

```bash
java Checked1App javaOrigin.txt
```

Now run it with a file that does not exist, and check the behaviour is in line with what you expect.

```bash
java Checked1App doesnotexist.txt
```

### 2.2 `Checked2App.java`

We can also handle the exception. Copy your code into a new file called `Checked2App.java` and update the class name.

We can handle the exception in the `main()` method, e.g.

```java
try {
    readTxtFile(fileName);
} catch (FileNotFoundException e) {
    System.out.println("### FileNotFoundException caught"); 
    System.out.println("### message: " + e.getMessage()); 
} catch (IOException e) {
    System.out.println("### IOException caught"); 
    System.out.println("### message: " + e.getMessage()); 
}
```

Here we list both exception types separately. This is because a catch block catching a specific exception class can also catch its sub- classes. 

`catch(Exception e) {...}` would catch *all* kinds of exceptions. However, this is not a good practice as the exception handler that is too general may unintentionally catches some subclasses’ exceptions it does not intend to.

In the above code, the separate `catch` clauses enable us to take different action dependent on each exception case separately.

Check that the file compiles and runs (test with both `javaOrigin.txt` and a non-existing filename). 

**Questions**

1. Are both `throws` declarations still needed? Experiment and try removing each in turn if you are not sure.

    <details> 
    <summary>Answer</summary>

    As the exception is handled by the `main()` method we can remove its `throws` declaration. However we still need it for `readTxtFile()` to declare that it will throw the exceptions rather than handle them.
    </details><br>

2. What happens if you change the order of the `catch` blocks, so that `IOException` comes first? (you should see the exception raised for a non-existing file changes - if you are not sure why ask one of the teaching staff)

    <details> 
    <summary>Answer</summary>

    The order of `catch`-blocks is important. 
    
    A subclass must be caught before its superclass. Otherwise you will receive a compilation error that the exception has already been caught.
    
    If you read the documentation of the `FileNotFoundException` class, you will find it is a subclass of `IOException`. 
    
    Therefore the `catch` block for the `FileNotFoundException` must be placed before the more general `IOException`.
</details><br>


### 2.3 `Checked3App.java`

Now copy your `Checked1App.java` code into a new file `Checked3App.java` (remember to update the corresponding class name). 

In this file place the `try-catch` clause within the `readTxtFile()` method. 

**Hint** In this case put all the code that can raise an exception in a single `try` code block, with two `catch` statements as above.

**Question**

When the `readTxtFile()` method handles the exceptions, which of the `throw` declarations are still needed?

Check your understanding by testing how happens when they are removed.

 <details> 
    <summary>Answer</summary>

Both declarations can be removed because the exception is handled and will not propagate through the call stack.

</details><br>

## 3. Rock Paper Scissors

### 3.1 `RPSApp.java`

In the final task for this workshop you should implement a program in Java to play Rock-Paper-Scissors game against the computer.

You should define an enum type for the three hand signs.

```java
enum HandSign {
    ROCK, 
    PAPER,
    SCISSORS 
}
```

The user should be able to enter:

 - `s` or `S` to choose SCISSORS 
 - `p` or `P` to choose PAPER
 - `r` or `R` to choose ROCK
 - `q` or `Q` to quit the program

After the user has selected their choice the  computer player will select rock, paper or scissors at random. 

The program should keep track of the score, and continue playing rounds until the user enters `q` to quit.


Start by using the template structure for the program below, and write additional code according to the comments to complete the functionality.

```java
import java.util.Scanner;
import java.util.Random;

enum HandSign {
    ROCK, 
    PAPER,
    SCISSORS 
}

public class RPSApp {
    /**
     * Get the computer’s move (randomly generated)
     */
    public static HandSign getComputerMove(){
        Random rd = new Random();
        int n = rd.nextInt(3); // n will be a random number in {0,1,2}
        
        HandSign computerMove = null; 

        // code using n to select
        // a HandSign

        return computerMove;
    }

    /**
     * Get the player move from the keyboard input
     */
    public static HandSign getPlayerMove(){
        // The Scanner class is used to get the keyboard input
        Scanner in = new Scanner(System.in);
        // Use a variable to tag if the input is valid 
        // (one of the characters {s,S,p,P,r,R,q,Q}) or not
        boolean validInput = true;
        HandSign playerHandSign = null;
        do {// repeat until valid input

            // Add your code to give some description about what input the
            //  users are supposed to give
            System.out.println("");

            // convert the input string into a char type
            char inChar = in.next().toLowerCase().charAt(0);

            // Add your code to output the player’s hand sign. A
            //switch-statement is a good choice.
        
            ...


        } while(!validInput);
        
        return playerHandSign;

      }

    /**
     * Check who wins
     *
     * @param h1 the first hand sign
     * @param h2 the second hand sign
     * @return 0 if two signs equal, 
     *        -1 if the second sign wins, 
     *         1 if the first sign wins
     *
     */
    public static int whoWins(HandSign h1, HandSign h2){
         ...
    }
    
    /**
     * The main method
     */
    public static void main(String[] args) {
        int playerScore = 0;
        int computerScore = 0;

        HandSign playerMove;// player’s sign from keyboard
        HandSign computerMove;// computer’s random sign

        int checkwin;
        boolean gameOver = false;
        while (!gameOver){
            // repeat this process till the user quits
            
            //Step1: Get the player move from the keyboard input

            //Step2: Get the computer’s move (randomly generated)

            //Step3: Check who wins

            //Step4: Output who played what and who won the round

            //Step5: Update and print player/computer scores

        }
    }
}
```

## 4. Generating Documentation

The `javadoc` tool allows us to automatically generate a set of Java documentation for a project from the source code. 

To generate the documentation classes and methods must be annotated using a comment black that preceeds the definition.

The format of the comment block will be covered in the module lectures but we can see an example in the code above:

```java
/**
 * Check who wins
 *
 * @param h1 the first hand sign
 * @param h2 the second hand sign
 * @return 0 if two signs equal, 
 *        -1 if the second sign wins, 
 *         1 if the first sign wins
 *
 */
public static int whoWins(HandSign h1, HandSign h2){
        ...
}
```

The top line of the comment block describes what the method does, and then additional `@param` and `@return` provide information on the inputs and output of the methods.

You can find further guidance on `javadoc` in web tutorials e.g. [dummies.com: Using javadoc to document your classes](https://www.dummies.com/article/technology/programming-web-design/java/how-to-use-javadoc-to-document-your-classes-153265/)

**TASK 4.1**

Select one of the programs you have written above (for example, `RPSApp.java`) and generate the java documentation for it using terminal commands:

First make a directory to store the output (you only need to run this once).

```
mkdir docs
```

Next use the `javadoc` tool to create the documentation:

```
javadoc RPSApp.java -d docs
```

To view the documentation it is easiest to open in a web browser on our computer.

Zip up the documents folder using the commands below:

```
cd docs
zip ../docs.zip .
cd ..
```

In case you are unfamiliar with (linux) shell commands this does the following:

(i) change directory to go into the `docs` directory. 

(ii) create an archive in parent folder called `docs.zip` and add all files in current location

(iii) change directory back into parent directory (back to worspace)

You can now download the `docs.zip` file from the CodeSpace (right click on file in files panel) and unzip and browed the html documentation which will open on your computer.
