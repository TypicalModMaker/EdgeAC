package xyz.edge.ac.util.discord;

private class Footer
{
    private final String text;
    private final String iconUrl;
    
    private Footer(final String text, final String iconUrl) {
        this.text = text;
        this.iconUrl = iconUrl;
    }
    
    private String getText() {
        return this.text;
    }
    
    private String getIconUrl() {
        return this.iconUrl;
    }
}
