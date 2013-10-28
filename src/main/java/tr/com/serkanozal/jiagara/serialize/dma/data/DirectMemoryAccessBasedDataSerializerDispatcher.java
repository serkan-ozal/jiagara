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

package tr.com.serkanozal.jiagara.serialize.dma.data;

import tr.com.serkanozal.jiagara.serialize.data.DataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.serialize.writer.OutputWriter;

/**
 * @author Serkan ÖZAL
 */
public class DirectMemoryAccessBasedDataSerializerDispatcher<T> implements DirectMemoryAccessBasedDataSerializer<T> {

	private DataSerializer<T, OutputWriter> dataSerializer;
	
	public DirectMemoryAccessBasedDataSerializerDispatcher(DataSerializer<T, OutputWriter> dataSerializer) {
		this.dataSerializer = dataSerializer;
	}

	@Override
	public void serializeData(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		dataSerializer.serializeData(obj, outputWriter);
	}

	@Override
	public void serializeDataContent(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		dataSerializer.serializeDataContent(obj, outputWriter);
	}
	
}
