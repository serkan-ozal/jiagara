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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tr.com.serkanozal.jiagara.service.serialize.SerializerService;
import tr.com.serkanozal.jiagara.service.serialize.SerializerServiceFactory;
import tr.com.serkanozal.jiagara.util.JvmUtil;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author Serkan ÖZAL
 */
@SuppressWarnings("serial")
// -XX:PermSize=256M -XX:MaxPermSize=512M -Xms1g -Xmx2g
public class SerializationBenchmarkTest implements Serializable {

	public static void main(String[] args) {
		new SerializationBenchmarkTest().runSerializationBenchmarkTestDrivers();
	}
	
	@Test
	public void runSerializationBenchmarkTestDrivers() {
		try {
			JvmUtil.info();
			
			final int SERIALIZATION_COUNT = 5000;
			ClassToSerialize[] objectArray = new ClassToSerialize[SERIALIZATION_COUNT];
			for (int i = 0; i < SERIALIZATION_COUNT; i++) {
				objectArray[i] = new ClassToSerialize();
			}
			
			
			List<SerializationBenchmarkTestDriver> serializationBenchmarkTestDriverList = 
					new ArrayList<SerializationBenchmarkTestDriver>();
			serializationBenchmarkTestDriverList.add(new JiagaraSerializationBenchmarkTestDriver());
			serializationBenchmarkTestDriverList.add(new KryoSerializationBenchmarkTestDriver());
			serializationBenchmarkTestDriverList.add(new JavaSerializationBenchmarkTestDriver());
			
			for (SerializationBenchmarkTestDriver driver : serializationBenchmarkTestDriverList) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				long start = System.currentTimeMillis();
				for (int i = 0; i < SERIALIZATION_COUNT; i++) {
					driver.serialize(objectArray[i], bos);
				}
				long finish = System.currentTimeMillis();
				System.out.println(driver.getName() + " has been executed " + SERIALIZATION_COUNT + 
						           " times in " + (finish - start) + " milliseconds ...");
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unused" })
	private class ClassToSerialize implements Serializable {
		
		private byte byteValue = 1;
		private boolean booleanValue = true;
		private char charValue = 'X';
		private short shortValue = 10;
		private int intValue = 100;
		private float floatValue = 200.0F;
		private long longValue = 1000;
		private double doubleValue = 2000.0;
		
	}
	
	private class JiagaraSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		private SerializerService serializerService;
		
		private JiagaraSerializationBenchmarkTestDriver() {
			serializerService = SerializerServiceFactory.getSerializerService();
		}
		
		@Override
		public String getName() {
			return "Jiagara Serializer";
		}
		
		@Override
		public void serialize(Object o, OutputStream os) {
			serializerService.serialize(o, os);
		}
		
	}
	
	private class KryoSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		private Kryo kryo;
		
		private KryoSerializationBenchmarkTestDriver() {
			kryo = new Kryo();
		}
		
		@Override
		public String getName() {
			return "Kryo Serializer";
		}
		
		@Override
		public void serialize(Object o, OutputStream os) {
			kryo.writeObject(new Output(os), o);
		}
		
	}
	
	private class JavaSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		private ObjectOutputStream oos;
		
		private JavaSerializationBenchmarkTestDriver() {
			
		}
		
		@Override
		public String getName() {
			return "Java Serializer";
		}
		
		@Override
		public void serialize(Object o, OutputStream os) {
			try {
				oos = new ObjectOutputStream(os);
				oos.writeObject(o);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private interface SerializationBenchmarkTestDriver {
		
		String getName();
		void serialize(Object o, OutputStream os);
		
	}
	
}
