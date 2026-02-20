
// import libraries from io (input/output)
// package to handle file streams
import java.io.*;

public class Checked1App {
    public static void main(String[] args) throws IOException {
        System.out.println("### Enter main() ...");

        // get filename as specified in the command argument
        String fileName = args[0];

        // read and print the file contents
        readTxtFile(fileName);

        // reached end of the file
        System.out.println("### Exit main()!");
    }

    public static void readTxtFile(String fileName) throws IOException {
        System.out.println("### Enter readTxtFile() ...");

        // open a file for reading and pass to a buffer
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

        // define a variable which will store the lines as we load them
        String line;

        // loop to read and print lines until file end
        line = String.format("Contents of '%s'", fileName);
        while (line != null) {
            System.out.println(line);
            line = bufferedReader.readLine();
        }

        // close buffer/file io stream
        bufferedReader.close();
        System.out.println("### Exit readTxtFile()!");

    }
}
