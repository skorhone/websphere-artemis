# websphere-artemis-rar
This module contains Artemis Resource Adapter for WebSphere Application Server. Artemis Resource Adapter uses ServiceLoader pattern for obtaining instance of TransactionManagerLocator service. The TransactionManagerLocator service is used to locate server's TransactionManager service.

# Notes
Accessing TransactionManager directly is considered a bad practice, because TransactionManager exposes operations that may cause server to fail when used incorrectly. Instead of using TransactionManager resource adapters should use TransactionSynchronizationRegistry provided by BootstrapContext.

## Additional note
Artemis Resource Adapter does not currently use prohibited operations. TransactionManager is used only for checking, if there is active transaction.
