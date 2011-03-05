/*
 * Copyright 2010 Henry Coles
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License. 
 */
package org.pitest.functional;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.pitest.functional.predicate.And;
import org.pitest.functional.predicate.Not;
import org.pitest.functional.predicate.Or;
import org.pitest.functional.predicate.Predicate;

/**
 * @author henry
 * 
 */
public abstract class Prelude {

  public final static <A> And<A> and(final Predicate<A>... ps) {
    return new And<A>(Arrays.asList(ps));
  }

  public final static <A> And<A> and(final Iterable<Predicate<A>> ps) {
    return new And<A>(ps);
  }

  public final static <A> Not<A> not(final Predicate<A> p) {
    return new Not<A>(p);
  }

  public final static <A> Or<A> or(final Predicate<A>... ps) {
    return new Or<A>(Arrays.asList(ps));
  }

  public final static <A> Or<A> or(final Iterable<Predicate<A>> ps) {
    return new Or<A>(ps);
  }

  public static <T> Predicate<T> isInstanceOf(final Class<?> clazz) {
    return new Predicate<T>() {
      public Boolean apply(final T a) {
        return clazz.isAssignableFrom(a.getClass());
      }
    };
  };

  public final static <A> SideEffect1<A> accumulateTo(
      final Collection<A> collection) {
    return new SideEffect1<A>() {

      public void apply(final A a) {
        collection.add(a);
      }

    };

  }

  public static <A, B> SideEffect1<A> putToMap(final Map<A, B> map,
      final B value) {
    return new SideEffect1<A>() {
      public void apply(final A key) {
        map.put(key, value);
      }
    };
  }

  public static <A, B> SideEffect1<A> putToMap(final Map<A, B> map,
      final F<A, B> f) {
    return new SideEffect1<A>() {
      public void apply(final A key) {
        map.put(key, f.apply(key));
      }
    };
  }

  public final static <A> F<A, List<A>> toSingletonList() {
    return new F<A, List<A>>() {

      public List<A> apply(final A a) {
        return Collections.singletonList(a);
      }
    };
  }

  public final static <A> F<A, List<A>> toSingletonList(final Class<A> type) {
    return toSingletonList();
  }

  public final static <A> F<A, A> id() {
    return new F<A, A>() {
      public A apply(final A a) {
        return a;
      }
    };
  }

  public final static <A> F<A, A> id(final Class<A> type) {
    return id();
  }

  public final static <T> SideEffect1<T> print() {
    return printTo(System.out);
  }

  public final static <T> SideEffect1<T> print(final Class<T> type) {
    return print();
  }

  public final static <T> SideEffect1<T> printTo(final Class<T> type,
      final PrintStream stream) {
    return printTo(stream);
  }

  public final static <T> SideEffect1<T> printTo(final PrintStream stream) {
    return new SideEffect1<T>() {
      public void apply(final T a) {
        stream.println(a);
      }
    };
  }

  public static <T> SideEffect1<T> printWith(final T t) {
    return new SideEffect1<T>() {
      public void apply(final T a) {
        System.out.println(t + " : " + a);
      }
    };
  }

  public static <T extends Number> Predicate<T> isGreaterThan(final T value) {
    return new Predicate<T>() {
      public Boolean apply(final T o) {
        return o.longValue() > value.longValue();
      }
    };
  }

  public static <T> Predicate<T> isEqualTo(final T value) {
    return new Predicate<T>() {
      public Boolean apply(final T o) {
        return o.equals(value);
      }
    };
  }

  public static <T> Predicate<T> isNotNull() {
    return new Predicate<T>() {
      public Boolean apply(final T o) {
        return (o != null);
      }
    };
  }

  public static <T> Predicate<T> isNull() {
    return new Predicate<T>() {
      public Boolean apply(final T o) {
        return (o == null);
      }
    };
  }

  public static <T> F<T, String> asString() {
    return new F<T, String>() {
      public String apply(final T t) {
        return t.toString();
      }

    };
  }

  public static F2<String, String, String> concatenateWith(
      final String seperator) {
    return new F2<String, String, String>() {
      public String apply(final String a, final String b) {
        return a + seperator + b;
      }
    };
  }
}
