package wci.backend;

import wci.backend.compiler.CodeGenerator;
import wci.backend.interpreter.Executor;

/**
 * <h1>BackendFactory</h1>
 *
 * <p>A factory class that creates compiler and interpreter components.</p>
 */
public class BackendFactory {

    public static Backend createBackend(String operation) throws Exception {
        if (operation.equalsIgnoreCase("compile")) {
            return new CodeGenerator();
        } else if (operation.equalsIgnoreCase("execute")) {
            return new Executor();
        } else {
            throw new Exception("Backend factory: Invalid operation '" + operation + "'");
        }
    };
}
