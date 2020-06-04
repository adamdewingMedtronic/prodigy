package com.mdt.prodigy.dto.request;

import lombok.Data;

@Data
public class FhirAuthorization {
    private String access_token;    // REQUIRED - OAuth 2.0 access token
    private String token_type;      // REQUIRED - fixed value: Bearer
    private String expires_in;      // REQUIRED - lifetime in seconds of the access token
    private String scope;           // REQUIRED - scopes the access token grants to the CDS Service
    private String subject;         // REQUIRED - OAuth 2.0 client id of the CDS Service’s auth server registration
}
