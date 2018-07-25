package manos.examples.streams.collectors.groupingBy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import manos.examples.streams.collectors.groupingBy.BlogPostExample.BlogPostType;

public class CollectoresGroupingByTest {
	private List<BlogPostExample> createBlogPosts(){
		BlogPostExample e0 = new BlogPostExample("title0", "author0", BlogPostType.GUIDE, 3);
		BlogPostExample e1 = new BlogPostExample("title1", "author0", BlogPostType.GUIDE, 5);
		BlogPostExample e2 = new BlogPostExample("title2", "author1", BlogPostType.NEWS, 1);
		BlogPostExample e3 = new BlogPostExample("title3", "author1", BlogPostType.REVIEW, 7);
		BlogPostExample e4 = new BlogPostExample("title4", "author1", BlogPostType.GUIDE, 0);
		BlogPostExample e5 = new BlogPostExample("title5", "author2", BlogPostType.NEWS, 90);
		BlogPostExample e6 = new BlogPostExample("title6", "author3", BlogPostType.NEWS, 1);
		BlogPostExample e7 = new BlogPostExample("title7", "author3", BlogPostType.NEWS, 1);
		return Arrays.asList(e0, e1, e2, e3, e4, e5, e6, e7);
	}
	
	/**
	 * Let’s start with the simplest groupingBy method, which only takes a classification function as its parameter. 
	 * A classification function is applied to each element of the stream. 
	 * The value that is returned by the function is used as a key to the map that we get from the groupingBy collector.
	 * To group the blog posts in the blog post list by their type:
	 */
	@Test
	public void groupingby_single_column(){
		Map<BlogPostType, List<BlogPostExample>> map = createBlogPosts().stream().collect(Collectors.groupingBy(blogPost->blogPost.getType()));
		Assert.assertEquals(3, map.get(BlogPostType.GUIDE).size());
		Assert.assertEquals(4, map.get(BlogPostType.NEWS).size());
		Assert.assertEquals(1, map.get(BlogPostType.REVIEW).size());
		
		//you can also put a new object as key like that
		/*Map<BlogPostExampleTuple, List<BlogPostExample>> map1 = createBlogPosts().stream()
				.collect(Collectors.groupingBy(blogPost -> new BlogPostExampleTuple(blogPost, blogPost.getAuthor())));
		Assert.assertEquals(8, map1.size());*/
	}
	
	/**
	 * The second overload of groupingBy takes an additional second collector (downstream collector), that is applied to the results of the first collector.
	 * When we specify only a classification function and not a downstream collector, the toList() collector is used behind the scenes.
	 * Let’s use the toSet() collector as the downstream collector and get a Set of blog posts (instead of a List):
	 */
	@Test
	public void groupingBy_with_DifferentCollector_collector(){
		Map<BlogPostType, Set<BlogPostExample>> map = createBlogPosts().stream().collect(Collectors.groupingBy(blogPost->blogPost.getType(), Collectors.toSet()));
		Assert.assertEquals(3, map.get(BlogPostType.GUIDE).size());
		Assert.assertEquals(4, map.get(BlogPostType.NEWS).size());
		Assert.assertEquals(1, map.get(BlogPostType.REVIEW).size());
	}
	
	@Test
	public void groupingBy_with_different_map_impl(){
		Map<BlogPostType, List<BlogPostExample>> map = createBlogPosts().stream()
				.collect(Collectors.groupingBy(BlogPostExample::getType, ()->new LinkedHashMap<>(), Collectors.toList()));
		Assert.assertTrue(map instanceof LinkedHashMap);
	}
	
	@Test
	public void groupingBy_Concurrent(){
		ConcurrentMap<BlogPostType, List<BlogPostExample>> map = createBlogPosts().stream()
				.collect(Collectors.groupingByConcurrent(BlogPostExample::getType));
		
		Assert.assertEquals(3, map.size());
		
	}
	/**
	 * A different application of the downstream collector is to do a secondary grouping by to the results of the first group by.
	 * To group the List of BlogPosts first by author and then by type:
	 */
	@Test
	public void groupingBy_with_secondary_GroupingBy_Collector(){
		Map<String, Map<BlogPostType, List<BlogPostExample>>> map = createBlogPosts().stream()
				.collect(Collectors.groupingBy(BlogPostExample::getAuthor, Collectors.groupingBy(BlogPostExample::getType)));
		Assert.assertEquals(4, map.size());//4 different authors
	}
	
	@Test
	public void grouping_by_getAverage(){
		Map<BlogPostType, Double> map = createBlogPosts().stream()
				.collect(Collectors.groupingBy(BlogPostExample::getType, Collectors.averagingInt(blogPost->blogPost.getLikes())));
		Assert.assertEquals(new Double((3.0+5.0+0.0)/3.0), map.get(BlogPostType.GUIDE));
		Assert.assertEquals(new Double((1.0+90.0+1.0+1.0)/4.0), map.get(BlogPostType.NEWS));
		Assert.assertEquals(new Double(7.0), map.get(BlogPostType.REVIEW));
	}
	
	@Test
	public void grouping_by_getSum(){
		Map<BlogPostType, Integer> map = createBlogPosts().stream()
				.collect(Collectors.groupingBy(BlogPostExample::getType, Collectors.summingInt(blogPost->blogPost.getLikes())));
		Assert.assertEquals(3+5+0, map.get(BlogPostType.GUIDE).intValue());
		Assert.assertEquals(1+90+1+1, map.get(BlogPostType.NEWS).intValue());
		Assert.assertEquals(7, map.get(BlogPostType.REVIEW).intValue());
	}
	
	@Test
	public void groupingBy_getStatistics(){
		Map<BlogPostType, IntSummaryStatistics> map = createBlogPosts().stream()
				.collect(Collectors.groupingBy(BlogPostExample::getType, Collectors.summarizingInt(BlogPostExample::getLikes)));
		
		Assert.assertEquals(8, map.get(BlogPostType.GUIDE).getSum());
		Assert.assertEquals(4, map.get(BlogPostType.NEWS).getCount());
		Assert.assertEquals(7, map.get(BlogPostType.REVIEW).getMax());
	}
	
	@Test
	public void grouping_by_getMax(){
		List<BlogPostExample> blogPostExamples = createBlogPosts();
		Map<BlogPostType, Optional<BlogPostExample>> map = blogPostExamples.stream()
				.collect(Collectors.groupingBy(BlogPostExample::getType, Collectors.maxBy(Comparator.comparingInt(BlogPostExample::getLikes))));
		Assert.assertEquals(blogPostExamples.get(1), map.get(BlogPostType.GUIDE).get());
		Assert.assertEquals(blogPostExamples.get(5), map.get(BlogPostType.NEWS).get());
		Assert.assertEquals(blogPostExamples.get(3), map.get(BlogPostType.REVIEW).get());
	}
	
	@Test
	public void groupingBy_mapToDifferentType(){
		Map<BlogPostType, String> map = createBlogPosts().stream()
				.collect(Collectors.groupingBy(BlogPostExample::getType, Collectors.mapping(BlogPostExample::getTitle, Collectors.joining("-", "[", "]"))));
		
		Assert.assertEquals("[title0-title1-title4]", map.get(BlogPostType.GUIDE));
		Assert.assertEquals("[title2-title5-title6-title7]", map.get(BlogPostType.NEWS));
		Assert.assertEquals("[title3]", map.get(BlogPostType.REVIEW));
	}
	
}
