package com.tieto.mockito.user.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tieto.mockito.user.connector.UserLdapConnector;
import com.tieto.mockito.user.domain.User;
import com.tieto.mockito.user.repository.UserRoleRepository;


public class UserLdapServiceJUGTest {
    
    private static final String USER_ID = "totu1222";
    @Mock
    private UserLdapConnector ldapConnector;
    @Mock
    private UserRoleRepository roleRepository;
    
    final User expectedUser = new User(USER_ID, "Tomas", "Turek");
    
    Set<String> roles = new HashSet<String>();
    
    @InjectMocks
    private UserLdapService service;
    
    @Before
    public void setUp(){
       MockitoAnnotations.initMocks(this); 
       roles.add("ADMIN");
    }
    
    @Test
    public void testGetUser() {
        mockGetUser();
        
        User actualUser = service.getUser(USER_ID);
        
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
        
        verifyGetUserMocks();
        verifyZeroInteractions(ldapConnector);
    }

    private void verifyGetUserMocks() {
        verify(ldapConnector).getUser(USER_ID);
        verify(roleRepository).getRolesForUser(USER_ID);
    }

    private void mockGetUser() {
        when(ldapConnector.getUser(USER_ID)).thenReturn(expectedUser);
        when(roleRepository.getRolesForUser(USER_ID)).thenReturn(roles);
    }
    
    @Test (expected = RuntimeException.class)
    public void testGetUserNotFound() {
        User actualUser = service.getUser(USER_ID);
    }
    
    @Test (expected = RuntimeException.class)
    public void testAddRoleToUserExistingRole() {
        mockGetUser();
        
        service.addRoleToUser(USER_ID, "ADMIN");
    }
    
    @Test
    public void testAddRoleToUserHappyPath() {
        mockGetUser();
        
        Set<String> roles = new HashSet<String>();
        roles.add("ADMIN");
        when(roleRepository.getRolesForUser(USER_ID)).thenReturn(roles);
        
        service.addRoleToUser(USER_ID, "USER");
        
        verifyGetUserMocks();
        verify(roleRepository).addRoleToUser(USER_ID, "USER");
    }
    
}
