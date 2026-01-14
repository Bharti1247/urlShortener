/*
 * Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2026-01-14T09:11:38.328+05:30 ERROR 22196 --- [linkShortener] [  restartedMain] o.s.b.d.LoggingFailureAnalysisReporter   : 

***************************
APPLICATION FAILED TO START
***************************

Description: A component required a bean of type 'org.springframework.cache.CacheManager' that could not be found.

Action: Consider defining a bean of type 'org.springframework.cache.CacheManager' in your configuration.
 * 
 */

// To resolve above error, either we need to add cache dependency (recommended) 
// or, write a CacheConfig class (not recommended, unless required)

package com.learn.linkShortener.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class CacheConfig {

    //@Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("shortUrlCache");
    }
}
