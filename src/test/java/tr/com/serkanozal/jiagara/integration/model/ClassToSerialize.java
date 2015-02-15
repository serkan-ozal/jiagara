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

package tr.com.serkanozal.jiagara.integration.model;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Serkan ÖZAL
 */
@SuppressWarnings("serial")
public class ClassToSerialize implements Serializable {

	private static final Random RANDOM = new SecureRandom();
	
	///////////////////////////////////////////////////////////////////////
	
	protected byte byteValue = 1;
	protected boolean booleanValue = true;
	protected char charValue = 'X';
	protected short shortValue = 10;
	protected int intValue = 100;
	protected float floatValue = 200.0F;
	protected long longValue = 1000L;
	protected double doubleValue = 2000.0;
	
	///////////////////////////////////////////////////////////////////////
	
	protected Byte byteObjectValue = 1;
	protected Boolean booleanObjectValue = true;
	protected Character charObjectValue = 'X';
	protected Short shortObjectValue = 10;
	protected Integer intObjectValue = 100;
	protected Float floatObjectValue = 200.0F;
	protected Long longObjectValue = 1000L;
	protected Double doubleObjectValue = 2000.0;
	
	@SuppressWarnings({ "unchecked" })
	public <T extends ClassToSerialize> T randomize() {
		byteValue = (byte) RANDOM.nextInt(256);
		booleanValue = RANDOM.nextBoolean();
		charValue = (char) RANDOM.nextInt(65536);
		shortValue = (short) RANDOM.nextInt(65536);
		intValue = RANDOM.nextInt();
		floatValue = RANDOM.nextFloat();
		longValue = RANDOM.nextLong();
		doubleValue = RANDOM.nextDouble();
		
		///////////////////////////////////////////////////////////////////////
		
		byteObjectValue = (byte) RANDOM.nextInt(256);
		booleanObjectValue = RANDOM.nextBoolean();
		charObjectValue = (char) RANDOM.nextInt(65536);
		shortObjectValue = (short) RANDOM.nextInt(65536);
		intObjectValue = RANDOM.nextInt();
		floatObjectValue = RANDOM.nextFloat();
		longObjectValue = RANDOM.nextLong();

		///////////////////////////////////////////////////////////////////////
		
		return (T) this;
	}
	
}
