import java.util.concurrent.TimeUnit;
import java.time.Instant;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class ClipboardMode {
    public static void main(String[] args) throws Exception {
        String clipboard;
        print("" + Instant.now() + ": Setting clipboard to empty.");
        setClipboard("");
        print("" + Instant.now() + ": Getting clipboard value.");

        while (true) {
            TimeUnit.MILLISECONDS.sleep(400);
            clipboard = getClipboard();

            switch (clipboard.split("\\R")[0]) {
                case "// #simai plus on":
                    print("" + Instant.now() + ": Found simai plus data.");
                    print("" + Instant.now() + ": Setting clipboard to converted simai data.");
                    setClipboard(Program.convertSimai(clipboard));
                    print("" + Instant.now() + ": Getting clipboard value.");
                    break;
                case "// #simai plus off":
                    breakpoint("" + Instant.now() + ": Program has been terminated.");
                    break;
            }
        }
    }

    public static String getClipboard() throws Exception {
        return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    }

    public static void setClipboard(String input) {
        StringSelection selection = new StringSelection(input);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    }

    public static void print(String message) {
        System.out.println(message);
    }

    public static void breakpoint(String message) {
        print(message);
        System.exit(0);
    }
}
