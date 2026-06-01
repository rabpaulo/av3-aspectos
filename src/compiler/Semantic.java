package compiler;

import java.util.*;

public class Semantic {
    public static Semantic instance = new Semantic();

    private Deque<Map<String,String>> scopes = new ArrayDeque<>();
    private Map<String, FunctionInfo> functions = new HashMap<>();
    private Deque<String> functionReturnTypes = new ArrayDeque<>();
    private boolean hasErrors = false;

    public Semantic() {
        enterScope(); // global scope
    }

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (scopes.isEmpty()) {
            error("Internal: exitScope with empty stack");
            return;
        }
        scopes.pop();
    }

    public void enterFunction(String returnType) {
        functionReturnTypes.push(returnType);
        enterScope();
    }

    public void exitFunction() {
        exitScope();
        if (!functionReturnTypes.isEmpty()) functionReturnTypes.pop();
    }

    public String currentFunctionReturnType() { return functionReturnTypes.peek(); }

    public void declareVariable(String name, String type) {
        Map<String,String> cur = scopes.peek();
        if (cur.containsKey(name)) {
            error("Redeclaration of variable: " + name);
        } else {
            cur.put(name, type);
        }
    }

    public String lookupVariable(String name) {
        for (Map<String,String> m : scopes) {
            if (m.containsKey(name)) return m.get(name);
        }
        return null;
    }

    public void declareFunction(String name, String returnType, List<Parameter> params) {
        if (functions.containsKey(name)) {
            error("Redeclaration of function: " + name);
            return;
        }
        List<String> ptypes = new ArrayList<>();
        if (params != null) {
            for (Parameter p : params) ptypes.add(p.type);
        }
        functions.put(name, new FunctionInfo(returnType, ptypes));
    }

    public FunctionInfo lookupFunction(String name) {
        return functions.get(name);
    }

    public void checkAssignment(String varName, String exprType) {
        String vartype = lookupVariable(varName);
        if (vartype == null) {
            error("Assignment to undeclared variable: " + varName);
            return;
        }
        if (exprType == null) {
            error("Assignment of unknown expression to " + varName);
            return;
        }
        if (!isCompatible(vartype, exprType)) {
            error("Type mismatch assigning " + exprType + " to " + vartype + " for variable " + varName);
        }
    }

    public boolean isCompatible(String lhs, String rhs) {
        if (lhs == null || rhs == null) return false;
        if (lhs.equals(rhs)) return true;
        if (lhs.equals("float") && rhs.equals("int")) return true; // allow promotion int->float
        return false;
    }

    public String arithmeticType(String a, String b) {
        if (a == null || b == null) return null;
        if (a.equals("float") || b.equals("float")) return "float";
        if (a.equals("int") && b.equals("int")) return "int";
        return null;
    }

    public void error(String msg) { System.err.println("[Semantic Error] " + msg); hasErrors = true; }
    public boolean hasErrors() { return hasErrors; }

    public static class FunctionInfo {
        public final String returnType;
        public final List<String> paramTypes;
        public FunctionInfo(String r, List<String> p) { this.returnType = r; this.paramTypes = p; }
    }
}
