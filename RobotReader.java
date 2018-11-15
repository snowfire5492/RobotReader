

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class RobotReader {
	
	private HashSet<String> links; 
	
	public RobotReader() {
		links = new HashSet<String>();
	}
	
	
	
	/**
	 * loads all parameter input into the links HashSet. This is primarily 
	 * used to initially load disallowed addresses from robots.txt
	 * 
	 * @param input String[] 
	 */
	public void loadLinks(String[] input) {
		
		for(int i = 0; i < input.length ; ++i) {
			links.add(input[i]);
		}
		System.out.println(links.toString());				// used to check links data	
	}
	
	
	
	/**
	 * fetches the disallowed addresses of a website and returns in String form
	 * 
	 * @param URL String. ex. "http://www.google.com"
	 * @return String[] all disallowed addresses
	 */
	public String[] fetchRobotRules(String URL) {

		ArrayList<String> roboRules = new ArrayList<String>();
		
		String[] disallowedURL = null;
		
		try(BufferedReader input = new BufferedReader(
				new InputStreamReader(new URL( URL + "/robots.txt").openStream())))	// getting all input from robots.txt 
		{
			String line = null;
			loop: while((line = input.readLine()) != null) {		// going through robots.txt file as long as next line exists
				
				if(line.equalsIgnoreCase("user-agent: *")) {		// looking for user-agent: * for all web-crawl agents
					while((line = input.readLine()) != null) {		// while the next line exists
						
						if(line.toLowerCase().contains("user-agent") || line.toLowerCase().startsWith("#")
								|| line.toLowerCase().contains("sitemap:")) { // arrived at another set of agent rules, or a comment, or sitemap info 
							break loop;											// so break loop
						}
						if(line.toLowerCase().contains("disallow:")) {
							roboRules.add(line);						// add line to 
						}		
					}			
				}
			}
			disallowedURL = new String[roboRules.size()];
			
			for(int i = 0; i < roboRules.size(); ++i) {
					
				disallowedURL[i] = URL + roboRules.get(i).substring(10);
			}
			
		}catch(Exception e) {
			System.out.println("file not found");						// file not found. only yahoo.com did this when testing for some reason
		}
		
		
		if(disallowedURL.length == 1 && disallowedURL[0].charAt(disallowedURL[0].length() - 1) != '/') {
			
			//disallowedURL = null;
			disallowedURL = new String[0];
			//System.out.println("Test");
			
		}
		
		
		
		
		
		
		return disallowedURL;
	}

	
	
	
	
	
	
	public static void main(String[] args)  {
		
		String URL = "https://www.cpp.edu";
		
		RobotReader rb = new RobotReader();
		
		String[] fetchRobotRules = rb.fetchRobotRules(URL);
		
		try {
				System.out.println( "Size " + fetchRobotRules.length);
			for(int i = 0 ; i < fetchRobotRules.length ; ++i) {
				
				System.out.println(fetchRobotRules[i]);
				
			}
		}catch(Exception e) {
			
		}
		
		rb.loadLinks(fetchRobotRules);
	}
}


