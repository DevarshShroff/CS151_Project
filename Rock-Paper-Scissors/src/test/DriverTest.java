import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DriverTest {
    public static void main(String[] args) {
        testMainExecutesWithRandomMode();
        testMainExecutesWithMachineLearningMode();
        // Silently exits if all tests pass
    }

    private static void testMainExecutesWithRandomMode() {
        InputStream originalSystemIn = System.in;

        try {
            String simulatedUserInput = buildGameInput();
            ByteArrayInputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
            System.setIn(in);

            Driver.main(new String[] { "-r" });

        } catch (Exception e) {
            assert false : "Driver.main(-r) threw an exception: " + e.getMessage();
        } finally {
            System.setIn(originalSystemIn);
        }
    }

    private static void testMainExecutesWithMachineLearningMode() {
        InputStream originalSystemIn = System.in;

        try {
            String simulatedUserInput = buildGameInput();
            ByteArrayInputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
            System.setIn(in);

            Driver.main(new String[] { "-m" });

        } catch (Exception e) {
            assert false : "Driver.main(-m) threw an exception: " + e.getMessage();
        } finally {
            System.setIn(originalSystemIn);
        }
    }

    private static String buildGameInput() {
        StringBuilder builder = new StringBuilder("Devarsh\n");

        for (int i = 0; i < 20; i++) {
            builder.append("1\n");
        }

        return builder.toString();
    }
}
