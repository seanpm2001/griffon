package org.codehaus.griffon.runtime.domain

import griffon.persistence.Entity

@Entity('sample')
class Book {
    String title

    static hasMany = [author: Author]
}
