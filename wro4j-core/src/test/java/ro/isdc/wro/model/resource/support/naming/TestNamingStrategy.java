/**
 * Copyright Alex Objelean
 */
package ro.isdc.wro.model.resource.support.naming;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ro.isdc.wro.model.resource.support.hash.HashStrategy;
import ro.isdc.wro.model.resource.support.naming.HashEncoderNamingStrategy;
import ro.isdc.wro.model.resource.support.naming.NamingStrategy;


/**
 * Test class for {@link NamingStrategy} implementations.
 * 
 * @author Alex Objelean
 * @created 15 Aug 2010
 */
public class TestNamingStrategy {
  private NamingStrategy namingStrategy;
  private static final String HASH = "HASH";
  
  @Before
  public void setUp() {
    namingStrategy = new HashEncoderNamingStrategy() {
      @Override
      protected HashStrategy newHashStrategy() {
        return new HashStrategy() {
          public String getHash(final InputStream inputStream)
              throws IOException {
            return HASH;
          }
        };
      };
    };
  }
  
  @Test
  public void testWithExtension()
      throws Exception {
    // second argument doesn't matter.
    final String result = namingStrategy.rename("fileName.js", null);
    Assert.assertEquals("fileName-" + HASH + ".js", result);
  }
  
  @Test
  public void testNoExtension()
      throws Exception {
    // second argument doesn't matter.
    final String result = namingStrategy.rename("fileName", null);
    Assert.assertEquals("fileName-" + HASH, result);
  }
}
