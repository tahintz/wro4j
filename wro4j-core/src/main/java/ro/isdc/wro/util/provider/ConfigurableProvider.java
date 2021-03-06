package ro.isdc.wro.util.provider;

import ro.isdc.wro.model.resource.processor.ProcessorProvider;
import ro.isdc.wro.model.resource.support.hash.HashStrategyProvider;
import ro.isdc.wro.model.resource.support.naming.NamingStrategyProvider;

/**
 * Bring all providers in a single place. Simplifies the way providers are implemented.
 * 
 * @author Alex Objelean
 * @created 16 Jun 2012
 * @since 1.4.7
 */
public interface ConfigurableProvider
    extends ProcessorProvider, NamingStrategyProvider, HashStrategyProvider {
}
