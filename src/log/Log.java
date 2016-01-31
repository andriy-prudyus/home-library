package log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {

    private static final Logger logger = Logger.getLogger(Log.class.getName());
    private static boolean isLoggerConfigured = false;

    public static void prepareLogConfiguration() {
        if (isLoggerConfigured) {
            return;
        }

        try {
            String tmpDir = System.getProperty("java.io.tmpdir");
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            // Create log directory if it is necessary
            File logDir = new File(tmpDir + "/HomeLibrary/logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            FileHandler fileHandler = new FileHandler(logDir + "/HomeLibrary_" + date + ".log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            isLoggerConfigured = true;
//            LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
        } catch (Exception e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
    }

}
