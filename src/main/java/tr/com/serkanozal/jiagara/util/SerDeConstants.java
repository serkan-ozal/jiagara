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

	int NULL_ENUM_ORDINAL = 0xFFFFFFFF;
	
	byte NULL = 0x00;
	
	byte OBJECT_NULL = NULL;
	byte OBJECT_DATA = 0x01;
	byte OBJECT_REFERENCE = 0x02;
	byte OBJECT_DATA_WITHOUT_TYPE = 0x03;
	
	byte ARRAY_NULL = NULL;
	byte ARRAY_DATA = 0x01;
	byte ARRAY_REFERENCE = 0x02;
	byte ARRAY_DATA_WITHOUT_TYPE = 0x03;
	
	byte CLASS_NAME_WITHOUT_CODE = 0x00;
	byte CLASS_NAME_WITH_CODE = 0x01;
	byte CLASS_CODE = 0x02;
	
	byte COLLECTION_NULL = NULL;
	byte COLLECTION_WITHOUT_TYPE = 0x01;
	byte COLLECTION_WITH_TYPE = 0x02;
	
	byte MAP_NULL = NULL;
	byte MAP_WITHOUT_KEY_TYPE_AND_WITHOUT_VALUE_TYPE = 0x01;
	byte MAP_WITHOUT_KEY_TYPE_AND_WITH_VALUE_TYPE = 0x02;
	byte MAP_WITH_KEY_TYPE_AND_WITHOUT_VALUE_TYPE = 0x03;
	byte MAP_WITH_KEY_TYPE_AND_WITH_VALUE_TYPE = 0x04;
	
	byte STRING_NULL = NULL;
	byte STRING_DATA_WITHOUT_OPTIMIZATION = 0x01;
	byte STRING_DATA_WITH_OPTIMIZATION = 0x02;
	byte STRING_REFERENCE = 0x03;
	
	byte SIZE_1_BYTE = 0x00;
	byte SIZE_2_BYTE = 0x10;
	byte SIZE_4_BYTE = 0x20;
	byte SIZE_8_BYTE = 0x40;
	
}
