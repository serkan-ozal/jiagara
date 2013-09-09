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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
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
// -XX:PermSize=512M -XX:MaxPermSize=1G -Xms1G -Xmx2G
public class SerializationBenchmarkTest implements Serializable {

	private static final int PRE_SERIALIZATION_COUNT = 10000;
	private static final int SERIALIZATION_COUNT = 10000;
	private static final int ROUND_COUNT = 10;
	
	//@Test
	public void runSerializationBenchmarkTestDrivers() {
		try {
			JvmUtil.info();

			SerializationBenchmarkTestDriver[] serializationBenchmarkTestDrivers = new SerializationBenchmarkTestDriver[5];
			serializationBenchmarkTestDrivers[0] = new JiagaraSerializationBenchmarkTestDriver();
			serializationBenchmarkTestDrivers[1] = new KryoSerializationBenchmarkTestDriver();
			serializationBenchmarkTestDrivers[2] = new AvroSerializationBenchmarkTestDriver();
			serializationBenchmarkTestDrivers[3] = new JavaSerializationBenchmarkTestDriver();
			serializationBenchmarkTestDrivers[4] = new CustomSerializationBenchmarkTestDriver();
			long[][] results = new long[serializationBenchmarkTestDrivers.length][ROUND_COUNT];
			
			for (int run = 0; run < results[0].length; run++) {
				System.out.println("Round " + (run + 1) + "/" + results[0].length + " starts...");
				for (int index = 0; index < serializationBenchmarkTestDrivers.length; index++) {
					SerializationBenchmarkTestDriver driver = serializationBenchmarkTestDrivers[index];
					
					ByteArrayOutputStream bos;
					long start, finish;
					
					System.gc();
					System.gc();
					System.gc();
					
					Thread.sleep(2000);
	
					System.gc();
	
					System.out.println("Warmup " + driver.getName() + " ...");
					bos = new ByteArrayOutputStream();
					start = System.nanoTime();
					for (int i = 0; i < PRE_SERIALIZATION_COUNT; i++) {
						driver.serialize(driver.getObjectToSerialize(), bos);
					}
					finish = System.nanoTime();
					System.out.println("Warmup for " + driver.getName() + " executed " + PRE_SERIALIZATION_COUNT + 
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
					driver.release(bos);
					finish = System.nanoTime();
					
					byte[] output = bos.toByteArray();
					long serializedDataSize = output.length;
					
					
					System.out.println(driver.getName() + " executed " + SERIALIZATION_COUNT + 
							           " times and serialized " + serializedDataSize + " bytes data in " + 
							           TimeUnit.NANOSECONDS.toMillis(finish - start) + " milliseconds ...");
					
					results[index][run] = (finish - start);
					
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
	
	@Test
	public void runSerializationBenchmarkTestDriversAsSeperateProcesses() {
		runSerializationBenchmarkTestDriversInSeperateProcess();
	}	
	
	public static void main(String[] args) {
		JvmUtil.info();
		
		long executionTime = runSerializationBenchmarkTestDriver(args[0]);
		System.out.println(executionTime);
	}

	@SuppressWarnings("unchecked")
	private static void runSerializationBenchmarkTestDriversInSeperateProcess() {
		Class<? extends SerializationBenchmarkTestDriver>[] testDriverClasses = 
				new Class[] {
					JiagaraSerializationBenchmarkTestDriver.class,
					KryoSerializationBenchmarkTestDriver.class,
					AvroSerializationBenchmarkTestDriver.class,
					JavaSerializationBenchmarkTestDriver.class,
					CustomSerializationBenchmarkTestDriver.class
				};
		long[] executionTimes = new long[testDriverClasses.length];
		
		int i = 0;
		for (Class<? extends SerializationBenchmarkTestDriver> testDriverClass : testDriverClasses) {
			executionTimes[i++] = runSerializationBenchmarkTestDriverInSeperateProcess(testDriverClass);
		}
		
		for (int j = 0; j < executionTimes.length; j++) {
			System.out.println(testDriverClasses[j].getName() + " has been executed in " + executionTimes[j] + " milliseconds");
		}
	}
	
	private static long runSerializationBenchmarkTestDriverInSeperateProcess(
			Class<? extends SerializationBenchmarkTestDriver> testDriverClass) {
		try {
			String processCmd = 
					"java" + " -cp " + "\"" + System.getProperty("java.class.path") + "\"" + " " + 
					"-XX:PermSize=512M -XX:MaxPermSize=1G -Xms1G -Xmx2G " + 
					SerializationBenchmarkTest.class.getName() + " " +  testDriverClass.getName();

			Process proc = Runtime.getRuntime().exec(processCmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			String line;
			String lastValidLine = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				lastValidLine = line;
			}

			BufferedReader brErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String lineErr;
			while ((lineErr = brErr.readLine()) != null) {
				System.out.println(lineErr);
			}
			
			proc.waitFor();
			
			if (lastValidLine != null) {
				return Long.parseLong(lastValidLine);
			}
			else {
				return -1;
			}	
		}
		catch (Throwable t) {
			System.out.println(t.getMessage());
			t.printStackTrace();
			return -1;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static long runSerializationBenchmarkTestDriver(String testDriverClassName) {
		try {
			return runSerializationBenchmarkTestDriver((Class<? extends SerializationBenchmarkTestDriver>)Class.forName(testDriverClassName));
		}
		catch (Throwable t) {
			System.out.println(t.getMessage());
			t.printStackTrace();
			return -1;
		}	
	}
	
	private static long runSerializationBenchmarkTestDriver(Class<? extends SerializationBenchmarkTestDriver> testDriverClass) {
		try {
			SerializationBenchmarkTestDriver driver = testDriverClass.newInstance();
			long[] results = new long[ROUND_COUNT];
			
			for (int run = 0; run < results.length; run++) {
				System.out.println("Round " + (run + 1) + "/" + results.length + " starts...");
				ByteArrayOutputStream bos;
				long start, finish;
					
				System.gc();
				System.gc();
				System.gc();
					
				Thread.sleep(2000);
	
				System.gc();
	
				System.out.println("Warmup " + driver.getName() + " ...");
				bos = new ByteArrayOutputStream();
				start = System.nanoTime();
				for (int i = 0; i < PRE_SERIALIZATION_COUNT; i++) {
					driver.serialize(driver.getObjectToSerialize(), bos);
				}
				finish = System.nanoTime();
				System.out.println("Warmup for " + driver.getName() + " executed " + PRE_SERIALIZATION_COUNT + 
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
				driver.release(bos);
				finish = System.nanoTime();
					
				byte[] output = bos.toByteArray();
				long serializedDataSize = output.length;
					
				System.out.println(driver.getName() + " executed " + SERIALIZATION_COUNT + 
							       " times and serialized " + serializedDataSize + " bytes data in " + 
							       TimeUnit.NANOSECONDS.toMillis(finish - start) + " milliseconds ...");
					
				results[run] = (finish - start);
					
				//long outputDataStartAddress = JvmUtil.getArrayBaseAddress(output, byte.class);
				//JvmUtil.dump(outputDataStartAddress, output.length);
			}
			System.out.println("");

			long result = 0;
			for (int run = 0; run < results.length; run++) {
				result += results[run];
			}
				
			long timeInMilliseconds =  TimeUnit.NANOSECONDS.toMillis(result / results.length);
			
			System.out.println(driver.getName() + " has been executed in avg " + timeInMilliseconds + " milliseconds ...");
			
			return timeInMilliseconds;
		}
		catch (Throwable t) {
			System.out.println(t.getMessage());
			t.printStackTrace();
			return -1;
		}
	}

	private static class JiagaraSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		@SuppressWarnings("unused")
		private SerializerService serializerService;
		private Serializer<ClassToSerialize> serializer;
		
		JiagaraSerializationBenchmarkTestDriver() {
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
		
		@Override
		public void release(OutputStream os) {
			serializer.release(os);
		}
		
	}
	
	private static class KryoSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		private Kryo kryo;
		private Map<OutputStream, Output> outputMap = new HashMap<OutputStream, Output>();
		
		KryoSerializationBenchmarkTestDriver() {
			kryo = new Kryo();
		}
		
		@Override
		public String getName() {
			return "Kryo Serializer";
		}
		
		@Override
		public void serialize(Object o, OutputStream os) {
			Output output = outputMap.get(os);
			if (output == null) {
				output = new Output(os);
				outputMap.put(os, output);
			}
			kryo.writeObject(output, o);
		}
		
		@Override
		public Object getObjectToSerialize() {
			return new ClassToSerialize().randomize();
		}	
		
		@Override
		public void release(OutputStream os) {
			try {
				Output output = outputMap.get(os);
				if (output != null) {
					output.flush();
				}
				os.flush();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class AvroSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		private DatumWriter<ClassToSerialize> avroWriter;
		private Map<OutputStream, Encoder> encoderMap = new HashMap<OutputStream, Encoder>();
		
		AvroSerializationBenchmarkTestDriver() {
			avroWriter = new ReflectDatumWriter<ClassToSerialize>(ClassToSerialize.class);
		}
		
		@Override
		public String getName() {
			return "Avro Serializer";
		}
		
		@Override
		public void serialize(Object o, OutputStream os) {
			try {
				Encoder encoder = encoderMap.get(os);
				if (encoder == null) {
					encoder = EncoderFactory.get().binaryEncoder(os, null);
					encoderMap.put(os, encoder);
				}
				avroWriter.write((ClassToSerialize)o, encoder);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public Object getObjectToSerialize() {
			return new ClassToSerialize().randomize();
		}	
		
		@Override
		public void release(OutputStream os) {
			try {
				Encoder encoder = encoderMap.get(os);
				if (encoder != null) {
					encoder.flush();
				}
				os.flush();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static class JavaSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		private ObjectOutputStream oos;
		
		JavaSerializationBenchmarkTestDriver() {
			
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
		
		@Override
		public void release(OutputStream os) {
			try {
				os.flush();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static class CustomSerializationBenchmarkTestDriver implements SerializationBenchmarkTestDriver {
		
		private ObjectOutputStream oos;
		
		CustomSerializationBenchmarkTestDriver() {
			
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
		
		@Override
		public void release(OutputStream os) {
			try {
				os.flush();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static interface SerializationBenchmarkTestDriver {
		
		String getName();
		void serialize(Object o, OutputStream os);
		Object getObjectToSerialize();
		void release(OutputStream os);
		
	}
	
}
