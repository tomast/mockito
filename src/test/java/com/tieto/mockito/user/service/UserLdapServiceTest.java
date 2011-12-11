/*
 * Copyright (c) 2008-2010, Tomas Turek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  - Neither the name of the DailyDev nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.tieto.mockito.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tieto.mockito.user.connector.UserLdapConnector;
import com.tieto.mockito.user.domain.User;
import com.tieto.mockito.user.repository.UserRoleRepository;

/**
 * @author Tomas Turek
 * 
 */
public class UserLdapServiceTest {

    @Mock
    private UserLdapConnector ldapConnectorMock;
    @Mock
    private UserRoleRepository userRoleRepositoryMock;

    @InjectMocks
    private UserLdapService service;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method for
     * {@link com.tieto.mockito.user.service.UserLdapService#getUser(java.lang.String)}
     * .
     */
    @Test
    public void testGetUserHappyPath() {
        String id = "totu2222";
        final User expectedUser = new User(id, "Tomas", "Turek");

        final Set<String> roles = new HashSet<String>();
        roles.add("ADMIN");

        when(ldapConnectorMock.getUser(anyString())).thenReturn(expectedUser);
        when(userRoleRepositoryMock.getRolesForUser(id)).thenReturn(roles);

        final User actualUser = service.getUser(id);

        InOrder inOrder = inOrder(ldapConnectorMock, userRoleRepositoryMock);
        inOrder.verify(ldapConnectorMock).getUser(id);
        inOrder.verify(userRoleRepositoryMock).getRolesForUser(id);
        inOrder.verifyNoMoreInteractions();

        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    /**
     * Test method for
     * {@link com.tieto.mockito.user.service.UserLdapService#getUser(java.lang.String)}
     * .
     */
    @Test(expected = RuntimeException.class)
    public void testGetUserNotFound() {
        String id = "totu2222";

        service.getUser(id);
    }
}
