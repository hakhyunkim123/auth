//package com.sso;
//
//import static org.hamcrest.CoreMatchers.hasItem;
//import static org.hamcrest.CoreMatchers.is;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import com.sso.dto.User;
//import com.sso.service.UserService;
//import com.sso.service.UserServiceImpl;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@WebAppConfiguration
//class SsoApplicationTests {
//	
//	@Autowired private UserServiceImpl userService;
//	
//	private User user1;
//
//	@Before
//    public void setup() {
//         user1 = new User();
//         user1.setUsername("sh20100094");
//         user1.setPassword("1111");
//         user1.setAccountNonExpired(true);
//         user1.setAccountNonLocked(true);
//         user1.setName("sh20100094");
//         user1.setCredentialsNonExpired(true);
//         user1.setEnabled(true);
//         user1.setAuthorities(AuthorityUtils.createAuthorityList("USER"));
//    }
//	
//	@Test
//    public void createUserTest() {
////         userService.deleteUser(user1.getUsername());
//		 User user1 = new User();
//		 
//		 user1.setUsername("sh20100094");
//         user1.setPassword("1111");
//         user1.setAccountNonExpired(true);
//         user1.setAccountNonLocked(true);
//         user1.setName("sh20100094");
//         user1.setCredentialsNonExpired(true);
//         user1.setEnabled(true);
//         user1.setAuthorities(AuthorityUtils.createAuthorityList("USER"));
//		 
//         userService.createUser(user1);
//         User user = userService.readUser(user1.getUsername());
////         assertThat(user.getUsername(), is(user1.getUsername()));
//        
//         PasswordEncoder passwordEncoder = userService.passwordEncoder();
////         assertThat(passwordEncoder.matches("pass1", user.getPassword()), is(true));
//
//         Collection<? extends GrantedAuthority> authorities1 = user1.getAuthorities();
//         Iterator<? extends GrantedAuthority> it = authorities1.iterator();
//         Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) user.getAuthorities();
//         while (it.hasNext()) {
//              GrantedAuthority authority = it.next();
////              assertThat(authorities, hasItem(new SimpleGrantedAuthority(authority.getAuthority())));
//         }
//    }
//
//}
