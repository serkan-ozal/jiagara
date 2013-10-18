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

package tr.com.serkanozal.jiagara.util;

/**
 * @author Serkan ÖZAL
 */
public interface SerDeConstants {

	int NULL_STRING_LENGTH = 0xFFFFFFFF;
	int NULL_ARRAY_LENGTH = 0xFFFFFFFF;
	int NULL_ENUM_ORDINAL = 0xFFFFFFFF;
	
	byte OBJECT_NULL = 0x00;
	byte OBJECT_DATA = 0x01;
	byte OBJECT_REFERENCE = 0x02;
	byte OBJECT_DATA_WITHOUT_CLASS_NAME = 0x03;
	
	byte CLASS_NAME_WITHOUT_CODE = 0x00;
	byte CLASS_NAME_WIT_CODE = 0x01;
	byte CLASS_CODE = 0x02;
	
	byte COLLECTION_NULL = 0x00;
	byte COLLECTION_WITHOUT_TYPE = 0x01;
	byte COLLECTION_WITH_TYPE = 0x02;
	
	byte MAP_NULL = 0x00;
	byte MAP_WITHOUT_KEY_TYPE_AND_WITHOUT_VALUE_TYPE = 0x01;
	byte MAP_WITHOUT_KEY_TYPE_AND_WITH_VALUE_TYPE = 0x02;
	byte MAP_WITH_KEY_TYPE_AND_WITHOUT_VALUE_TYPE = 0x03;
	byte MAP_WITH_KEY_TYPE_AND_WITH_VALUE_TYPE = 0x04;
	
}
