package ca.grimoire.mainspring.daemon;

import org.apache.commons.daemon.DaemonContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Spring postprocessor to handle the {@link DaemonContextAware} interface by
 * injecting an instance of {@link DaemonContext}.
 */
public class DaemonContextAwareProcessor implements BeanPostProcessor {
    private final DaemonContext daemonContext;

    /**
     * Prepares a processor for a given daemon context.
     * 
     * @param daemonContext
     *            the context to inject.
     */
    public DaemonContextAwareProcessor(DaemonContext daemonContext) {
        this.daemonContext = daemonContext;
    }

    /**
     * Does nothing to the passed bean.
     * 
     * @param bean
     *            the bean to process.
     * @param beanName
     *            the bean's name.
     * @return <var>bean</var>, unmodified.
     */
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    /**
     * Injects the configured {@link DaemonContext} into a bean, if it
     * implements {@link DaemonContextAware}. Otherwise, this leaves the bean
     * as-is.
     * 
     * @param bean
     *            the bean to process.
     * @param beanName
     *            the bean's name.
     * @return <var>bean</var>, post-injection.
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof DaemonContextAware) {
            DaemonContextAware contextAwareBean = (DaemonContextAware) bean;
            contextAwareBean.setDaemonContext(this.daemonContext);
        }

        return bean;
    }

}
