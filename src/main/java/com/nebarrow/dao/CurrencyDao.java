package com.nebarrow.dao;

import com.nebarrow.entity.Currency;
import com.nebarrow.exception.ElementAlreadyExistsException;
import com.nebarrow.exception.DaoException;
import com.nebarrow.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CurrencyDao implements ICrudDao<Currency> {

    private static final CurrencyDao INSTANCE = new CurrencyDao();
    private final String SQL_GET_CURRENCY_BY_NAME = "SELECT id, name, code, sign FROM currencies WHERE code=?";
    private final String SQL_CREATE_CURRENCY = "INSERT INTO currencies(name, code, sign) VALUES (?, ?, ?)";
    private final String SQL_UPDATE_CURRENCY = "UPDATE currencies SET name=?, code=?, sign=? WHERE id=?";
    private final String SQL_GET_ALL_CURRENCIES = "SELECT id, name, code, sign FROM currencies;";

    private final boolean SUCCESS = true;
    private final boolean FAILURE = false;

    private CurrencyDao() {}

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    @Override
    public Currency create(Currency model) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_CREATE_CURRENCY)) {
            setParameters(statement, model.getFullname(), model.getCode(), model.getSign());
            statement.executeUpdate();
            var result = statement.getGeneratedKeys();
            long generatedId = result.getLong(1);
            if (result.next()) {
                return Currency.builder()
                        .id(generatedId)
                        .code(model.getCode())
                        .fullname(model.getFullname())
                        .sign(model.getSign())
                        .build();
            } throw new DaoException("Failed to retrieve generated ID for the new Currency");
        } catch (SQLException e) {
            throw new DaoException("Currency can't be created");
        }
    }


    @Override
    public Optional<Currency> update(Currency currency) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_UPDATE_CURRENCY)) {
            setParameters(statement, currency.getFullname(), currency.getCode(), currency.getSign(), currency.getId());
            var result = statement.executeUpdate();
            if (result > 0) {
                return Optional.of(currency);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Can't update currency " + currency.getCode(), e);
        }
    }

    @Override
    public List<Currency> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_GET_ALL_CURRENCIES)) {
            var result = statement.executeQuery();
            List<Currency> currencies = new ArrayList<>();
            while (result.next()) {
                currencies.add(Currency.builder()
                        .id(result.getLong(1))
                        .fullname(result.getString(2))
                        .code(result.getString(3))
                        .sign(result.getString(4))
                        .build());
            }
            return currencies;
        } catch (SQLException e) {
            throw new DaoException("Can't find currencies");
        }
    }

    @Override
    public Optional<Currency> findByName(String name) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SQL_GET_CURRENCY_BY_NAME)) {
            setParameters(statement, name);
            var result = statement.executeQuery();
            if (result.next()) {
                return Optional.of(buildCurrencyWithParameters(
                        result.getLong(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4)));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Can't find currency");
        }
    }

    private void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 1; i <= parameters.length; ++i) {
            statement.setObject(i, parameters[i - 1]);
        }
    }

    private Currency buildCurrencyWithParameters(long id, String fullname, String code, String sign) {
        return Currency.builder()
                .id(id)
                .fullname(fullname)
                .code(code)
                .sign(sign)
                .build();
    }
}
