package org.example.config;

import lombok.Builder;
import lombok.Data;

/**
 * This configuration class is to hold the Mutual TLS details like
 * mtlsDigitalSigningPrivateKeyLocation  - Digital signing private key classpath location e.g. /etc/ssl/certs/sign.pem
 * mtlsTransportCertLocation - Transport certificate classpath location e.g. e://certs/transport.pem
 * mtlsTransportCertPassword - Transport certificate password and
 * proxy - Proxy details, if any e.g. <a href="proxy">https://proxy.domain.com:8443</a>
 */
@Data
@Builder
public class MTLSConfiguration {

    private String mtlsDigitalSigningPrivateKeyLocation;

    private String mtlsTransportCertLocation;

    private String mtlsTransportCertPassword;

    private String proxy;

}