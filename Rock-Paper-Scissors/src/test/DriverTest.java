package test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DriverTest {
    public static void main(String[] args) {
        testMainExecutesWithoutCrashing();
        // Silently exits if all tests pass
    }

    private static void testMainExecutesWithoutCrashing() {
        InputStream originalSystemIn = System.in;
        
        try {
            // Simulate typing "Devarsh", hitting Enter, then typing "1"
            String simulatedUserInput = "Devarsh\n1\n";
            ByteArrayInputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
            System.setIn(in); 

            // Run the main method
            Driver.main(new String[]{}); 
            
        } catch (Exception e) {
            // If any exception happens, the assertion fails
            assert false : "Driver.main() threw an exception: " + e.getMessage();
        } finally {
            // CRITICAL: Always restore standard input!
            System.setIn(originalSystemIn);
        }
    }
}
