package compiler;

public class Parameter {
    public final String name;
    public final String type;
    public Parameter(String name, String type) {
        this.name = name;
        this.type = type;
    }
    @Override
    public String toString() { return type + " " + name; }
}
