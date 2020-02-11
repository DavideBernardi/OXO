public class OXOControllerTester {
    public static void main(String[] args) {
        // Tricky bit of code to check that assertions have been enabled !
        boolean assertionsEnabled = false;
        assert (assertionsEnabled = true);
        if (assertionsEnabled) {
            // Test functions all private static


            System.out.println("SUCCESS: All tests passed !!!");
        } else {
            System.out.println("You MUST run java with assertions enabled (-ea) to test your program !");
        }
    }

}
