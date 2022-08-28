package xyz.edge.ac.util.discord;

private class Field
{
    private final String name;
    private final String value;
    private final boolean inline;
    
    private Field(final String name, final String value, final boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }
    
    private String getName() {
        return this.name;
    }
    
    private String getValue() {
        return this.value;
    }
    
    private boolean isInline() {
        return this.inline;
    }
}
