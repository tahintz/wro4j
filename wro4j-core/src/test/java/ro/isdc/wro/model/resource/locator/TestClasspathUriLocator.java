/*
 * Copyright (c) 2008. All rights reserved.
 */
package ro.isdc.wro.model.resource.locator;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ro.isdc.wro.config.Context;
import ro.isdc.wro.util.WroTestUtils;


/**
 * Tests if {@link ClasspathUriLocator} works properly.
 *
 * @author Alex Objelean
 * @created Created on Nov 3, 2008
 */
public class TestClasspathUriLocator {
  /**
   * UriLocator to test.
   */
  private ClasspathUriLocator uriLocator;

  @Before
  public void init() {
    Context.set(Context.standaloneContext());
    uriLocator = new ClasspathUriLocator();
  }

  private String createUri(final String location) {
    return ClasspathUriLocator.PREFIX + location;
  }

  @Test
  public void resourceAvailable()
      throws IOException {
    Assert.assertNotNull(uriLocator.locate(createUri("test.css")));
  }

  @Test
  public void resourceAvailableWithTrailingSpaces()
      throws IOException {
    Assert.assertNotNull(uriLocator.locate(createUri(" test.css ")));
  }
  
  @Test(expected = IOException.class)
  public void cannotDetectInexistentResourcesWithWildcard()
      throws IOException {
    uriLocator.locate(createUri("*.NOTEXIST"));
  }

  @Test
  public void testWildcard1Resources()
      throws IOException {
    WroTestUtils.compare(uriLocator.locate(createUri("ro/isdc/wro/http/*.merged")),
        uriLocator.locate(createUri("ro/isdc/wro/http/*.css")));
  }

  @Test
  public void testWildcard2Resources()
      throws IOException {
    uriLocator.locate(createUri("ro/isdc/wro/http/*.cs?"));
  }

  @Test
  public void testWildcard3Resources()
      throws IOException {
    uriLocator.locate(createUri("ro/isdc/wro/http/*.???"));
  }

  @Test
  public void shouldLocateRecursiveWildcardResources()
      throws IOException {
    uriLocator.locate(createUri("ro/isdc/wro/http/**.css"));
  }
  
  @Test(expected = IOException.class)
  public void shouldNotLocateWildcardResourcesWhenWildcardIsDisabled()
      throws IOException {
    uriLocator.setEnableWildcards(false).locate(createUri("**.css"));
  }

  @Test
  public void testRecursiveWildcard4Resources()
      throws IOException {
    uriLocator.locate(createUri("ro/isdc/wro/**.cs?"));
  }

  @Test(expected = IOException.class)
  public void resourceUnavailable() throws Exception {
    uriLocator.locate(createUri("123123.css"));
  }
}
