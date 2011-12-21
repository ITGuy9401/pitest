/*
 * Copyright 2011 Henry Coles
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
package org.pitest.testng;

import java.util.Collections;

import org.pitest.PitError;
import org.pitest.extension.ResultCollector;
import org.pitest.internal.ClassLoaderDetectionStrategy;
import org.pitest.internal.IsolationUtils;
import org.pitest.testunit.AbstractTestUnit;
import org.testng.ITestListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestNGTestUnit extends AbstractTestUnit {

  private final ClassLoaderDetectionStrategy classloaderDetection;
  private final Class<?>                     clazz;
  private final String                       method;

  public TestNGTestUnit(ClassLoaderDetectionStrategy classloaderDetection,
      final Class<?> clazz, final String method) {
    super(new org.pitest.Description(method, clazz));
    this.clazz = clazz;
    this.classloaderDetection = classloaderDetection;
    this.method = method;
  }

  public TestNGTestUnit(final Class<?> clazz, String method) {
    this(IsolationUtils.loaderDetectionStrategy(), clazz, method);
  }

  @Override
  public void execute(final ClassLoader loader, final ResultCollector rc) {

    if (this.classloaderDetection.fromDifferentLoader(this.clazz, loader)) {
      throw new PitError(
          "mutation of static initializers not currently supported for TestNG");
    }

    final ITestListener listener = new TestNGAdapter(this.getDescription(), rc);
    final TestNG testng = new TestNG();

    XmlSuite suite = createSuite();

    testng.setUseDefaultListeners(false);

    testng.setXmlSuites(Collections.singletonList(suite));

    testng.addListener(listener);
    testng.run();
  }

  private XmlSuite createSuite() {
    XmlSuite suite = new XmlSuite();
    suite.setName(this.clazz.getName());
    XmlTest test = new XmlTest(suite);
    test.setName(this.method);
    XmlClass xclass = new XmlClass(this.clazz.getName());
    XmlInclude include = new XmlInclude(this.method);
    xclass.setIncludedMethods(Collections.singletonList(include));
    test.setXmlClasses(Collections.singletonList(xclass));

    return suite;
  }

}
