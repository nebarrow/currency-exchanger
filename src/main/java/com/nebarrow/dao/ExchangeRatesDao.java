package com.nebarrow.dao;

import com.nebarrow.entity.Currency;
import com.nebarrow.entity.ExchangeRate;
import com.nebarrow.exception.DaoException;
import com.nebarrow.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao implements ICrudDao<ExchangeRate> {
    private final String SQL_CREATE_CURRENCY = """
            INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate)
            VALUES ((SELECT id FROM currencies WHERE code=?),
            (SELECT id FROM currencies WHERE code=?),
            ?)
            """;

    private final String SQL_UPDATE_CURRENCY = """
            UPDATE exchange_rates SET rate=?
            WHERE base_currency_id=(SELECT id FROM currencies WHERE code=?)
            AND target_currency_id=(SELECT id FROM currencies WHERE code=?)
            """;

    private final String SQL_SELECT_EXCHANGE_RATE_AFTER_UPDATE = """
            SELECT id, base_currency_id, target_currency_id, rate
            FROM exchange_rates
            WHERE base_currency_id=(SELECT id FROM currencies WHERE code=?)
            AND target_currency_id=(SELECT id FROM currencies WHERE code=?);
            """;

    private final String SQL_GET_ALL_CURRENCIES = "SELECT id, base_currency_id, target_currency_id, rate FROM exchange_rates;";
    private final String SQL_GET_CURRENCY = "SELECT id, code, name, sign FROM currencies WHERE id=?;";
    private final String SQL_GET_RATES_BY_NAME = """
    SELECT id, base_currency_id, target_currency_id, rate
    FROM exchange_rates
    WHERE CONCAT((SELECT code FROM currencies WHERE id = base_currency_id),
                 (SELECT code FROM currencies WHERE id = target_currency_id)) = ?
""";

    private final String SQL_FIND_RATE_BY_NAMES = """
            WITH AllRates AS (
                SELECT
                    base_currency_id,
                    target_currency_id,
                    rate
                FROM exchange_rates
                WHERE base_currency_id = (SELECT id FROM currencies WHERE code = ?)
                  AND target_currency_id = (SELECT id FROM currencies WHERE code = ?)
                        
                UNION ALL
                        
                SELECT
                    target_currency_id AS base_currency_id,
                    base_currency_id AS target_currency_id,
                    1.0 / rate AS rate
                FROM exchange_rates
                WHERE base_currency_id = (SELECT id FROM currencies WHERE code = ?)
                  AND target_currency_id = (SELECT id FROM currencies WHERE code = ?)
                        
                UNION ALL
                        
                SELECT
                    er1.target_currency_id AS base_currency_id,
                    er2.target_currency_id AS target_currency_id,
                    (er2.rate / er1.rate) AS rate
                FROM exchange_rates er1
                         JOIN exchange_rates er2
                         ON er1.base_currency_id = er2.base_currency_id
                WHERE er1.target_currency_id = (SELECT id FROM currencies WHERE code = ?)
                  AND er2.target_currency_id = (SELECT id FROM currencies WHERE code = ?)
            )
                        
            SELECT
                base_currency_id,
                target_currency_id,
                ROUND((rate), 2) AS rate
            FROM AllRates
            GROUP BY base_currency_id, target_currency_id
            LIMIT 1;
            """;


    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();

    private ExchangeRatesDao() {
    }

    public static ExchangeRatesDao getInstance() {
        return INSTANCE;
    }

    public Optional<ExchangeRate> findRateByCodes(String baseName, String targetName) throws DaoException {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_FIND_RATE_BY_NAMES)) {
            setParameters(statement, baseName, targetName, targetName, baseName, baseName, targetName);
            var result = statement.executeQuery();
            if (result.next()) {
                return Optional.of(ExchangeRate.builder()
                        .baseCurrency(getCurrency(result.getLong(1)))
                        .targetCurrency(getCurrency(result.getLong(2)))
                        .rate(result.getDouble(3))
                        .build());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Can't find rate by these names: " + baseName + " to " + targetName, e);
        }
    }

    @Override
    public List<ExchangeRate> findAll() throws DaoException {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_GET_ALL_CURRENCIES)) {
            var result = statement.executeQuery();
            List<ExchangeRate> exchangeRateList = new ArrayList<>();
            while (result.next()) {
                exchangeRateList.add(ExchangeRate.builder()
                        .id(result.getLong(1))
                        .baseCurrency(getCurrency(result.getLong(2)))
                        .targetCurrency(getCurrency(result.getLong(3)))
                        .rate(result.getDouble(4))
                        .build());
            }
            return exchangeRateList;
        } catch (SQLException e) {
            throw new DaoException("Can't find all exchange rates", e);
        }
    }

    @Override
    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) throws DaoException {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_UPDATE_CURRENCY)) {
            setParameters(statement, exchangeRate.getRate(),
                    exchangeRate.getBaseCurrency().getCode(),
                    exchangeRate.getTargetCurrency().getCode());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return findUpdatedExchangeRate(exchangeRate);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Error updating exchange rate", e);
        }
    }

    @Override
    public ExchangeRate create(ExchangeRate exchangeRate) throws DaoException {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_CREATE_CURRENCY)) {
            setParameters(statement, exchangeRate.getBaseCurrency().getCode(), exchangeRate.getTargetCurrency().getCode(), exchangeRate.getRate());
            var result = statement.executeUpdate();
            if (result == 1) {
                return getExchangeRateAfterUpdate(exchangeRate, statement);
            }
            throw new DaoException("Failed to retrieve generated ID for the new ExchangeRate");
        } catch (SQLException e) {
            throw new DaoException("Can't create exchange rate: ", e);
        }
    }

    @Override
    public Optional<ExchangeRate> findByName(String name) throws DaoException {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_GET_RATES_BY_NAME)) {
            setParameters(statement, name);
            var result = statement.executeQuery();
            if (result.next()) {
                return Optional.of(ExchangeRate.builder()
                        .id(result.getLong(1))
                        .baseCurrency(getCurrency(result.getLong(2)))
                        .targetCurrency(getCurrency(result.getLong(3)))
                        .rate(result.getDouble(4))
                        .build());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Can't find exchange rate by name " + name);
        }
    }

    private Currency getCurrency(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_GET_CURRENCY)) {
            setParameters(statement, id);
            var result = statement.executeQuery();
            return Currency.builder()
                    .id(result.getLong(1))
                    .code(result.getString(2))
                    .fullname(result.getString(3))
                    .sign(result.getString(4))
                    .build();
        } catch (SQLException e) {
            throw new DaoException("Can't get currency by id: " + id);
        }
    }

    private void setParameters(PreparedStatement statement, Object... parameters) {
        for (int i = 1; i <= parameters.length; ++i) {
            try {
                statement.setObject(i, parameters[i - 1]);
            } catch (SQLException e) {
                throw new DaoException("Can't set parameters for objects: ", e);
            }
        }
    }

    private ExchangeRate buildExchangeRateWithParameters(long id, ExchangeRate exchangeRate) {
        return ExchangeRate.builder()
                .id(id)
                .baseCurrency(exchangeRate.getBaseCurrency())
                .targetCurrency(exchangeRate.getTargetCurrency())
                .rate(exchangeRate.getRate())
                .build();
    }

    private ExchangeRate getExchangeRateAfterUpdate(ExchangeRate exchangeRate, PreparedStatement statement) {
        try (var generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                long generatedId = generatedKeys.getLong(1);
                return buildExchangeRateWithParameters(generatedId, exchangeRate);
            } else {
                throw new DaoException("Failed to retrieve generated ID for the new ExchangeRate.");
            }
        } catch (SQLException e) {
            throw new DaoException("Can't get exchange rate", e);
        }
    }

    private Optional<ExchangeRate> findUpdatedExchangeRate(ExchangeRate exchangeRate) throws DaoException {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_SELECT_EXCHANGE_RATE_AFTER_UPDATE)) {
            setParameters(statement, exchangeRate.getBaseCurrency().getCode(), exchangeRate.getTargetCurrency().getCode());
            var result = statement.executeQuery();
            if (result.next()) {
                return Optional.of(ExchangeRate.builder()
                        .id(result.getLong(1))
                        .baseCurrency(getCurrency(result.getLong(2)))
                        .targetCurrency(getCurrency(result.getLong(3)))
                        .rate(result.getDouble(4))
                        .build());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Error retrieving updated exchange rate", e);
        }
    }
}
