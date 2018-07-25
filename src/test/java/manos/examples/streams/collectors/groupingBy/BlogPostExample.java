package manos.examples.streams.collectors.groupingBy;

public class BlogPostExample {
	public enum BlogPostType{
		NEWS,
		REVIEW,
		GUIDE
	}
	String title;
	String author;
	BlogPostType type;
	int likes;
	public BlogPostExample(String title, String author, BlogPostType type, int likes) {
		this.title = title;
		this.author = author;
		this.type = type;
		this.likes = likes;
	}
	public BlogPostExample() {}
	
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public BlogPostType getType() {
		return type;
	}
	public int getLikes() {
		return likes;
	}
	
}
