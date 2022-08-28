package xyz.edge.ac.util.discord;

private class Author
{
    private final String name;
    private final String url;
    private final String iconUrl;
    
    private Author(final String name, final String url, final String iconUrl) {
        this.name = name;
        this.url = url;
        this.iconUrl = iconUrl;
    }
    
    private String getName() {
        return this.name;
    }
    
    private String getUrl() {
        return this.url;
    }
    
    private String getIconUrl() {
        return this.iconUrl;
    }
}
