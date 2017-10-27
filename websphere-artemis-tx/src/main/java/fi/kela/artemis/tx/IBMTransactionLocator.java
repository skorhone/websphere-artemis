package fi.kela.artemis.tx;

import java.lang.reflect.Method;

import javax.transaction.TransactionManager;

import org.apache.activemq.artemis.service.extensions.transactions.TransactionManagerLocator;
import org.jboss.logging.Logger;

public class IBMTransactionLocator implements TransactionManagerLocator {
	private final static String[] TX_MANAGER_FACTORY_CLASSES = { "com.ibm.tx.jta.TransactionManagerFactory",
			"com.ibm.ws.Transaction.TransactionManagerFactory" };
	private final static String TX_MANAGER_FACTORY_METHOD = "getTransactionManager";
	private static final Logger logger = Logger.getLogger(IBMTransactionLocator.class);
	
	@Override
	public TransactionManager getTransactionManager() {
		try {
			Class<?> clazz = getTXManagerClass();
			Method method = clazz.getMethod(TX_MANAGER_FACTORY_METHOD);
			return (TransactionManager) method.invoke(null);
		} catch (Exception exception) {
			logger.warn("Could not obtain transaction manager", exception);
			return null;
		}
	}

	private Class<?> getTXManagerClass() throws ClassNotFoundException {
		ClassNotFoundException storedException = null;
		for (String className : TX_MANAGER_FACTORY_CLASSES) {
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException exception) {
				storedException = exception;
			}
		}
		throw storedException;
	}
}
