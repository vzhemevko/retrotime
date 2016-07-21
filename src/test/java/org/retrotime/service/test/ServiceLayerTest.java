package org.retrotime.service.test;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.retrotime.configuration.spring.WebMvcConfiguration;
import org.retrotime.model.Content;
import org.retrotime.model.Retro;
import org.retrotime.model.Team;
import org.retrotime.model.User;
import org.retrotime.service.ContentService;
import org.retrotime.service.RetroService;
import org.retrotime.service.TeamService;
import org.retrotime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by vzhemevko on 10/13/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
    classes = WebMvcConfiguration.class, 
    loader = AnnotationConfigWebContextLoader.class)
@Transactional
@TransactionConfiguration(defaultRollback=true)
public class ServiceLayerTest {
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RetroService retroService;
	
	@Autowired
	private ContentService contentService;
	
	private boolean isInit = false;
	
	private Team testTeam;
	private Retro testRetro;
	private Content testContent;
	private User testUser;
	
	
	/**
	 * Creates default records for Team, Retro, User tables.
	 */
	@Before
	public  void setUp() {
		if (!isInit) {
			testUser = new User("testuser", "User Name", "user@mail", "pwd");
			userService.saveUser(testUser);
			
			testTeam = new Team();
			testTeam.setName("testTeam");
			testTeam.setUsers(Stream.of(testUser).collect(Collectors.toCollection(HashSet::new)));
			teamService.saveTeam(testTeam);
			
			testRetro = new Retro();
			testRetro.setName("testretro");
			testRetro.setTeam(testTeam);
			retroService.saveRetro(testRetro);
		}
		else {
			//do nothing
		}
	}

	/**
	 * Test whether we are able to store a content record
	 * in the database. The method runs in its own 
	 * transaction and is rolled back after execution. 
	 * 
	 */
	@Test
	public void testContentStorage() {
		testContent = new Content();
		testContent.setKudos("To some user");
		testContent.setRetro(testRetro);
		testContent.setUser(testUser);
		
		contentService.saveContent(testContent);
	}
}