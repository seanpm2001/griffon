/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.domain

import griffon.core.GriffonApplication
import griffon.core.ArtifactInfo
import griffon.domain.artifacts.GriffonDomainClass
// import griffon.domain.artifacts.DefaultGriffonDomainClass

import java.beans.Introspector
import java.util.regex.Pattern

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Andres Almiray
 */
final class DomainClassUtils {
    private static final Log LOG = LogFactory.getLog(DomainClassUtils.class)
    private static final Pattern PLURAL_PATTERN = Pattern.compile(".*[^aeiouy]y", Pattern.CASE_INSENSITIVE)

    private final Map ARTIFACTS = [:]
    private final Map DOMAIN_CLASSES = [:]
    private final Map ENTITY_NAMES = [:]
    private static final DomainClassUtils INSTANCE = new DomainClassUtils()

    public static DomainClassUtils getInstance() {
        INSTANCE
    }

    private DomainClassUtils() {}

    void init(GriffonApplication app) {
        app.artifactManager.domainArtifacts.each { domain ->
            String entityName = fetchAndSetEntityNameFor(domain)
            ARTIFACTS[domain.klass.name] = domain
            // DOMAIN_CLASSES[domain.klass.name] = new DefaultGriffonDomainClass(domain, entityName)
        }
    }
    
    String getEntityNameFor(Class klass) {
        ENTITY_NAMES[klass.name]
    }

    String getEntityNameFor(ArtifactInfo artifactInfo) {
        ENTITY_NAMES[artifactInfo.klass.name]
    }

    ArtifactInfo getArtifactInfoFor(Class klass) {
        ARTIFACTS[klass.name]
    }

    GriffonDomainClass getDomainClassFor(Class klass) {
        DOMAIN_CLASSES[klass.name]
    }

    GriffonDomainClass getDomainClassFor(ArtifactInfo artifactInfo) {
        DOMAIN_CLASSES[artifactInfo.klass.name]
    }

    // ----------------------------------------------

    private final String fetchAndSetEntityNameFor(ArtifactInfo domain) {
        String key = domain.klass.name
        String entityName = domain.simpleName
        boolean matchesIESRule = PLURAL_PATTERN.matcher(entityName).matches()
        entityName = matchesIESRule ? entityName[0..-1] + "ies" : entityName + "s"
        ENTITY_NAMES[key] = entityName
        return entityName
    }
}
