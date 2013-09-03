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
public class AssertUtil {

	public static final String DEFAULT_NULL_MESSAGE_TEMPLATE = "$0 must be null";
	public static final String DEFAULT_NOT_NULL_MESSAGE_TEMPLATE = "$0 cannot be null";
	
	public static final String DEFAULT_ZERO_MESSAGE_TEMPLATE = "$0 must be zero";
	public static final String DEFAULT_NOT_ZERO_MESSAGE_TEMPLATE = "$0 cannot be zero";
	
	public static final String DEFAULT_NEGATIVE_MESSAGE_TEMPLATE = "$0 must be negative: $1";
	public static final String DEFAULT_NOT_NEGATIVE_MESSAGE_TEMPLATE = "$0 cannot be negative: $1";
	
	public static final String DEFAULT_POSITIVE_MESSAGE_TEMPLATE = "$0 must be positive: $1";
	public static final String DEFAULT_NOT_POSITIVE_MESSAGE_TEMPLATE = "$0 cannot be positive: $1";
	
	public static final String DEFAULT_EQUAL_MESSAGE_TEMPLATE = "$0 must be equal to $2: $1";
	public static final String DEFAULT_NOT_EQUAL_MESSAGE_TEMPLATE = "$0 cannot be equal to $2: $1";
	
	public static final String DEFAULT_SMALLER_MESSAGE_TEMPLATE = "$0 must be smaller than $2: $1";
	public static final String DEFAULT_NOT_SMALLER_MESSAGE_TEMPLATE = "$0 cannot be smaller than $2: $1";
	
	public static final String DEFAULT_EQUAL_OR_SMALLER_MESSAGE_TEMPLATE = "$0 must be equal or smaller than $2: $1";
	public static final String DEFAULT_NOT_EQUAL_OR_SMALLER_MESSAGE_TEMPLATE = "$0 cannot be equal or smaller than $2: $1";
	
	public static final String DEFAULT_EQUAL_OR_BIGGER_MESSAGE_TEMPLATE = "$0 must be equal or bigger than $2: $1";
	public static final String DEFAULT_NOT_EQUAL_OR_BIGGER_MESSAGE_TEMPLATE = "$0 cannot be equal or bigger than $2: $1";
	
	public static final String DEFAULT_EMPTY_MESSAGE_TEMPLATE = "$0 must be empty string: $1";
	public static final String DEFAULT_NOT_EMPTY_MESSAGE_TEMPLATE = "$0 cannot be empty string: $1";
	
	public static final String DEFAULT_HAS_LENGTH_MESSAGE_TEMPLATE = "$0 must be $2 length string: $1";
	public static final String DEFAULT_HAS_NOT_LENGTH_MESSAGE_TEMPLATE = "$0 cannot be $2 length string: $1";
	
	public static final String DEFAULT_HAS_MIN_LENGTH_MESSAGE_TEMPLATE = "$0 must be $2 length string at minimum: $1";
	public static final String DEFAULT_HAS_NOT_MIN_LENGTH_MESSAGE_TEMPLATE = "$0 cannot be $2 length string at minimum: $1";
	
	public static final String DEFAULT_HAS_MAX_LENGTH_MESSAGE_TEMPLATE = "$0 must be $2 length string at maximum: $1";
	public static final String DEFAULT_HAS_NOT_MAX_LENGTH_MESSAGE_TEMPLATE = "$0 cannot be $2 length string at maximum: $1";
	
	private AssertUtil() {
		
	}
	
	public static void isTrue(boolean condition, String message) {
		if (condition == false) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void notTrue(boolean condition, String message) {
		if (condition == true) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void isFalse(boolean condition, String message) {
		if (condition == true) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void notFalse(boolean condition, String message) {
		if (condition == false) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void isNull(Object obj, String message) {
		if (obj != null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void notNull(Object obj, String message) {
		if (obj == null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void zero(long value, String message) {
		if (value != 0) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
		}
	}
	
	public static void notZero(long value, String message) {
		if (value == 0) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
		}
	}
	
	public static void negative(long value, String message) {
		if (value >= 0) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
		}
	}
	
	public static void notNegative(long value, String message) {
		if (value < 0) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
		}
	}
	
	public static void positive(long value, String message) {
		if (value <= 0) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
		}
	}
	
	public static void notPositive(long value, String message) {
		if (value > 0) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
		}
	}
	
	public static void equal(long value, long eq, String message) {
		if (value != eq) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, eq));
		}
	}
	
	public static void notEqual(long value, long eq, String message) {
		if (value == eq) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, eq));
		}
	}
	
	public static void smaller(long value, long limit, String message) {
		if (value >= limit) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, limit));
		}
	}
	
	public static void notSmaller(long value, long limit, String message) {
		if (value < limit) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, limit));
		}
	}
	
	public static void equalOrSmaller(long value, long limit, String message) {
		if (value > limit) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, limit));
		}
	}
	
	public static void notEqualOrSmaller(long value, long limit, String message) {
		if (value <= limit) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, limit));
		}
	}
	
	public static void bigger(long value, long limit, String message) {
		if (value <= limit) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, limit));
		}
	}
	
	public static void notBigger(long value, long limit, String message) {
		if (value > limit) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, limit));
		}
	}
	
	public static void equalOrBigger(long value, long limit, String message) {
		if (value < limit) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, limit));
		}
	}
	
	public static void notEqualOrBigger(long value, long limit, String message) {
		if (value >= limit) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, limit));
		}
	}
	
	public static void empty(String value, String message) {
		if (value != null && value.trim().length() > 0) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
		}
	}
	
	public static void notEmpty(String value, String message) {
		if (value == null || value.trim().length() == 0) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
		}
	}
	
	public static void hasLength(String value, int length, String message) {
		notNegative(length, "Length cannot be negative: $0");
		if (value == null) {
			if (length != 0) {
				throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
			}	
		}
		else if (value.length() != length) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
		}
	}
	
	public static void hasNotLength(String value, int length, String message) {
		notNegative(length, "Length cannot be negative: $0");
		if (value == null) {
			if (length == 0) {
				throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
			}	
		}
		else if (value.length() == length) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value));
		}
	}
	
	public static void hasMinLength(String value, int minLength, String message) {
		notNegative(minLength, "Length cannot be negative: $0");
		if (value == null) {
			if (minLength != 0) {
				throw new IllegalArgumentException(StringUtil.bindArguments(message, value, minLength));
			}	
		}
		else if (value.length() < minLength) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, minLength));
		}
	}
	
	public static void hasNotMinLength(String value, int minLength, String message) {
		notNegative(minLength, "Length cannot be negative: $0");
		if (value == null) {
			if (minLength == 0) {
				throw new IllegalArgumentException(StringUtil.bindArguments(message, value, minLength));
			}	
		}
		else if (value.length() >= minLength) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, minLength));
		}
	}
	
	public static void hasMaxLength(String value, int maxLength, String message) {
		notNegative(maxLength, "Length cannot be negative: $0");
		if (value == null) {
			if (maxLength != 0) {
				throw new IllegalArgumentException(StringUtil.bindArguments(message, value, maxLength));
			}	
		}
		else if (value.length() > maxLength) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, maxLength));
		}
	}
	
	public static void hasNotMaxLength(String value, int maxLength, String message) {
		notNegative(maxLength, "Length cannot be negative: $0");
		if (value == null) {
			if (maxLength == 0) {
				throw new IllegalArgumentException(StringUtil.bindArguments(message, value, maxLength));
			}	
		}
		else if (value.length() <= maxLength) {
			throw new IllegalArgumentException(StringUtil.bindArguments(message, value, maxLength));
		}
	}
	
}
