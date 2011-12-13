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

import com.tieto.mockito.user.connector.UserLdapConnector;
import com.tieto.mockito.user.domain.User;
import com.tieto.mockito.user.repository.UserRoleRepository;

/**
 * @author Tomas Turek
 * 
 */
public class UserLdapService implements UserService {

    private final UserLdapConnector userLdapConnector;
    private final UserRoleRepository userRoleRepository;

    public UserLdapService(UserLdapConnector userLdapConnector, UserRoleRepository userRoleRepository) {
        this.userLdapConnector = userLdapConnector;
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(String id) {

        final User user = this.userLdapConnector.getUser(id);

        if (user == null) {
            throw new RuntimeException(String.format("User '%s' not found.", id));
        }
        user.getRoles().addAll(this.userRoleRepository.getRolesForUser(id));

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRoleToUser(String userId, String... roles) {
        final User user = getUser(userId);

        for (String role : roles) {
            if (user.getRoles().contains(role)) {
                throw new RuntimeException(String.format("User '%s' already has role '%s'.", userId, role));
            }
        }
        this.userRoleRepository.addRoleToUser(userId, roles);
    }
}
