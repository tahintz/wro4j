package ro.isdc.wro.util.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.ServiceRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.util.WroUtil;


/**
 * Helps to find available providers of any supported type.
 * 
 * @author Alex Objelean
 * @created 16 Jun 2012
 * @since 1.4.7
 */
public class ProviderFinder<T> {
  private static final Logger LOG = LoggerFactory.getLogger(ProviderFinder.class);
  
  private Class<T> type;
  
  /**
   * @VisibleForTesting.
   * @param type
   *          the type of providers to find.
   */
  ProviderFinder(final Class<T> type) {
    this.type = type;
  }
  
  /**
   * Creates a {@link ProviderFinder} which will find providers of type provided as argument..
   * 
   * @param type
   *          the type of providers to search.
   * @return {@link ProviderFinder} handling providers lookup.
   */
  public static <T> ProviderFinder<T> of(Class<T> type) {
    return new ProviderFinder<T>(type);
  }
  
  /**
   * @return the list of all providers found in classpath.
   */
  public List<T> find() {
    final List<T> providers = new ArrayList<T>();
    try {
      final Iterator<T> iterator = lookupProviders(type);
      for (; iterator.hasNext();) {
        final T provider = iterator.next();
        LOG.debug("found provider: {}", provider);
        providers.add(provider);
      }
      collectConfigurableProviders(providers);
    } catch (Exception e) {
      LOG.error("Failed to discover providers using ServiceRegistry. Cannot continue...", e);
      WroUtil.wrapWithWroRuntimeException(e);
    }
    return providers;
  }
  
  /**
   * Collects also providers of type {@link ConfigurableProvider} if the T type is a supertype of
   * {@link ConfigurableProvider}.
   * 
   * @param providers
   *          the list where found providers will be added.
   */
  @SuppressWarnings("unchecked")
  private void collectConfigurableProviders(final List<T> providers) {
    if (type.isAssignableFrom(ConfigurableProvider.class)) {
      final Iterator<ConfigurableProvider> iterator = lookupProviders(ConfigurableProvider.class);
      for (; iterator.hasNext();) {
        T provider = (T) iterator.next();
        LOG.debug("found provider: {}", provider);
        providers.add(provider);
      }
    }
  }
  
  /**
   * This method is useful for mocking the lookup operation.
   * 
   * @param clazz
   *          the class of the provider to lookup.
   * @VisibleForTesting
   * @return the iterator of found providers.
   */
  <P> Iterator<P> lookupProviders(Class<P> clazz) {
    LOG.debug("searching for providers of type : {}", clazz);
    return ServiceRegistry.lookupProviders(clazz);
  }
}
