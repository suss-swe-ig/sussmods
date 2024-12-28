package io.swe.api

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.security.cert.X509Certificate
import javax.net.ssl.*


@Configuration
class RestClientConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        val sslContext = SSLContext.getInstance("TLS")
        val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(x509Certificates: Array<X509Certificate?>?, s: String?) {
            }

            override fun checkServerTrusted(x509Certificates: Array<X509Certificate?>?, s: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        })
        sslContext.init(null, trustManagers, null)

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { _: String?, _: SSLSession? -> true }

        return RestTemplateBuilder()
            .requestFactory(SimpleClientHttpRequestFactory::class.java)
            .build()
    }

}