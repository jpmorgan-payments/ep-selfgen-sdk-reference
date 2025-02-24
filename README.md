### Disclaimer

⚠️ Warning: Use at Your Own Risk ⚠️

The code and instructions provided in this repository are intended for reference purposes only. The maintainers of this
repository do not assume any responsibility for any issues, damages, or losses that may arise from the use of this code
or instructions.<br/>
By using this repository, you acknowledge that:

- The code may not be stable or functional.
- There are no guarantees regarding the performance, reliability, or suitability of the code for any particular purpose.
- You are solely responsible for any consequences resulting from the use of this code.<br/>

Please proceed with caution and ensure you understand the implications of using this code in your projects.

# Overview

Use this documentation to learn how to generate a simple API client for consuming APIs offered on
the J.P.
Morgan [Payments Developer Portal](https://developer.payments.jpmorgan.com/api/home).
You can also walk through the steps to enable mutual TLS authentication and digital signing logic on the same API
client.

To generate the simple API client, you can use [OpenAPI Generator](https://openapi-generator.tech/). There are multiple
ways to use the OpenAPI Generator, but in
this guide you only need to look at its plugin
capability

At the end of this you will know how to:

1. Generate an API client using our API specifications
   and [OpenAPI Generator plugin](https://openapi-generator.tech/docs/plugins)
2. Integrate the generated code with mTLS for authentication and digital signing logic to sign payloads

## Pre-requisites

### Open API Specification

Download the API spec files
from
our [developer portal](https://developer.payments.jpmorgan.com/api/home),
for the product you wish to use.

Please note, there could be multiple specs you may have to download, if for example you need to interact with both
Embedded Payments as well as
Digital Onboarding API's

### Project technical requirements

- The application needs to be of java language - jdk17 or above.
- The application uses maven for dependency management.
- The application uses spring framework – spring3
- You have the Mutual TLS transport and digital signing certificate with you and ready to use

## Step-By-Step Guide

This guide is broken into **five** steps to be completed in order. Once you have finished, you should be able to
generate a simple API client to use with J.P. Morgan's payment APIs.

1. [Create a project](README.md#Create-a-project)
2. [Configure OpenAPI Generator](README.md#Configure-OpenAPI-Generator)
3. [Generate code using the OpenAPI Generator plugin](README.md#Generate-code-using-the-OpenAPI-Generator-plugin)
4. [Enable mTLS authentication and digital signing](README.md#Enable-mTLS-authentication-and-digital-signing)
5. [How to use the SDK](README.md#How-to-use-the-SDK)

### Create a project:

1. Create a java project with jdk17 or above and maven for dependency management
2. Create a folder in the root directory of your project, to store all the spec file(s) downloaded earlier
   from [Payments Developer Portal](https://developer.payments.jpmorgan.com/api/home)

### Configure OpenAPI Generator

In the pom file of your application, add the OpenAPI Generator plugin.

1. Take a look at the plugin details and choose your preferred configuration
   from [here](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-maven-plugin)
2. Since you may have multiple spec files, a reference on how to configure the plugin can be found
   at - [pom file](pom.xml)
3. The models/api's generated with the reference configuration at [pom.xml](pom.xml), use commonly used annotations
   from jackson,jakarta and spring. Please make sure you have the following dependencies added to your pom file

<details>

<summary>Dependencies</summary>

```xml

<dependencies>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>6.1.12</version>
  </dependency>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>6.1.12</version>
  </dependency>
  <dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>jackson-databind-nullable</artifactId>
    <version>0.2.6</version>
  </dependency>
  <dependency>
    <groupId>jakarta.annotation</groupId>
    <artifactId>jakarta.annotation-api</artifactId>
    <version>3.0.0</version>
  </dependency>
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.34</version>
  </dependency>
</dependencies>
```

</details>

### Generate code using the OpenAPI Generator plugin

Execute the following command to generate code.

```shell
mvn clean compile
```

The output of which will be generated into the output directory you configured on the plugin, and will contain

- Java models for request/response schemas
- Java models for callback events
- API classes for all api's in the specification - to perform GET/POST requests
- API Client which has the core logic to make api requests and receive responses

Please note, the code generated by the OpenAPI Generator acts as a base for you to build your business logic on
top of.

### Enable mTLS authentication and digital signing

> [Disclaimer]
> This reference code is a simple form of implementing mTLS authentication with the certificates being present on the file system. We do not recommend using this implementation as is.
> Please ensure the certificates are stored as per your organization guidelines.

To enable mutual TLS authentication and payload signing capability on top of the generated API
client, add the following code under the sources in your project

- MtlsConfiguration - [see here](src/main/java/org/example/config/MTLSConfiguration.java)
- RestTemplateConfigurer - [see here](src/main/java/org/example/config/RestTemplateConfigurer.java)
- The code above will need the following dependencies added to your pom file

<details>
<summary>Dependencies</summary>

```xml

<dependencies>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
  </dependency>
  <dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5</artifactId>
    <version>5.3.1</version>
  </dependency>
</dependencies>
```

</details>

And now with this code added, you are ready to start integrating with
the [developer portal API's](https://developer.payments.jpmorgan.com/api/home).

### How to use the SDK

You could either use the above project to implement your business interaction with JPMorgan payments API's or package
the above generated code above as an artifact to use in your business project.

<!-- markdown-link-check-disable -->

#### Getting Started against the mock - without mTLS Auth

<details>
<summary>Sample code</summary>

```java

public class Example {

    public static void main(String[] args) {

        // Since we are not using mTLS in this instance, we create an empty configuration
        MTLSConfiguration mtlsConfiguration = MTLSConfiguration.builder().build();

        // Use the above mTLS configuration to create a rest template
        RestTemplateConfigurer restTemplateConfigurer = new RestTemplateConfigurer();
        RestTemplate restTemplate = restTemplateConfigurer.restTemplate(mtlsConfiguration);

        // Create an APIClient (OpenAPI generated code) instance using the resttemplate that has mTLS configured above
        ApiClient apiClient = new ApiClient(restTemplate);

        // Set a base path for the api you are trying to reach to on the api client - which is the mock API in this instance
        apiClient.setBasePath("https://api-mock.payments.jpmorgan.com//tsapi/ef/v1");

        try {
            // In this sample we are initializing accounts api (OpenAPI Generated code) to query a list of accounts
            AccountsApi accountsApi = new AccountsApi(apiClient);
            ListAccountsResponse listAccountsResponse = accountsApi.getAccounts(null, null, null, null);
            System.out.println(listAccountsResponse);
            //..your business logic goes here
        } catch (HttpClientErrorException clientErrorException) {

            //Any 4xx response will result in a HttpClientErrorException, you can extract the error response like shown below
            ApiError apiError = clientErrorException.getResponseBodyAs(ApiError.class);
            System.out.println(apiError.getContext());

        } catch (HttpServerErrorException serverErrorException) {

            //Any 5xx response will result in a HttpServerErrorException, you can extract the error response like shown below
            ApiError apiError = serverErrorException.getResponseBodyAs(ApiError.class);
            System.out.println(apiError.getContext());

        }

    }
}

```

</details>

Please note that the above code can be used to integrate against our mock
environment - https://api-mock.payments.jpmorgan.com/
and does not require any type of authentication.

<!-- markdown-link-check-enable -->

#### Getting Started against the real API - with mTLS auth

As you are ready to code against the real API, please find below an example on how to use the SDK with mTLS
authentication.

<details>
<summary>Sample code</summary>

```java

public class Example {

    public static void main(String[] args) {

        // Create a MTLS configuration object with transport certificate and digital signing key to be able to use mTLS authentication in your API request
        MTLSConfiguration mtlsConfiguration = MTLSConfiguration.builder()
                .mtlsTransportCertLocation("certs/transportCert.p12")
                .mtlsDigitalSigningPrivateKeyLocation("certs/private.key")
                .mtlsTransportCertPassword("credential of transport certificate").build();

        // Use the above mTLS configuration to create a rest template
        RestTemplateConfigurer restTemplateConfigurer = new RestTemplateConfigurer();
        RestTemplate restTemplate = restTemplateConfigurer.restTemplate(mtlsConfiguration);

        // Create an APIClient (OpenAPI generated code) instance using the resttemplate that has mTLS configured above
        ApiClient apiClient = new ApiClient(restTemplate);

        // Set a base path for the api you are trying to reach to on the api client
        apiClient.setBasePath("https://apigatewaycat.jpmorgan.com/tsapi/ef/v1");

        try {
            // In this sample we are initializing accounts api (OpenAPI Generated code) to query a list of accounts
            AccountsApi accountsApi = new AccountsApi(apiClient);
            ListAccountsResponse listAccountsResponse = accountsApi.getAccounts(null, null, null, null);
            System.out.println(listAccountsResponse);
            //..your business logic goes here
        } catch (HttpClientErrorException clientErrorException) {

            //Any 4xx response will result in a HttpClientErrorException, you can extract the error response like shown below
            ApiError apiError = clientErrorException.getResponseBodyAs(ApiError.class);
            System.out.println(apiError.getContext());

        } catch (HttpServerErrorException serverErrorException) {

            //Any 5xx response will result in a HttpServerErrorException, you can extract the error response like shown below
            ApiError apiError = serverErrorException.getResponseBodyAs(ApiError.class);
            System.out.println(apiError.getContext());

        }

    }
}

```

</details>

#### Getting Started with webhook notifications

The SDK also supports handling webhook notifications/callback events. Since it's generated using the OpenAPI
specification, it also generates models for webhook notifications/callback events.

Webhook notifications are handled differently from regular API calls. For making API calls, the SDK-generated code
provides an APIClient to perform requests and receive responses. However, with webhook notifications, the user of the
SDK needs to build out REST controllers and deserialize the incoming notification to the models generated as part of the
SDK generation.

For more information on managing notifications, please refer to
the [developer portal](https://developer.payments.jpmorgan.com/docs/embedded-finance-solutions/embedded-payments/how-to/notifications)

Below is an example of how the callback event looks like

<details>
<summary>Sample CallbackEvent</summary>

```json
{
  "eventId": "1863",
  "eventType": "ACCOUNT_CLOSED",
  "resourceType": "TRANSACTIONS",
  "resource": "{\"id\": \"UcMyVrUdXvZVhjUt\",\"clientId\": \"0005191231\",\"type\": \"ACH\",\"debtorAccountId\": \"10de11e98255479dbf43fd77ff55aa51\",\"recipientId\": null,\"amount\": \"0.1\",\"currency\": \"USD\",\"status\": \"REJECTED\",\"transactionReferenceId\": null,\"paymentDate\": \"2023-11-23\",\"ledgerBalance\": \"100.0\"}",
  "error": {
    "title": "BAD_REQUEST",
    "httpStatus": 422,
    "traceId": "string",
    "requestId": "string",
    "context": [{}]
  },
  "resourceObject": {
    "id": "string",
    "clientId": "string",
    "type": "ACH",
    "debtorAccountId": "VTA: dc2eda9084bf40b7a1d8baa8c5e0ea0ax or DDA: 0563635817",
    "creditorAccountId": "VTA: dc2eda9084bf40b7a1d8baa8c5e0ea0ax or DDA: 0563635817",
    "recipientId": "616d93a1-ce53-4c71-b03b-02a11396db28",
    "amount": "100",
    "currency": "string",
    "status": "COMPLETED",
    "transactionReferenceId": "inv-pay-1234",
    "originatingId": "ff1b6152-3ea7-4c59-b4ae-606c6497cd52",
    "originatingTransactionType": "string",
    "memo": "ORIG CO NAME:JPMC C1 ORIG ID:1115241202  TRACE#:02100002112497667 EED:111112 IND ID:W9diFdfgssoPyfito IND NAME:JPMC C1 R02 - ACCOUNT CLOSED O RIGIN# 1123897186 ACH RETURN BLK# 012  TRN: 11129037667TC",
    "paymentDate": "2022-12-31",
    "ledgerBalance": "100.0",
    "changeDetails": {
      "title": "Clearing Notification of Change",
      "context": {
        "correctedValues": []
      }
    }
  }
}
```

</details>

And here is an example on how to use the SDK generated models to handle webhook notifications.

<details>
<summary>Sample code</summary>

```java

public class Example {

    public static void main(String[] args) {

        //Given the callback event json string looks like below
        String callbackEvent = "{\"eventId\":\"5120\",\"eventType\":\"ACCOUNT_CREATED\",\"resourceType\":\"ACCOUNTS\",\"resource\":\"{\\\"id\\\":\\\"9c7798a8643a40068cb4d12d97ab68f5\\\",\\\"clientId\\\":\\\"1000020694\\\",\\\"type\\\":\\\"LIMITED_DDA\\\",\\\"label\\\":\\\"Taxes6871\\\",\\\"state\\\":\\\"OPEN\\\",\\\"createdAt\\\":\\\"2024-06-17T14:04:22.963402108Z\\\"}\",\"resourceObject\":{\"id\":\"9c7798a8643a40068cb4d12d97ab68f5\",\"clientId\":\"1000020694\",\"type\":\"LIMITED_DDA\",\"label\":\"Taxes6871\",\"state\":\"OPEN\",\"createdAt\":\"2024-06-17T14:04:22.963402108Z\"}}";

        //Create an object mapper to deserialize the callback event string
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  //this is because of the date time type the generator uses
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        objectMapper.setDateFormat(df);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //Create a webhook callback event object using the generated model and the callback event string
        WebhookCallbackEvent webhookCallbackEvent = objectMapper.readValue(testContext.getCallbackEventString(),
                WebhookCallbackEvent.class);

        //The callback event can be of different types, you can use the resourceType to determine the type of event and handle it accordingly
    }
}
```

</details>

### Info

The instructions provided above have been verified with the following versions of the API specifications, in conjunction
with mTLS authentication:

- Version 1.0.10 of the Embedded Payments API
- Version 2.0.13 of the Embedded Payments API
- Version 1.0.14 of the Digital Onboarding API
