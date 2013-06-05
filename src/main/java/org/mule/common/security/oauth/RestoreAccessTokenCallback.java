
package org.mule.common.security.oauth;

public interface RestoreAccessTokenCallback
{

    /**
     * Restore access token and secret
     */
    void restoreAccessToken();

    /**
     * Retrieve the just restored access token
     * 
     * @return A string representing the access token
     */
    String getAccessToken();

    /**
     * Retrieve the access token secret
     * 
     * @return A string representing the access token secret
     */
    String getAccessTokenSecret();
}
