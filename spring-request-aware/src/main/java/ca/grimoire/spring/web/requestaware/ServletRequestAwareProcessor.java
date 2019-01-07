package ca.grimoire.spring.web.requestaware;

import javax.servlet.ServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Injects the in-flight {@link ServletRequest} from
 * {@link ServletRequestAwareListener} into beans that implement the
 * {@link ServletRequestAware} marker interface.
 * <p>
 * Deploying this processor is as easy as adding
 * 
 * <pre>
 * &lt;bean class="ca.grimoire.spring.web.requestaware.ServletRequestAwareProcessor" /&gt;
 * </pre>
 * 
 * to your Spring context. This class is also annotated with {@link Component},
 * allowing annotation-driven configurations to work with no further effort.
 */
@Component
public class ServletRequestAwareProcessor implements BeanPostProcessor {

	/**
	 * Injects the current {@link ServletRequest} if <var>bean</var> implements
	 * {@link ServletRequestAware}.
	 * 
	 * @param bean
	 *            the object to post-process.
	 * @param beanName
	 *            the object's name.
	 * @return <var>bean</var>.
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof ServletRequestAware) {
			((ServletRequestAware) bean).setServletRequest(ServletRequestHolder
					.getRequest());
		}
		return bean;
	}

	/**
	 * No post-processing required.
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}
}
