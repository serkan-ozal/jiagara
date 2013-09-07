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
import java.util.concurrent.TimeUnit;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectDatumWriter;
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
			
			final int PRE_SERIALIZATION_COUNT = 10000;
			final int SERIALIZATION_COUNT = 100000;

			SerializationBenchmarkTestDriver[] serializationBenchmarkTestDrivers = new SerializationBenchmarkTestDriver[5];
			serializationBenchmarkTestDrivers[0] = new JiagaraSerializationBenchmarkTestDriver();
			serializationBenchmarkTestDrivers[1] = new KryoSerializationBenchmarkTestDriver();
			serializationBenchmarkTestDrivers[2] = new AvroSerializationBenchmarkTestDriver();
			serializationBenchmarkTestDrivers[3] = new JavaSerializationBenchmarkTestDriver();
			serializationBenchmarkTestDrivers[4] = new CustomSerializationBenchmarkTestDriver();
			long[][] results = new long[serializationBenchmarkTestDrivers.length][10];
			
			for (int run = 0; run < results[0].length; run++) {
				System.out.println("Round " + (run + 1) + "/" + results[0].length + " starts...");
				for (int index = 0; index < serializationBenchmarkTestDrivers.length; index++) {
					SerializationBenchmarkTestDriver driver = serializationBenchmarkTestDrivers[index];
					System.gc();
					System.gc();
					System.gc();
					
					Thread.sleep(2000);
	
					System.gc();
	
					System.out.println("Warmup " + driver.getName() + " ...");
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					long start = System.nanoTime();
					for (int i = 0; i < PRE_SERIALIZATION_COUNT; i++) {
						driver.serialize(driver.getObjectToSerialize(), bos);
					}
					long finish = System.nanoTime();
					System.out.println("Warmup for " + driver.getName() + " has been executed " + PRE_SERIALIZATION_COUNT + 
							           " times in " + TimeUnit.NANOSECONDS.toMillis(finish - start) + " milliseconds ...");
	
					System.gc();
					System.gc();
					System.gc();
					
					Thread.sleep(2000);
					
					System.gc();
					
					bos = new ByteArrayOutputStream();
					start = System.nanoTime();
					for (int i = 0; i < SERIALIZATION_COUNT; i++) {
						driver.serialize(driver.getObjectToSerialize(), bos);
					}
					finish = System.nanoTime();
					System.out.println(driver.getName() + " has been executed " + SERIALIZATION_COUNT + 
							           " times in " + TimeUnit.NANOSECONDS.toMillis(finish - start) + " milliseconds ...");
					
					results[index][run] = (finish - start);
					
					//byte[] output = bos.toByteArray();
					//long outputDataStartAddress = JvmUtil.getArrayBaseAddress(output, byte.class);
					//JvmUtil.dump(outputDataStartAddress, output.length);
				}
				System.out.println("");
			}
			
			for (int index = 0; index < serializationBenchmarkTestDrivers.length; index++) {
				SerializationBenchmarkTestDriver driver = serializationBenchmarkTestDrivers[index];
				
				long result = 0;
				for (int run = 0; run < results[index].length; run++) {
					result += results[index][run];
				}
				
				System.out.println(driver.getName() + " has been executed in avg " + 
									TimeUnit.NANOSECONDS.toMillis(result / results[index].length) + " milliseconds ...");
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
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
			return new ClassToSerialize().randomize();
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
			return new ClassToSerialize().randomize();
		}	
		
	}
	
	private class AvroSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		private DatumWriter<ClassToSerialize> avroWriter;
		
		private AvroSerializationBenchmarkTestDriver() {
			avroWriter = new ReflectDatumWriter<ClassToSerialize>(ClassToSerialize.class);
		}
		
		@Override
		public String getName() {
			return "Avro Serializer";
		}
		
		@Override
		public void serialize(Object o, OutputStream os) {
			try {
				avroWriter.write((ClassToSerialize) o, EncoderFactory.get().binaryEncoder(os, null));
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public Object getObjectToSerialize() {
			return new ClassToSerialize().randomize();
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
			return new SerializableClassToSerialize().randomize();
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
			return new ExternalizableClassToSerialize().randomize();
		}	
		
	}
	
	private interface SerializationBenchmarkTestDriver {
		
		String getName();
		void serialize(Object o, OutputStream os);
		Object getObjectToSerialize();
		
	}
	
}
