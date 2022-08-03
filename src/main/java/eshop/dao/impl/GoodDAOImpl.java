package eshop.dao.impl;

import eshop.exception.ProductNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import eshop.utils.DBUtils;
import eshop.dao.GoodDAO;
import eshop.model.Good;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GoodDAOImpl implements GoodDAO {

    private static final Logger LOGGER = LogManager.getLogger(GoodDAOImpl.class.getName());

    private static final String QUERY_SELECT_FROM_GOOD = "SELECT * FROM good";

    @Override
    public List<Good> getAll() {
        List<Good> goods = new ArrayList<>();

        try (PreparedStatement statement = getPreparedStatement(QUERY_SELECT_FROM_GOOD);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Good good = new Good(rs.getLong("id"),
                        rs.getString("title"),
                        rs.getBigDecimal("price"));

                goods.add(good);
            }
        } catch (SQLException e) {
            LOGGER.error("Request to get all goods has failed. Error message: {}", e.getMessage());
        }

        return goods;
    }

    @Override
    public Good getByTitleAndPrice(String title, String price) {
        return getAll().stream()
                .filter(good -> title.equals(good.getTitle())
                        && price.equals(String.valueOf(good.getPrice())))
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with title %s and price %s not found", title, price)));
    }

    private PreparedStatement getPreparedStatement(String query) throws SQLException {
        return DBUtils.getConnection().prepareStatement(query);
    }
}
