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
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tr.com.serkanozal.jiagara.serialize.Serializer;
import tr.com.serkanozal.jiagara.serialize.SerializerFactory;
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

	@Test
	public void runSerializationBenchmarkTestDrivers() {
		try {
			JvmUtil.info();
			
			final int SERIALIZATION_COUNT = 1000;

			List<SerializationBenchmarkTestDriver> serializationBenchmarkTestDriverList = 
					new ArrayList<SerializationBenchmarkTestDriver>();
			serializationBenchmarkTestDriverList.add(new JiagaraSerializationBenchmarkTestDriver());
			serializationBenchmarkTestDriverList.add(new KryoSerializationBenchmarkTestDriver());
			serializationBenchmarkTestDriverList.add(new JavaSerializationBenchmarkTestDriver());
			serializationBenchmarkTestDriverList.add(new CustomSerializationBenchmarkTestDriver());
			
			for (SerializationBenchmarkTestDriver driver : serializationBenchmarkTestDriverList) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				long start = System.currentTimeMillis();
				for (int i = 0; i < SERIALIZATION_COUNT; i++) {
					driver.serialize(driver.getObjectToSerialize(), bos);
				}
				long finish = System.currentTimeMillis();
				System.out.println(driver.getName() + " has been executed " + SERIALIZATION_COUNT + 
						           " times in " + (finish - start) + " milliseconds ...");
				//byte[] output = bos.toByteArray();
				//long outputDataStartAddress = JvmUtil.getArrayBaseAddress(output, byte.class);
				//JvmUtil.dump(outputDataStartAddress, output.length);
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unused" })
	private class ClassToSerialize {
		
		private byte byteValue = 1;
		private boolean booleanValue = true;
		private char charValue = 'X';
		private short shortValue = 10;
		private int intValue = 100;
		private float floatValue = 200.0F;
		private long longValue = 1000;
		private double doubleValue = 2000.0;
		
	}
	
	@SuppressWarnings("unused")
	private class SerializableClassToSerialize implements Serializable {
		
		private byte byteValue = 1;
		private boolean booleanValue = true;
		private char charValue = 'X';
		private short shortValue = 10;
		private int intValue = 100;
		private float floatValue = 200.0F;
		private long longValue = 1000;
		private double doubleValue = 2000.0;
		
	}
	
	private class ExternalizableClassToSerialize implements Externalizable  {
		
		private byte byteValue = 1;
		private boolean booleanValue = true;
		private char charValue = 'X';
		private short shortValue = 10;
		private int intValue = 100;
		private float floatValue = 200.0F;
		private long longValue = 1000;
		private double doubleValue = 2000.0;
		
		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeByte(byteValue);
			out.writeBoolean(booleanValue);
			out.writeChar(charValue);
			out.writeShort(shortValue);
			out.writeInt(intValue);
			out.writeFloat(floatValue);
			out.writeLong(longValue);
			out.writeDouble(doubleValue);
		}
		@Override
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			
		}
		
	}
	
	private class JiagaraSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		@SuppressWarnings("unused")
		private SerializerService serializerService;
		private Serializer<ClassToSerialize> serializer;
		
		private JiagaraSerializationBenchmarkTestDriver() {
			serializerService = SerializerServiceFactory.getSerializerService();
			serializer = SerializerFactory.createSerializer(ClassToSerialize.class);
		}
		
		@Override
		public String getName() {
			return "Jiagara Serializer";
		}
		
		@Override
		public void serialize(Object o, OutputStream os) {
			serializer.serialize((ClassToSerialize) o, os);
		}
		
		@Override
		public Object getObjectToSerialize() {
			return new ClassToSerialize();
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
		
		@Override
		public Object getObjectToSerialize() {
			return new ClassToSerialize();
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
		
		@Override
		public Object getObjectToSerialize() {
			return new SerializableClassToSerialize();
		}	
		
	}
	
	private class CustomSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		private ObjectOutputStream oos;
		
		private CustomSerializationBenchmarkTestDriver() {
			
		}
		
		@Override
		public String getName() {
			return "Custom Serializer";
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
		
		@Override
		public Object getObjectToSerialize() {
			return new ExternalizableClassToSerialize();
		}	
		
	}
	
	private interface SerializationBenchmarkTestDriver {
		
		String getName();
		void serialize(Object o, OutputStream os);
		Object getObjectToSerialize();
		
	}
	
}
