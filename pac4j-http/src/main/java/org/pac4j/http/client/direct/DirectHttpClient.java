/*
  Copyright 2012 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.http.client.direct;

import org.pac4j.core.client.DirectClient;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.http.credentials.authenticator.Authenticator;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.http.credentials.extractor.Extractor;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.http.profile.creator.AuthenticatorProfileCreator;
import org.pac4j.http.profile.creator.ProfileCreator;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.http.profile.HttpProfile;

/**
 * <p>This class is the base HTTP client to authenticate <b>directly</b> users through HTTP protocol.</p>
 * <p>All the logic is based on {@link Extractor}, {@link Authenticator} and {@link ProfileCreator}.</p>
 * <p>It returns a {@link HttpProfile}.</p>
 *
 * @see HttpProfile
 * @author Jerome Leleu
 * @since 1.8.0
 */
public abstract class DirectHttpClient<C extends Credentials> extends DirectClient<C, CommonProfile> {

    protected Extractor<C> extractor;

    private Authenticator<C> authenticator;

    private ProfileCreator<C, CommonProfile> profileCreator = AuthenticatorProfileCreator.INSTANCE;

    @Override
    protected void internalInit(final WebContext context) {
        CommonHelper.assertNotNull("extractor", this.extractor);
        CommonHelper.assertNotNull("authenticator", getAuthenticator());
        CommonHelper.assertNotNull("profileCreator", getProfileCreator());
    }

    @Override
    public C getCredentials(WebContext context) throws RequiresHttpAction {
        init(context);
        try {
            C credentials = this.extractor.extract(context);
            if (credentials == null) {
                return null;
            }
            getAuthenticator().validate(credentials);
            return credentials;
        } catch (CredentialsException e) {
            logger.error("Failed to retrieve or validate credentials", e);
        }
        return null;
    }

    @Override
    protected CommonProfile retrieveUserProfile(final C credentials, final WebContext context) {
        // create the user profile
        final CommonProfile profile = getProfileCreator().create(credentials);
        logger.debug("profile: {}", profile);
        return profile;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "name", getName(), "extractor", this.extractor,
                "authenticator", getAuthenticator(), "profileCreator", getProfileCreator());
    }

    public Authenticator<C> getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(Authenticator<C> authenticator) {
        this.authenticator = authenticator;
    }

    public ProfileCreator<C, CommonProfile> getProfileCreator() {
        return profileCreator;
    }

    public void setProfileCreator(ProfileCreator<C, CommonProfile> profileCreator) {
        this.profileCreator = profileCreator;
    }
}
