/*
 * Copyright 2002-2013 the original author or authors.
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

package tr.com.serkanozal.jiagara.benchmark;

import java.io.Serializable;

/**
 * @author Serkan ÖZAL
 */
@SuppressWarnings({ "serial", "unused" })
public class SerializableClassToSerialize implements Serializable {

	private byte byteValue = 1;
	private boolean booleanValue = true;
	private char charValue = 'X';
	private short shortValue = 10;
	private int intValue = 100;
	private float floatValue = 200.0F;
	private long longValue = 1000;
	private double doubleValue = 2000.0;
	
}
