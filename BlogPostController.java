package com.techtalentsouth.TechTalentBlog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class BlogPostController {
	
	@Autowired
	private BlogPostRepository blogPostRepository;
	private static List<BlogPost> posts= new ArrayList<>();
	@GetMapping(value="/")
	public String index(BlogPost blogPost) {
		return "blogpost/index";
	}
	//We can't initialize this, we can't construct this...
	//So we are going to ask SpringBoot to magically fill this
	//value in when our BlogPostController is created
	
	private BlogPost blogPost;
	
	public String index(Model model) {
		//Since we are using the @Controller
		//annotation rather than the @RestController
		//annotation, the String we are returning 
		//is actually a reference to a html template page.		
		//This model variable is a lot like a HashMap, except all
		//the keys have to be strings.
		List<BlogPost>posts= new ArrayList<>();
		for(BlogPost post:blogPostRepository.findAll()){
			posts.add(post);
		}
		model.addAttribute("posts",posts);
		return "blogpost/index";
	}
	
	@GetMapping(path="/blogposts/new")
	public String newBlog(Model model) {
		model.addAttribute("blogPost", new BlogPost());
		return "blogpost/new";
	}
	@PostMapping(path="/blogposts")
	public String addNewBlogPost(BlogPost blogPost, Model model) {
		blogPostRepository.save(blogPost);
		model.addAttribute("blogPost", blogPost);
		return "blogpost/result";
	}
	
	
	/* If we specify a path with a variable name in {} it becomes a wildcard*/
	@GetMapping(path="/blogposts/{id}")
	public String editPostWithId(@PathVariable Long id, Model model) {
		// We can now load in the blogpost that has that id.
		Optional<BlogPost> postBox=blogPostRepository.findById(id);
		if(postBox.isPresent()) {
			BlogPost post= postBox.get();
			model.addAttribute("blogPost", post);
		}
		return "blogpost/edit";
	}
	@PostMapping(path="/blogposts/{id}")
	public String updateExistingPost(@PathVariable Long id, BlogPost blogPost, Model model) {
		Optional<BlogPost> postBox=blogPostRepository.findById(id);
		if(postBox.isPresent()) {
			BlogPost actualPost= postBox.get();
			actualPost.setTitle(blogPost.getTitle());
			actualPost.setAuthor(blogPost.getAuthor());
			actualPost.setBlogEntry(blogPost.getBlogEntry());
			
			blogPostRepository.save(actualPost);//Update the existing post
			model.addAttribute("blogPost", actualPost);
		}
		return "blogpost/result";
	}
	
@GetMapping(path="/blogposts/delete/{id}")
public String deletePostById(@PathVariable Long id) {
	blogPostRepository.deleteById(id);
	return "blogpost/delete";
}
	
}
