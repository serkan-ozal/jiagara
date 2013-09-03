package tr.com.serkanozal.jiagara;

import java.io.FileDescriptor;
import java.lang.reflect.Method;

import tr.com.serkanozal.jiagara.util.JvmUtil;

public class Jiagara {

	static class X {
			
		volatile long l;
			
		public long getL() {
			return l;
		}
			
		public void setL(long l) {
			this.l = l;
		}
			
	}
	
	public static void main(String[] args) throws SecurityException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException {
		Class<?> cls = Class.forName("sun.nio.ch.FileDispatcher");
		Method m = cls.getDeclaredMethod("write", new Class[] {FileDescriptor.class, long.class, int.class });
		m.setAccessible(true);
		System.out.println(m);
	}
	
	private static void test1() throws SecurityException, NoSuchFieldException {
		final int LENGTH = 1000000;
		final long COUNT = 1000000000L;
		
		X[] array = new X[LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			array[i] = new X();
		}
		
		sun.misc.Unsafe unsafe = JvmUtil.getUnsafe();
		
		///////////////////////////////////////////////
		
		long l0a = System.currentTimeMillis();
		for (long i = 0; i < COUNT; i++) {
			X x = array[(int) (i % LENGTH)];
			long j = x.l;
		}	
		long l0b = System.currentTimeMillis();
		System.out.println(l0b - l0a);
		
		///////////////////////////////////////////////
		
		long l1a = System.currentTimeMillis();
		for (long i = 0; i < COUNT; i++) {
			X x = array[(int) (i % LENGTH)];
			long j = x.getL();
		}	
		long l1b = System.currentTimeMillis();
		System.out.println(l1b - l1a);
		
		///////////////////////////////////////////////
		
//		long l3a = System.currentTimeMillis();
//		for (long i = 0; i < COUNT; i++) {
//			X x = array[(int) (i % LENGTH)];
//			long address = JvmUtil.addressOfField(x, "l");
//			long j = unsafe.getLong(address, i);
//		}	
//		long l3b = System.currentTimeMillis();
//		System.out.println(l3b - l3a);
	}

	
	private static void test2() throws SecurityException, NoSuchFieldException {
		final int LENGTH = 1000000;
		final long COUNT = 100000000L;
		
		X[] array = new X[LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			array[i] = new X();
		}
		
		sun.misc.Unsafe unsafe = JvmUtil.getUnsafe();
		
		///////////////////////////////////////////////
		
		long l0a = System.currentTimeMillis();
		for (long i = 0; i < COUNT; i++) {
			X x = array[(int) (i % LENGTH)];
			x.l = i;
		}	
		long l0b = System.currentTimeMillis();
		System.out.println(l0b - l0a);
		
		///////////////////////////////////////////////
		
		long l1a = System.currentTimeMillis();
		for (long i = 0; i < COUNT; i++) {
			X x = array[(int) (i % LENGTH)];
			x.setL(i);
		}	
		long l1b = System.currentTimeMillis();
		System.out.println(l1b - l1a);
		
		///////////////////////////////////////////////
		
		long offset = JvmUtil.getUnsafe().fieldOffset(X.class.getDeclaredField("l"));
		long l2a = System.currentTimeMillis();
		for (long i = 0; i < COUNT; i++) {
			X x = array[(int) (i % LENGTH)];
			unsafe.putLong(x, offset, i);
		}	
		long l2b = System.currentTimeMillis();
		System.out.println(l2b - l2a);
		
		///////////////////////////////////////////////
		
//		long l3a = System.currentTimeMillis();
//		for (long i = 0; i < COUNT; i++) {
//			X x = array[(int) (i % LENGTH)];
//			long address = JvmUtil.addressOfField(x, "l");
//			unsafe.putLong(address, i);
//		}	
//		long l3b = System.currentTimeMillis();
//		System.out.println(l3b - l3a);
	}

}
