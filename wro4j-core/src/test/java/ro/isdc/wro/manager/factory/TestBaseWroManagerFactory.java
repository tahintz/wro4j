/**
 * Copyright@2011 wro4j
 */
package ro.isdc.wro.manager.factory;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ro.isdc.wro.config.Context;
import ro.isdc.wro.manager.WroManager;
import ro.isdc.wro.manager.callback.LifecycleCallback;
import ro.isdc.wro.manager.callback.LifecycleCallbackSupport;
import ro.isdc.wro.model.WroModel;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.factory.XmlModelFactory;
import ro.isdc.wro.model.resource.support.naming.NoOpNamingStrategy;
import ro.isdc.wro.util.WroUtil;

/**
 * @author Alex Objelean
 */
public class TestBaseWroManagerFactory {
  BaseWroManagerFactory factory;
  @Before
  public void setUp() {
    Context.set(Context.standaloneContext());
  }
  @Test
  public void defaultModelFactoryIsXml() {
    new BaseWroManagerFactory() {
      @Override
      protected WroModelFactory newModelFactory() {
        final WroModelFactory modelFactory = super.newModelFactory();
        Assert.assertEquals(XmlModelFactory.class, modelFactory.getClass());
        return modelFactory;
      }
    };
  }

  @Test
  public void shouldCreateManager() throws Exception {
    factory = new BaseWroManagerFactory();
    final WroManager manager = factory.create();
    Assert.assertNotNull(manager);
    Assert.assertEquals(NoOpNamingStrategy.class, manager.getNamingStrategy().getClass());
  }

  @Test
  public void shouldSetCallback() throws Exception {
    final LifecycleCallback callback = Mockito.spy(new LifecycleCallbackSupport());
    factory = new BaseWroManagerFactory() {
      @Override
      protected void onAfterInitializeManager(final WroManager manager) {
        manager.registerCallback(callback);
      }
    }.setModelFactory(WroUtil.factoryFor(new WroModel()));
    final WroManager manager = factory.create();
    manager.getModelFactory().create();

    Mockito.verify(callback).onBeforeModelCreated();
    Mockito.verify(callback).onAfterModelCreated();
  }


  @Test
  public void shouldNotFailWhenReloadingModelOutsideOfContext() throws Exception {
    Context.unset();
    factory = new BaseWroManagerFactory();
    factory.onModelPeriodChanged(0);
  }

  @Test
  public void shouldNotFailWhenReloadingCacheOutsideOfContext() throws Exception {
    Context.unset();
    factory = new BaseWroManagerFactory();
    factory.onCachePeriodChanged(0);
  }
}
