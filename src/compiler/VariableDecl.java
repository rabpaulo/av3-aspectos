package compiler;

public class VariableDecl {
    public final String name;
    public final String initType; // may be null
    public final boolean hasInit;
    public VariableDecl(String name, String initType, boolean hasInit) {
        this.name = name;
        this.initType = initType;
        this.hasInit = hasInit;
    }
    @Override
    public String toString() { return name + (hasInit ? (" = (" + initType + ")") : ""); }
}
