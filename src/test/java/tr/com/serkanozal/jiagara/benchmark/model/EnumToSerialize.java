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

package tr.com.serkanozal.jiagara.benchmark.model;

import java.io.Serializable;

/**
 * @author Serkan ÖZAL
 */
public enum EnumToSerialize implements Serializable {

	ENUM_1(1, "Enum 1"),
	ENUM_2(2, "Enum 2"),
	ENUM_3(3, "Enum 3"),
	ENUM_4(4, "Enum 4"),
	ENUM_5(5, "Enum 5"),
	ENUM_6(6, "Enum 6"),
	ENUM_7(7, "Enum 7"),
	ENUM_8(8, "Enum 8"),
	ENUM_9(9, "Enum 9");
	
	int code;
	String text;
	
	EnumToSerialize(int code, String text) {
		this.code = code;
		this.text = text;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getText() {
		return text;
	}
	
	public static EnumToSerialize random() {
		return values()[(int) (Math.random() * 9)];
	}
	
}
