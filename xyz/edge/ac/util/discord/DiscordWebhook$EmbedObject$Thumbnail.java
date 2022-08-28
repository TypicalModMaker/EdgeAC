package xyz.edge.ac.util.discord;

private class Thumbnail
{
    private final String url;
    
    private Thumbnail(final String url) {
        this.url = url;
    }
    
    private String getUrl() {
        return this.url;
    }
}
