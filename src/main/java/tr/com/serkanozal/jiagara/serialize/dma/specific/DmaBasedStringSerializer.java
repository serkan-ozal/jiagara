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

package tr.com.serkanozal.jiagara.serialize.dma.specific;

import java.lang.reflect.Field;

import tr.com.serkanozal.jiagara.serialize.dma.AbstractDirectMemoryAccessBasedFieldAndDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.data.DirectMemoryAccessBasedDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.field.DirectMemoryAccessBasedFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;

/**
 * @author Serkan ÖZAL
 */
public class DmaBasedStringSerializer<T> extends AbstractDirectMemoryAccessBasedFieldAndDataSerializer<T, DirectMemoryAccessBasedOutputWriter>  
		implements DirectMemoryAccessBasedFieldSerializer<T>, DirectMemoryAccessBasedDataSerializer<T> {
		
	private StringSerializer stringSerializer;
	private boolean optimized = true;
	
	public DmaBasedStringSerializer(Field field) {
		super(field);
		if (optimized) {
			stringSerializer = new OptimizedStringSerializer();
		}
		else {
			stringSerializer = new NonOptimizedStringSerializer();
		}
	}
	
	public DmaBasedStringSerializer(Class<T> clazz) {
		super(clazz);
		if (optimized) {
			stringSerializer = new OptimizedStringSerializer();
		}
		else {
			stringSerializer = new NonOptimizedStringSerializer();
		}
	}
	
	@Override
	public void serializeField(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		stringSerializer.serializeField(obj, fieldOffset, outputWriter);
	}

	@Override
	public void serializeDataContent(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		String o = (String)obj;
		if (o == null) {
			outputWriter.writeNull();
		}
		else {
			stringSerializer.serializeData(o, outputWriter);
		}
	}
	
	private boolean isAscii(String s) {
		int charCount = s.length();
		for (int i = 0; i < charCount; i++) {
            int c = s.charAt(i);
            if (c > 127) {
            	return false;
            }
		}
		return true;
	}
	
	private interface StringSerializer {
		
		void serializeField(Object obj, long fieldOffset, DirectMemoryAccessBasedOutputWriter outputWriter);
		void serializeData(String str, DirectMemoryAccessBasedOutputWriter outputWriter);
		
	}
	
	private class NonOptimizedStringSerializer implements StringSerializer {

		@Override
		public void serializeField(Object obj, long fieldOffset, DirectMemoryAccessBasedOutputWriter outputWriter) {
			outputWriter.writeString(obj, fieldOffset);
		}
		
		@Override
		public void serializeData(String str, DirectMemoryAccessBasedOutputWriter outputWriter) {
			outputWriter.write(str);
		}
		
	}
	
	private class OptimizedStringSerializer implements StringSerializer {

		@SuppressWarnings("restriction")
		@Override
		public void serializeField(Object obj, long fieldOffset, DirectMemoryAccessBasedOutputWriter outputWriter) {
			serializeData((String)unsafe.getObject(obj, fieldOffset), outputWriter);
		}
		
		@Override
		public void serializeData(String str, DirectMemoryAccessBasedOutputWriter outputWriter) {
			if (str == null) {
				outputWriter.writeNull();
			}
			else {
				boolean isAscii = isAscii(str);
				if (isAscii) {
					outputWriter.writeAscii(str);
				}
				else {
					outputWriter.write(str);
				}
			}
		}
		
		/*
		private class Serializer {
		
			private static final int MAX_BLOCK_SIZE = 1024;
			private static final int CHAR_BUF_SIZE = 256;
			private final byte[] buf = new byte[MAX_BLOCK_SIZE];
			private final char[] cbuf = new char[CHAR_BUF_SIZE];
			private int pos = 0;
			private ByteBuffer buffer;
			private DirectMemoryAccessBasedOutputWriter outputWriter;
			
			private Serializer(int maxLength, DirectMemoryAccessBasedOutputWriter outputWriter) {
				this.outputWriter = outputWriter;
				this.buffer = ByteBuffer.allocateDirect(maxLength);
			}
			
			private void serialize(String str) {
				
			}
			
			private boolean isAscii(String s) {
				int charCount = s.length();
				for (int i = 0; i < charCount; i++) {
                    int c = s.charAt(i);
                    if (c > 127) {
                    	return false;
                    }
				}
				return true;
			}
			
			/*
			private long getUTFLength(String s) {
			    int len = s.length();
			    long utflen = 0;
			    for (int off = 0; off < len; ) {
					int csize = Math.min(len - off, CHAR_BUF_SIZE);
					s.getChars(off, off + csize, cbuf, 0);
					for (int cpos = 0; cpos < csize; cpos++) {
					    char c = cbuf[cpos];
					    if (c >= 0x0001 && c <= 0x007F) {
					    	utflen++;
					    } 
					    else if (c > 0x07FF) {
					    	utflen += 3;
					    } 
					    else {
					    	utflen += 2;
					    }
					}
					off += csize;
				}
			    return utflen;
			}
	
			private void writeUTF(String s, long utflen) throws IOException {
			    if (utflen > 0xFFFFL) {
			    	throw new UTFDataFormatException();
			    }
			    writeShort((int) utflen);
			    if (utflen == (long) s.length()) {
			    	writeBytes(s);
			    } 
			    else {
			    	writeUTFBody(s);
			    }
			}
	
			@SuppressWarnings("unused")
			private void writeLongUTF(String s) throws IOException {
			    writeLongUTF(s, getUTFLength(s));
			}
	
			private void writeLongUTF(String s, long utflen) throws IOException {
			    writeLong(utflen);
			    if (utflen == (long) s.length()) {
			    	writeBytes(s);
			    } 
			    else {
			    	writeUTFBody(s);
			    }
			}
	
			private void writeUTFBody(String s) throws IOException {
			    int limit = MAX_BLOCK_SIZE - 3;
			    int len = s.length();
			    for (int off = 0; off < len; ) {
					int csize = Math.min(len - off, CHAR_BUF_SIZE);
					s.getChars(off, off + csize, cbuf, 0);
					for (int cpos = 0; cpos < csize; cpos++) {
					    char c = cbuf[cpos];
					    if (pos <= limit) {
							if (c <= 0x007F && c != 0) {
							    buf[pos++] = (byte) c;
							} 
							else if (c > 0x07FF) {
							    buf[pos + 2] = (byte) (0x80 | ((c >> 0) & 0x3F));
							    buf[pos + 1] = (byte) (0x80 | ((c >> 6) & 0x3F));
							    buf[pos + 0] = (byte) (0xE0 | ((c >> 12) & 0x0F));
							    pos += 3;
							} 
							else {
							    buf[pos + 1] = (byte) (0x80 | ((c >> 0) & 0x3F));
							    buf[pos + 0] = (byte) (0xC0 | ((c >> 6) & 0x1F));
							    pos += 2;
							}
					    } 
					    else { 	// write one byte at a time to normalize block
							if (c <= 0x007F && c != 0) {
							    write(c);
							} 
							else if (c > 0x07FF) {
							    write(0xE0 | ((c >> 12) & 0x0F));
							    write(0x80 | ((c >> 6) & 0x3F));
							    write(0x80 | ((c >> 0) & 0x3F));
							} 
							else {
							    write(0xC0 | ((c >> 6) & 0x1F));
							    write(0x80 | ((c >> 0) & 0x3F));
							}
					    }
					}
					off += csize;
			    }
			}
			
		}
		*/
	}
	
}
