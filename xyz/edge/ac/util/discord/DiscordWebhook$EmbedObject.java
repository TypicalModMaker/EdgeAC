package xyz.edge.ac.util.discord;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public static class EmbedObject
{
    private String title;
    private String description;
    private String url;
    private Color color;
    private Footer footer;
    private Thumbnail thumbnail;
    private Image image;
    private Author author;
    private final List<Field> fields;
    
    public EmbedObject() {
        this.fields = new ArrayList<Field>();
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public Footer getFooter() {
        return this.footer;
    }
    
    public Thumbnail getThumbnail() {
        return this.thumbnail;
    }
    
    public Image getImage() {
        return this.image;
    }
    
    public Author getAuthor() {
        return this.author;
    }
    
    public List<Field> getFields() {
        return this.fields;
    }
    
    public EmbedObject setTitle(final String title) {
        this.title = title;
        return this;
    }
    
    public EmbedObject setDescription(final String description) {
        this.description = description;
        return this;
    }
    
    public EmbedObject setUrl(final String url) {
        this.url = url;
        return this;
    }
    
    public EmbedObject setColor(final Color color) {
        this.color = color;
        return this;
    }
    
    public EmbedObject setFooter(final String text, final String icon) {
        this.footer = new Footer(text, icon);
        return this;
    }
    
    public EmbedObject setThumbnail(final String url) {
        this.thumbnail = new Thumbnail(url);
        return this;
    }
    
    public EmbedObject setImage(final String url) {
        this.image = new Image(url);
        return this;
    }
    
    public EmbedObject setAuthor(final String name, final String url, final String icon) {
        this.author = new Author(name, url, icon);
        return this;
    }
    
    public EmbedObject addField(final String name, final String value, final boolean inline) {
        this.fields.add(new Field(name, value, inline));
        return this;
    }
    
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
    
    private class Image
    {
        private final String url;
        
        private Image(final String url) {
            this.url = url;
        }
        
        private String getUrl() {
            return this.url;
        }
    }
    
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
}
