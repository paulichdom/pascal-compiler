package wci;

import java.io.BufferedReader;
import java.io.FileReader;

import wci.frontend.*;
import wci.intermediate.*;
import wci.backend.*;
import wci.message.*;

import static wci.message.MessageType.*;

/**
 * <h1>Pascal</h1>
 *
 * <p>Compile or interpret a Pascal source program.</p>
 */
public class Pascal {

    private Parser parser;   // language-independent parser
    private Source source;   // language-independent scanner
    private ICode iCode;     // generated intermediate code
    private SymTab symTab;   // generated symbol table
    private Backend backend; // backend

    /**
     * Compile or interpret a Pascal source program.
     * @param operation either "compile" or "execute".
     * @param filePath the source file path.
     * @param flags the command line flags.
     */
    public Pascal(String operation, String filePath, String flags) {
        try {
            boolean intermediate = flags.indexOf('i') > -1;
            boolean xref         = flags.indexOf('x') > -1;

            source = new Source(new BufferedReader(new FileReader(filePath)));
            source.addMessageListener(new SourceMessageListener());

            parser = FrontendFactory.createParser("Pascal", "top-down", source);
            parser.addMessageListener(new ParserMessageListener());

            backend = BackendFactory.createBackend(operation);
            backend.addMessageListener(new BackendMessageListener());

            parser.parse();
            source.close();

            iCode = parser.getICode();
        } catch (Exception ex) {
            System.out.println("***** Internal translator error. *****");
            ex.printStackTrace();
        }
    }

    private static final String FLAGS = "[-ix]";
    private static final String USAGE = "Usage: Pascal execute|compile " + FLAGS + " <source file path>";

    public static void main(String[] args) {
        try {
            String operation = args[0];

            // Operation.
            if(!(operation.equalsIgnoreCase("compile")
                || operation.equalsIgnoreCase("execute"))) {
                throw new Exception();
            }

            int i = 0;
            String flags = "";

            // Flags.
            while ((++i < args.length) && (args[i].charAt(0) == "-")) {
                flags += args[i].substring(1);
            };

            // Source path.
            if(i < args.length) {
                String path = args[i];
                new Pascal(operation, path, flags);
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println(USAGE);
        }
    }

    private static final String SOURCE_LINE_FORMAT = "%03d %s";

    /**
     * Listener for source messages.
     */
    private class SourceMessageListener implements MessageListener {
        /**
         * Called by the source whenever it produces a message.
         * @param message the message.
         */
        public void messageReceived(Message message) {
            MessageType type = message.getType();
            Object[] body = (Object[]) message.getBody();

            switch (type) {
                case SOURCE_LINE: {
                    int lineNumber = (Integer) body[0];
                    String lineText = (String) body[1];

                    System.out.println(String.format(SOURCE_LINE_FORMAT, lineNumber, lineText));

                    break;
                }
            }
        }
    }

    private static final String PARSER_SUMMARY_FORMAT =
            "\n%,20d source lines." +
            "\n% 20d syntax errors." +
            "\n%,20.2f seconds total parsing time. \n";
}
