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

package griffon.domain.orm;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Andres Almiray
 */
public class BinaryExpression implements Criterion {
    private final String propertyName;
    private final Operator operator;
    private final Object value;

    public BinaryExpression(String propertyName, Operator operator, Object value) {
        this.propertyName = propertyName; 
        this.operator = operator; 
        this.value = value; 
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }

    public String toString() {
        return propertyName +" "+ operator +" "+ value;
    }

    public int hashCode() {
       return new HashCodeBuilder(17, 37)
           .append(propertyName)
           .append(operator)
           .append(value)
           .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj.getClass() != getClass()) return false; 
        BinaryExpression rhs = (BinaryExpression) obj;
        return new EqualsBuilder()
                      .append(propertyName, rhs.propertyName)
                      .append(operator, rhs.operator)
                      .append(value, rhs.value)
                      .isEquals();
    } 
}