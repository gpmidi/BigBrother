package me.taylorkelly.bigbrother.fixes;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import me.taylorkelly.bigbrother.BBLogging;
import me.taylorkelly.bigbrother.BBSettings;
import me.taylorkelly.bigbrother.datasource.ConnectionManager;

public class Fix3 extends Fix {

    public Fix3(File dataFolder) {
        super(dataFolder);
    }
    protected int version = 3;
    public static final String UPDATE_MYSQL = "ALTER TABLE `bbdata` MODIFY `data` VARCHAR(500);";

    @Override
    public void apply() {
        if (needsUpdate(version)) {
            BBLogging.info("Updating table for 1.6.2");
            boolean sqlite = !BBSettings.mysql;

            if (updateTable(sqlite)) {
                updateVersion(version);
            }
        }
    }

    private static boolean updateTable(boolean sqlite) {
        if (BBSettings.mysql) {
            Connection conn = null;
            Statement st = null;
            try {
                conn = ConnectionManager.getConnection();
                st = conn.createStatement();
                st.executeUpdate(UPDATE_MYSQL);
                conn.commit();
                return true;
            } catch (SQLException e) {
                BBLogging.severe("Update Table 1.6.2 Fail " + ((sqlite) ? " sqlite" : " mysql"), e);
                return false;
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                } catch (SQLException e) {
                    BBLogging.severe("Update Table 1.6.2 Fail (on close)");
                }
            }
        } else {
            return true;
        }
    }
}
