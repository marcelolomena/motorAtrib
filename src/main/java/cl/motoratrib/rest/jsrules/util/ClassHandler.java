/*
 * The MIT License
 *
 * Copyright 2018 Marcelo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cl.motoratrib.rest.jsrules.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import cl.motoratrib.rest.jsrules.exception.ClassHandlerException;
import org.joda.time.DateTime;
import org.apache.commons.lang3.time.DateUtils;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Marcelo
 */
public enum ClassHandler {
    BOOLEAN {
        @Override
        public Class getMyClass() {
            return Boolean.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Boolean convertString(String string) {
            return Boolean.parseBoolean(string);
        }
    },
    DOUBLE {
        @Override
        public Class getMyClass() {
            return Double.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Double convertString(String string) {
            return Double.parseDouble(string);
        }
    },
    LONG {
        @Override
        public Class getMyClass() {
            return Long.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Long convertString(String string) {
            return Long.parseLong(string);
        }
    },
    STRING {
        @Override
        public Class getMyClass() {
            return String.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public String convertString(String string) {
            return string;
        }
    },
    STRINGSET {
        @Override
        public Class getMyClass() {
            return Set.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Set<String> convertString(String string) throws ClassHandlerException {
            Set<String> stringSet;

            try {
                stringSet = new HashSet<String>(Arrays.asList(string.split(",")));
            } catch (Exception ex) {
                throw new ClassHandlerException(ACTION_1 + string + ACTION_3, ex);
            }

            return stringSet;
        }
    },
    NUMBERSET {
        @Override
        public Class getMyClass() {
            return Set.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Set<Number> convertString(String string) throws ClassHandlerException {

            Set<Number> numberSet;
                    
            try {
                numberSet = MAPPER.readValue(string, Set.class);
            } catch (IOException ex) {
                throw new ClassHandlerException(ACTION_1 + string + ACTION_2, ex);
            }

            return numberSet;
        }
    },
    DATETIME {
        @Override
        public Class getMyClass() {
            return DateTime.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public DateTime convertString(String string) throws ClassHandlerException {
            DateTime dateTime;

            try {
                dateTime = DateTime.parse(string);
            } catch (IllegalArgumentException ex) {
                throw new ClassHandlerException("Invalid date string: " + string, ex);
            }

            return dateTime;
        }
    },
    TODAYADDNUM {
        @Override
        public Class getMyClass() {
            return DateTime.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public DateTime convertString(String string) throws ClassHandlerException {
            DateTime dateTime;

            try {
                Date toDate=DateUtils.addDays(new Date(), Integer.parseInt(string));
                dateTime = new DateTime(toDate);
            } catch (IllegalArgumentException ex) {
                throw new ClassHandlerException("Invalid date string: " + string, ex);
            }

            return dateTime;
        }
    },
    DATESET {
        @Override
        public Class getMyClass() {
            return Set.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Set<DateTime> convertString(String string) throws ClassHandlerException {
            Set<DateTime> dateSet;

            try {
                dateSet = MAPPER.readValue(string, Set.class);
            } catch (IOException ex) {
                throw new ClassHandlerException(ACTION_1 + string + ACTION_4, ex);
            }

            return dateSet;
        }
    };
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String ACTION_1 = "Unable to convert ";
    private static final String ACTION_2 = " into a Set of Numbers";
    private static final String ACTION_3 = " into a Set of Strings";
    private static final String ACTION_4 = " into a Set of Dates";
    public abstract Class getMyClass();
    public abstract <T> T convertString(String string) throws ClassHandlerException;


}
