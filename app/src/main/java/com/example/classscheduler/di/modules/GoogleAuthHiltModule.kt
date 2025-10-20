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

@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthHiltModule {

    @Provides
    fun googleIdOption(): GetGoogleIdOption{
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(SecretsConstants.WEB_CLIENT_ID)
            .build();
    }

    @Provides
    fun credentialsRequest(
        googleIdOption: GetGoogleIdOption
    ): GetCredentialRequest{
        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption).
            build();
    }

    @Provides
    fun credentialManager(
        @ApplicationContext context: Context
    ): CredentialManager{
        return CredentialManager.create(context);
    }
}