package com.example.classscheduler.di.modules

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.classscheduler.core.utils.constants.SecretsConstants
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module that provides the necessary Google Identity and Credential Manager
 * components used for Google Sign-In authentication.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthHiltModule {

    /**
     * Provides a configured [GetGoogleIdOption] used to request Google ID tokens
     * during the authentication process.
     *
     * @return The configured [GetGoogleIdOption] instance.
     */
    @Provides
    fun googleIdOption(): GetGoogleIdOption{
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(SecretsConstants.WEB_CLIENT_ID)
            .build();
    }

    /**
     * Provides a [GetCredentialRequest] that wraps the Google ID credential option.
     *
     * @param googleIdOption The Google ID credential option to include in the request.
     * @return A fully built [GetCredentialRequest] instance.
     */
    @Provides
    fun credentialsRequest(
        googleIdOption: GetGoogleIdOption
    ): GetCredentialRequest{
        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption).
            build();
    }

    /**
     * Provides a [CredentialManager] instance used to interact with the
     * system Credential Management API.
     *
     * @param context The application context.
     * @return A [CredentialManager] bound to the application context.
     */
    @Provides
    fun credentialManager(
        @ApplicationContext context: Context
    ): CredentialManager{
        return CredentialManager.create(context);
    }
}