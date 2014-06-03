/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn.dataflow;

import com.arjuna.databroker.data.DataProvider;
import com.arjuna.databroker.data.DataSource;
import com.google.common.base.Strings;
import com.google.common.primitives.Longs;
import ncl.tsb.smn.connectors.DirectProvider;
import ncl.tsb.smn.data.SmnRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static ncl.tsb.smn.SmnDataFlowNodeFactory.*;

public class SmnDataSource implements DataSource {
	private static final Logger logger = Logger.getLogger(SmnDataSource.class.getName());

	private String _name;

	private Map<String, String> _properties;

	private DataProvider<SmnRecord> _dataProvider;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public SmnDataSource(final String name, final Map<String, String> properties) {
		logger.info("SmnDataSource: " + name + ", " + properties);

		_name = name;
		_properties = properties;

		_dataProvider = new DirectProvider<>(this);

		setupDatabaseScan();
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public Map<String, String> getProperties() {
		return Collections.unmodifiableMap(_properties);
	}

	@Override
	public Collection<Class<?>> getDataProviderDataClasses() {
		final Set<Class<?>> dataProviderDataClasses = new HashSet<>();

		dataProviderDataClasses.add(SmnRecord.class);

		return dataProviderDataClasses;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> DataProvider<T> getDataProvider(final Class<T> dataClass) {
		if (dataClass == SmnRecord.class) {
			return (DataProvider<T>) _dataProvider;
		} else {
			return null;
		}
	}

	public void emit(final SmnRecord data) {
		logger.info("SmnDataSource.emit: " + data);

		_dataProvider.produce(data);
	}

	private void setupDatabaseScan() {
		// Which DB server to connect to
		final String databaseHost = getProperty(SOURCE_DB_HOST, "localhost");
		final String databasePort = getProperty(SOURCE_DB_PORT, "5432");
		final String databaseDB = getProperty(SOURCE_DB_DATABASE, "smn");

		// Credentials to connect to the DB Server
		final String databaseUser = getProperty(SOURCE_DB_USER, "smn");
		final String databasePass = getProperty(SOURCE_DB_PASS, "smn");

		// Table and column to scan for updates
		final String databaseTable = getProperty(SOURCE_DB_TABLE, "dbdata");
		final String databaseColumn = getProperty(SOURCE_DB_COLUMN, "inserttime");

		// How often (and how much) to update
		final Integer updateFrequency = Integer.valueOf(getProperty(SOURCE_INTERVAL, "30"));
		final Integer batchSize = Integer.valueOf(getProperty(SOURCE_BATCH_SIZE, "100"));


		try {
			// Connect to the database
			String connectionURL = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s&ssl=true", databaseHost, databasePort, databaseDB, databaseUser, databasePass);
			final Connection conn = DriverManager.getConnection(connectionURL);

			logger.info(String.format("Connection URL: [%s]", connectionURL));

			// Create the task to be periodically run
			final Runnable updateScanner = new Runnable() {
				// Keep track of the most recent record seen (so we can ask only for newer ones)
				private long insertTime = Long.MIN_VALUE;

				public void run() {
					try {
						String query;

						// Are we checking for the first time or a repeat? (changes the logic)
						if (insertTime == Long.MIN_VALUE) {
							query = String.format("SELECT * FROM %s ORDER BY %s ASC LIMIT %s", databaseTable, databaseColumn, batchSize);
						} else {
							query = String.format("SELECT * FROM %s WHERE %s > %s ORDER BY %s ASC LIMIT %s", databaseTable, databaseColumn, insertTime, databaseColumn, batchSize);
						}

						logger.info(String.format("SQL: [%s]", query));
						final ResultSet resultSet = conn.createStatement().executeQuery(query);

						// Churn through the results and push each one out of the Data Source
						while (resultSet.next()) {
							// keep track of the newest value seen (for update scanning)
							insertTime = Longs.max(insertTime, resultSet.getLong(databaseColumn));

							emit(new SmnRecord(resultSet));
						}
					} catch (SQLException e) {
						logger.warning("Could not query for updates.");
						e.printStackTrace();
					}
				}
			};

			// Set the task to periodically scan
			scheduler.scheduleAtFixedRate(updateScanner, updateFrequency, updateFrequency, TimeUnit.SECONDS);
		} catch (SQLException e) {
			logger.warning("Could not establish JDBC connection.");
			e.printStackTrace();
		}
	}

	// Helper method to get a property or provide some default value
	private String getProperty(final String key, final String defaultValue) {
		if (Strings.isNullOrEmpty(_properties.get(key))) {
			return defaultValue;
		}

		return _properties.get(key);
	}
}
