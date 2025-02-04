/*
 * Copyright 2004-2005 the original author or authors.
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
package org.codehaus.griffon.exceptions;

/**
 * <p>Occurs when the creation of a new instance fails.
 *
 * @author Steven Devijver
 * @since Jul 2, 2005
 */
public class NewInstanceCreationException extends GriffonException {

	public NewInstanceCreationException() {
		super();
	}

	public NewInstanceCreationException(String arg0) {
		super(arg0);
	}

	public NewInstanceCreationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NewInstanceCreationException(Throwable arg0) {
		super(arg0);
	}

}
