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

package tr.com.serkanozal.jiagara.domain.builder.buffer;

import tr.com.serkanozal.jiagara.domain.builder.Builder;
import tr.com.serkanozal.jiagara.domain.model.buffer.Buffer;

/**
 * @author Serkan ÖZAL
 */
public class BufferBuilder implements Builder<Buffer> {

	private byte[] buffer;
	private int size;

	public BufferBuilder buffer(byte[] buffer) {
		this.buffer = buffer;
		return this;
	}

	public BufferBuilder size(int size) {
		this.size = size;
		return this;
	}
	
	@Override
	public Buffer build() {
		if (buffer != null) {
			return new Buffer(buffer);
		}
		else if (size != 0) {
			return new Buffer(size);
		}
		else {
			return new Buffer();
		}
	}
	
}